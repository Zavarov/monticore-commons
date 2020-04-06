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

import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;
import vartas.monticore.cd2code.creator.FactoryCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class CDFactoryTemplate implements CD2CodeVisitor, UnaryOperator<ASTCDCompilationUnit> {
    private final CDGeneratorHelper generatorHelper;
    private final List<ASTCDClass> cdFactoryClasses = new ArrayList<>();
    private ASTCDDefinition cdDefinition;

    public CDFactoryTemplate(CDGeneratorHelper generatorHelper){
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
    public void visit(ASTCDClass ast){
        if(CDGeneratorHelper.hasStereoValue(ast, CDGeneratorHelper.FACTORY_STEREOVALUE)) {
            ASTCDClass cdFactory = FactoryCreator.create(ast, generatorHelper.getGlex());
            cdFactoryClasses.add(cdFactory);
        }
    }

    @Override
    public void endVisit(ASTCDDefinition ast){
        cdDefinition.addAllCDClasss(cdFactoryClasses);
    }

    @Override
    public void endVisit(ASTCDCompilationUnit ast){
        ast.setCDDefinition(cdDefinition);
        ast.addPackage(CDGeneratorHelper.FACTORY_PACKAGE);
    }
}
