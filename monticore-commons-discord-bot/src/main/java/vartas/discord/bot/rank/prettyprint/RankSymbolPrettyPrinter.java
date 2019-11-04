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
import net.sourceforge.plantuml.StringUtils;
import vartas.discord.bot.rank._symboltable.ICommonRankSymbol;
import vartas.discord.bot.rank._symboltable.RankNameSymbol;
import vartas.discord.bot.rank._symboltable.UserWithRankSymbol;
import vartas.discord.bot.rank._visitor.RankSymbolVisitor;

import java.util.Iterator;
import java.util.Locale;

public class RankSymbolPrettyPrinter implements RankSymbolVisitor {
    protected RankSymbolVisitor realThis = this;
    protected IndentPrinter printer;

    public RankSymbolPrettyPrinter(IndentPrinter printer){
        this.printer = printer;
    }

    @Override
    public void setRealThis(RankSymbolVisitor realThis){
        this.realThis = realThis;
    }

    @Override
    public RankSymbolVisitor getRealThis(){
        return realThis;
    }

    public String prettyprint(ICommonRankSymbol symbol){
        symbol.accept(getRealThis());

        String content = printer.getContent();
        printer.clearBuffer();
        return content;
    }

    public void visit(UserWithRankSymbol symbol){
        printer.print("user : ");
        printer.print(symbol.getName());
        printer.print("L");
        printer.print(" has rank ");
    }

    @Override
    public void traverse(UserWithRankSymbol ast){
        Iterator<RankNameSymbol> iterator = ast.getSpannedScope().getLocalRankNameSymbols().iterator();

        while(iterator.hasNext()){
            iterator.next().accept(getRealThis());
            if(iterator.hasNext())
                printer.print(", ");
        }
        printer.println();
    }

    @Override
    public void visit(RankNameSymbol symbol){
        printer.print(StringUtils.capitalize(symbol.getName().toLowerCase(Locale.ENGLISH)));
    }
}
