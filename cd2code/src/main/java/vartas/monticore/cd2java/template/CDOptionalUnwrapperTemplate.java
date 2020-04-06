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

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.codegen.mc2cd.TransformationHelper;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mccollectiontypes._ast.ASTMCOptionalType;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;

public class CDOptionalUnwrapperTemplate implements CDConsumerTemplate<ASTCDCompilationUnit> {
    private final CDGeneratorHelper generatorHelper;

    public CDOptionalUnwrapperTemplate(CDGeneratorHelper generatorHelper){
        this.generatorHelper = generatorHelper;
    }

    @Override
    public void visit(ASTCDAttribute ast){
        ast.accept(new CDAttributeVisitor());
    }

    private class CDAttributeVisitor implements CD2CodeVisitor {
        private ASTCDAttribute cdAttribute;

        @Override
        public void visit(ASTCDAttribute ast) {
            cdAttribute = ast;
        }

        public void handle(ASTMCOptionalType ast) {
            //Optional.empty() represented by null
            generatorHelper.getGlex().replaceTemplate(
                    CDGeneratorHelper.ANNOTATION_TEMPLATE,
                    cdAttribute,
                    new TemplateHookPoint(CDGeneratorHelper.NULLABLE_TEMPLATE)
            );

            TransformationHelper.createType("Object");

            //Optional<?> -> Object, otherwise Optional<X> -> X
            ASTMCTypeArgument mcTypeArgument = ast.getMCTypeArgument();
            ASTMCType mcType = mcTypeArgument.getMCTypeOpt().orElse(TransformationHelper.createType("Object"));

            cdAttribute.setMCType(mcType);

            TransformationHelper.addStereoType(cdAttribute, CDGeneratorHelper.NULLABLE_STEREOVALUE.getName(), null);
        }
    }
}
