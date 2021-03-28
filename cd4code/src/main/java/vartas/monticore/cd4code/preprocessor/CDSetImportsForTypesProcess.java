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

package vartas.monticore.cd4code.preprocessor;

import com.google.common.collect.Lists;
import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.se_rwth.commons.Joiners;
import de.se_rwth.commons.Splitters;
import vartas.monticore.cd4code.CDGeneratorHelper;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CDSetImportsForTypesProcess extends CDProcess {
    private Set<List<String>> importSet;

    public CDSetImportsForTypesProcess(GlobalExtensionManagement glex) {
        super(glex);
    }

    @Override
    public void visit(ASTCDType ast){
        //Regenerate the imports for every type
        importSet = new TreeSet<>(Comparator.comparing(Joiners.DOT::join));
        //Load imports for this CD
        loadImports(ast.getSymbol());
        ast.getSymbol().getSuperTypesTransitive().forEach(this::loadImports);
    }

    @Override
    public void visit(ASTCDAttribute ast) {
        //Load the imports from the CDs the type is associated with. We need those due to the decorator.
        if (!CDGeneratorHelper.isPrimitive(ast.getMCType())) {
            CDTypeSymbol symbol = ast.getSymbol().getType().lazyLoadDelegate();
            loadImport(symbol); //Everything in the same scope as the symbol may not be explicitly imported
            loadImports(symbol);
            symbol.getSuperTypesTransitive().forEach(this::loadImports);
        }
    }

    @Override
    public void endVisit(ASTCDType ast){
        glex.replaceTemplate(CDGeneratorHelper.IMPORT_HOOK, ast, new TemplateHookPoint("core.Import", importSet));
    }

    private void loadImport(CDTypeSymbol cdTypeSymbol){
        //Import all symbols in the shared scope
        for(CDTypeSymbol symbol : cdTypeSymbol.getEnclosingScope().getLocalCDTypeSymbols()){
            loadImport(symbol.getFullName());
        }
    }

    private void loadImports(CDTypeSymbol cdTypeSymbol){
        //CDType -> CDDefinition::SpannedScope -> CDArtifact::SpannedScope
        for(CDDefinitionSymbol symbol : cdTypeSymbol.getEnclosingScope().getEnclosingScope().getLocalCDDefinitionSymbols())
            loadImports(symbol);
    }

    private void loadImports(CDDefinitionSymbol symbol){
        for(String qualifiedImport : symbol.getImports()){
            loadImport(qualifiedImport);
        }
    }

    private void loadImport(String qualifiedImport){
        List<String> qualifiedNames = Lists.newArrayList(Splitters.DOT.split(qualifiedImport));
        //Remove the Class Diagram from the import.
        qualifiedNames.remove(qualifiedNames.size() - 2);
        importSet.add(qualifiedNames);
    }
}
