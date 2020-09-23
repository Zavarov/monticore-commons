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

package vartas.monticore.cd4code.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.generating.GeneratorSetup;
import vartas.monticore.cd4code.CDGeneratorHelper;
import vartas.monticore.cd4code.CDPreprocessorGenerator;
import vartas.monticore.cd4code._symboltable.CD4CodeGlobalScope;

import javax.annotation.Nonnull;

public class CDDecoratorGenerator extends CDPreprocessorGenerator {
    public CDDecoratorGenerator(@Nonnull GeneratorSetup generatorSetup, @Nonnull CDGeneratorHelper generatorHelper, CD4CodeGlobalScope globalScope) {
        super(
                generatorSetup,
                generatorHelper
        );
    }

    @Override
    public void generate(@Nonnull ASTCDDefinition node){
        node = DecoratorCreator.create(node.deepClone(), generatorSetup.getGlex());
        super.generate(node);
    }
}
