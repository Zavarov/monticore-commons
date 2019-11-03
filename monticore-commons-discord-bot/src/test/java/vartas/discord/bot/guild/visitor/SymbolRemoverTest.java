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

package vartas.discord.bot.guild.visitor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.guild.AbstractGuildTest;
import vartas.discord.bot.guild._symboltable.*;
import vartas.discord.bot.guild.creator.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class SymbolRemoverTest extends AbstractGuildTest {
    Path target = Paths.get("src/test/resources/directory/junk.gld");

    GuildArtifactSymbol guildSymbol;
    PrefixEntrySymbol prefixSymbol;
    BlacklistEntrySymbol blacklistSymbol;
    SubredditGroupEntrySymbol subredditSymbol;
    RoleGroupEntrySymbol roleSymbol;
    LongGroupElementSymbol longSymbol;

    @Before
    public void setUp() throws IOException {
        Files.createDirectory(target.getParent());
        Files.createFile(target);

        guildScope = new GuildScope();

        guildSymbol = GuildArtifactSymbolCreator.create(guildScope, guild, target);
        prefixSymbol = PrefixEntrySymbolCreator.create(guildScope, "prefix");
        blacklistSymbol = BlacklistEntrySymbolCreator.create(guildScope, "blacklist");
        roleSymbol = RoleGroupEntrySymbolCreator.create(guildScope, "role");
        subredditSymbol = SubredditGroupEntrySymbolCreator.create(guildScope, "subreddit");
        longSymbol = LongGroupElementSymbolCreator.create(guildScope, "type", "12345");
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(target);
        Files.deleteIfExists(target.getParent());
    }

    @Test
    public void testRemoveGuildArtifactSymbol() {
        assertThat(target).exists();
        assertThat(guildScope.getLocalGuildArtifactSymbols()).contains(guildSymbol);

        SymbolRemover.remove(guildSymbol);

        assertThat(guildScope.getLocalGuildArtifactSymbols()).doesNotContain(guildSymbol);
        assertThat(target).doesNotExist();
    }

    @Test(expected=IllegalStateException.class)
    public void testRemoveGuildDeleteFileFailure(){
        assertThat(target).exists();
        assertThat(guildScope.getLocalGuildArtifactSymbols()).contains(guildSymbol);

        guildSymbol.setReference(target.getParent());
        SymbolRemover.remove(guildSymbol);
    }

    @Test
    public void removePrefixEntrySymbol(){
        assertThat(guildScope.getLocalPrefixEntrySymbols()).contains(prefixSymbol);

        SymbolRemover.remove(prefixSymbol);

        assertThat(guildScope.getLocalPrefixEntrySymbols()).doesNotContain(prefixSymbol);
    }

    @Test
    public void removeBlacklistEntrySymbol(){
        assertThat(guildScope.getLocalBlacklistEntrySymbols()).contains(blacklistSymbol);

        SymbolRemover.remove(blacklistSymbol);

        assertThat(guildScope.getLocalBlacklistEntrySymbols()).doesNotContain(blacklistSymbol);
    }

    @Test
    public void removeRoleGroupValueSymbol(){
        assertThat(guildScope.getLocalRoleGroupEntrySymbols()).contains(roleSymbol);

        SymbolRemover.remove(roleSymbol);

        assertThat(guildScope.getLocalRoleGroupEntrySymbols()).doesNotContain(roleSymbol);
    }

    @Test
    public void removeSubredditGroupValueSymbol(){
        assertThat(guildScope.getLocalSubredditGroupEntrySymbols()).contains(subredditSymbol);

        SymbolRemover.remove(subredditSymbol);

        assertThat(guildScope.getLocalSubredditGroupEntrySymbols()).doesNotContain(subredditSymbol);
    }

    @Test
    public void removeLongGroupElementSymbol(){
        assertThat(guildScope.getLocalLongGroupElementSymbols()).contains(longSymbol);

        SymbolRemover.remove(longSymbol);

        assertThat(guildScope.getLocalLongGroupElementSymbols()).doesNotContain(longSymbol);
    }

    @Test
    public void testSetRealThis(){
        SymbolRemover visitor = new SymbolRemover();

        assertThat(visitor.getRealThis()).isEqualTo(visitor);
        visitor.setRealThis(null);
        assertThat(visitor.getRealThis()).isNull();
    }
}
