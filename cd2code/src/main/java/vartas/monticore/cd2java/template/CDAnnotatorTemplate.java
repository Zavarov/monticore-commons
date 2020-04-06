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
import de.monticore.generating.templateengine.TemplateHookPoint;
import vartas.monticore.cd2code.CDGeneratorHelper;

public class CDAnnotatorTemplate implements CDConsumerTemplate<ASTCDCompilationUnit> {
    private final CDGeneratorHelper generatorHelper;

    public CDAnnotatorTemplate(CDGeneratorHelper generatorHelper){
        this.generatorHelper = generatorHelper;
    }

    @Override
    public void visit(ASTCDType ast){
        if(CDGeneratorHelper.hasStereoValue(ast, CDGeneratorHelper.NONNULL_STEREOVALUE))
            generatorHelper.getGlex().replaceTemplate(
                    CDGeneratorHelper.ANNOTATION_TEMPLATE,
                    ast,
                    new TemplateHookPoint(CDGeneratorHelper.NONNULL_TEMPLATE)
            );
        else if(CDGeneratorHelper.hasStereoValue(ast, CDGeneratorHelper.NULLABLE_STEREOVALUE))
            generatorHelper.getGlex().replaceTemplate(
                    CDGeneratorHelper.ANNOTATION_TEMPLATE,
                    ast,
                    new TemplateHookPoint(CDGeneratorHelper.NULLABLE_TEMPLATE)
            );
    }
}
