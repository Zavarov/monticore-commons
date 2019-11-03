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
import vartas.discord.bot.guild._ast.ASTPrefixEntry;
import vartas.discord.bot.guild._symboltable.GuildScope;
import vartas.discord.bot.guild._symboltable.PrefixEntrySymbol;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PrefixEntrySymbolCreatorTest extends AbstractGuildTest {
    protected String value = "value";
    @Before
    public void setUp(){
        guildScope = new GuildScope();

        PrefixEntrySymbolCreator.create(guildScope, value);
    }
    @Test
    public void testStringEntrySymbol(){
        Optional<PrefixEntrySymbol> symbolOpt = guildScope.resolvePrefixEntry(value);
        PrefixEntrySymbol symbol;
        Optional<ASTPrefixEntry> astOpt;
        ASTPrefixEntry ast;

        assertThat(symbolOpt).isPresent();

        symbol = symbolOpt.get();
        astOpt = symbol.getAstNode();

        assertThat(symbol.getName()).isEqualTo(value);
        assertThat(astOpt).isPresent();

        ast = astOpt.get();

        assertThat(ast.getName()).isEqualTo(value);
    }
}
