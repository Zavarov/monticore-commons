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
import vartas.discord.bot.entities.Rank;
import vartas.discord.bot.rank.RankHelper;
import vartas.discord.bot.rank._ast.ASTRankArtifact;

import static org.assertj.core.api.Assertions.assertThat;

public class RankVisitorTest extends AbstractTest {
    Rank config;
    ASTRankArtifact artifact;

    @Before
    public void setUp(){
        config = new Rank();
        artifact = RankHelper.parse("src/test/resources/rank.perm");
        new RankVisitor().accept(artifact, config);
    }

    @Test
    public void resolveTest(){
        assertThat(config.resolve(user1, Rank.Ranks.ROOT)).isTrue();
        assertThat(config.resolve(user1, Rank.Ranks.REDDIT)).isTrue();
        assertThat(config.resolve(user2, Rank.Ranks.DEVELOPER)).isTrue();
        assertThat(config.resolve(user2, Rank.Ranks.REDDIT)).isTrue();
    }
}
