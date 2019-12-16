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

package vartas.discord.bot.configuration.visitor;

import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.configuration.ConfigurationHelper;
import vartas.discord.bot.configuration._ast.ASTConfigurationArtifact;
import vartas.discord.bot.entities.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationVisitorTest extends AbstractTest {
    ASTConfigurationArtifact ast;
    ConfigurationVisitor visitor;
    Configuration configuration;
    Path reference;

    @Before
    public void setUp(){
        reference = Paths.get("src","test","resources","configuration.gld");
        ast = ConfigurationHelper.parse(reference.toString());
        configuration = new Configuration(12345);
        visitor = new ConfigurationVisitor();

        visitor.accept(ast, configuration, guild);
    }

    @Test
    public void checkConfigurationTest(){
        assertThat(configuration.getGuildId()).isEqualTo(12345L);
        assertThat(configuration.getPrefix()).contains("prefix");
        assertThat(configuration.getPattern()).map(Pattern::pattern).contains("expression");
        assertThat(configuration.resolve(Configuration.LongType.SUBREDDIT, "x", 0L)).isTrue();
        assertThat(configuration.resolve(Configuration.LongType.SUBREDDIT, "x", 1L)).isTrue();
        assertThat(configuration.resolve(Configuration.LongType.SUBREDDIT, "y", 2L)).isTrue();
        assertThat(configuration.resolve(Configuration.LongType.SUBREDDIT, "y", 3L)).isTrue();
        assertThat(configuration.resolve(Configuration.LongType.SUBREDDIT, "z", 4L)).isTrue();
        assertThat(configuration.resolve(Configuration.LongType.SELFASSIGNABLE, "a", 5L)).isTrue();
        assertThat(configuration.resolve(Configuration.LongType.SELFASSIGNABLE, "a", 6L)).isTrue();
        assertThat(configuration.resolve(Configuration.LongType.SELFASSIGNABLE, "b", 7L)).isTrue();
        assertThat(configuration.resolve(Configuration.LongType.SELFASSIGNABLE, "b", 8L)).isTrue();
        assertThat(configuration.resolve(Configuration.LongType.SELFASSIGNABLE, "c", 9L)).isTrue();
    }
}
