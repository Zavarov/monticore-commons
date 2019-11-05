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

import de.se_rwth.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import vartas.discord.argument._ast.ASTArgument;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class DateArgumentSymbolTest extends AbstractArgumentSymbolTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "example.date 11-11-2000", "11-11-2000", true},
                { "example.date x-11-2000", "", false},
                { "example.date 11-x-2000", "", false},
                { "example.date 11-11-x", "", false}
        });
    }

    @Parameterized.Parameter
    public String argument;

    @Parameterized.Parameter(1)
    public String expected;

    @Parameterized.Parameter(2)
    public boolean valid;

    protected SimpleDateFormat format;

    @Before
    public void setUp(){
        parse(argument);
        format = new SimpleDateFormat("dd-MM-yyyy");
    }

    @Test
    public void testAccept(){
        String name = getParameters(symbol).get(0).getName();
        ASTArgument argument = ast.getArgumentList().get(0);

        DateArgumentSymbol symbol = new DateArgumentSymbol(name);
        symbol.setAstNode(argument);

        if(valid) {
            assertThat(symbol.accept().map(format::format)).contains(expected);

            symbol.dateFormat = new SimpleDateFormat("");

            symbol.accept();

            assertThat(Log.getFindings()).isNotEmpty();
        }else {
            assertThat(symbol.accept()).isNotPresent();
        }
    }
}
