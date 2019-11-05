/*
 * Copyright (c) 2019 Zavarov
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

package vartas.discord.aggregated.argument.symboltable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import vartas.discord.argument._ast.ASTArgument;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ExpressionArgumentSymbolTest extends AbstractArgumentSymbolTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "example.expression 11-11-2000", -2000},
                { "example.expression 1+2*sin(pi)", 1+2*Math.sin(Math.PI) },
                { "example.expression e-0", Math.E }
        });
    }

    @Parameterized.Parameter
    public String argument;
    @Parameterized.Parameter(1)
    public double expected;

    @Before
    public void setUp(){
        parse(argument);
    }

    @Test
    public void testAccept(){
        String name = getParameters(symbol).get(0).getName();
        ASTArgument argument = ast.getArgumentList().get(0);

        ExpressionArgumentSymbol symbol = new ExpressionArgumentSymbol(name);
        symbol.setAstNode(argument);

        assertThat(symbol.accept().map(BigDecimal::doubleValue)).contains(expected);
    }
}
