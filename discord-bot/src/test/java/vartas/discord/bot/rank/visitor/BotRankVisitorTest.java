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

package vartas.discord.bot.rank.visitor;

import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.entities.BotRank;
import vartas.discord.bot.rank.RankHelper;
import vartas.discord.bot.rank._ast.ASTRankArtifact;

import static org.assertj.core.api.Assertions.assertThat;

public class BotRankVisitorTest extends AbstractTest {
    protected BotRank config;
    protected ASTRankArtifact artifact;

    @Before
    public void setUp(){
        config = new BotRank(null);
        artifact = RankHelper.parse("src/test/resources/rank.perm", null);
        new BotRankVisitor().accept(artifact, config, jda);
    }

    @Test
    public void resolveTest(){
        assertThat(config.resolve(user1, BotRank.Type.ROOT)).isTrue();
        assertThat(config.resolve(user1, BotRank.Type.REDDIT)).isTrue();
        assertThat(config.resolve(user2, BotRank.Type.DEVELOPER)).isTrue();
        assertThat(config.resolve(user2, BotRank.Type.REDDIT)).isTrue();
    }
}
