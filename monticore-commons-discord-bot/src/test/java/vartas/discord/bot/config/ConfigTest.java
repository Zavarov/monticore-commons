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

package vartas.discord.bot.config;

import de.monticore.io.paths.ModelPath;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.config._ast.ASTConfigArtifact;
import vartas.discord.bot.config._symboltable.ConfigGlobalScope;
import vartas.discord.bot.config._symboltable.ConfigLanguage;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigTest {
    ASTConfigArtifact ast;

    @Before
    public void setUp(){
        ModelPath modelPath = new ModelPath(Paths.get("src/test/resources"));
        ConfigLanguage language = new ConfigLanguage();
        ConfigGlobalScope scope = new ConfigGlobalScope(modelPath, language);

        ast = ConfigHelper.parse(scope, "src/test/resources/config.cfg");
    }

    @Test
    public void testStatusMessageUpdateInterval(){
        assertThat(ast.getStatusMessageUpdateInterval()).isEqualTo(5);
    }

    @Test
    public void testDiscordShard(){
        assertThat(ast.getDiscordShards()).isEqualTo(2);
    }

    @Test
    public void testInteractiveMessageLifetime(){
        assertThat(ast.getInteractiveMessageLifetime()).isEqualTo(10);
    }

    @Test
    public void testActivityUpdateInterval(){
        assertThat(ast.getActivityUpdateInterval()).isEqualTo(15);
    }

    @Test
    public void testInviteSupportServer(){
        assertThat(ast.getInviteSupportServer()).isEqualTo("invite");
    }

    @Test
    public void testBotName(){
        assertThat(ast.getBotName()).isEqualTo("bot");
    }

    @Test
    public void testGlobalPrefix(){
        assertThat(ast.getGlobalPrefix()).isEqualTo("globalPrefix");
    }

    @Test
    public void testWikiLink(){
        assertThat(ast.getWikiLink()).isEqualTo("wiki");
    }

    @Test
    public void testImageWidth(){
        assertThat(ast.getImageWidth()).isEqualTo(1024);
    }

    @Test
    public void testImageHeight(){
        assertThat(ast.getImageHeight()).isEqualTo(768);
    }

    @Test
    public void testDiscordToken(){
        assertThat(ast.getDiscordToken()).isEqualTo("token");
    }

    @Test
    public void testRedditAccount(){
        assertThat(ast.getRedditAccount()).isEqualTo("account");
    }

    @Test
    public void testRedditId(){
        assertThat(ast.getRedditId()).isEqualTo("id");
    }

    @Test
    public void testRedditSecret(){
        assertThat(ast.getRedditSecret()).isEqualTo("secret");
    }
}
