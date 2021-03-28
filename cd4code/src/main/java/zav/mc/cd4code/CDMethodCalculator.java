/*
 * Copyright (c) 2020 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package zav.mc.cd4code;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.cd.cd4code._visitor.CD4CodeVisitor;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;

import java.util.*;
import java.util.function.BiFunction;

/**
 * This class determines all visible methods of a given {@link ASTCDType}.<br>
 * It not only includes methods declared in the type itself, but also all accessible
 * methods in superclasses and interfaces.
 *
 * The class attempts to resolve all generic types
 * to match the provided types.
 * All generic types are automatically resolved using the arguments provided in the super-structure. And in order
 * to avoid ambiguity, all of those types have to specified.
 */
public class CDMethodCalculator implements BiFunction<CDTypeSymbol, List<ASTMCQualifiedType>, Set<ASTCDMethod>>, CD4CodeInheritanceVisitor {
    /**
     * Determines all methods in the given type, as well as all super types.
     * @param node the type associated with the methods.
     * @param qualifiedTypes a list of generic arguments associated with the type.
     * @return a list of all methods accessible by the provided type.
     */
    @Override
    public Set<ASTCDMethod> apply(CDTypeSymbol node, List<ASTMCQualifiedType> qualifiedTypes) {
        Set<ASTCDMethod> methods = new TreeSet<>(new CDMethodComparator());

        //Derive methods of the current type
        MethodVisitor visitor = new MethodVisitor(mapGenericTypes(node, qualifiedTypes));
        node.getAstNode().deepClone().accept(visitor);
        methods.addAll(visitor.methods);

        //Derive methods of super types
        for(CDTypeSymbol superType : node.getSuperTypes())
            methods.addAll(apply(superType, qualifiedTypes));

        return methods;
    }

    public Set<ASTCDMethod> apply(ASTCDType node, List<ASTMCQualifiedType> qualifiedTypes){
        return apply(node.getSymbol(), qualifiedTypes);
    }

    /**
     * This method calculates the mapping that associates each generic type with an qualified type. The generic
     * types of a class are provided through the stereotypes. This method goes through all stereotypes and
     * maps each generic type to its qualified type, according to their index.
     * @param symbol The symbol for an {@link ASTCDType} with potentially generic types.
     * @param qualifiedTypes The qualified types for the generic types of the corresponding {@link ASTCDType}.
     * @return A mapping from generic types to qualified types.
     */
    private ListMultimap<String, String> mapGenericTypes(CDTypeSymbol symbol, List<ASTMCQualifiedType> qualifiedTypes){
        ListMultimap<String, String> result = LinkedListMultimap.create();
        List<String> genericTypes = new ArrayList<>();

        //symbol.getStereotypes() may also include stereotypes of the modifier
        CD4CodeVisitor visitor = new CD4CodeVisitor(){
            @Override
            public void handle(ASTCDClass node){
                if(node.isPresentStereotype())
                    count(node.getStereotype());
            }
            @Override
            public void handle(ASTCDInterface node){
                if(node.isPresentStereotype())
                    count(node.getStereotype());
            }
            private void count(ASTCDStereotype stereotype){
                for(ASTCDStereoValue value : stereotype.getValueList())
                    genericTypes.add(value.getName());
            }
        };

        symbol.getAstNode().accept(visitor);

        for(int i = 0 ; i < Math.min(genericTypes.size(), qualifiedTypes.size()) ; ++i)
            result.putAll(genericTypes.get(i), qualifiedTypes.get(i).getNameList());

        return result;
    }

    /**
     * This visitor collects all methods of a specified CDType. Additionally, all occurrences of generic types are
     * replaced with their qualified type.
     */
    private static class MethodVisitor implements CD4CodeInheritanceVisitor{
        /**
         * A set containing all fully qualified methods of the current type.
         */
        private final Set<ASTCDMethod> methods = new HashSet<>();
        /**
         * A mapping association the generic types with qualified types.
         */
        private final ListMultimap<String, String> genericTypes;

        /**
         * Creates a new instance of the visitor. This visitor is applied to an {@link ASTCDType} to extract
         * the containing methods. Additionally, it also qualifies all encountered generic types.
         * @param genericTypes The generic types of the {@link ASTCDType}.
         */
        public MethodVisitor(ListMultimap<String, String> genericTypes){
            this.genericTypes = genericTypes;
        }

        /**
         * If the name matches one of the names of the generic types, it is replaced with its qualified name.
         * @param node The name of an arbitrary node in the {@link ASTCDType}.
         */
        @Override
        public void visit(ASTMCQualifiedName node){
            if(genericTypes.containsKey(node.getQName()))
                node.setPartsList(genericTypes.get(node.getQName()));
        }

        /**
         * Appends the {@link ASTCDMethod} to {@link #methods}.
         * @param node One of the methods of the corresponding {@link ASTCDType}.
         */
        @Override
        public void visit(ASTCDMethod node){
            //Skip (package-)private methods
            //In case of interfaces, methods are implicitly public, even without modifier
            if(!node.getModifier().isProtected() && !node.getModifier().isPrivate())
                methods.add(node);
        }
    }
}
