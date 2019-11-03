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

package vartas.discord.bot.config.cocos;

import de.se_rwth.commons.logging.Finding;
import de.se_rwth.commons.logging.Log;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.config.ConfigHelper;
import vartas.discord.bot.config._ast.ASTConfig;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class CoCosTest extends AbstractTest {
    protected static String baseDir = "src/test/resources/cocos/config/";

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {
                        "AllEntriesExist.cfg",
                        AllEntriesExistCoCo.ERROR_MESSAGE,
                        ASTConfig.STATUS_MESSAGE_UPDATE_INTERVAL.name()
                },
                {
                        "EntryIsUnique.cfg",
                        EntryIsUniqueCoCo.ERROR_MESSAGE,
                        ASTConfig.STATUS_MESSAGE_UPDATE_INTERVAL.name()
                }
        });
    }

    @Parameterized.Parameter
    public String fileName;
    @Parameterized.Parameter(1)
    public String errorMsg;
    @Parameterized.Parameter(2)
    public String config;

    @After
    public void tearDown(){
        Log.getFindings().clear();
    }

    @Test
    public void testValid(){
        ConfigHelper.parse(baseDir+"Valid.cfg");
        checkValid();
    }

    @Test
    public void testInvalid(){
        ConfigHelper.parse( baseDir+"invalid/"+fileName);
        checkInvalid();
    }

    protected void checkInvalid(){
        Finding finding = Finding.error(String.format(errorMsg, config));

        assertThat(Log.getFindings()).hasSize(1);
        assertThat(Log.getFindings()).contains(finding);
    }

    protected void checkValid(){
        assertThat(Log.getFindings().isEmpty());
    }
}