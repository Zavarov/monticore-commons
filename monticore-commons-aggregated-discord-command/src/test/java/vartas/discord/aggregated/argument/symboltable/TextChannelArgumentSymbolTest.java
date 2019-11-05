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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class TextChannelArgumentSymbolTest extends AbstractArgumentSymbolTest{
    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[] {
                "example.textchannel \""+channelName+"\"",
                "example.textchannel "+channelName,
                "example.textchannel "+channelId,
                "example.textchannel <#"+channelId+">"
        };
    }

    @Parameterized.Parameter
    public String argument;

    @Before
    public void setUp(){
        parse(argument);
    }

    @Test
    public void testAccept(){
        String name = getParameters(symbol).get(0).getName();
        ASTArgument argument = ast.getArgumentList().get(0);

        TextChannelArgumentSymbol symbol = new TextChannelArgumentSymbol(name);
        symbol.setAstNode(argument);

        assertThat(symbol.accept(message)).contains(channel);
    }
}
