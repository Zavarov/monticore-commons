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

import de.monticore.cd.cd4analysis._ast.ASTCD4AnalysisNode;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code.DecoratorHelper;
import vartas.monticore.cd2code._visitor.CD2CodeInheritanceVisitor;

public class CDAnnotatorTransformer implements CD2CodeInheritanceVisitor {
    private final GlobalExtensionManagement glex;

    public CDAnnotatorTransformer(GlobalExtensionManagement glex){
        this.glex = glex;
    }

    public static void apply(ASTCD4AnalysisNode ast, GlobalExtensionManagement glex){
        CDAnnotatorTransformer visitor = new CDAnnotatorTransformer(glex);
        ast.accept(visitor);
    }

    @Override
    public void visit(ASTCD4AnalysisNode ast){
        if(CDGeneratorHelper.hasStereoValue(ast, DecoratorHelper.NONNULL_STEREOVALUE))
            glex.replaceTemplate(
                    CDGeneratorHelper.ANNOTATION_TEMPLATE,
                    ast,
                    new TemplateHookPoint(CDGeneratorHelper.NONNULL_TEMPLATE)
            );
        else if(CDGeneratorHelper.hasStereoValue(ast, DecoratorHelper.NULLABLE_STEREOVALUE))
            glex.replaceTemplate(
                    CDGeneratorHelper.ANNOTATION_TEMPLATE,
                    ast,
                    new TemplateHookPoint(CDGeneratorHelper.NULLABLE_TEMPLATE)
            );
    }
}
