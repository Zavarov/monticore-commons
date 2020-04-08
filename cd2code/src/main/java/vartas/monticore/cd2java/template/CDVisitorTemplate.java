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

package vartas.monticore.cd2java.template;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.codegen.mc2cd.TransformationHelper;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.utils.Names;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;
import vartas.monticore.cd2code.creator.VisitorCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class CDVisitorTemplate implements CD2CodeVisitor, UnaryOperator<ASTCDCompilationUnit> {
    private final CDGeneratorHelper generatorHelper;
    private final List<ASTCDInterface> cdVisitorInterfaces = new ArrayList<>();
    private ASTCDDefinition cdDefinition;

    public CDVisitorTemplate(CDGeneratorHelper generatorHelper){
        this.generatorHelper = generatorHelper;
    }

    @Override
    public ASTCDCompilationUnit apply(ASTCDCompilationUnit ast) {
        ast = ast.deepClone();
        ast.accept(this);
        return ast;
    }

    @Override
    public void visit(ASTCDCompilationUnit ast){
        cdDefinition = vartas.monticore.cd2code._ast.CD2CodeMill.cDDefinitionBuilder()
                .setName(ast.getCDDefinition().getName())
                .build();
    }

    @Override
    public void visit(ASTCDDefinition ast){
        ASTCDInterface cdVisitor = VisitorCreator.create(ast, generatorHelper.getGlex());

        cdVisitorInterfaces.add(cdVisitor);

        //If the TOP mechanism has been applied to the visited classes, refer to the TOP classes instead.
        for(ASTCDMethod cdMethod : cdVisitor.getCDMethodList())
            cdMethod.accept(new CDMethodVisitor());
    }

    @Override
    public void endVisit(ASTCDDefinition ast){
        cdDefinition.addAllCDInterfaces(cdVisitorInterfaces);
    }

    @Override
    public void endVisit(ASTCDCompilationUnit ast){
        ast.setCDDefinition(cdDefinition);
        ast.addPackage(CDGeneratorHelper.VISITOR_PACKAGE);
    }

    private class CDMethodVisitor implements CD2CodeVisitor{
        @Override
        public void visit(ASTCDParameter ast){
            ASTMCType baseType = ast.getMCType();
            String baseName = CDGeneratorHelper.prettyprint(baseType);
            //The visitor will only visit classes from the root package
            String qualifiedName = Names.getQualifiedName(generatorHelper.getRootPackage(), baseName);
            //Use the more general TOP class for the visitor
            if(TransformationHelper.existsHandwrittenClass(generatorHelper.getSourcesPath(), qualifiedName)) {
                String TOPTypeName = baseName + CDGeneratorHelper.TOP_POSTFIX;
                ASTMCType TOPType = TransformationHelper.createType(TOPTypeName);
                ast.setMCType(TOPType);
            }
        }
    }
}
