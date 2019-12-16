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

import de.monticore.io.paths.ModelPath;
import de.se_rwth.commons.logging.Log;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.Response;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.*;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.utils.config.AuthorizationConfig;
import org.junit.After;
import org.junit.Before;
import vartas.discord.bot.entities.Rank;
import vartas.discord.bot.rank._ast.ASTRankName;
import vartas.discord.bot.rank._symboltable.RankNameSymbol;
import vartas.discord.call._ast.ASTCallArtifact;
import vartas.discord.call._parser.CallParser;
import vartas.discord.command.CommandHelper;
import vartas.discord.command._symboltable.CommandGlobalScope;
import vartas.discord.command._symboltable.CommandLanguage;
import vartas.discord.command._symboltable.CommandSymbol;
import vartas.discord.parameter._symboltable.ParameterVariableSymbol;
import vartas.discord.permission._symboltable.PermissionNameSymbol;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractArgumentSymbolTest {
    protected static long guildId = 0L;
    protected static long channelId = 1L;
    protected static long roleId = 2L;
    protected static long userId = 3L;
    protected static long memberId = userId;
    protected static long messageId = 4L;

    protected static String guildName = "guild";
    protected static String channelName = "channel";
    protected static String roleName = "role";
    protected static String userName = "user";
    protected static String memberNickname = "member";

    protected JDAImpl jda;
    protected GuildImpl guild;
    protected TextChannelImpl channel;
    protected RoleImpl role;
    protected UserImpl user;
    protected MemberImpl member;
    protected Message message;

    Map<String, GuildImpl> guildMap;
    Map<String, TextChannelImpl> channelMap;
    Map<String, RoleImpl> roleMap;
    Map<String, UserImpl> userMap;
    Map<String, MemberImpl> memberMap;
    Map<String, Message> messageMap;


    @Before
    public void initJda(){
        AuthorizationConfig config = new AuthorizationConfig(AccountType.BOT, "12345");

        guildMap = new HashMap<>();
        channelMap = new HashMap<>();
        roleMap = new HashMap<>();
        userMap = new HashMap<>();
        memberMap = new HashMap<>();
        messageMap = new HashMap<>();

        jda = new JDAImpl(config){
            @Override
            public GuildImpl getGuildById(long id){
                return getGuildById(Long.toString(id));
            }
            @Override
            public GuildImpl getGuildById(@Nonnull String id){
                return guildMap.get(id);
            }
            @Nonnull
            @Override
            public List<Guild> getGuildsByName(@Nonnull String name, boolean ignoreCase){
                return Collections.singletonList(guildMap.get(name));
            }
            @Override
            public UserImpl getUserById(long id){
                return getUserById(Long.toString(id));
            }
            @Override
            public UserImpl getUserById(@Nonnull String id){
                return userMap.get(id);
            }
            @Nonnull
            @Override
            public List<User> getUsersByName(@Nonnull String name, boolean ignoreCase){
                return Collections.singletonList(userMap.get(name));
            }
        };

        guild = new GuildImpl(jda, guildId){
            @Override
            public RoleImpl getRoleById(long id){
                return getRoleById(Long.toString(id));
            }
            @Override
            public RoleImpl getRoleById(@Nonnull String id){
                return roleMap.get(id);
            }
            @Nonnull
            @Override
            public List<Role> getRolesByName(@Nonnull String name, boolean ignoreCase){
                return Collections.singletonList(roleMap.get(name));
            }
            @Override
            public TextChannelImpl getTextChannelById(long id){
                return getTextChannelById(Long.toString(id));
            }
            @Override
            public TextChannelImpl getTextChannelById(@Nonnull String id){
                return channelMap.get(id);
            }
            @Nonnull
            @Override
            public List<TextChannel> getTextChannelsByName(@Nonnull String name, boolean ignoreCase){
                return Collections.singletonList(channelMap.get(name));
            }
            @Override
            public MemberImpl getMemberById(long id){
                return getMemberById(Long.toString(id));
            }
            @Override
            public MemberImpl getMemberById(@Nonnull String id){
                return memberMap.get(id);
            }
            @Override
            public List<Member> getMembersByName(@Nonnull String name, boolean ignoreCase){
                return Collections.singletonList(memberMap.get(name));
            }
            @Nonnull
            @Override
            public List<Member> getMembersByEffectiveName(@Nonnull String name, boolean ignoreCase){
                return getMembersByName(name, ignoreCase);
            }
            @Nonnull
            @Override
            public List<Member> getMembersByNickname(String name, boolean ignoreCase){
                return getMembersByName(name, ignoreCase);
            }
        };

        message = new DataMessage(false, null, null, null){
            @Nonnull
            @Override
            public JDAImpl getJDA(){
                return jda;
            }
            @Nonnull
            @Override
            public GuildImpl getGuild(){
                return guild;
            }
            @Nonnull
            @Override
            public TextChannelImpl getTextChannel(){
                return channel;
            }
            @Nonnull
            @Override
            public String getId(){
                return Long.toString(getIdLong());
            }
            @Override
            public long getIdLong(){
                return messageId;
            }
        };

        channel = new TextChannelImpl(channelId, guild){
            @Override
            public RestAction<Message> retrieveMessageById(long id){
                return retrieveMessageById(Long.toString(id));
            }
            @Nonnull
            @Override
            public RestAction<Message> retrieveMessageById(@Nonnull String id){
                return new RestActionImpl<Message>(jda, null){
                    @Override
                    public Message complete(){
                        if(messageMap.containsKey(id))
                            return messageMap.get(id);
                        else
                            throw ErrorResponseException.create(ErrorResponse.UNAUTHORIZED, new Response(0L, Collections.emptySet()));
                    }
                };
            }
        };
        role = new RoleImpl(roleId, guild);
        user = new UserImpl(userId, jda);
        member = new MemberImpl(guild, user);

        guild.setName(guildName);
        channel.setName(channelName);
        role.setName(roleName);
        user.setName(userName);
        member.setNickname(memberNickname);

        guildMap.put(guild.getId(), guild);
        channelMap.put(channel.getId(), channel);
        roleMap.put(role.getId(), role);
        userMap.put(user.getId(), user);
        memberMap.put(member.getId(), member);
        messageMap.put(message.getId(), message);

        guildMap.put(guild.getName(), guild);
        channelMap.put(channel.getName(), channel);
        roleMap.put(role.getName(), role);
        userMap.put(user.getName(), user);
        memberMap.put(member.getNickname(), member);
    }

    protected CommandGlobalScope scope;

    @Before
    public void initScope(){
        ModelPath path = new ModelPath(Paths.get(""));
        CommandLanguage language = new CommandLanguage();
        scope = new CommandGlobalScope(path, language);

        CommandHelper.parse(scope,"src/test/resources/Command.cmd");
    }

    @Before
    public void initLog(){
        Log.enableFailQuick(false);
    }

    @After
    public void clearLog(){
        Log.getFindings().clear();
    }

    protected ASTCallArtifact ast;
    protected CommandSymbol symbol;

    protected void parse(String content){
        try{
            CallParser parser = new CallParser();

            Optional<ASTCallArtifact> call = parser.parse_String(content);

            assertThat(call).isPresent();

            ast = call.get();

            Optional<CommandSymbol> symbolOpt =  scope.resolveCommand(ast.getQualifiedName());

            assertThat(symbolOpt).isPresent();

            symbol = symbolOpt.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    protected List<ParameterVariableSymbol> getParameters(CommandSymbol command){
        return command.getSpannedScope().getLocalParameterVariableSymbols();
    }

    protected List<Rank.Ranks> getValidRanks(CommandSymbol command){
        return command.getSpannedScope().getLocalRankNameSymbols().stream().map(RankNameSymbol::getAstNode).map(ASTRankName::getRank).collect(Collectors.toList());
    }

    protected List<Permission> getRequiredPermission(CommandSymbol command){
        return command.getSpannedScope().getLocalPermissionNameSymbols().stream().map(PermissionNameSymbol::getPermission).collect(Collectors.toList());
    }
}
