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

import net.dv8tion.jda.internal.entities.RoleImpl;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import vartas.discord.bot.guild.AbstractGuildTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class LongGroupArtifactSymbolTest {
    @RunWith(Parameterized.class)
    public static class TextChannelGroupArtifactSymbolTest extends AbstractGuildTest{

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "x", Arrays.asList(0L, 1L) },
                    { "y", Arrays.asList(2L, 3L) },
                    { "z", Collections.singletonList(4L)}
            });
        }

        @Parameter
        public String name;
        @Parameter(1)
        public List<Long> ids;

        LongGroupArtifactSymbol symbol;
        IGuildScope spannedScope;
        IGuildScope enclosingScope;
        List<TextChannelImpl> channels;
        Iterator<TextChannelImpl> iterator;

        @Before
        public void setUp(){
            Optional<LongGroupArtifactSymbol> symbolOpt;
            symbolOpt = guildScope.resolveLongGroupArtifactDown("SUBREDDIT."+name);
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
                assertThat(symbol.resolveAsTextChannels(guild)).containsExactlyInAnyOrderElementsOf(channels);
                super.channels.remove(iterator.next().getIdLong());
                iterator.remove();
            }
        }
        @Test
        public void testValidateTextChannels(){
            List<String> invalid = new ArrayList<>();

            while(iterator.hasNext()){
                check(invalid, symbol.validateTextChannels(guild));

                TextChannelImpl channel = iterator.next();
                invalid.add(channel.getId());
                super.channels.remove(channel.getIdLong());
                iterator.remove();
            }

            //Remove the last symbol
            check(invalid, symbol.validateTextChannels(guild));
        }
    }
    @RunWith(Parameterized.class)
    public static class RoleGroupArtifactTest extends AbstractGuildTest{

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "a", Arrays.asList(5L, 6L) },
                    { "b", Arrays.asList(7L, 8L) },
                    { "c", Collections.singletonList(9L)}
            });
        }

        @Parameter
        public String name;
        @Parameter(1)
        public List<Long> ids;

        LongGroupArtifactSymbol symbol;
        IGuildScope spannedScope;
        IGuildScope enclosingScope;
        List<RoleImpl> roles;
        Iterator<RoleImpl> iterator;

        @Before
        public void setUp(){
            Optional<LongGroupArtifactSymbol> symbolOpt;
            symbolOpt = guildScope.resolveLongGroupArtifactDown("ROLEGROUP."+name);
            assertThat(symbolOpt).isPresent();

            symbol = symbolOpt.get();
            spannedScope = symbol.getSpannedScope();
            enclosingScope = symbol.getEnclosingScope();
            roles = ids.stream().map(id -> super.roles.get(id)).collect(Collectors.toList());
            iterator = roles.iterator();
        }

        @Test
        public void testResolveAsRoles(){
            while(iterator.hasNext()){
                assertThat(symbol.resolveAsRoles(guild)).containsExactlyInAnyOrderElementsOf(roles);
                super.roles.remove(iterator.next().getIdLong());
                iterator.remove();
            }
        }
        @Test
        public void testValidateRoles(){
            List<String> invalid = new ArrayList<>();

            while(iterator.hasNext()){
                check(invalid, symbol.validateRoles(guild));

                RoleImpl role = iterator.next();
                invalid.add(role.getId());
                super.roles.remove(role.getIdLong());
                iterator.remove();
            }

            //Remove the last symbol
            check(invalid, symbol.validateTextChannels(guild));
        }
    }

    protected static void check(List<String> expected, List<LongGroupValueSymbol> symbols){
        List<String> invalid = symbols.stream().map(LongGroupValueSymbol::getName).collect(Collectors.toList());
        assertThat(invalid).containsExactlyInAnyOrderElementsOf(expected);
    }
}
