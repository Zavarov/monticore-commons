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

package vartas.monticore.cd4analysis.calculator;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.cd.cd4code._visitor.CD4CodeVisitor;
import de.monticore.types.MCTypeFacade;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import vartas.monticore.cd4analysis.CDMethodComparator;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This class determines all methods of a given {@link ASTCDType}.<br>
 * In addition to the methods defined in the type itself, it will also compute inherited
 * methods from possible superclasses and interfaces.<br>
 * All generic types are automatically resolved using the arguments provided in the super-structure. And in order
 * to avoid ambiguity, all of those types have to specified.
 */
public class CDMethodCalculator implements BiFunction<ASTCDType, List<ASTMCTypeArgument>, Set<ASTCDMethod>>, CD4CodeInheritanceVisitor {
    /**
     * Contains all accessible methods.
     */
    private final Set<ASTCDMethod> methods = new TreeSet<>(new CDMethodComparator());
    /**
     * Associates a generic type with their effective type.<br>
     * The keys represent the the generic types, specified in the individual supertypes.
     * The values represent the types they are associated with.
     */
    private final Map<ASTMCQualifiedName, ASTMCQualifiedName> typeMap = new TreeMap<>(Comparator.comparing(ASTMCQualifiedName::getQName));

    /**
     * Determines all methods in the given type, as well as all super types.
     * @param ast the type associated with the methods.
     * @param typeArguments a list of generic arguments associated with the type.
     * @return a list of all methods accessible by the provided type.
     */
    @Override
    public Set<ASTCDMethod> apply(ASTCDType ast, List<ASTMCTypeArgument> typeArguments) {
        this.methods.clear();
        this.typeMap.clear();
        loadMap(ast, typeArguments);
        ast.deepClone().accept(getRealThis());
        return methods;
    }

    /**
     * Maps the generic arguments of the provided {@link ASTCDType} to their effective type, specified in
     * an respective sub-class. It is possible that the number of provided arguments is higher than the
     * number of generic types in the current type. Any generic class also extends {@link Object}, for example,
     * even though it has no generic types on its own.
     * @param ast the current {@link ASTCDType} associated with the effective types.
     * @param typeArguments the effective types for the generic arguments specified in the sub-type.
     */
    private void loadMap(ASTCDType ast, List<ASTMCTypeArgument> typeArguments){
        RetrieveStereotypeVisitor retrieveStereotypeVisitor = new RetrieveStereotypeVisitor();
        RetrieveArgumentVisitor retrieveArgumentVisitor = new RetrieveArgumentVisitor();

        //Generic types
        List<ASTMCQualifiedName> stereotypes = retrieveStereotypeVisitor.apply(ast);
        //Explicit types
        List<ASTMCQualifiedName> arguments = retrieveArgumentVisitor.apply(typeArguments);

        //If a generic class extends from a non-generic class, we have more arguments than stereotypes
        assert(stereotypes.size() <= arguments.size());

        for(int i = 0 ; i < stereotypes.size() ; ++i)
            typeMap.put(stereotypes.get(i), arguments.get(i));
    }

    /**
     * Replaces the generic type with the effective type.
     * @param ast one of the generic arguments of the current {@link ASTCDType}.
     */
    @Override
    public void visit(ASTMCQualifiedName ast){
        //Replace the generic type with the type specified in the superclass
        if(typeMap.containsKey(ast))
            ast.setPartList(typeMap.get(ast).getPartList());
    }

    /**
     * One of the methods of the current {@link ASTCDType}. It is possible for a type
     * to have several instances of the same method, for example when implementing multiple interfaces.
     * To avoid any collisions, all but one are ignored.
     * @param ast one of the methods of the current {@link ASTCDType}.
     */
    @Override
    public void endVisit(ASTCDMethod ast){
        methods.add(ast);
    }

    /**
     * Retrieves all generic types of a given {@link ASTCDType}.<br>
     * The generic types are represented by the keys of the stereotypes.
     */
    private static class RetrieveStereotypeVisitor implements CD4CodeVisitor, Function<ASTCDType, List<ASTMCQualifiedName>>{
        /**
         * Contains the stereotypes of the provided {@link ASTCDType}.
         */
        private final List<ASTMCQualifiedName> qualifiedNames = new ArrayList<>();
        /**
         * The utility class for transforming the stereotypes into instances of {@link ASTMCType}.
         */
        private final MCTypeFacade mcTypeFacade = MCTypeFacade.getInstance();

        /**
         * Retrieves the generic types of the provided {@link ASTCDType}. Since those values are only
         * accessible by the underlying {@link ASTCDClass} or {@link ASTCDInterface}, we have to break
         * the encapsulation via the visitor, to access those elements.
         * @param ast the type associated with the retrieved types.
         * @return a list containing all generic types of the {@link ASTCDType}.
         */
        @Override
        public List<ASTMCQualifiedName> apply(ASTCDType ast) {
            qualifiedNames.clear();
            ast.accept(getRealThis());
            return qualifiedNames;
        }

        /**
         * Stores the stereotypes of the provided {@link ASTCDClass} in {@link #qualifiedNames}.
         * @param ast the {@link ASTCDClass} associated with the retrieved stereotypes.
         */
        @Override
        public void visit(ASTCDClass ast){
            if(ast.isPresentStereotype())
                for(ASTCDStereoValue stereoValue : ast.getStereotype().getValueList())
                    qualifiedNames.add(mcTypeFacade.createQualifiedType(stereoValue.getName()).getMCQualifiedName());
        }

        /**
         * Stores the stereotypes of the provided {@link ASTCDInterface} in {@link #qualifiedNames}.
         * @param ast the {@link ASTCDInterface} associated with the retrieved stereotypes.
         */
        @Override
        public void visit(ASTCDInterface ast){
            if(ast.isPresentStereotype())
                for(ASTCDStereoValue stereoValue : ast.getStereotype().getValueList())
                    qualifiedNames.add(mcTypeFacade.createQualifiedType(stereoValue.getName()).getMCQualifiedName());
        }
    }

    /**
     * This class is used to unwrap the the name of the provided argument type.
     */
    private static class RetrieveArgumentVisitor implements CD4CodeVisitor, Function<List<ASTMCTypeArgument>, List<ASTMCQualifiedName>>{
        /**
         * Internal buffer for the qualified names.
         */
        private final List<ASTMCQualifiedName> qualifiedNames = new ArrayList<>();

        /**
         * Stores the qualified name in the internal buffer.
         * @param ast the qualified name of an {@link ASTMCTypeArgument}.
         */
        @Override
        public void visit(ASTMCQualifiedName ast){
            qualifiedNames.add(ast);
        }

        /**
         * Iterates over all arguments and extracts their name. It is possible that an argument doesn't have a name,
         * e.g. for wildcards. In such a case, the name is simply ignored.
         * @param astList the generic arguments of an {@link ASTCDType}.
         * @return a list containing all qualified argument names.
         */
        @Override
        public List<ASTMCQualifiedName> apply(List<ASTMCTypeArgument> astList) {
            qualifiedNames.clear();
            for(ASTMCTypeArgument ast : astList)
                ast.accept(getRealThis());
            return qualifiedNames;
        }
    }
}
