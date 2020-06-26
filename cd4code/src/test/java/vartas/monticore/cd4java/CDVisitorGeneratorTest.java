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

package vartas.monticore.cd4java;

import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import vartas.monticore.cd4analysis.CDDecoratorGenerator;
import vartas.monticore.cd4analysis.CDVisitorGenerator;

public class CDVisitorGeneratorTest extends BasicCDGeneratorTest{
    CDVisitorGenerator generator;

    @BeforeEach
    public void setUp(){
        super.setUp();

        generator = new CDVisitorGenerator(
                setup,
                helper,
                globalScope
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "vartas.monticore.cd4code.Visitor",
            "vartas.monticore.cd4code.InheritanceVisitor"
    })
    public void testGenerate(String modelPath){
        CDDecoratorGenerator decorator;
        decorator = new CDDecoratorGenerator(setup ,helper);

        CDDefinitionSymbol cdDefinitionSymbol = globalScope.resolveCDDefinition(modelPath).orElseThrow();
        generator.generate(cdDefinitionSymbol);
        decorator.generate(cdDefinitionSymbol);
    }
}
