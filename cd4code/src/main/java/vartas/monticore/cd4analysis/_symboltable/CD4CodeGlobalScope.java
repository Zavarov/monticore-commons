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

import de.monticore.cd.cd4code._symboltable.CD4CodeLanguage;
import de.monticore.cd.cd4code._symboltable.ICD4CodeScope;
import de.monticore.io.paths.ModelPath;

/**
 * This class patches #getEnclosingScope() to not cause an error,
 * in order to have determineFullName() work for all symbols.
 */
public class CD4CodeGlobalScope extends de.monticore.cd.cd4code._symboltable.CD4CodeGlobalScope {
    public CD4CodeGlobalScope(ModelPath modelPath, CD4CodeLanguage language) {
        super(modelPath, language);
    }

    @Override
    public ICD4CodeScope getEnclosingScope() {
        return null;
    }
}
