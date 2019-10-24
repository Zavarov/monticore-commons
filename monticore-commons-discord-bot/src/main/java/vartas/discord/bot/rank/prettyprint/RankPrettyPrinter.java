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
import vartas.discord.bot.rank.RankConfiguration;
import vartas.discord.bot.rank.RankType;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class RankPrettyPrinter {
    protected IndentPrinter printer;

    public RankPrettyPrinter(IndentPrinter printer){
        this.printer = printer;
    }

    public String prettyprint(RankConfiguration config){
        printer.clearBuffer();

        printer.addLine("rank {");

        printPermission(config);

        printer.addLine("}");

        return printer.getContent();
    }

    private void printPermission(RankConfiguration config){
        for(Map.Entry<Long, Collection<RankType>> entry : config.getRanks().asMap().entrySet())
            printPermission(entry.getKey(), entry.getValue());
    }

    private void printPermission(long id, Collection<RankType> types){
        printer.print(String.format("user : %d L has rank ", id));

        Iterator<RankType> iterator = types.iterator();

        while(iterator.hasNext()){
            printer.print(iterator.next().getMontiCoreName());
            if(iterator.hasNext())
                printer.print(",");
        }

        printer.println();
    }
}
