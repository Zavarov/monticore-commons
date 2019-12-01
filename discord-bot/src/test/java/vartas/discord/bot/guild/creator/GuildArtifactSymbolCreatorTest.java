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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.guild.AbstractGuildTest;
import vartas.discord.bot.guild._ast.ASTGuildArtifact;
import vartas.discord.bot.guild._symboltable.GuildArtifactSymbol;
import vartas.discord.bot.guild._symboltable.GuildScope;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GuildArtifactSymbolCreatorTest extends AbstractGuildTest {
    protected String guildArtifact;
    protected String value;
    protected Path target = Paths.get("src/test/resources/junk.gld");
    @Before
    public void setUp(){
        guildScope = new GuildScope();

        value = guild.getId();
        guildArtifact = value;
    }
    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(target);
    }
    @Test
    public void testGuildArtifactSymbol(){
        GuildArtifactSymbolCreator.create(guildScope, guild, target);
        Optional<GuildArtifactSymbol> symbolOpt = guildScope.resolveGuildArtifactDown(guildArtifact);
        GuildArtifactSymbol symbol;
        ASTGuildArtifact ast;

        assertThat(symbolOpt).isPresent();

        symbol = symbolOpt.get();
        ast = symbol.getAstNode();

        assertThat(symbol.getName()).isEqualTo(value);
        assertThat(symbol.getReference()).isEqualTo(target);
        assertThat(symbol.getReference()).exists();
        
        assertThat(ast.getName()).isEqualTo(guild.getId());
    }

    @Test(expected=IllegalStateException.class)
    public void testGuildArtifactWriteToFileFailure() throws IOException {
        Files.createFile(target);
        assertThat(target.toFile().setWritable(false)).isTrue();
        GuildArtifactSymbolCreator.create(guildScope, guild, target);
    }
}
