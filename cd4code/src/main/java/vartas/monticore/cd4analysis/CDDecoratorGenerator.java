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

package vartas.monticore.cd4analysis;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.generating.GeneratorSetup;
import vartas.monticore.cd4analysis.decorator.CDDefinitionDecorator;
import vartas.monticore.cd4analysis.template.CDBindImportTemplate;
import vartas.monticore.cd4analysis.template.CDBindPackageTemplate;
import vartas.monticore.cd4analysis.template.CDHandwrittenFileTemplate;
import vartas.monticore.cd4analysis.template.CDInitializerTemplate;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class CDDecoratorGenerator extends CDTemplateGenerator{
    private final CDDefinitionDecorator decorator;
    public CDDecoratorGenerator(@Nonnull GeneratorSetup generatorSetup, @Nonnull CDGeneratorHelper generatorHelper) {
        super(
                generatorSetup,
                generatorHelper,
                Arrays.asList(
                        new CDBindPackageTemplate(generatorSetup.getGlex()),
                        new CDBindImportTemplate(generatorSetup.getGlex()),
                        new CDInitializerTemplate(generatorSetup.getGlex()),
                        new CDHandwrittenFileTemplate(generatorSetup.getGlex())
                )
        );
        this.decorator = new CDDefinitionDecorator(generatorSetup.getGlex());
    }

    @Override
    public void generate(@Nonnull CDDefinitionSymbol cdDefinitionSymbol){
        ASTCDDefinition ast = cdDefinitionSymbol.getAstNode();
        ast = decorator.decorate(ast);
        cdDefinitionSymbol.setAstNode(ast);

        super.generate(cdDefinitionSymbol);
    }
}
