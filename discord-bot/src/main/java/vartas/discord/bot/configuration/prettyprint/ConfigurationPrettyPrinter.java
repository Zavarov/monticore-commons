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

package vartas.discord.bot.configuration.prettyprint;

import com.google.common.collect.Multimap;
import de.monticore.prettyprint.IndentPrinter;
import vartas.MonticoreEscapeUtils;
import vartas.discord.bot.entities.BotGuild;

import java.util.Collection;
import java.util.regex.Pattern;

public class BotGuildPrettyPrinter {
    protected IndentPrinter printer = new IndentPrinter();

    public BotGuildPrettyPrinter(){
        printer.setIndentLength(4);
    }

    public String prettyPrint(BotGuild config){
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
            printer.println("\"");
        });
    }

    private void printBlacklist(BotGuild config){
        config.blacklist().map(Pattern::pattern).ifPresent(pattern -> {
            printer.print("blacklist ");
            printer.print("\"");
            printer.print(MonticoreEscapeUtils.escapeMonticore(pattern));
            printer.println("\"");
        });
    }
    private void printRoleGroups(BotGuild config){
        Multimap<String, Long> roles = config.resolve(BotGuild.ROLEGROUP, (g,l) -> l);
        roles.asMap().forEach(this::printRoleGroup);
    }
    private void printRoleGroup(String key, Collection<Long> roles) {
        printer.print(BotGuild.ROLEGROUP);
        printer.print(" \"");
        printer.print(MonticoreEscapeUtils.escapeMonticore(key));
        printer.print("\" ");
        printer.addLine("{");
        roles.forEach(role -> {
            printer.print("role : ");
            printer.print(role);
            printer.println("L");
        });
        printer.addLine("}");
    }
    private void printSubreddits(BotGuild config){
        Multimap<String, Long> channels = config.resolve(BotGuild.SUBREDDIT, (g,l) -> l);
        channels.asMap().forEach(this::printSubreddit);
    }
    private void printSubreddit(String key, Collection<Long> channels) {
        printer.print(BotGuild.SUBREDDIT);
        printer.print(" \"");
        printer.print(MonticoreEscapeUtils.escapeMonticore(key));
        printer.print("\" ");
        printer.addLine("{");
        channels.forEach(channel -> {
            printer.print("channel : ");
            printer.print(channel);
            printer.println("L");
        });
        printer.addLine("}");
    }
}
