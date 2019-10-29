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

package vartas.discord.aggregated.generator;

import com.ibm.icu.text.RuleBasedNumberFormat;
import de.se_rwth.commons.Joiners;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import org.atteo.evo.inflector.English;
import vartas.chart.Interval;
import vartas.discord.aggregated.argument.symboltable.*;
import vartas.discord.aggregated.parameter.visitor.ParameterTypeVisitor;
import vartas.discord.argument._ast.ASTArgument;
import vartas.discord.command._ast.ASTCommandArtifact;
import vartas.discord.parameter._ast.ASTParameter;

import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class CommandGeneratorHelper {

    public static final String DISCORD_MESSAGE = Message.class.getCanonicalName();
    public static final String DISCORD_USER = User.class.getCanonicalName();
    public static final String DISCORD_TEXTCHANNEL = TextChannel.class.getCanonicalName();
    public static final String DISCORD_GUILD = Guild.class.getCanonicalName();
    public static final String DISCORD_ROLE = Role.class.getCanonicalName();
    public static final String DISCORD_MEMBER = Member.class.getCanonicalName();
    public static final String DISCORD_ONLINESTATUS = OnlineStatus.class.getCanonicalName();
    public static final String CHART_INTERVAL = Interval.class.getCanonicalName();
    public static final String JAVA_DATE = Date.class.getCanonicalName();
    public static final String JAVA_STRING = String.class.getCanonicalName();
    public static final String MONTICORE_EXPRESSION = BigDecimal.class.getCanonicalName();

    protected static ParameterTypeVisitor typeVisitor = new ParameterTypeVisitor();

    protected RuleBasedNumberFormat ordinalFormatter = new RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.ORDINAL);

    public String formatAsOrdinal(Number number){
        return ordinalFormatter.format(number);
    }

    public String pluralOf(String word, int count){
        return English.plural(word, count);
    }

    public static Path getQualifiedPath(String packageName, String fileName){
        return Paths.get(packageName.replaceAll("\\.", FileSystems.getDefault().getSeparator()), fileName + ".java");
    }

    public static String getType(ASTParameter ast){
        return typeVisitor.accept(ast).orElseThrow(() -> new IllegalArgumentException(ast.getName() + " doesn't have a valid type."));
    }

    public static String getPackage(ASTCommandArtifact ast){
        return Joiners.DOT.join(ast.getPackageList());
    }

    public static String getPackageFolder(ASTCommandArtifact ast){
        return ast.getPackageList().stream().reduce((u,v) -> u + FileSystems.getDefault().getSeparator() + v).orElse("");
    }

    public static Optional<Message> resolveMessage(String name, ASTArgument argument, Message context){
        MessageArgumentSymbol symbol = new MessageArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context);
    }

    public static Optional<User> resolveUser(String name, ASTArgument argument, Message context){
        UserArgumentSymbol symbol = new UserArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context);
    }

    public static Optional<TextChannel> resolveTextChannel(String name, ASTArgument argument, Message context){
        TextChannelArgumentSymbol symbol = new TextChannelArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context);
    }

    public static Optional<String> resolveString(String name, ASTArgument argument, Message context){
        StringArgumentSymbol symbol = new StringArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept();
    }

    public static Optional<Role> resolveRole(String name, ASTArgument argument, Message context){
        RoleArgumentSymbol symbol = new RoleArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context);
    }

    public static Optional<OnlineStatus> resolveOnlineStatus(String name, ASTArgument argument, Message context){
        OnlineStatusArgumentSymbol symbol = new OnlineStatusArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept();
    }

    public static Optional<Member> resolveMember(String name, ASTArgument argument, Message context){
        MemberArgumentSymbol symbol = new MemberArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context);
    }

    public static Optional<Interval> resolveInterval(String name, ASTArgument argument, Message context){
        IntervalArgumentSymbol symbol = new IntervalArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept();
    }

    public static Optional<Guild> resolveGuild(String name, ASTArgument argument, Message context){
        GuildArgumentSymbol symbol = new GuildArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context);
    }

    public static Optional<BigDecimal> resolveExpression(String name, ASTArgument argument, Message context){
        ExpressionArgumentSymbol symbol = new ExpressionArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept();
    }

    public static Optional<Date> resolveDate(String name, ASTArgument argument, Message context){
        DateArgumentSymbol symbol = new DateArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept();
    }
}