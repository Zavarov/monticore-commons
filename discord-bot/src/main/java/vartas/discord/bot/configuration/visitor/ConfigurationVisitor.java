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

package vartas.discord.bot.configuration.visitor;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import vartas.MonticoreEscapeUtils;
import vartas.discord.bot.configuration._ast.*;
import vartas.discord.bot.entities.Configuration;

import java.util.Optional;
import java.util.regex.Pattern;

public class ConfigurationVisitor implements vartas.discord.bot.configuration._visitor.ConfigurationVisitor {
    protected Configuration config;
    protected Guild guild;
    protected String log = this.getClass().getSimpleName();

    public void accept(ASTConfigurationArtifact artifact, Configuration config, Guild guild){
        this.config = config;
        this.guild = guild;
        artifact.accept(getRealThis());
    }

    public void handle(ASTPrefixEntry node){
        config.setPrefix(MonticoreEscapeUtils.unescapeMonticore(node.getName()));
    }

    public void handle(ASTBlacklistEntry node){
        config.setPattern(Pattern.compile(MonticoreEscapeUtils.unescapeMonticore(node.getName())));
    }

    public void handle(ASTRoleGroupEntry node){
        for(ASTLongGroupElement element : node.getLongGroup().getLongGroupElementList()){
            String id = element.getElement().getDigits();
            Optional<Role> roleOpt = Optional.ofNullable(guild.getRoleById(id));
            roleOpt.ifPresent(role -> config.add(node.getName(), role));
        }
    }

    public void handle(ASTSubredditGroupEntry node){
        for(ASTLongGroupElement element : node.getLongGroup().getLongGroupElementList()){
            String id = element.getElement().getDigits();
            Optional<TextChannel> channelOpt = Optional.ofNullable(guild.getTextChannelById(id));
            channelOpt.ifPresent(channel -> config.add(node.getName(), channel));
        }
    }
}
