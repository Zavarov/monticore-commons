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

import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.utils.Names;
import vartas.monticore.cd4analysis.CDGeneratorHelper;

public class CDBindDecoratorTemplate extends CDConsumerTemplate {
    public CDBindDecoratorTemplate(GlobalExtensionManagement glex) {
        super(glex);
    }

    @Override
    public void visit(ASTCDMethod ast){
        //Use the package path to bind the template
        String templateName = Names.getQualifiedName(CDGeneratorHelper.DECORATOR_MODULE, ast.getSymbol().getFullName());
        glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, new TemplateHookPoint(templateName, ast));
    }
}
