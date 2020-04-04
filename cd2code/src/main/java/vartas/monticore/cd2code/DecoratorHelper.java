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

package vartas.monticore.cd2code;

import com.google.common.base.Preconditions;
import de.monticore.cd.cd4analysis._ast.ASTCD4AnalysisNode;
import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDField;
import de.monticore.cd.cd4analysis._ast.ASTCDStereoValue;
import de.monticore.types.mcbasictypes._ast.ASTMCBasicTypesNode;
import de.monticore.types.mccollectiontypes._ast.ASTMCCollectionTypesNode;
import de.monticore.types.mccollectiontypes._ast.ASTMCGenericType;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import org.apache.commons.lang3.StringUtils;
import org.atteo.evo.inflector.English;
import vartas.monticore.cd2code._ast.CD2CodeMill;
import vartas.monticore.cd2code._visitor.CD2CodeInheritanceVisitor;
import vartas.monticore.cd2code.prettyprint.CD2CodePrettyPrinter;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._visitor.CD2CodeCollectionTypesInheritanceVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DecoratorHelper {
    public static final ASTCDStereoValue JSON_STEREOVALUE = CD2CodeMill.cDStereoValueBuilder().setName("json").build();
    public static final ASTCDStereoValue NULLABLE_STEREOVALUE = CD2CodeMill.cDStereoValueBuilder().setName("Nullable").build();
    public static final ASTCDStereoValue NONNULL_STEREOVALUE = CD2CodeMill.cDStereoValueBuilder().setName("Nonnull").build();
    public static final ASTCDStereoValue CACHED_STEREOVALUE = CD2CodeMill.cDStereoValueBuilder().setName("cached").build();
    public static final ASTCDStereoValue FACTORY_STEREOVALUE = CD2CodeMill.cDStereoValueBuilder().setName("factory").build();
    private static CD2CodePrettyPrinter PRINTER = new CD2CodePrettyPrinter();

    public static ASTMCTypeArgument getMCTypeArgument(@Nonnull ASTCDAttribute cdAttribute, int genericTypeIndex){
        return ArgumentTypeVisitor.getGenericTypeArgumentOf(cdAttribute, genericTypeIndex);
    }

    public static String getMCTypeArgumentName(@Nonnull ASTCDAttribute cdAttribute, int genericTypeIndex){
        ASTMCTypeArgument mcTypeArgument = getMCTypeArgument(cdAttribute, genericTypeIndex);

        return prettyprint(mcTypeArgument);
    }

    public static void setPrinter(CD2CodePrettyPrinter printer){
        PRINTER = printer;
    }

    public static String prettyprint(ASTMCCollectionTypesNode ast){
        return PRINTER.prettyprint(ast);
    }

    public static String prettyprint(ASTMCBasicTypesNode ast){
        return PRINTER.prettyprint(ast);
    }

    public static String prettyprint(ASTCD4AnalysisNode ast){
        return PRINTER.prettyprint(ast);
    }


    public static String toSingular(ASTCDField cdField){
        return English.plural(cdField.getName(), 1);
    }

    public static String toPlural(ASTCDField cdField){
        return English.plural(cdField.getName());
    }

    public static String toSingularCapitalized(ASTCDField cdField){
        return English.plural(getCapitalizedName(cdField), 1);
    }

    public static String toPluralCapitalized(ASTCDField cdField){
        return English.plural(getCapitalizedName(cdField));
    }

    public static String getCapitalizedName(ASTCDField cdField){
        return StringUtils.capitalize(cdField.getName());
    }

    @Nonnull
    private static class ArgumentTypeVisitor implements CD2CodeInheritanceVisitor, CD2CodeCollectionTypesInheritanceVisitor {
        private int genericTypeIndex;
        @Nullable
        private ASTMCTypeArgument genericTypeArgument;

        private ArgumentTypeVisitor(int genericTypeIndex){
            Preconditions.checkArgument(genericTypeIndex >= 0);
            this.genericTypeIndex = genericTypeIndex;
        }

        @Nonnull
        public static ASTMCTypeArgument getGenericTypeArgumentOf(@Nonnull ASTCDAttribute cdAttribute, int genericTypeIndex){
            ArgumentTypeVisitor visitor = new ArgumentTypeVisitor(genericTypeIndex);
            cdAttribute.accept(visitor);
            return Preconditions.checkNotNull(visitor.genericTypeArgument);
        }

        @Override
        public void visit(@Nonnull ASTMCGenericType ast){
            if(ast.getMCTypeArgumentList().size() > genericTypeIndex)
                genericTypeArgument = ast.getMCTypeArgument(genericTypeIndex);
        }
    }
}
