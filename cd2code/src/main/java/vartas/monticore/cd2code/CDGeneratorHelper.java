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

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mccollectiontypes._ast.ASTMCListType;
import de.monticore.types.mccollectiontypes._ast.ASTMCMapType;
import de.monticore.types.mccollectiontypes._ast.ASTMCOptionalType;
import de.monticore.types.mccollectiontypes._ast.ASTMCSetType;
import de.se_rwth.commons.Joiners;
import de.se_rwth.commons.Splitters;
import org.apache.commons.lang3.StringUtils;
import vartas.monticore.cd2code._ast.CD2CodeMill;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._ast.ASTMCCacheType;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CDGeneratorHelper {@Nonnull
public static final String CLASS_TEMPLATE = "core.Class";
    @Nonnull
    public static final String ENUM_TEMPLATE = "core.Enum";
    @Nonnull
    public static final String INTERFACE_TEMPLATE = "core.Interface";
    @Nonnull
    public static final String ANNOTATION_TEMPLATE = "hook.Annotation";
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
    private final ASTCDCompilationUnit ast;

    public CDGeneratorHelper(@Nonnull ASTCDCompilationUnit ast){
        this.ast = ast;
    }

    public List<String> getPackageList(){
        return ast.getPackageList();
    }

    public List<String> getPackageList(String subPackage){
        List<String> packageList = new ArrayList<>(getPackageList());
        packageList.addAll(Splitters.DOT.splitToList(subPackage));
        return packageList;
    }

    public String getPackage(){
        return Joiners.DOT.join(getPackageList());
    }

    public String getPackage(String subPackage){
        return Joiners.DOT.join(getPackage(), subPackage);
    }

    public Path getPackagePath(){
        return getPackagePath(getPackageList());
    }

    public Path getPackagePath(String subPackage){
        return getPackagePath(getPackageList(subPackage));
    }

    public Path getPackagePath(List<String> packageList){
        return Paths.get("", packageList.toArray(String[]::new));
    }

    public ASTMCImportStatement getPackageAsImport(){
        return getPackageAsImport(getPackageList());
    }

    public ASTMCImportStatement getPackageAsImport(String subPackage){
        return getPackageAsImport(getPackageList(subPackage));
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
