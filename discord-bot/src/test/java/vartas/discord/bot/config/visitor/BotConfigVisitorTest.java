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

package vartas.discord.bot.config.visitor;

import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.config.ConfigHelper;
import vartas.discord.bot.config._ast.ASTConfigArtifact;
import vartas.discord.bot.entities.BotConfig;

import static org.assertj.core.api.Assertions.assertThat;

public class BotConfigVisitorTest extends AbstractTest {
    protected BotConfig config;
    protected ASTConfigArtifact artifact;
    @Before
    public void setUp(){
        config = new BotConfig();
        artifact = ConfigHelper.parse("src/test/resources/config.cfg");
        new BotConfigVisitor().accept(artifact, config);
    }

    @Test
    public void getStatusMessageUpdateIntervalTest(){
        assertThat(config.getStatusMessageUpdateInterval()).isEqualTo(5);
    }

    @Test
    public void getDiscordShardsTest(){
        assertThat(config.getDiscordShards()).isEqualTo(2);
    }

    @Test
    public void getInteractiveMessageLifetimeTest(){
        assertThat(config.getInteractiveMessageLifetime()).isEqualTo(10);
    }

    @Test
    public void getActivityUpdateIntervalTest(){
        assertThat(config.getActivityUpdateInterval()).isEqualTo(15);
    }

    @Test
    public void getInviteSupportServerTest(){
        assertThat(config.getInviteSupportServer()).isEqualTo("invite");
    }

    @Test
    public void getBotNameTest(){
        assertThat(config.getBotName()).isEqualTo("bot");
    }

    @Test
    public void getGlobalPrefixTest(){
        assertThat(config.getGlobalPrefix()).isEqualTo("globalPrefix");
    }

    @Test
    public void getWikiLinkTest(){
        assertThat(config.getWikiLink()).isEqualTo("wiki");
    }

    @Test
    public void getImageWidthTest(){
        assertThat(config.getImageWidth()).isEqualTo(1024);
    }

    @Test
    public void getImageHeightTest(){
        assertThat(config.getImageHeight()).isEqualTo(768);
    }

    @Test
    public void getDiscordTokenTest(){
        assertThat(config.getDiscordToken()).isEqualTo("token");
    }

    @Test
    public void getRedditAccountTest(){
        assertThat(config.getRedditAccount()).isEqualTo("account");
    }

    @Test
    public void getRedditIdTest(){
        assertThat(config.getRedditId()).isEqualTo("id");
    }

    @Test
    public void getRedditSecretTest(){
        assertThat(config.getRedditSecret()).isEqualTo("secret");
    }
}
