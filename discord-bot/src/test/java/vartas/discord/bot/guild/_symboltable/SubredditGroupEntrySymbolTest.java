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

import net.dv8tion.jda.internal.entities.TextChannelImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import vartas.discord.bot.guild.AbstractGuildTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SubredditGroupEntrySymbolTest extends AbstractGuildTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "x", Arrays.asList(0L, 1L) },
                { "y", Arrays.asList(2L, 3L) },
                { "z", Collections.singletonList(4L)}
        });
    }

    @Parameterized.Parameter
    public String name;
    @Parameterized.Parameter(1)
    public List<Long> ids;

    SubredditGroupEntrySymbol symbol;
    IGuildScope spannedScope;
    IGuildScope enclosingScope;
    List<TextChannelImpl> channels;
    Iterator<TextChannelImpl> iterator;

    @Before
    public void setUp(){
        Optional<SubredditGroupEntrySymbol> symbolOpt;
        symbolOpt = guildScope.resolveSubredditGroupEntryDown(name);
        assertThat(symbolOpt).isPresent();


        symbol = symbolOpt.get();
        spannedScope = symbol.getSpannedScope();
        enclosingScope = symbol.getEnclosingScope();
        channels = ids.stream().map(id -> super.channels.get(id)).collect(Collectors.toList());
        iterator = channels.iterator();
    }

    @Test
    public void testResolveAsTextChannels(){
        while(iterator.hasNext()){
            assertThat(symbol.resolve(guild)).containsExactlyInAnyOrderElementsOf(channels);
            super.channels.remove(iterator.next().getIdLong());
            iterator.remove();
        }
    }
    @Test
    public void testValidateTextChannels(){
        List<String> invalid = new ArrayList<>();

        while(iterator.hasNext()){
            check(invalid, symbol.validate(guild));

            TextChannelImpl channel = iterator.next();
            invalid.add(channel.getId());
            super.channels.remove(channel.getIdLong());
            iterator.remove();
        }

        //Remove the last symbol
        check(invalid, symbol.validate(guild));
    }

    protected static void check(List<String> expected, List<LongGroupElementSymbol> symbols){
        List<String> invalid = symbols.stream().map(LongGroupElementSymbol::getName).collect(Collectors.toList());
        assertThat(invalid).containsExactlyInAnyOrderElementsOf(expected);
    }
}
