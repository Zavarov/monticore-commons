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
import de.monticore.generating.templateengine.HookPoint;
import de.monticore.generating.templateengine.StringHookPoint;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import vartas.monticore.cd2code.CDGeneratorHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CDImportTransformer implements CDConsumerTemplate<ASTCDCompilationUnit> {
    private final CDGeneratorHelper generatorHelper;

    public CDImportTransformer(CDGeneratorHelper generatorHelper){
        this.generatorHelper = generatorHelper;
    }

    @Override
    public void handle(ASTCDCompilationUnit ast){
        generatorHelper.getGlex().replaceTemplate(
                CDGeneratorHelper.IMPORT_TEMPLATE,
                getHookPoints(ast)
        );
    }

    private List<HookPoint> getHookPoints(ASTCDCompilationUnit ast){
        return getImports(ast).stream().map(StringHookPoint::new).collect(Collectors.toUnmodifiableList());
    }

    private List<String> getImports(ASTCDCompilationUnit ast){
        return getMcImports(ast).stream().map(mcImport -> mcImport.printType() + "\n").collect(Collectors.toUnmodifiableList());
    }

    private List<ASTMCImportStatement> getMcImports(ASTCDCompilationUnit ast){
        List<ASTMCImportStatement> mcImports = new ArrayList<>(ast.getMCImportStatementList());
        //The default & visitor package will always exist
        mcImports.add(generatorHelper.getPackageAsImport());
        mcImports.add(generatorHelper.getPackageAsImport(CDGeneratorHelper.VISITOR_PACKAGE));

        return mcImports;
    }
}
