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

import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.generating.GeneratorSetup;
import vartas.monticore.cd4analysis.template.CDConsumerTemplate;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class CDTemplateGenerator extends CDGenerator{
    private final List<CDConsumerTemplate> templateList;

    public CDTemplateGenerator(@Nonnull GeneratorSetup generatorSetup, @Nonnull CDGeneratorHelper generatorHelper) {
        this(generatorSetup, generatorHelper, Collections.emptyList());
    }

    public CDTemplateGenerator(
            @Nonnull GeneratorSetup generatorSetup,
            @Nonnull CDGeneratorHelper generatorHelper,
            @Nonnull List<CDConsumerTemplate> templateList
    )
    {
        super(generatorSetup, generatorHelper);
        this.templateList = templateList;
    }

    @Override
    public void generate(@Nonnull CDDefinitionSymbol cdDefinitionSymbol){
        for(CDConsumerTemplate template : templateList)
            template.accept(cdDefinitionSymbol.getAstNode());

        super.generate(cdDefinitionSymbol);
    }
}
