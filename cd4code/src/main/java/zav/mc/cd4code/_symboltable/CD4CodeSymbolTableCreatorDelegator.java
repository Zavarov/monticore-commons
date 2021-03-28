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

package zav.mc.cd4code._symboltable;

import com.google.common.collect.ImmutableList;
import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4code._symboltable.ICD4CodeArtifactScope;
import de.monticore.cd.cd4code._symboltable.ICD4CodeGlobalScope;
import de.monticore.symboltable.ImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.se_rwth.commons.Names;
import zav.mc.cd4code.CDGeneratorHelper;

import java.util.ArrayList;

/**
 * This class patches the original createFromAST method by including the package, as well as the
 * all import statements of the underlying ASTCDCompilationUnit.
 */
public class CD4CodeSymbolTableCreatorDelegator extends de.monticore.cd.cd4code._symboltable.CD4CodeSymbolTableCreatorDelegator {
    public CD4CodeSymbolTableCreatorDelegator(ICD4CodeGlobalScope globalScope) {
        super(globalScope);

        //Apply the CD4Analysis patch for CD4Code symbols
        CD4AnalysisSTCForCD4Code cD4AnalysisSymbolTableCreator = new CD4AnalysisSTCForCD4Code(this.scopeStack);
        this.setCD4AnalysisVisitor(cD4AnalysisSymbolTableCreator);
    }

    @Override
    public ICD4CodeArtifactScope createFromAST(ASTCDCompilationUnit ast){
        ICD4CodeArtifactScope artifactScope = super.createFromAST(ast);
        //TODO Manually patch the ArtifactScope since packages and imports are clearly overrated
        artifactScope.setPackageName(Names.constructQualifiedName(ast.getPackageList()));

        //Make imports mutable because how could those POSSIBLY be broken, right?
        artifactScope.setImportsList(new ArrayList<>(artifactScope.getImportsList()));

        for(ASTMCImportStatement importStatement : ast.getMCImportStatementList())
            artifactScope.addImports(new ImportStatement(importStatement.getQName(), importStatement.isStar()));
        //TODO Imagine that: Classes may inherit from a different class diagram, so their imports need to be included
        for(CDDefinitionSymbol definition : artifactScope.getLocalCDDefinitionSymbols())
            for(String importStatement : CDGeneratorHelper.getImports(definition))
                artifactScope.addImports(new ImportStatement(importStatement, false));

        //TODO The definition symbols also need a copy of the import statements
        for(CDDefinitionSymbol definition : artifactScope.getLocalCDDefinitionSymbols())
            for(ImportStatement importStatement : artifactScope.getImportsList())
                definition.addImport(importStatement.getStatement());


        //Make imports immutable again
        artifactScope.setImportsList(ImmutableList.copyOf(artifactScope.getImportsList()));

        return artifactScope;
    }
}
