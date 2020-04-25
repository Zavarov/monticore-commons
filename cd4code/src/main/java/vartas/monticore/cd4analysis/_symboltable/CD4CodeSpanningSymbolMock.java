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

import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;

/**
 * Patches the dependency for a CD4AnalysisArtifactScope when determining the symbol package
 * by creating a mock CDDefinitionSymbol already containing the package name.
 */
public class CD4CodeSpanningSymbolMock extends CDDefinitionSymbol {
    public CD4CodeSpanningSymbolMock(String name) {
        super(name);
    }
}
