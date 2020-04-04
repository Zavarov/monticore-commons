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
import de.monticore.generating.templateengine.StringHookPoint;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code._visitor.CD2CodeInheritanceVisitor;

public class CDImportTransformer implements CD2CodeInheritanceVisitor {
    private static void apply(ASTCDCompilationUnit ast, StringBuilder builder){
        for(ASTMCImportStatement mcImportStatement : ast.getMCImportStatementList())
            builder.append(mcImportStatement.printType()).append("\n");
    }

    public static void applyDefaultPackage(ASTCDCompilationUnit ast, GlobalExtensionManagement glex, CDGeneratorHelper genHelper){
        StringBuilder builder = new StringBuilder();

        apply(ast, builder);

        //Visitor
        ASTMCImportStatement mcImportStatement = genHelper.getPackageAsImport(CDGeneratorHelper.VISITOR_PACKAGE);
        builder.append(mcImportStatement.printType()).append("\n");

        glex.replaceTemplate(CDGeneratorHelper.IMPORT_TEMPLATE, new StringHookPoint(builder.toString()));
    }

    public static void applyFactoryPackage(ASTCDCompilationUnit ast, GlobalExtensionManagement glex, CDGeneratorHelper genHelper){
        StringBuilder builder = new StringBuilder();

        apply(ast, builder);

        //Default package
        ASTMCImportStatement mcImportStatement = genHelper.getPackageAsImport();
        builder.append(mcImportStatement.printType()).append("\n");

        glex.replaceTemplate(CDGeneratorHelper.IMPORT_TEMPLATE, new StringHookPoint(builder.toString()));
    }

    public static void applyVisitorPackage(ASTCDCompilationUnit ast, GlobalExtensionManagement glex, CDGeneratorHelper genHelper){
        StringBuilder builder = new StringBuilder();

        apply(ast, builder);

        //Default package
        ASTMCImportStatement mcImportStatement = genHelper.getPackageAsImport();
        builder.append(mcImportStatement.printType()).append("\n");

        glex.replaceTemplate(CDGeneratorHelper.IMPORT_TEMPLATE, new StringHookPoint(builder.toString()));
    }
}
