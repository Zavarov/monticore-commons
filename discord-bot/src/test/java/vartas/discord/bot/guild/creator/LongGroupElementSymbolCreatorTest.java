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

package vartas.discord.bot.guild.creator;

import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.guild.AbstractGuildTest;
import vartas.discord.bot.guild._ast.ASTLongGroupElement;
import vartas.discord.bot.guild._symboltable.GuildScope;
import vartas.discord.bot.guild._symboltable.LongGroupElementSymbol;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LongGroupElementSymbolCreatorTest extends AbstractGuildTest {
    protected String type = "role";
    protected String value = "12345";
    @Before
    public void setUp(){
        guildScope = new GuildScope();
        LongGroupElementSymbolCreator.create(guildScope, type, value);
    }
    @Test
    public void testCreate(){
        Optional<LongGroupElementSymbol> symbolOpt = guildScope.resolveLongGroupElement(value);
        LongGroupElementSymbol symbol;
        ASTLongGroupElement ast;

        assertThat(symbolOpt).isPresent();

        symbol = symbolOpt.get();
        ast = symbol.getAstNode();

        assertThat(symbol.getName()).isEqualTo(value);

        assertThat(ast.getType()).isEqualTo(type);
        assertThat(ast.getName()).isEqualTo(value);
    }
}
