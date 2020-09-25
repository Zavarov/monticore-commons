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

package vartas.monticore.cd4code.preprocessor;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4analysis._symboltable.Stereotype;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.se_rwth.commons.Joiners;
import vartas.monticore.cd4code.CDGeneratorHelper;

import java.util.Optional;

public class CDInitializeContainerProcess extends CDProcess {
    public CDInitializeContainerProcess(GlobalExtensionManagement glex) {
        super(glex);
    }

    @Override
    public void visit(ASTCDAttribute ast){
            CDTypeSymbol cdTypeSymbol = ast.getSymbol().getType();
            //Only containers need to be initialized
            if(cdTypeSymbol.getStereotype("container").isPresent())
                initialize(ast);
    }

    private void initialize(ASTCDAttribute ast){
        Optional<String> argument = ast.getSymbol().getStereotype(CDGeneratorHelper.INITIALIZER_MODULE).map(Stereotype::getValue);
        glex.replaceTemplate(CDGeneratorHelper.VALUE_HOOK, ast, new TemplateHookPoint(getTemplateName(ast), argument));
    }

    private String getTemplateName(ASTCDAttribute ast){
        CDTypeSymbol cdTypeSymbol = ast.getSymbol().getType();

        String moduleName = CDGeneratorHelper.INITIALIZER_MODULE;
        String packageName = cdTypeSymbol.getPackageName();
        String typeName = cdTypeSymbol.getName();

        return Joiners.DOT.join(moduleName, packageName, typeName);
    }
}
