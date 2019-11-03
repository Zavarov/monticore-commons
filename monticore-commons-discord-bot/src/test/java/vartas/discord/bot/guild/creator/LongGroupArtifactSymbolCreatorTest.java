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

import de.se_rwth.commons.Joiners;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.guild._symboltable.GuildScope;
import vartas.discord.bot.guild._symboltable.LongGroupArtifactSymbol;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LongGroupArtifactSymbolCreatorTest extends LongGroupValueSymbolCreatorTest {
    protected String longGroupArtifact;
    protected String group = "role";
    @Before
    public void setUp(){
        guildScope = new GuildScope();
        LongGroupArtifactSymbolCreator.create(guildScope, group, type, value);

        longGroupArtifact = group;
        longGroupValue = Joiners.DOT.join(group, value);
    }
    @Test
    public void testLongGroupArtifactSymbol(){
        Optional<LongGroupArtifactSymbol> symbolOpt = guildScope.resolveLongGroupArtifactDown(longGroupArtifact);
        LongGroupArtifactSymbol symbol;

        assertThat(symbolOpt).isPresent();

        symbol = symbolOpt.get();

        assertThat(symbol.getName()).isEqualTo(group);
    }
}
