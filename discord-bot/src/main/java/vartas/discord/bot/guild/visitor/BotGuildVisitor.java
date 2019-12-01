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

package vartas.discord.bot.guild.visitor;

import de.se_rwth.commons.logging.Log;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import vartas.MonticoreEscapeUtils;
import vartas.discord.bot.entities.BotGuild;
import vartas.discord.bot.guild._ast.*;
import vartas.discord.bot.guild._visitor.GuildVisitor;

import java.util.Optional;
import java.util.regex.Pattern;

public class BotGuildVisitor implements GuildVisitor {
    protected BotGuild config;
    protected Guild guild;
    protected String log = this.getClass().getSimpleName();

    public void accept(ASTGuildArtifact artifact, BotGuild config, Guild guild){
        this.config = config;
        this.guild = guild;
        artifact.accept(getRealThis());
    }

    public void handle(ASTPrefixEntry node){
        config.set(MonticoreEscapeUtils.unescapeMonticore(node.getName()));
    }

    public void handle(ASTBlacklistEntry node){
        config.set(Pattern.compile(MonticoreEscapeUtils.unescapeMonticore(node.getName())));
    }

    public void handle(ASTRoleGroupEntry node){
        for(ASTLongGroupElement element : node.getLongGroup().getLongGroupElementList()){
            Log.debug(String.format("Loading role %s in group '%s'",element.getName(), node.getName()), log);
            Optional<Role> roleOpt = Optional.ofNullable(guild.getRoleById(element.getName()));
            roleOpt.ifPresentOrElse(role -> config.add(node.getName(), role), () -> Log.debug("Role couldn't be resolved", log));
        }
    }

    public void handle(ASTSubredditGroupEntry node){
        for(ASTLongGroupElement element : node.getLongGroup().getLongGroupElementList()){
            Log.debug(String.format("Loading channel %s in group '%s'",element.getName(), node.getName()), log);
            Optional<TextChannel> channelOpt = Optional.ofNullable(guild.getTextChannelById(element.getName()));
            channelOpt.ifPresentOrElse(channel -> config.add(node.getName(), channel), () -> Log.debug("Channel couldn't be resolved", log));
        }
    }
}
