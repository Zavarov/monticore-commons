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

import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.entities.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationPrettyPrinterTest extends AbstractTest {
    Configuration configuration;
    Path reference;
    ConfigurationPrettyPrinter prettyPrinter;

    @Before
    public void setUp(){
        configuration = new Configuration(12345);
        configuration.setPrefix("prefix");
        configuration.setPattern(Pattern.compile("expression"));
        configuration.add(Configuration.LongType.SUBREDDIT, "x", 0L);
        configuration.add(Configuration.LongType.SUBREDDIT, "x", 1L);
        configuration.add(Configuration.LongType.SUBREDDIT, "y", 2L);
        configuration.add(Configuration.LongType.SUBREDDIT, "y", 3L);
        configuration.add(Configuration.LongType.SUBREDDIT, "z", 4L);

        configuration.add(Configuration.LongType.SELFASSIGNABLE, "a", 5L);
        configuration.add(Configuration.LongType.SELFASSIGNABLE, "a", 6L);
        configuration.add(Configuration.LongType.SELFASSIGNABLE, "b", 7L);
        configuration.add(Configuration.LongType.SELFASSIGNABLE, "b", 8L);
        configuration.add(Configuration.LongType.SELFASSIGNABLE, "c", 9L);

        reference = Paths.get("src","test","resources","configuration.gld");
        prettyPrinter = new ConfigurationPrettyPrinter();
    }

    @Test
    public void testPrettyPrint() {
        String expected = prettyPrinter.prettyPrint(configuration);
        assertThat(reference).hasContent(expected);
    }
}
