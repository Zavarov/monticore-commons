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

package vartas.monticore.cd2code.transformer;

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import vartas.monticore.cd2code.CDGeneratorHelper;

public class CDPackageTransformer {
    public static void applyDefaultPackage(ASTCDCompilationUnit ast, GlobalExtensionManagement glex, CDGeneratorHelper genHelper){
        glex.replaceTemplate(
                CDGeneratorHelper.PACKAGE_TEMPLATE,
                new TemplateHookPoint("core.Package", genHelper.getPackageList())
        );
    }

    public static void applyFactoryPackage(ASTCDCompilationUnit ast, GlobalExtensionManagement glex, CDGeneratorHelper genHelper){
        glex.replaceTemplate(
                CDGeneratorHelper.PACKAGE_TEMPLATE,
                new TemplateHookPoint("core.Package", genHelper.getPackageList(CDGeneratorHelper.FACTORY_PACKAGE))
        );
    }

    public static void applyVisitorPackage(ASTCDCompilationUnit ast, GlobalExtensionManagement glex, CDGeneratorHelper genHelper){
        glex.replaceTemplate(
                CDGeneratorHelper.PACKAGE_TEMPLATE,
                new TemplateHookPoint("core.Package", genHelper.getPackageList(CDGeneratorHelper.VISITOR_PACKAGE))
        );
    }
}
