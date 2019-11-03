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

package vartas.discord.bot.guild._symboltable;

import org.junit.Test;
import vartas.discord.bot.guild.AbstractGuildTest;
import vartas.discord.bot.guild._ast.ASTIdentifier;

import java.util.Optional;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class StringEntrySymbolTest extends AbstractGuildTest {
    @Test
    public void testResolveAsPattern(){
        Optional<StringEntrySymbol> symbolOpt = guildScope.resolveStringEntryLocally(ASTIdentifier.BLACKLIST.name());
        StringEntrySymbol symbol;

        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();

        Pattern pattern = symbol.resolveAsPattern();
        assertThat(pattern).returns(false, s -> s.matcher("expression").matches());
    }

    @Test
    public void testResolveAsString(){
        Optional<StringEntrySymbol> symbolOpt = guildScope.resolveStringEntryLocally(ASTIdentifier.PREFIX.name());
        StringEntrySymbol symbol;

        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();

        assertThat(symbol.resolveAsString()).contains("prefix");
    }

    @Test
    public void testRecompilePattern(){
        Optional<StringEntrySymbol> symbolOpt = guildScope.resolveStringEntryLocally(ASTIdentifier.BLACKLIST.name());
        StringEntrySymbol symbol;

        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();

        Pattern pattern;

        pattern = symbol.resolveAsPattern();
        assertThat(pattern).returns(false, s -> s.matcher("expression").matches());

        symbol.recompilePattern();

        pattern = symbol.resolveAsPattern();
        assertThat(pattern).returns(true, s -> s.matcher("expression").matches());
    }
}
