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

package zav.mc.monticore.cd4code.visitor;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import zav.mc.cd4code.decorator.CDDecoratorGenerator;
import zav.mc.cd4code.visitor.CDVisitorGenerator;
import zav.mc.monticore.cd4code.BasicCDGeneratorTest;

public class CDVisitorGeneratorTest extends BasicCDGeneratorTest {
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
            "vartas.monticore.cd4code.visitor.Visitor",
            "vartas.monticore.cd4code.visitor.InheritanceVisitor"
    })
    public void testGenerate(String modelPath){
        CDDecoratorGenerator decorator;
        decorator = new CDDecoratorGenerator(setup ,helper, null);

        ASTCDDefinition node = globalScope.resolveCDDefinition(modelPath).map(CDDefinitionSymbol::getAstNode).orElseThrow();
        generator.generate(node);
        decorator.generate(node);
    }
}
