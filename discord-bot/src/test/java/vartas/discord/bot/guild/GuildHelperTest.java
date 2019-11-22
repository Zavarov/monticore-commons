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

package vartas.discord.bot.guild;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.guild._ast.ASTGuildArtifact;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class GuildHelperTest extends AbstractGuildTest {
    Path target;
    Path source;
    Path copy;
    @Before
    public void setUp() throws IOException {
        source = Paths.get("src/test/resources/guild.gld");
        copy = Paths.get("target/test/resources/directory/guild.gld");
        target = Paths.get("target/test/resources/directory/junk.rnk");
        FileUtils.deleteDirectory(target.getParent().toFile());
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(target.getParent().toFile());
        FileUtils.deleteDirectory(copy.getParent().toFile());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseArtifactFileNotFound(){
        GuildHelper.parse("#", target);
    }
    @Test(expected=IllegalArgumentException.class)
    public void testParseArtifactInvalidFile(){
        GuildHelper.parse("src/test/resources/junk.txt", target);
    }
    @Test
    public void testStore() throws IOException {
        ASTGuildArtifact ast = GuildHelper.parse(source.toString(), copy);
        GuildHelper.store(ast.getSymbol());

        String original = FileUtils
                .readFileToString(source.toFile())
                .replaceAll("\\s|\\R","");
        String expected = FileUtils
                .readFileToString(copy.toFile())
                .replaceAll("\\s|\\R","");

        assertThat(original).isEqualTo(expected);
    }
}
