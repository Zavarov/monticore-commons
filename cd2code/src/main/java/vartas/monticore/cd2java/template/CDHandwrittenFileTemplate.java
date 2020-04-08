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
import de.monticore.utils.Names;
import de.se_rwth.commons.Joiners;
import vartas.monticore.cd2code.CDGeneratorHelper;

public class CDHandwrittenFileTemplate implements CDConsumerTemplate<ASTCDCompilationUnit> {
    private ASTCDCompilationUnit cdCompilationUnit;
    private final CDGeneratorHelper generatorHelper;

    public CDHandwrittenFileTemplate(CDGeneratorHelper generatorHelper){
        this.generatorHelper = generatorHelper;
    }

    @Override
    public void visit(ASTCDCompilationUnit ast){
        cdCompilationUnit = ast;
    }

    @Override
    public void visit(ASTCDInterface ast){
        String packageName = Joiners.DOT.join(cdCompilationUnit.getPackageList());
        String qualifiedName = Names.getQualifiedName(packageName, ast.getName());

        if(TransformationHelper.existsHandwrittenClass(generatorHelper.getSourcesPath(), qualifiedName))
            //Rename the interface
            ast.setName(ast.getName() + CDGeneratorHelper.TOP_POSTFIX);
    }

    @Override
    public void visit(ASTCDEnum ast){
        String packageName = Joiners.DOT.join(cdCompilationUnit.getPackageList());
        String qualifiedName = Names.getQualifiedName(packageName, ast.getName());

        if(TransformationHelper.existsHandwrittenClass(generatorHelper.getSourcesPath(), qualifiedName)) {
            //Rename the enum
            ast.setName(ast.getName() + CDGeneratorHelper.TOP_POSTFIX);
            //Rename the constructor as well to match the enum name
            for (ASTCDConstructor cdConstructor : ast.getCDConstructorList())
                cdConstructor.setName(ast.getName());
        }
    }

    @Override
    public void visit(ASTCDClass ast){
        String packageName = Joiners.DOT.join(cdCompilationUnit.getPackageList());
        String qualifiedName = Names.getQualifiedName(packageName, ast.getName());

        if(TransformationHelper.existsHandwrittenClass(generatorHelper.getSourcesPath(), qualifiedName)) {
            //Rename the class
            ast.setName(ast.getName() + CDGeneratorHelper.TOP_POSTFIX);
            //Rename the constructor as well to match the class name
            for (ASTCDConstructor cdConstructor : ast.getCDConstructorList())
                cdConstructor.setName(ast.getName());
        }
    }
}
