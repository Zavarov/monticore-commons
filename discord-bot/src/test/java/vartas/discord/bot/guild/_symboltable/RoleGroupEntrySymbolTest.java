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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import vartas.discord.bot.guild.AbstractGuildTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class RoleGroupEntrySymbolTest extends AbstractGuildTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "a", Arrays.asList(5L, 6L) },
                { "b", Arrays.asList(7L, 8L) },
                { "c", Collections.singletonList(9L)}
        });
    }

    @Parameterized.Parameter
    public String name;
    @Parameterized.Parameter(1)
    public List<Long> ids;

    RoleGroupEntrySymbol symbol;
    IGuildScope spannedScope;
    IGuildScope enclosingScope;
    List<RoleImpl> roles;
    Iterator<RoleImpl> iterator;

    @Before
    public void setUp(){
        Optional<RoleGroupEntrySymbol> symbolOpt;
        symbolOpt = guildScope.resolveRoleGroupEntryDown(name);
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
            assertThat(symbol.resolve(guild)).containsExactlyInAnyOrderElementsOf(roles);
            super.roles.remove(iterator.next().getIdLong());
            iterator.remove();
        }
    }
    @Test
    public void testValidateRoles(){
        List<String> invalid = new ArrayList<>();

        while(iterator.hasNext()){
            check(invalid, symbol.validate(guild));

            RoleImpl role = iterator.next();
            invalid.add(role.getId());
            super.roles.remove(role.getIdLong());
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
