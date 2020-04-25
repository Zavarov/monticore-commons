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

import de.monticore.cd.cd4analysis._symboltable.CD4AnalysisLanguage;
import de.monticore.cd.cd4code._symboltable.ICD4CodeGlobalScope;
import de.se_rwth.commons.Names;

import java.util.Collections;
import java.util.Set;

/**
 * This class defines implements the header of the CD4Code language and
 * links to the patched symbol table creator.
 */
public class CD4CodeLanguage extends de.monticore.cd.cd4code._symboltable.CD4CodeLanguage {
    public CD4CodeLanguage() {
        super("CD4Code Language", CD4AnalysisLanguage.FILE_ENDING);
    }

    @Override
    public CD4CodeSymbolTableCreatorDelegator getSymbolTableCreator(ICD4CodeGlobalScope enclosingScope) {
        return new CD4CodeSymbolTableCreatorDelegator(enclosingScope);
    }

    /**
     * Determine the name of the class diagram, given the CD Type.
     * @param name
     * @return
     */
    @Override
    protected Set<String> calculateModelNamesForCDType(String name){
        return Names.getQualifier(name).isEmpty() ? Collections.emptySet() : Collections.singleton(Names.getQualifier(name));
    }
}
