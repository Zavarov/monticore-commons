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

import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import org.junit.Test;
import vartas.discord.argument._ast.ASTArgument;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageArgumentSymbolTest extends AbstractArgumentSymbolTest{
    @Test
    public void testAccept(){
        parse("example.message "+messageId);

        String name = getParameters(symbol).get(0).getName();
        ASTArgument argument = ast.getArgumentList().get(0);

        MessageArgumentSymbol symbol = new MessageArgumentSymbol(name);
        symbol.setAstNode(argument);

        assertThat(symbol.accept(message)).contains(message);
    }

    @Test(expected= ErrorResponseException.class)
    public void testFailure(){
        parse("example.message "+Long.MAX_VALUE);

        String name = getParameters(symbol).get(0).getName();
        ASTArgument argument = ast.getArgumentList().get(0);

        MessageArgumentSymbol symbol = new MessageArgumentSymbol(name);
        symbol.setAstNode(argument);

        symbol.accept(message);
    }
}
