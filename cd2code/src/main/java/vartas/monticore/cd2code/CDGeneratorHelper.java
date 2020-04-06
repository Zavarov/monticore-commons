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
import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.types.mcbasictypes._ast.ASTMCBasicTypesNode;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mccollectiontypes._ast.*;
import de.monticore.utils.Names;
import de.se_rwth.commons.Joiners;
import de.se_rwth.commons.Splitters;
import org.apache.commons.lang3.StringUtils;
import org.atteo.evo.inflector.English;
import vartas.monticore.cd2code._ast.CD2CodeMill;
import vartas.monticore.cd2code._visitor.CD2CodeInheritanceVisitor;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;
import vartas.monticore.cd2code.creator.VisitorCreator;
import vartas.monticore.cd2code.prettyprint.CD2CodePrettyPrinter;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._ast.ASTMCCacheType;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._visitor.CD2CodeCollectionTypesInheritanceVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CDGeneratorHelper {
    //----------------------------------------------------------------------------------------------------------------//
    //
    //        Global Constants
    //
    //----------------------------------------------------------------------------------------------------------------//
    @Nonnull
    public static final String CLASS_TEMPLATE = "core.Class";
    @Nonnull
    public static final String ENUM_TEMPLATE = "core.Enum";
    @Nonnull
    public static final String INTERFACE_TEMPLATE = "core.Interface";
    @Nonnull
    public static final String ANNOTATION_TEMPLATE = "hook.Annotation";
    @Nonnull
    public static final String CONSTRUCTOR_TEMPLATE = "hook.Constructor";
    @Nonnull
    public static final String PACKAGE_TEMPLATE = "hook.Package";
    @Nonnull
    public static final String IMPORT_TEMPLATE = "hook.Import";
    @Nonnull
    public static final String METHOD_TEMPLATE = "hook.Method";
    @Nonnull
    public static final String VALUE_TEMPLATE = "hook.Value";
    @Nonnull
    public static final String ATTRIBUTE_TEMPLATE = "hook.Attribute";
    @Nonnull
    public static final String NULLABLE_TEMPLATE = "annotation.Nullable";
    @Nonnull
    public static final String NONNULL_TEMPLATE = "annotation.Nonnull";
    @Nonnull
    public static final String FACTORY_PACKAGE = "factory";
    @Nonnull
    public static final String VISITOR_PACKAGE = "visitor";
    @Nonnull
    public static final String TOP_POSTFIX = "TOP";
    @Nonnull
    public static final ASTCDStereoValue JSON_STEREOVALUE = CD2CodeMill.cDStereoValueBuilder().setName("json").build();
    @Nonnull
    public static final ASTCDStereoValue NULLABLE_STEREOVALUE = CD2CodeMill.cDStereoValueBuilder().setName("Nullable").build();
    @Nonnull
    public static final ASTCDStereoValue NONNULL_STEREOVALUE = CD2CodeMill.cDStereoValueBuilder().setName("Nonnull").build();
    @Nonnull
    public static final ASTCDStereoValue CACHED_STEREOVALUE = CD2CodeMill.cDStereoValueBuilder().setName("cached").build();
    @Nonnull
    public static final ASTCDStereoValue FACTORY_STEREOVALUE = CD2CodeMill.cDStereoValueBuilder().setName("factory").build();
    @Nonnull
    private static CD2CodePrettyPrinter PRINTER = new CD2CodePrettyPrinter();
    //----------------------------------------------------------------------------------------------------------------//
    //
    //        Local Variables
    //
    //----------------------------------------------------------------------------------------------------------------//
    @Nonnull
    private final GlobalExtensionManagement glex;
    @Nonnull
    private final ASTCDCompilationUnit ast;
    @Nonnull
    private final ASTCDInterface visitor;

    public CDGeneratorHelper(@Nonnull ASTCDCompilationUnit ast, @Nonnull GlobalExtensionManagement glex){
        this.glex = glex;
        this.ast = ast;
        this.visitor = VisitorCreator.create(ast.getCDDefinition(), new GlobalExtensionManagement());
    }

    public CDGeneratorHelper(@Nonnull ASTCDCompilationUnit ast){
        this(ast, new GlobalExtensionManagement());
    }

    @Nonnull
    public GlobalExtensionManagement getGlex(){
        return glex;
    }

    @Nonnull
    public ASTCDInterface getVisitor(){
        return visitor;
    }

    @Nonnull
    public ASTCDCompilationUnit getAst(){
        return ast;
    }

    public String getRootPackage(){
        return Joiners.DOT.join(ast.getPackageList());
    }

    public ASTMCImportStatement getPackageAsImport(){
        return getPackageAsImport(Splitters.DOT.splitToList(getRootPackage()));
    }

    public ASTMCImportStatement getPackageAsImport(String subPackage){
        String qualifiedName = Names.getQualifiedName(getRootPackage(), subPackage);
        return getPackageAsImport(Splitters.DOT.splitToList(qualifiedName));
    }

    private ASTMCImportStatement getPackageAsImport(List<String> packageList){
        ASTMCQualifiedName mcQualifiedName;
        ASTMCImportStatement mcImportStatement;

        mcQualifiedName = CD2CodeMill.mCQualifiedNameBuilder().setPartList(packageList).build();
        mcImportStatement = CD2CodeMill.mCImportStatementBuilder().setMCQualifiedName(mcQualifiedName).build();

        mcImportStatement.setStar(true);

        return mcImportStatement;
    }


    public static String getDefaultSetter(ASTCDField cdField){
        return "set" + StringUtils.capitalize(cdField.getName());
    }

    public static String getDefaultGetter(ASTCDField cdField){
        return "get" + StringUtils.capitalize(cdField.getName());
    }

    public static List<String> getStereoValueValues(ASTCD4AnalysisNode ast, ASTCDStereoValue cdStereoValue){
        return GetStereoValueValuesVisitor.apply(ast, cdStereoValue);
    }

    private static class GetStereoValueValuesVisitor implements CD2CodeVisitor {
        private List<String> stereoValueValues = new ArrayList<>();
        private final String cdStereoValueName;

        private GetStereoValueValuesVisitor(ASTCDStereoValue cdStereoValue){
            this.cdStereoValueName = cdStereoValue.getName().toLowerCase(Locale.ENGLISH);
        }

        public static List<String> apply(ASTCD4AnalysisNode ast, ASTCDStereoValue cdStereoValue){
            GetStereoValueValuesVisitor visitor = new GetStereoValueValuesVisitor(cdStereoValue);
            ast.accept(visitor);
            return visitor.stereoValueValues;
        }

        @Override
        public void visit(ASTCDStereoValue cdStereoValue){
            String cdStereoValueName = cdStereoValue.getName().toLowerCase(Locale.ENGLISH);
            if(this.cdStereoValueName.equals(cdStereoValueName) && cdStereoValue.isPresentValue())
                stereoValueValues.add(cdStereoValue.getValue());
        }
    }
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


    public static boolean hasStereoValue(ASTCD4AnalysisNode ast, ASTCDStereoValue cdStereoValue) {
        return HasStereoValueVisitor.apply(ast, cdStereoValue);
    }

    private static class HasStereoValueVisitor implements CD2CodeVisitor {
        private boolean isPresent = false;
        private final String cdStereoValueName;

        private HasStereoValueVisitor(ASTCDStereoValue cdStereoValue){
            this.cdStereoValueName = cdStereoValue.getName().toLowerCase(Locale.ENGLISH);
        }

        public static boolean apply(ASTCD4AnalysisNode ast, ASTCDStereoValue cdStereoValue){
            HasStereoValueVisitor visitor = new HasStereoValueVisitor(cdStereoValue);
            ast.accept(visitor);
            return visitor.isPresent;
        }

        @Override
        public void visit(ASTCDStereoValue cdStereoValue){
            String cdStereoValueName = cdStereoValue.getName().toLowerCase(Locale.ENGLISH);
            if(this.cdStereoValueName.equals(cdStereoValueName))
                this.isPresent = true;
        }
    }

    public static boolean isMandatory(ASTCDAttribute cdAttribute){
        return IsMandatoryVisitor.apply(cdAttribute);
    }

    private static class IsMandatoryVisitor implements CD2CodeVisitor{
        private boolean isMandatory = true;

        public static boolean apply(ASTCD4AnalysisNode ast){
            IsMandatoryVisitor visitor = new IsMandatoryVisitor();
            ast.accept(visitor);
            return visitor.isMandatory;
        }

        @Override
        public void visit(ASTMCCacheType ast){
            isMandatory = false;
        }

        @Override
        public void visit(ASTMCMapType ast){
            isMandatory = false;
        }

        @Override
        public void visit(ASTMCListType ast){
            isMandatory = false;
        }

        @Override
        public void visit(ASTMCSetType ast){
            isMandatory = false;
        }

        @Override
        public void visit(ASTMCOptionalType ast){
            isMandatory = false;
        }
    }
}
