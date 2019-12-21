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

import com.google.common.base.Joiner;
import de.se_rwth.commons.Joiners;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import vartas.chart.Interval;
import vartas.discord.aggregated.argument.symboltable.*;
import vartas.discord.argument._ast.ASTArgument;
import vartas.discord.bot.entities.Cluster;
import vartas.discord.bot.entities.Rank;
import vartas.discord.bot.rank._ast.ASTRankName;
import vartas.discord.bot.rank._symboltable.RankNameSymbol;
import vartas.discord.command._ast.ASTCommandArtifact;
import vartas.discord.command._ast.ASTRestriction;
import vartas.discord.command._symboltable.CommandSymbol;
import vartas.discord.parameter._ast.ASTCardinality;
import vartas.discord.parameter._ast.ASTParameter;
import vartas.discord.parameter._ast.ASTParameterVariable;
import vartas.discord.parameter._symboltable.ParameterVariableSymbol;
import vartas.discord.permission._symboltable.PermissionNameSymbol;

import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class CommandGeneratorHelper {
    public CommandGeneratorHelper(){}

    private static final Map<ASTParameter, String> PARAMETER_MAP = new HashMap<>();

    static{
        PARAMETER_MAP.put(ASTParameter.MESSAGE,Message.class.getCanonicalName());
        PARAMETER_MAP.put(ASTParameter.USER,User.class.getCanonicalName());
        PARAMETER_MAP.put(ASTParameter.TEXT_CHANNEL,TextChannel.class.getCanonicalName());
        PARAMETER_MAP.put(ASTParameter.GUILD,Guild.class.getCanonicalName());
        PARAMETER_MAP.put(ASTParameter.ROLE,Role.class.getCanonicalName());
        PARAMETER_MAP.put(ASTParameter.MEMBER,Member.class.getCanonicalName());
        PARAMETER_MAP.put(ASTParameter.ONLINE_STATUS,OnlineStatus.class.getCanonicalName());
        PARAMETER_MAP.put(ASTParameter.INTERVAL,Interval.class.getCanonicalName());
        PARAMETER_MAP.put(ASTParameter.DATE,LocalDate.class.getCanonicalName());
        PARAMETER_MAP.put(ASTParameter.STRING,String.class.getCanonicalName());
        PARAMETER_MAP.put(ASTParameter.EXPRESSION,BigDecimal.class.getCanonicalName());
    }

    public static Path getQualifiedPath(String packageName, String fileName){
        return Paths.get(packageName.replaceAll("\\.", FileSystems.getDefault().getSeparator()), fileName + ".java");
    }

    public static String getType(ASTParameterVariable ast){
        return PARAMETER_MAP.get(ast.getParameter());
    }

    public static String getPackageFolder(ASTCommandArtifact ast){
        return Joiner.on(FileSystems.getDefault().getSeparator()).join(ast.getPackageList());
    }

    public static Message resolveMessage(String name, ASTArgument argument, Message context){
        MessageArgumentSymbol symbol = new MessageArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context).orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as a Message instance."));
    }

    public static User resolveUser(String name, ASTArgument argument, Message context){
        UserArgumentSymbol symbol = new UserArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context).orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as an User instance."));
    }

    public static TextChannel resolveTextChannel(String name, ASTArgument argument, Message context){
        TextChannelArgumentSymbol symbol = new TextChannelArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context).orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as a TextChannel instance."));
    }

    public static String resolveString(String name, ASTArgument argument, Message context){
        StringArgumentSymbol symbol = new StringArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept().orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as String instance."));
    }

    public static Role resolveRole(String name, ASTArgument argument, Message context){
        RoleArgumentSymbol symbol = new RoleArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context).orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as a Role instance."));
    }

    public static OnlineStatus resolveOnlineStatus(String name, ASTArgument argument, Message context){
        OnlineStatusArgumentSymbol symbol = new OnlineStatusArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept().orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as an OnlineStatus instance."));
    }

    public static Member resolveMember(String name, ASTArgument argument, Message context){
        MemberArgumentSymbol symbol = new MemberArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context).orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as a Member instance."));
    }

    public static Interval resolveInterval(String name, ASTArgument argument, Message context){
        IntervalArgumentSymbol symbol = new IntervalArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept().orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as an Interval instance."));
    }

    public static Guild resolveGuild(String name, ASTArgument argument, Message context){
        GuildArgumentSymbol symbol = new GuildArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept(context).orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as a Guild instance."));
    }

    public static BigDecimal resolveExpression(String name, ASTArgument argument, Message context){
        ExpressionArgumentSymbol symbol = new ExpressionArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept().orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as an Expression instance."));
    }

    public static LocalDate resolveDate(String name, ASTArgument argument, Message context){
        DateArgumentSymbol symbol = new DateArgumentSymbol(name);
        symbol.setAstNode(argument);
        return symbol.accept().orElseThrow(() -> new IllegalArgumentException("The argument '"+name+"' couldn't be resolved as a Date instance."));
    }

    public static boolean requiresGuild(CommandSymbol symbol){
        return !symbol.getSpannedScope().resolveRestrictionNameMany(ASTRestriction.GUILD.name()).isEmpty();
    }

    public static boolean requiresAttachment(CommandSymbol symbol){
        return !symbol.getSpannedScope().resolveRestrictionNameMany(ASTRestriction.ATTACHMENT.name()).isEmpty();
    }

    public static String getClassName(CommandSymbol symbol){
        return symbol.getSpannedScope().getLocalClassAttributeSymbols().get(0).getName();
    }

    public static List<Permission> getPermissions(CommandSymbol symbol){
        return symbol
                .getSpannedScope()
                .getLocalPermissionNameSymbols()
                .stream()
                .map(PermissionNameSymbol::getPermission)
                .collect(Collectors.toList());
    }

    public static List<Rank.Ranks> getRanks(CommandSymbol symbol){
        return symbol
                .getSpannedScope()
                .getLocalRankNameSymbols()
                .stream()
                .map(RankNameSymbol::getAstNode)
                .map(ASTRankName::getRank)
                .collect(Collectors.toList());
    }

    public static List<ParameterVariableSymbol> getParameters(CommandSymbol symbol){
        return symbol
                .getSpannedScope()
                .getLocalParameterVariableSymbols();
    }

    public static boolean checkRank(Cluster cluster , User user, Rank.Ranks rank){
        AtomicBoolean valid = new AtomicBoolean(false);

        Cluster.Visitor visitor = new Cluster.ClusterVisitor() {
            @Override
            public void handle(Rank _rank){
                valid.set(_rank.resolve(user, rank));
            }
        };

        cluster.accept(visitor);

        return valid.get();
    }

    public static boolean checkPermission(Member member, Permission permission){
        return PermissionUtil.checkPermission(member, permission);
    }

    public static boolean isMany(ParameterVariableSymbol symbol){
        return isStar(symbol) || isPlus(symbol);
    }

    public static boolean isStar(ParameterVariableSymbol symbol){
        ASTParameterVariable ast = symbol.getAstNode();
        return ast.isPresentCardinality() && ast.getCardinality().equals(ASTCardinality.STAR);

    }

    public static boolean isPlus(ParameterVariableSymbol symbol){
        ASTParameterVariable ast = symbol.getAstNode();
        return ast.isPresentCardinality() && ast.getCardinality().equals(ASTCardinality.PLUS);
    }

    public static long getMinSize(List<ParameterVariableSymbol> symbols){
        return symbols.stream().filter(symbol -> !isStar(symbol)).count();
    }

    public static String getPackageName(ASTCommandArtifact artifact){
        return Joiners.DOT.join(artifact.getPackageList());

    }

    public static Path getPackagePath(ASTCommandArtifact artifact){
        List<String> packageList = artifact.getPackageList();
        return Paths.get(packageList.get(0), packageList.subList(1, packageList.size()).toArray(new String[0]));
    }
}