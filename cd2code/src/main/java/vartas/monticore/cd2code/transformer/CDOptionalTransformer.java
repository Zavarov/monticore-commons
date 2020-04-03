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

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDStereotype;
import de.monticore.cd.cd4analysis._ast.ASTModifier;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.StringHookPoint;
import de.monticore.types.MCTypeFacade;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mccollectiontypes._ast.ASTMCOptionalType;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code.DecoratorHelper;
import vartas.monticore.cd2code._ast.CD2CodeMill;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;

import javax.annotation.Nullable;

public class CDOptionalTransformer implements CD2CodeVisitor {
    private final GlobalExtensionManagement glex;
    private final ASTCDAttribute cdAttribute;
    public CDOptionalTransformer(GlobalExtensionManagement glex,ASTCDAttribute cdAttribute){
        this.glex = glex;
        this.cdAttribute = cdAttribute;
    }

    public static void apply(ASTCDClass cdClass, GlobalExtensionManagement glex){
        cdClass.forEachCDAttributes(cdAttribute -> apply(cdAttribute, glex));
    }

    public static void apply(ASTCDAttribute cdAttribute, GlobalExtensionManagement glex){
        CDOptionalTransformer visitor = new CDOptionalTransformer(glex, cdAttribute);
        cdAttribute.accept(visitor);
    }

    public void handle(ASTMCOptionalType ast){
        //Optional.empty() represented by null
        glex.replaceTemplate(
                CDGeneratorHelper.ANNOTATION_TEMPLATE,
                cdAttribute,
                new StringHookPoint("@"+ Nullable.class.getName() +"\n")
        );

        //Optional<?> -> Object, otherwise Optional<X> -> X
        ASTMCTypeArgument mcTypeArgument = ast.getMCTypeArgument();
        ASTMCType mcDefaultType = MCTypeFacade.getInstance().createQualifiedType("Object");

        cdAttribute.setMCType(mcTypeArgument.getMCTypeOpt().orElse(mcDefaultType));

        //Initialize modifier and stereotype if not present
        ASTModifier cdDefaultModifier = CD2CodeMill.modifierBuilder().build();
        ASTCDStereotype cdDefaultStereotype = CD2CodeMill.cDStereotypeBuilder().build();

        if(!cdAttribute.isPresentModifier())
            cdAttribute.setModifier(cdDefaultModifier);
        if(!cdAttribute.getModifier().isPresentStereotype())
            cdAttribute.getModifier().setStereotype(cdDefaultStereotype);

        //Mark the transformed attribute as nullable
        cdAttribute.getModifier().getStereotype().addValue(DecoratorHelper.NULLABLE_STEREOVALUE);
    }
}
