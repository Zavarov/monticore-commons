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

package vartas.monticore.cd4analysis.template;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.codegen.mc2cd.TransformationHelper;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.utils.Names;
import vartas.monticore.cd4analysis.CDGeneratorHelper;

public class CDHandwrittenFileTemplate extends CDConsumerTemplate{
    private String packageName;

    public CDHandwrittenFileTemplate(GlobalExtensionManagement glex) {
        super(glex);
    }

    @Override
    public void visit(ASTCDType ast){
        this.packageName = ast.getSymbol().getPackageName();
    }

    @Override
    public void visit(ASTCDInterface ast){
        String qualifiedName = Names.getQualifiedName(packageName, ast.getName());

        if(TransformationHelper.existsHandwrittenClass(CDGeneratorHelper.SOURCES_PATH, qualifiedName))
            //Rename the interface
            ast.setName(ast.getName() + CDGeneratorHelper.HANDWRITTEN_FILE_POSTFIX);
    }

    @Override
    public void visit(ASTCDEnum ast){
        String qualifiedName = Names.getQualifiedName(packageName, ast.getName());

        if(TransformationHelper.existsHandwrittenClass(CDGeneratorHelper.SOURCES_PATH, qualifiedName)) {
            //Rename the enum
            ast.setName(ast.getName() + CDGeneratorHelper.HANDWRITTEN_FILE_POSTFIX);
            //Rename the constructor as well to match the enum name
            for (ASTCDConstructor cdConstructor : ast.getCDConstructorList())
                cdConstructor.setName(ast.getName());
        }
    }

    @Override
    public void visit(ASTCDClass ast){
        String qualifiedName = Names.getQualifiedName(packageName, ast.getName());

        if(TransformationHelper.existsHandwrittenClass(CDGeneratorHelper.SOURCES_PATH, qualifiedName)) {
            //Rename the class
            ast.setName(ast.getName() + CDGeneratorHelper.HANDWRITTEN_FILE_POSTFIX);
            //Rename the constructor as well to match the class name
            for (ASTCDConstructor cdConstructor : ast.getCDConstructorList())
                cdConstructor.setName(ast.getName());
        }
    }
}
