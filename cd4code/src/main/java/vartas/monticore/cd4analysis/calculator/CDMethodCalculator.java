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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.cd.cd4code._visitor.CD4CodeVisitor;
import de.monticore.types.MCTypeFacade;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CDMethodCalculator implements BiFunction<ASTCDType, List<ASTMCTypeArgument>, List<ASTCDMethod>>, CD4CodeInheritanceVisitor {
    private final List<ASTCDMethod> methods = new ArrayList<>();
    private final ListMultimap<String, String> typeMap = ArrayListMultimap.create();

    @Override
    public List<ASTCDMethod> apply(ASTCDType ast, List<ASTMCTypeArgument> typeArguments) {
        this.methods.clear();
        this.typeMap.clear();
        loadMap(ast, typeArguments);
        ast.deepClone().accept(getRealThis());
        return methods;
    }

    private void loadMap(ASTCDType ast, List<ASTMCTypeArgument> typeArguments){
        StereotypeVisitor stereotypeVisitor = new StereotypeVisitor();
        ArgumentVisitor argumentVisitor = new ArgumentVisitor();

        //Generic types
        List<ASTMCQualifiedName> stereotypes = stereotypeVisitor.apply(ast);
        //Explicit types
        List<ASTMCQualifiedName> arguments = argumentVisitor.apply(typeArguments);

        //If a generic class extends from a non-generic class, we have more arguments than stereotypes
        assert(stereotypes.size() <= arguments.size());

        for(int i = 0 ; i < stereotypes.size() ; ++i)
            typeMap.putAll(stereotypes.get(i).getQName(), arguments.get(i).getPartList());
    }

    @Override
    public void visit(ASTMCQualifiedName ast){
        //Replace the generic type with the type specified in the superclass
        if(typeMap.containsKey(ast.getQName()))
            ast.setPartList(typeMap.get(ast.getQName()));
    }

    @Override
    public void endVisit(ASTCDMethod ast){
        //Methods in interfaces always visible -> We can't just check if they're public
        if(!ast.getModifier().isPrivate() && !ast.getModifier().isProtected() && !contains(ast))
            methods.add(ast);
    }

    private boolean contains(ASTCDMethod ast){
        //TODO Don't iterate over the entire list
        for (ASTCDMethod method : methods)
            //Already in the list -> break
            if (method.deepEquals(ast))
                return true;
        return false;
    }

    private static class StereotypeVisitor implements CD4CodeVisitor, Function<ASTCDType, List<ASTMCQualifiedName>>{
        private final List<ASTMCQualifiedName> qualifiedNames = new ArrayList<>();
        private final MCTypeFacade mcTypeFacade = MCTypeFacade.getInstance();

        @Override
        public List<ASTMCQualifiedName> apply(ASTCDType ast) {
            qualifiedNames.clear();
            ast.accept(getRealThis());
            return qualifiedNames;
        }

        @Override
        public void visit(ASTCDClass ast){
            if(ast.isPresentStereotype())
                for(ASTCDStereoValue stereoValue : ast.getStereotype().getValueList())
                    qualifiedNames.add(mcTypeFacade.createQualifiedType(stereoValue.getName()).getMCQualifiedName());
        }

        @Override
        public void visit(ASTCDInterface ast){
            if(ast.isPresentStereotype())
                for(ASTCDStereoValue stereoValue : ast.getStereotype().getValueList())
                    qualifiedNames.add(mcTypeFacade.createQualifiedType(stereoValue.getName()).getMCQualifiedName());
        }
    }

    private static class ArgumentVisitor implements CD4CodeVisitor, Function<List<ASTMCTypeArgument>, List<ASTMCQualifiedName>>{
        private final List<ASTMCQualifiedName> qualifiedNames = new ArrayList<>();

        @Override
        public void visit(ASTMCQualifiedName ast){
            qualifiedNames.add(ast);
        }

        @Override
        public List<ASTMCQualifiedName> apply(List<ASTMCTypeArgument> astList) {
            qualifiedNames.clear();
            for(ASTMCTypeArgument ast : astList)
                ast.accept(getRealThis());
            return qualifiedNames;
        }
    }
}
