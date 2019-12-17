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

package vartas.discord.bot.credentials.visitor;

import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.credentials.CredentialsHelper;
import vartas.discord.bot.credentials._ast.ASTCredentialsArtifact;
import vartas.discord.bot.entities.Credentials;

import static org.assertj.core.api.Assertions.assertThat;

public class CredentialsVisitorTest extends AbstractTest {
    protected Credentials credentials;
    protected ASTCredentialsArtifact artifact;
    @Before
    public void setUp(){
        credentials = new Credentials();
        artifact = CredentialsHelper.parse("src/test/resources/credentials.cfg");
        new CredentialsVisitor().accept(artifact, credentials);
    }

    @Test
    public void getStatusMessageUpdateIntervalTest(){
        assertThat(credentials.getStatusMessageUpdateInterval()).isEqualTo(5);
    }

    @Test
    public void getInteractiveMessageLifetimeTest(){
        assertThat(credentials.getInteractiveMessageLifetime()).isEqualTo(10);
    }

    @Test
    public void getActivityUpdateIntervalTest(){
        assertThat(credentials.getActivityUpdateInterval()).isEqualTo(15);
    }

    @Test
    public void getInviteSupportServerTest(){
        assertThat(credentials.getInviteSupportServer()).isEqualTo("invite");
    }

    @Test
    public void getBotNameTest(){
        assertThat(credentials.getBotName()).isEqualTo("bot");
    }

    @Test
    public void getGlobalPrefixTest(){
        assertThat(credentials.getGlobalPrefix()).isEqualTo("globalPrefix");
    }

    @Test
    public void getWikiLinkTest(){
        assertThat(credentials.getWikiLink()).isEqualTo("wiki");
    }

    @Test
    public void getImageWidthTest(){
        assertThat(credentials.getImageWidth()).isEqualTo(1024);
    }

    @Test
    public void getImageHeightTest(){
        assertThat(credentials.getImageHeight()).isEqualTo(768);
    }

    @Test
    public void getDiscordTokenTest(){
        assertThat(credentials.getDiscordToken()).isEqualTo("token");
    }

    @Test
    public void getRedditAccountTest(){
        assertThat(credentials.getRedditAccount()).isEqualTo("account");
    }

    @Test
    public void getRedditIdTest(){
        assertThat(credentials.getRedditId()).isEqualTo("id");
    }

    @Test
    public void getRedditSecretTest(){
        assertThat(credentials.getRedditSecret()).isEqualTo("secret");
    }
}
