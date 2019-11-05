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

package vartas.discord.aggregated.argument.symboltable;

import net.dv8tion.jda.api.Permission;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.rank._ast.ASTRank;
import vartas.discord.command._ast.ASTRestriction;
import vartas.discord.parameter._ast.ASTParameter;
import vartas.discord.parameter._ast.ASTParameterVariable;
import vartas.discord.parameter._symboltable.ParameterVariableSymbol;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ArgumentSymbolTest extends AbstractArgumentSymbolTest {
    @Before
    public void setUp(){
        parse("example.test \"guild\" 11-11-2000");
    }

    @Test
    public void isInGuildTest(){
        assertThat(symbol.getSpannedScope().resolveRestrictionName(ASTRestriction.GUILD.name())).isPresent();
    }

    @Test
    public void parametersTest(){
        assertThat(getParameters(symbol)).hasSize(2);
        checkParameter(getParameters(symbol).get(0), ASTParameter.GUILD);
        checkParameter(getParameters(symbol).get(1), ASTParameter.DATE);
    }

    @Test
    public void permissionsTest(){
        assertThat(getRequiredPermission(symbol)).containsExactlyInAnyOrder(Permission.ADMINISTRATOR, Permission.MESSAGE_MANAGE);
    }

    @Test
    public void ranksTest(){
        assertThat(getValidRanks(symbol)).containsExactlyInAnyOrder(ASTRank.ROOT, ASTRank.DEVELOPER);
    }

    private void checkParameter(ParameterVariableSymbol parameter, ASTParameter expected){
        Optional<ASTParameter> parameterOpt = parameter.getAstNode().map(ASTParameterVariable::getParameter);
        assertThat(parameterOpt).contains(expected);
    }
}
