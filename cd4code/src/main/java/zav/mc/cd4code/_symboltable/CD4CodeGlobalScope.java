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

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4code._parser.CD4CodeParser;
import de.monticore.cd.cd4code._symboltable.CD4CodeSymbolTableCreatorDelegator;
import de.monticore.io.paths.ModelPath;
import de.monticore.modelloader.ParserBasedAstProvider;

import java.util.Optional;

/**
 * This class patches #getEnclosingScope() to not cause an error,
 * in order to have determineFullName() work for all symbols.
 */
public class CD4CodeGlobalScope extends de.monticore.cd.cd4code._symboltable.CD4CodeGlobalScope {
    public CD4CodeGlobalScope(ModelPath modelPath, String modelFileExtension) {
        super(modelPath, modelFileExtension);
    }

    /**
     * CD4Code is still barely functional
     */
    public void enableModelLoader() {
        ParserBasedAstProvider<ASTCDCompilationUnit> astProvider = new ParserBasedAstProvider<>(new CD4CodeParser(), "CD4Code");
        //TODO Use the patched STC
        CD4CodeSymbolTableCreatorDelegator stc = new zav.mc.cd4code._symboltable.CD4CodeSymbolTableCreatorDelegator(getRealThis());
        //TODO Use the patched model loader
        CD4CodeModelLoader ml = new CD4CodeModelLoader(astProvider, stc, modelFileExtension);
        this.modelLoader = Optional.of(ml);
    }
}
