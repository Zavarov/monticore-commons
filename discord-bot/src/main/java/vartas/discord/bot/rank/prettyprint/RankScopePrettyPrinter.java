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
import vartas.discord.bot.rank._symboltable.IRankScope;
import vartas.discord.bot.rank._symboltable.RankArtifactScope;
import vartas.discord.bot.rank._symboltable.RankScope;
import vartas.discord.bot.rank._symboltable.UserWithRankSymbol;
import vartas.discord.bot.rank._visitor.RankScopeVisitor;

public class RankScopePrettyPrinter implements RankScopeVisitor {
    protected RankSymbolPrettyPrinter prettyPrinter;
    protected RankScopeVisitor realThis = this;
    protected IndentPrinter printer;

    public RankScopePrettyPrinter(IndentPrinter printer){
        this.printer = printer;
        this.prettyPrinter = new RankSymbolPrettyPrinter(printer);
    }

    @Override
    public void setRealThis(RankScopeVisitor realThis){
        this.realThis = realThis;
    }

    @Override
    public RankScopeVisitor getRealThis(){
        return realThis;
    }

    public String prettyPrint(IRankScope scope){
        scope.accept(getRealThis());

        String content = printer.getContent();
        printer.clearBuffer();
        return content;
    }

    @Override
    public void visit(RankArtifactScope scope){
        printer.addLine("rank {");
    }

    @Override
    public void handle(RankScope scope) {
        for(UserWithRankSymbol symbol : scope.getLocalUserWithRankSymbols())
            symbol.accept(prettyPrinter);
    }

    @Override
    public void endVisit(RankArtifactScope scope){
        printer.addLine("}");
    }
}
