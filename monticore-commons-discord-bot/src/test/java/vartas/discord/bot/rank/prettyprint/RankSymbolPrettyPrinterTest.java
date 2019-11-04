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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.rank.RankHelper;
import vartas.discord.bot.rank._ast.ASTRankArtifact;
import vartas.discord.bot.rank._symboltable.RankNameSymbol;
import vartas.discord.bot.rank._symboltable.UserWithRankSymbol;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class RankSymbolPrettyPrinterTest{
    static RankSymbolPrettyPrinter prettyPrinter;
    static ASTRankArtifact artifact;

    @BeforeClass
    public static void setUp(){
        String source = "src/test/resources/rank.perm";

        File reference = new File(source);
        artifact = RankHelper.parse(source, reference.toPath());

        IndentPrinter printer = new IndentPrinter();
        printer.setIndentLength(4);

        prettyPrinter = new RankSymbolPrettyPrinter(printer);
    }

    @RunWith(Parameterized.class)
    public static class RankPrettyPrinterTest extends AbstractTest{
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "1.ROOT", "Root" },
                    { "1.REDDIT", "Reddit" },
                    { "2.DEVELOPER", "Developer" },
                    { "2.REDDIT", "Reddit" }
            });
        }
        @Parameter
        public String input;
        @Parameter(1)
        public String expected;

        @Test
        public void testPrettyPrint(){

            Optional<RankNameSymbol> optional = artifact.getSpannedScope().resolveRankNameDown(input);
            assertThat(optional).isPresent();
            RankNameSymbol symbol = optional.get();

            assertThat(prettyPrinter.prettyprint(symbol)).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class UserWithRankPrettyPrinterTest extends AbstractTest{
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "1", "user : 1L has rank Root, Reddit\n" },
                    { "2", "user : 2L has rank Developer, Reddit\n" },
            });
        }
        @Parameter
        public String input;
        @Parameter(1)
        public String expected;

        @Test
        public void testPrettyPrint(){
            Optional<UserWithRankSymbol> optional = artifact.getSpannedScope().resolveUserWithRank(input);
            assertThat(optional).isPresent();
            UserWithRankSymbol symbol = optional.get();

            assertThat(prettyPrinter.prettyprint(symbol)).isEqualTo(expected);
        }
    }

    public static class RankSymbolPrettyPrinterMethodsTest extends AbstractTest{
        RankSymbolPrettyPrinter prettyPrinter = new RankSymbolPrettyPrinter(new IndentPrinter());
        @Test
        public void testSetRealThis(){
            assertThat(prettyPrinter.getRealThis()).isEqualTo(prettyPrinter);
            prettyPrinter.setRealThis(null);
            assertThat(prettyPrinter.getRealThis()).isNull();
        }
    }
}
