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

package vartas.discord.argument.symboltable;

import net.dv8tion.jda.api.OnlineStatus;
import org.assertj.core.data.Percentage;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import vartas.chart.Interval;
import vartas.discord.argument._ast.ASTArgument;
import vartas.discord.call._ast.ASTCallArtifact;
import vartas.discord.call._parser.CallParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ArgumentSymbolTest {
    static Percentage precision = Percentage.withPercentage(10e-15);
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

    @RunWith(Parameterized.class)
    public static class TestResolveDate{
        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.date 11-11-2000", "11-11-2000"}
            });
        }

        @Parameterized.Parameter
        public String argument;

        @Parameterized.Parameter(1)
        public String expected;

        @Test
        public void resolveDateTest(){
            ASTCallArtifact call = parse(argument);

            ASTArgument argument = call.getArgumentList().get(0);

            DateArgumentSymbol symbol = new DateArgumentSymbol("date");
            symbol.setAstNode(argument);

            Date date = symbol.resolve(null).get();
            assertThat(new SimpleDateFormat("dd-MM-yyyy").format(date)).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveExpression{
        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.expression 11-11-2000", new BigDecimal(11-11-2000) },
                    { "example.expression 1+2*sin(pi)", new BigDecimal(1+2*Math.sin(Math.PI)) },
                    { "example.expression e-0", new BigDecimal(Math.E) }
            });
        }

        @Parameterized.Parameter
        public String argument;
        @Parameterized.Parameter(1)
        public BigDecimal expected;

        @Test
        public void resolveExpressionTEst(){
            ASTCallArtifact call = parse(argument);
            ASTArgument argument = call.getArgumentList().get(0);

            ExpressionArgumentSymbol symbol = new ExpressionArgumentSymbol("expression");
            symbol.setAstNode(argument);

            assertThat(symbol.resolve(null).get()).isCloseTo(expected, precision);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveGuild{
        @Parameterized.Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.guild \"guild\"",
                    "example.guild guild",
                    "example.guild 12345"
            };
        }

        @Parameterized.Parameter
        public String argument;

        @Test
        public void resolveGuildTest(){
            ASTCallArtifact call = parse(argument);
            ASTArgument argument = call.getArgumentList().get(0);

            GuildArgumentSymbol symbol = new GuildArgumentSymbol("guild");
            symbol.setAstNode(argument);

            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveMember{
        @Parameterized.Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.member \"name\"",
                    "example.member name",
                    "example.member 12345",
                    "example.member <@12345>",
                    "example.member <@!12345>"
            };
        }

        @Parameterized.Parameter
        public String argument;

        @Test
        public void resolveMemberTest(){
            ASTCallArtifact call = parse(argument);
            ASTArgument argument = call.getArgumentList().get(0);

            MemberArgumentSymbol symbol = new MemberArgumentSymbol("member");
            symbol.setAstNode(argument);
            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveInterval{
        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.interval day", Interval.DAY},
                    { "example.interval month", Interval.MONTH},
                    { "example.interval week", Interval.WEEK},
                    { "example.interval year", Interval.YEAR}
            });
        }

        @Parameterized.Parameter
        public String argument;

        @Parameterized.Parameter(1)
        public Interval expected;

        @Test
        public void resolveIntervalTest(){
            ASTCallArtifact call = parse(argument);
            ASTArgument argument = call.getArgumentList().get(0);

            IntervalArgumentSymbol symbol = new IntervalArgumentSymbol("interval");
            symbol.setAstNode(argument);

            Interval interval = symbol.resolve(null).get();
            assertThat(interval).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveOnlineStatus{
        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.onlinestatus online", OnlineStatus.ONLINE},
                    { "example.onlinestatus busy", OnlineStatus.DO_NOT_DISTURB},
                    { "example.onlinestatus idle", OnlineStatus.IDLE},
                    { "example.onlinestatus invisible", OnlineStatus.INVISIBLE}
            });
        }

        @Parameterized.Parameter
        public String argument;

        @Parameterized.Parameter(1)
        public OnlineStatus expected;

        @Test
        public void resolveOnlineStatusTest(){
            ASTCallArtifact call = parse(argument);
            ASTArgument argument = call.getArgumentList().get(0);

            OnlineStatusArgumentSymbol symbol = new OnlineStatusArgumentSymbol("onlinestatus");
            symbol.setAstNode(argument);

            OnlineStatus onlineStatus = symbol.resolve(null).get();
            assertThat(onlineStatus).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveString{
        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "example.string \"foo\"", "foo"},
                    { "example.string bar", "bar"}
            });
        }

        @Parameterized.Parameter
        public String argument;

        @Parameterized.Parameter(1)
        public String expected;

        @Test
        public void resolveStringTest(){
            ASTCallArtifact call = parse(argument);
            ASTArgument argument = call.getArgumentList().get(0);

            StringArgumentSymbol symbol = new StringArgumentSymbol("name");
            symbol.setAstNode(argument);

            String value = symbol.resolve(null).get();
            assertThat(value).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveUser{
        @Parameterized.Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.user \"name\"",
                    "example.user name",
                    "example.user 12345",
                    "example.user <@12345>"
            };
        }

        @Parameterized.Parameter
        public String argument;

        @Test
        public void resolveUserTest(){
            ASTCallArtifact call = parse(argument);
            ASTArgument argument = call.getArgumentList().get(0);

            UserArgumentSymbol symbol = new UserArgumentSymbol("user");
            symbol.setAstNode(argument);
            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveTextChannel{
        @Parameterized.Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.textchannel \"name\"",
                    "example.textchannel name",
                    "example.textchannel 12345",
                    "example.textchannel <#12345>"
            };
        }

        @Parameterized.Parameter
        public String argument;

        @Test
        public void resolveTextChannelTest(){
            ASTCallArtifact call = parse(argument);
            ASTArgument argument = call.getArgumentList().get(0);

            TextChannelArgumentSymbol symbol = new TextChannelArgumentSymbol("textchannel");
            symbol.setAstNode(argument);
            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveRole{
        @Parameterized.Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.role \"name\"",
                    "example.role name",
                    "example.role 12345",
                    "example.role <@&12345>"
            };
        }

        @Parameterized.Parameter
        public String argument;

        @Test
        public void resolveRoleTest(){
            ASTCallArtifact call = parse(argument);
            ASTArgument argument = call.getArgumentList().get(0);

            RoleArgumentSymbol symbol = new RoleArgumentSymbol("role");
            symbol.setAstNode(argument);
            assertThat(symbol).isNotNull();
        }
    }

    @RunWith(Parameterized.class)
    public static class TestResolveMessage{
        @Parameterized.Parameters
        public static Object[] data() {
            return new Object[] {
                    "example.message 12345"
            };
        }

        @Parameterized.Parameter
        public String argument;

        @Test
        public void resolveMessageTest(){
            ASTCallArtifact call = parse(argument);
            ASTArgument argument = call.getArgumentList().get(0);

            MessageArgumentSymbol symbol = new MessageArgumentSymbol("message");
            symbol.setAstNode(argument);
            assertThat(symbol).isNotNull();
        }
    }
}
