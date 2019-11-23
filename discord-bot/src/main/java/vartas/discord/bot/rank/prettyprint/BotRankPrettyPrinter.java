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

package vartas.discord.bot.rank.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import vartas.discord.bot.entities.BotRank;

public class BotRankPrettyPrinter {
    public String prettyPrint(BotRank config){
        IndentPrinter printer = new IndentPrinter();
        printer.setIndentLength(4);
        printer.addLine("rank {");
        config.get().asMap().forEach( (user, ranks) -> {
            printer.print("user : ");
            printer.print(user);
            printer.print("L");
            printer.print(" has rank ");
            printer.println(ranks.stream().map(BotRank.Type::getName).reduce((u,v) -> u + ", " + v).orElse(""));
        });
        printer.addLine("}");
        return printer.getContent();
    }
}
