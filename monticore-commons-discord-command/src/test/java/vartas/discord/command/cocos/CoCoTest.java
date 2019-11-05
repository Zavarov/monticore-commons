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

package vartas.discord.command.cocos;

import de.monticore.io.paths.ModelPath;
import de.se_rwth.commons.logging.Finding;
import de.se_rwth.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import vartas.discord.command.AbstractTest;
import vartas.discord.command.CommandHelper;
import vartas.discord.command._symboltable.CommandGlobalScope;
import vartas.discord.command._symboltable.CommandLanguage;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class CoCoTest extends AbstractTest {
    protected static String baseDir = "src/test/resources/cocos/";
    private CommandGlobalScope scope;

    @Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
            {
                "AtMostOneAttachment.cmd",
                AtMostOneAttachmentCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "AtMostOneGuild.cmd",
                AtMostOneGuildCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "AtMostOneParameter.cmd",
                AtMostOneParameterCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "AtMostOnePermission.cmd",
                AtMostOnePermissionCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "AtMostOneRank.cmd",
                AtMostOneRankCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "ClassNameIsUnique.cmd",
                ClassNameIsUniqueCoCo.ERROR_MESSAGE,
                "TestCommand"
            },
            {
                "ClassNameStartsWithCapitalLetter.cmd",
                ClassNameStartsWithCapitalLetterCoCo.ERROR_MESSAGE,
                "testCommand"
            },
            {
                "CommandNameIsUnique.cmd",
                CommandNameIsUniqueCoCo.ERROR_MESSAGE,
                "test"
            },
            {
                "ExactlyOneClassName.cmd",
                ExactlyOneClassNameCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "MemberParameterRequiresGuild.cmd",
                MemberParameterRequiresGuildCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "MessageParameterRequiresGuild.cmd",
                MessageParameterRequiresGuildCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "PermissionOnlyInGuild.cmd",
                PermissionOnlyInGuildCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "RoleParameterRequiresGuild.cmd",
                RoleParameterRequiresGuildCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "TextChannelParameterRequiresGuild.cmd",
                TextChannelParameterRequiresGuildCoCo.ERROR_MESSAGE,
                ""
            },
            {
                "NamesHaveNoWhitespaces.cmd",
                NamesHaveNoWhitespacesCoCo.ERROR_MESSAGE,
                "Test Command"
            },
            {
                "ManyOnlyAtLastParameter.cmd",
                ManyOnlyAtLastParameterCoCo.ERROR_MESSAGE,
                ""
            }
        });
    }

    @Parameter
    public String fileName;
    @Parameter(1)
    public String errorMsg;
    @Parameter(2)
    public String className;

    @Before
    public void setUp(){
        ModelPath modelPath = new ModelPath(Paths.get(baseDir));
        CommandLanguage language = new CommandLanguage();
        scope = new CommandGlobalScope(modelPath, language);
    }

    @After
    public void tearDown(){
        Log.getFindings().clear();
    }

    @Test
    public void testValid(){
        CommandHelper.parse(scope, baseDir+"Valid.cmd");
        checkValid();
    }

    @Test
    public void testInvalid(){
        CommandHelper.parse(scope, baseDir+"invalid/"+fileName);
        checkInvalid();
    }

    protected void checkInvalid(){
        Finding finding = Finding.error(String.format(errorMsg, className));

        assertThat(Log.getFindings()).hasSize(1);
        assertThat(Log.getFindings()).contains(finding);
    }

    protected void checkValid(){
        assertThat(Log.getFindings().isEmpty());
    }
}
