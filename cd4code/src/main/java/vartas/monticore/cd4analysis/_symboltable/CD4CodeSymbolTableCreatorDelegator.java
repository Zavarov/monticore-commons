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

package vartas.monticore.cd4analysis._symboltable;

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4code._symboltable.CD4CodeArtifactScope;
import de.monticore.cd.cd4code._symboltable.ICD4CodeGlobalScope;
import de.monticore.symboltable.ImportStatement;
import de.se_rwth.commons.Joiners;
import de.se_rwth.commons.Names;

import java.util.stream.Collectors;

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
    public CD4CodeArtifactScope createFromAST(ASTCDCompilationUnit ast){
        CD4CodeArtifactScope artifactScope = super.createFromAST(ast);

        //TODO Manually patch the ArtifactScope since packages and imports are clearly overrated
        artifactScope.setPackageName(Names.getQualifiedName(ast.getPackageList()));
        artifactScope.setImportList(ast.getMCImportStatementList().stream().map(mcImportStatement -> new ImportStatement(mcImportStatement.getQName(), mcImportStatement.isStar())).collect(Collectors.toList()));
        //TODO Since CD4CodeArtifactScope is not an instance of CD4AnalysisScope, the package is not used -> Repurpose the name as package
        artifactScope.setName(Joiners.DOT.join(ast.getPackageList()));
        //TODO Packages apparently cease to exist in super languages (See above)
        artifactScope.setPackageName(Joiners.DOT.join(ast.getPackageList()));
        return artifactScope;
    }
}
