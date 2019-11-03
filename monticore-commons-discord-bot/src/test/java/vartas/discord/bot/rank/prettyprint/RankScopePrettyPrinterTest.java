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

package vartas.discord.bot.rank.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.rank.RankHelper;
import vartas.discord.bot.rank._ast.ASTRankArtifact;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class RankScopePrettyPrinterTest extends AbstractTest {
    IndentPrinter printer;
    RankScopePrettyPrinter prettyPrinter;
    File reference;
    ASTRankArtifact artifact;

    @Before
    public void setUp(){
        String source = "src/test/resources/rank.perm";

        reference = new File(source);
        artifact = RankHelper.parse(source, reference.toPath());

        printer = new IndentPrinter();
        printer.setIndentLength(4);

        prettyPrinter = new RankScopePrettyPrinter(printer);
    }

    @Test
    public void testPrettyPrint() throws IOException {
        String original = FileUtils.readFileToString(reference).replaceAll("\\R","");
        String expected = prettyPrinter.prettyPrint(artifact.getEnclosingScope()).replaceAll("\\R","");

        assertThat(original).isEqualTo(expected);
    }

    @Test
    public void testSetRealThis(){
        assertThat(prettyPrinter.getRealThis()).isEqualTo(prettyPrinter);
        prettyPrinter.setRealThis(null);
        assertThat(prettyPrinter.getRealThis()).isNull();
    }
}
