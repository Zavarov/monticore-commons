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

package vartas.monticore.cd4analysis.preprocessor.process;

import com.google.common.collect.Lists;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.se_rwth.commons.Splitters;
import vartas.monticore.cd4analysis.CDGeneratorHelper;

import java.util.List;

public class CDSetPackageForTypesProcess extends CDProcess {
    public CDSetPackageForTypesProcess(GlobalExtensionManagement glex) {
        super(glex);
    }

    @Override
    public void visit(ASTCDType ast){
        String packageName = ast.getSymbol().getPackageName();
        List<String> packageList = Lists.newArrayList(Splitters.DOT.split(packageName));
        glex.replaceTemplate(
                CDGeneratorHelper.PACKAGE_HOOK,
                ast,
                new TemplateHookPoint("core.Package", packageList)
        );
    }
}
