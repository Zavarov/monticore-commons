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
import vartas.discord.aggregated.argument.symboltable.*;
import vartas.discord.argument._ast.ASTArgument;
import vartas.discord.bot.rank._ast.ASTRank;
import vartas.discord.bot.rank._symboltable.RankNameSymbol;
import vartas.discord.call._ast.ASTCallArtifact;
import vartas.discord.call._parser.CallParser;
import vartas.discord.command.CommandHelper;
import vartas.discord.command._ast.ASTRestriction;
import vartas.discord.command._symboltable.CommandGlobalScope;
import vartas.discord.command._symboltable.CommandLanguage;
import vartas.discord.command._symboltable.CommandSymbol;
import vartas.discord.parameter._ast.ASTParameter;
import vartas.discord.parameter._ast.ASTParameterVariable;
import vartas.discord.parameter._symboltable.ParameterVariableSymbol;
import vartas.discord.permission._symboltable.PermissionNameSymbol;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ArgumentSymbolsTest {
    static CommandGlobalScope commandScope;
    static Percentage precision = Percentage.withPercentage(10e-15);

    @BeforeClass
    public static void setUp(){
        ModelPath path = new ModelPath(Paths.get(""));
        CommandLanguage language = new CommandLanguage();
        commandScope = new CommandGlobalScope(path, language);

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

            assertThat(command.getSpannedScope().resolveRestrictionName(ASTRestriction.GUILD.name())).isPresent();
        }

        @Test
        public void parametersTest(){
            ASTCallArtifact call = parse("example.test \"guild\" 11-11-2000");
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            assertThat(getParameters(command)).hasSize(2);
            checkParameter(getParameters(command).get(0), ASTParameter.GUILD);
            checkParameter(getParameters(command).get(1), ASTParameter.DATE);
        }

        @Test
        public void permissionsTest(){
            ASTCallArtifact call = parse("example.test \"guild\" 11-11-2000");
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            assertThat(getRequiredPermission(command)).containsExactlyInAnyOrder(Permission.ADMINISTRATOR, Permission.MESSAGE_MANAGE);
        }

        @Test
        public void ranksTest(){
            ASTCallArtifact call = parse("example.test \"guild\" 11-11-2000");
            CommandSymbol command = commandScope.resolveCommand(call.getQualifiedName()).get();

            assertThat(getValidRanks(command)).containsExactlyInAnyOrder(ASTRank.ROOT, ASTRank.DEVELOPER);
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

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            DateArgumentSymbol symbol =new DateArgumentSymbol(name);
            symbol.setAstNode(argument);

            Date date = symbol.accept().get();
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

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            ExpressionArgumentSymbol symbol = new ExpressionArgumentSymbol(name);
            symbol.setAstNode(argument);

            assertThat(symbol.accept().get()).isCloseTo(expected, precision);
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

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            GuildArgumentSymbol symbol = new GuildArgumentSymbol(name);
            symbol.setAstNode(argument);

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

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            MemberArgumentSymbol symbol = new MemberArgumentSymbol(name);
            symbol.setAstNode(argument);
            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveInterval{
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.interval Day", Interval.DAY},
                    { "example.interval Month", Interval.MONTH},
                    { "example.interval Week", Interval.WEEK},
                    { "example.interval Year", Interval.YEAR}
            });
        }

        @Parameter
        public String argument;

        @Parameter(1)
        public Interval expected;

        @Test
        public void resolveIntervalTest(){
            ASTCallArtifact call = parse(argument);

            CommandSymbol command = commandScope.resolveCommandDown(call.getQualifiedName()).get();

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            IntervalArgumentSymbol symbol = new IntervalArgumentSymbol(name);
            symbol.setAstNode(argument);

            Interval interval = symbol.accept().get();
            assertThat(interval).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveOnlineStatus{
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.onlinestatus Online", OnlineStatus.ONLINE},
                    { "example.onlinestatus Busy", OnlineStatus.DO_NOT_DISTURB},
                    { "example.onlinestatus Idle", OnlineStatus.IDLE},
                    { "example.onlinestatus Invisible", OnlineStatus.INVISIBLE}
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

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            OnlineStatusArgumentSymbol symbol = new OnlineStatusArgumentSymbol(name);
            symbol.setAstNode(argument);

            OnlineStatus onlineStatus = symbol.accept().get();
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

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            StringArgumentSymbol symbol = new StringArgumentSymbol(name);
            symbol.setAstNode(argument);

            String value = symbol.accept().get();
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

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            UserArgumentSymbol symbol = new UserArgumentSymbol(name);
            symbol.setAstNode(argument);

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

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            TextChannelArgumentSymbol symbol = new TextChannelArgumentSymbol(name);
            symbol.setAstNode(argument);

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

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            RoleArgumentSymbol symbol = new RoleArgumentSymbol(name);
            symbol.setAstNode(argument);

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

            String name = getParameters(command).get(0).getName();
            ASTArgument argument = call.getArgumentList().get(0);

            MessageArgumentSymbol symbol = new MessageArgumentSymbol(name);
            symbol.setAstNode(argument);

            assertThat(symbol).isNotNull();
        }
    }

    private static void checkParameter(ParameterVariableSymbol parameter, ASTParameter expected){
        Optional<ASTParameter> parameterOpt = parameter.getAstNode().map(ASTParameterVariable::getParameter);
        assertThat(parameterOpt).contains(expected);
    }

    private static List<ParameterVariableSymbol> getParameters(CommandSymbol command){
        return command.getSpannedScope().getLocalParameterVariableSymbols();
    }

    private static List<ASTRank> getValidRanks(CommandSymbol command){
        return command.getSpannedScope().getLocalRankNameSymbols().stream().map(RankNameSymbol::getRank).collect(Collectors.toList());
    }

    private static List<Permission> getRequiredPermission(CommandSymbol command){
        return command.getSpannedScope().getLocalPermissionNameSymbols().stream().map(PermissionNameSymbol::getPermission).collect(Collectors.toList());
    }
}
