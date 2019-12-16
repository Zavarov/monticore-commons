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

package vartas.discord.bot.configuration.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.configuration.AbstractGuildTest;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationSymbolPrettyPrinterTest extends AbstractGuildTest {
    IndentPrinter printer;
    ConfigurationPrettyPrinter prettyPrinter;

    @Before
    public void setUp(){
        String source = "src/test/resources/configuration.gld";

        reference = new File(source);

        printer = new IndentPrinter();
        printer.setIndentLength(4);

        prettyPrinter = new ConfigurationPrettyPrinter(printer);
    }

    @Test
    public void testPrettyPrint() throws IOException {
        String original = FileUtils
                .readFileToString(reference)
                .replaceAll("\\R","");
        String expected = prettyPrinter
                .prettyPrint(ast.getSymbol())
                .replaceAll("\\R","");

        assertThat(original).isEqualTo(expected);
    }

    @Test
    public void testSetRealThis(){
        assertThat(prettyPrinter.getRealThis()).isEqualTo(prettyPrinter);
        prettyPrinter.setRealThis(null);
        assertThat(prettyPrinter.getRealThis()).isNull();
    }
}
