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

package vartas.discord.bot.rank.cocos;

import de.se_rwth.commons.logging.Finding;
import de.se_rwth.commons.logging.Log;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.rank.RankHelper;
import vartas.discord.bot.rank._ast.ASTRank;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class CoCosTest extends AbstractTest {
    protected static String baseDir = "src/test/resources/cocos/rank/";

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {
                        "UserIsUnique.rnk",
                        UserIsUniqueCoCo.ERROR_MESSAGE,
                        "1",
                        ASTRank.ROOT.name()
                },
                {
                        "UserRanksAreUnique.rnk",
                        UserRanksAreUniqueCoCo.ERROR_MESSAGE,
                        ASTRank.ROOT.name(),
                        "1"
                }
        });
    }

    @Parameterized.Parameter
    public String fileName;
    @Parameterized.Parameter(1)
    public String errorMsg;
    @Parameterized.Parameter(2)
    public String firstArg;
    @Parameterized.Parameter(3)
    public String secondArg;

    @After
    public void tearDown(){
        Log.getFindings().clear();
    }

    @Test
    public void testValid(){
        RankHelper.parse(baseDir+"Valid.rnk");
        checkValid();
    }

    @Test
    public void testInvalid(){
        RankHelper.parse( baseDir+"invalid/"+fileName);
        checkInvalid();
    }

    protected void checkInvalid(){
        Finding finding = Finding.error(String.format(errorMsg, firstArg, secondArg));

        assertThat(Log.getFindings()).hasSize(1);
        assertThat(Log.getFindings()).contains(finding);
    }

    protected void checkValid(){
        assertThat(Log.getFindings().isEmpty());
    }
}