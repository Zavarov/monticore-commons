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

package vartas.discord.aggregated.command.symboltable;

import de.monticore.io.paths.ModelPath;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import org.assertj.core.data.Percentage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import vartas.chart.Interval;
import vartas.discord.aggregated.parameter.symboltable.*;
import vartas.discord.argument._ast.ASTArgumentType;
import vartas.discord.bot.rank.RankType;
import vartas.discord.call._ast.ASTCallArtifact;
import vartas.discord.call._parser.CallParser;
import vartas.discord.command.CommandHelper;
import vartas.discord.command._symboltable.CommandLanguage;
import vartas.discord.command._symboltable.CommandSymbol;
import vartas.discord.parameter._ast.ASTDateParameter;
import vartas.discord.parameter._ast.ASTGuildParameter;
import vartas.discord.parameter._symboltable.MessageParameterSymbol;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class AggregatedCommandGlobalScopeTest{
    static AggregatedCommandGlobalScope commandScope;
    static Percentage precision = Percentage.withPercentage(10e-15);

    @BeforeClass
    public static void setUp(){
        ModelPath path = new ModelPath(Paths.get(""));
        CommandLanguage language = new CommandLanguage();
        commandScope = new AggregatedCommandGlobalScope(path, language);

        CommandHelper.parse(commandScope,"src/test/resources/Command.cmd");
    }
    protected static ASTCallArtifact parse(String content){
        try{
            CallParser parser = new CallParser();

            Optional<ASTCallArtifact> call = parser.parse_String(content);
            if(parser.hasErrors())
                throw new IllegalArgumentException("The parser encountered errors while parsing:\n"+content);
            if(!call.isPresent())
                throw new IllegalArgumentException("The command file couldn't be parsed");

            return call.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    public static class TestCommandAttributes{
        @Test
        public void isInGuildTest(){
            ASTCallArtifact call = parse("example.test \"guild\" 11-11-2000");
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            assertThat(command.requiresGuild()).isTrue();
        }

        @Test
        public void parametersTest(){
            ASTCallArtifact call = parse("example.test \"guild\" 11-11-2000");
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            assertThat(command.getParameters()).hasSize(2);
            assertThat(command.getParameters().get(0)).isInstanceOf(ASTGuildParameter.class);
            assertThat(command.getParameters().get(1)).isInstanceOf(ASTDateParameter.class);
        }

        @Test
        public void permissionsTest(){
            ASTCallArtifact call = parse("example.test \"guild\" 11-11-2000");
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            assertThat(command.getRequiredPermissions()).containsExactlyInAnyOrder(Permission.ADMINISTRATOR, Permission.MESSAGE_MANAGE);
        }

        @Test
        public void ranksTest(){
            ASTCallArtifact call = parse("example.test \"guild\" 11-11-2000");
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            assertThat(command.getValidRanks()).containsExactlyInAnyOrder(RankType.ROOT, RankType.DEVELOPER);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveDate{
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.date 11-11-2000", "11-11-2000"}
            });
        }

        @Parameter
        public String argument;

        @Parameter(1)
        public String expected;

        @Test
        public void resolveDateTest(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            DateParameter2ArgumentSymbol symbol = commandScope.resolveDateParameter(name, argument);

            Date date = symbol.resolve(null).get();
            assertThat(new SimpleDateFormat("dd-MM-yyyy").format(date)).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveExpression{
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.expression 11-11-2000", new BigDecimal(11-11-2000) },
                    { "example.expression 1+2*sin(pi)", new BigDecimal(1+2*Math.sin(Math.PI)) },
                    { "example.expression e-0", new BigDecimal(Math.E) }
            });
        }

        @Parameter
        public String argument;
        @Parameter(1)
        public BigDecimal expected;

        @Test
        public void resolveExpressionTEst(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            ExpressionParameter2ArgumentSymbol symbol = commandScope.resolveExpressionParameter(name, argument);

            assertThat(symbol.resolve(null).get()).isCloseTo(expected, precision);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveGuild{
        @Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.guild \"guild\"",
                    "example.guild guild",
                    "example.guild 12345"
            };
        }

        @Parameter
        public String argument;

        @Test
        public void resolveGuildTest(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            GuildParameter2ArgumentSymbol symbol = commandScope.resolveGuildParameter(name, argument);

            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveMember{
        @Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.member \"name\"",
                    "example.member name",
                    "example.member 12345",
                    "example.member <@12345>",
                    "example.member <@!12345>"
            };
        }

        @Parameter
        public String argument;

        @Test
        public void resolveMemberTest(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            MemberParameter2ArgumentSymbol symbol = commandScope.resolveMemberParameter(name, argument);
            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveInterval{
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.interval day", Interval.DAY},
                    { "example.interval month", Interval.MONTH},
                    { "example.interval week", Interval.WEEK},
                    { "example.interval year", Interval.YEAR}
            });
        }

        @Parameter
        public String argument;

        @Parameter(1)
        public Interval expected;

        @Test
        public void resolveIntervalTest(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            IntervalParameter2ArgumentSymbol symbol = commandScope.resolveIntervalParameter(name, argument);

            Interval interval = symbol.resolve(null).get();
            assertThat(interval).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveOnlineStatus{
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.onlinestatus online", OnlineStatus.ONLINE},
                    { "example.onlinestatus busy", OnlineStatus.DO_NOT_DISTURB},
                    { "example.onlinestatus idle", OnlineStatus.IDLE},
                    { "example.onlinestatus invisible", OnlineStatus.INVISIBLE}
            });
        }

        @Parameter
        public String argument;

        @Parameter(1)
        public OnlineStatus expected;

        @Test
        public void resolveOnlineStatusTest(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            OnlineStatusParameter2ArgumentSymbol symbol = commandScope.resolveOnlineStatusParameter(name, argument);

            OnlineStatus onlineStatus = symbol.resolve(null).get();
            assertThat(onlineStatus).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveString{
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.string \"foo\"", "foo"},
                    { "example.string bar", "bar"}
            });
        }

        @Parameter
        public String argument;

        @Parameter(1)
        public String expected;

        @Test
        public void resolveStringTest(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            StringParameter2ArgumentSymbol symbol = commandScope.resolveStringParameter(name, argument);

            String value = symbol.resolve(null).get();
            assertThat(value).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveUser{
        @Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.user \"name\"",
                    "example.user name",
                    "example.user 12345",
                    "example.user <@12345>"
            };
        }

        @Parameter
        public String argument;

        @Test
        public void resolveUserTest(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            UserParameter2ArgumentSymbol symbol = commandScope.resolveUserParameter(name, argument);
            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveTextChannel{
        @Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.textchannel \"name\"",
                    "example.textchannel name",
                    "example.textchannel 12345",
                    "example.textchannel <#12345>"
            };
        }

        @Parameter
        public String argument;

        @Test
        public void resolveTextChannelTest(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            TextChannelParameter2ArgumentSymbol symbol = commandScope.resolveTextChannelParameter(name, argument);
            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveRole{
        @Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.role \"name\"",
                    "example.role name",
                    "example.role 12345",
                    "example.role <@&12345>"
            };
        }

        @Parameter
        public String argument;

        @Test
        public void resolveRoleTest(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            RoleParameter2ArgumentSymbol symbol = commandScope.resolveRoleParameter(name, argument);
            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveMessage{
        @Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.message 12345"
            };
        }

        @Parameter
        public String argument;

        @Test
        public void resolveMessageTest(){
            ASTCallArtifact call = parse(argument);
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            String name = command.getParameters().get(0).getName();
            ASTArgumentType argument = call.getArgumentList().get(0);

            MessageParameterSymbol symbol = commandScope.resolveMessageParameter(name, argument);
            assertThat(symbol).isNotNull();
        }
    }
}
