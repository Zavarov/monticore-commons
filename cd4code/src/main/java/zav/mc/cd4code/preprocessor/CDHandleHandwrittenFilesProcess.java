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

package zav.mc.cd4code.preprocessor;

import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDConstructor;
import de.monticore.cd.cd4analysis._ast.ASTCDEnum;
import de.monticore.cd.cd4analysis._ast.ASTCDInterface;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import zav.mc.cd4code.CDGeneratorHelper;

public class CDHandleHandwrittenFilesProcess extends CDProcess {
    private final CDGeneratorHelper generatorHelper;

    public CDHandleHandwrittenFilesProcess(GlobalExtensionManagement glex, CDGeneratorHelper generatorHelper) {
        super(glex);
        this.generatorHelper = generatorHelper;
    }

    @Override
    public void visit(ASTCDInterface ast){
        if(generatorHelper.existsHandwrittenClass(ast))
            //Rename the interface
            ast.setName(ast.getName() + CDGeneratorHelper.HANDWRITTEN_FILE_POSTFIX);
    }

    @Override
    public void visit(ASTCDEnum ast){
        if(generatorHelper.existsHandwrittenClass(ast)) {
            //Rename the enum
            ast.setName(ast.getName() + CDGeneratorHelper.HANDWRITTEN_FILE_POSTFIX);
            //Rename the constructor as well to match the enum name
            for (ASTCDConstructor cdConstructor : ast.getCDConstructorList())
                cdConstructor.setName(ast.getName());
            //Make the enum abstract
            ast.getModifier().setAbstract(true);
        }
    }

    @Override
    public void visit(ASTCDClass ast){
        if(generatorHelper.existsHandwrittenClass(ast)) {
            //Rename the class
            ast.setName(ast.getName() + CDGeneratorHelper.HANDWRITTEN_FILE_POSTFIX);
            //Rename the constructor as well to match the class name
            for (ASTCDConstructor cdConstructor : ast.getCDConstructorList())
                cdConstructor.setName(ast.getName());
            //Make the class abstract
            ast.getModifier().setAbstract(true);
        }
    }
}
