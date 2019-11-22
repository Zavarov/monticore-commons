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

package vartas.discord.bot.guild.prettyprint;

import com.google.common.collect.Multimap;
import de.monticore.prettyprint.IndentPrinter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import vartas.discord.bot.entities.BotGuild;
import vartas.reddit.MonticoreEscapeUtils;

import java.util.Collection;
import java.util.regex.Pattern;

public class BotGuildPrettyPrinter {
    protected IndentPrinter printer = new IndentPrinter();

    public String prettyPrint(BotGuild config){
        IndentPrinter printer = new IndentPrinter();
        printer.print("guild ");
        printer.print(config.getId());
        printer.addLine("L {");
        printPrefix(config);
        printBlacklist(config);
        printSubreddits(config);
        printRoleGroups(config);
        printer.addLine("}");

        String result = printer.getContent();
        printer.clearBuffer();
        return result;
    }

    private void printPrefix(BotGuild config){
        config.prefix().ifPresent(prefix -> {
            printer.print("prefix ");
            printer.print("\"");
            printer.print(MonticoreEscapeUtils.escapeMonticore(prefix));
            printer.print("\"");
        });
    }

    private void printBlacklist(BotGuild config){
        config.blacklist().map(Pattern::pattern).ifPresent(pattern -> {
            printer.print("blacklist ");
            printer.print("\"");
            printer.print(MonticoreEscapeUtils.escapeMonticore(pattern));
            printer.print("\"");
        });
    }
    private void printRoleGroups(BotGuild config){
        Multimap<String, Role> roles = config.resolve(BotGuild.ROLEGROUP, Guild::getRoleById);
        roles.asMap().forEach(this::printRoleGroup);
    }
    private void printRoleGroup(String key, Collection<Role> roles) {
        printer.print("rolegroup ");
        printer.print("\"");
        printer.print(MonticoreEscapeUtils.escapeMonticore(key));
        printer.print("\"");
        printer.addLine("{");
        roles.forEach(role -> {
            printer.print("role : ");
            printer.print(role.getId());
            printer.print("L");
        });
        printer.addLine("}");
    }
    private void printSubreddits(BotGuild config){
        Multimap<String, TextChannel> channels = config.resolve(BotGuild.SUBREDDIT, Guild::getTextChannelById);
        channels.asMap().forEach(this::printSubreddit);
    }
    private void printSubreddit(String key, Collection<TextChannel> channels) {
        printer.print("subreddit ");
        printer.print("\"");
        printer.print(MonticoreEscapeUtils.escapeMonticore(key));
        printer.print("\"");
        printer.addLine("{");
        channels.forEach(channel -> {
            printer.print("channel : ");
            printer.print(channel.getId());
            printer.print("L");
        });
        printer.addLine("}");
    }
}
