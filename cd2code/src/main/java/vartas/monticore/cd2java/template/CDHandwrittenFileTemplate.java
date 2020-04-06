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

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.codegen.mc2cd.TransformationHelper;
import de.monticore.utils.Names;
import de.se_rwth.commons.Joiners;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2java.CD2JavaGeneratorHelper;

public class CDHandwrittenFileTemplate implements CDConsumerTemplate<ASTCDCompilationUnit> {
    private ASTCDCompilationUnit cdCompilationUnit;

    public CDHandwrittenFileTemplate(){
    }

    @Override
    public void visit(ASTCDCompilationUnit ast){
        cdCompilationUnit = ast;
    }

    @Override
    public void visit(ASTCDType ast){
        String packageName = Joiners.DOT.join(cdCompilationUnit.getPackageList());
        String qualifiedName = Names.getQualifiedName(packageName, ast.getName());

        //Rename the class to avoid duplicates
        if(TransformationHelper.existsHandwrittenClass(CD2JavaGeneratorHelper.SOURCES, qualifiedName))
            ast.setName(ast.getName() + CDGeneratorHelper.TOP_POSTFIX);
    }
}
