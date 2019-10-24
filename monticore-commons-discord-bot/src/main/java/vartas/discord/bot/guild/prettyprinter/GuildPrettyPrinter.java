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

package vartas.discord.bot.guild.prettyprinter;

import com.google.common.collect.Multimap;
import de.monticore.prettyprint.IndentPrinter;
import vartas.discord.bot.guild.GuildConfiguration;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class GuildPrettyPrinter {
    protected IndentPrinter printer;

    public GuildPrettyPrinter(IndentPrinter printer){
        this.printer = printer;
    }

    public String prettyprint(GuildConfiguration config){
        printer.clearBuffer();

        printer.addLine("guild {");

        printPrefix(config);
        printFilters(config);
        printRoleGroups(config);
        printSubredditFeeds(config);

        printer.addLine("}");

        return printer.getContent();
    }

    private void printPrefix(GuildConfiguration config){
        config.getPrefix().ifPresent(prefix -> printer.println(String.format("prefix = \"%s\"", prefix)));
    }

    private void printFilters(GuildConfiguration guild){
        Set<String> expressions = guild.getFilter();

        if(!expressions.isEmpty()){
            printer.addLine("filter {");

            for(String expression : expressions)
                printer.println(String.format("\"%s\"", expression));

            printer.addLine("}");
        }
    }

    private void printRoleGroups(GuildConfiguration config){
        Multimap<String, Long> roleGroups = config.getTags();

        for(Map.Entry<String, Collection<Long>> entry : roleGroups.asMap().entrySet()){
            printer.addLine(String.format("tag \"%s\" {", entry.getKey()));

            for(Long role : entry.getValue())
                printer.println(String.format("role:%dL", role));

            printer.addLine("}");
        }
    }

    private void printSubredditFeeds(GuildConfiguration config){
        Multimap<String, Long>  redditFeeds = config.getRedditFeeds();

        for(Map.Entry<String, Collection<Long>> entry : redditFeeds.asMap().entrySet()){
            printer.addLine(String.format("subreddit \"%s\" {", entry.getKey()));

            for(Long channel : entry.getValue())
                printer.println(String.format("channel:%dL", channel));

            printer.addLine("}");
        }
    }
}
