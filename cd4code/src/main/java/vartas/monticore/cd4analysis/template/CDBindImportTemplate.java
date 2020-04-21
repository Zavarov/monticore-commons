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

package vartas.monticore.cd4analysis.template;

import com.google.common.collect.Lists;
import de.monticore.cd.cd4analysis._ast.ASTCDField;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.se_rwth.commons.Splitters;
import vartas.monticore.cd4analysis.CDGeneratorHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CDBindImportTemplate extends CDConsumerTemplate{
    private Set<List<String>> importSet = new HashSet<>();

    public CDBindImportTemplate(GlobalExtensionManagement glex) {
        super(glex);
    }

    @Override
    public void visit(ASTCDType ast){
        //Regenerate the imports for every type
        importSet = new HashSet<>();
        //Load imports for this CD
        loadImports(ast.getSymbol());
    }

    @Override
    public void visit(ASTCDField ast){
        //Load the imports from the CDs the type is associated with.
        //We need those due to the decorator.
        ast.getSymbol().getType().loadSymbol().ifPresent(this::loadImports);
    }

    @Override
    public void endVisit(ASTCDType ast){
        glex.replaceTemplate(CDGeneratorHelper.IMPORT_HOOK, ast, new TemplateHookPoint("core.Import", importSet));
    }

    private void loadImports(CDTypeSymbol cdTypeSymbol){
        for(CDDefinitionSymbol symbol : cdTypeSymbol.getSpannedScope().getLocalCDDefinitionSymbols())
            loadImports(symbol);
    }

    private void loadImports(CDDefinitionSymbol symbol){
        for(String qualifiedImport : symbol.getImports())
            importSet.add(Lists.newArrayList(Splitters.DOT.split(qualifiedImport)));
    }
}
