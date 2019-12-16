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

import de.monticore.prettyprint.IndentPrinter;
import org.jetbrains.annotations.NotNull;
import vartas.MonticoreEscapeUtils;
import vartas.discord.bot.entities.Configuration;
import vartas.discord.bot.visitor.ConfigurationVisitor;

import java.util.Collection;
import java.util.regex.Pattern;

public class ConfigurationPrettyPrinter implements ConfigurationVisitor {
    protected IndentPrinter printer = new IndentPrinter();

    public ConfigurationPrettyPrinter(){
        printer.setIndentLength(4);
    }

    public String prettyPrint(Configuration config){
        handle(config);

        String result = printer.getContent();
        printer.clearBuffer();
        return result;
    }

    @Override
    public void visit(Configuration config){
        printer.print("configuration ");
        printer.print(config.getGuildId());
        printer.addLine("L {");
    }

    @Override
    public void endVisit(@NotNull Configuration config){
        printer.addLine("}");
    }

    @Override
    public void visit(@NotNull String prefix){
        printer.print("prefix ");
        printer.print("\"");
        printer.print(MonticoreEscapeUtils.escapeMonticore(prefix));
        printer.println("\"");
    }

    @Override
    public void visit(Pattern pattern){
        printer.print("blacklist ");
        printer.print("\"");
        printer.print(MonticoreEscapeUtils.escapeMonticore(pattern.pattern()));
        printer.println("\"");
    }

    @Override
    public void visit(Configuration.LongType type, @NotNull String key, Collection<Long> values) {
        printer.print(type.getName());
        printer.print(" \"");
        printer.print(MonticoreEscapeUtils.escapeMonticore(key));
        printer.print("\" ");
        printer.addLine("{");
        for(long value : values){
            if(type == Configuration.LongType.SELFASSIGNABLE)
                printer.print("role : ");
            else if(type == Configuration.LongType.SUBREDDIT)
                printer.print("channel : ");
            printer.print(value);
            printer.println("L");
        }
        printer.addLine("}");
    }
}
