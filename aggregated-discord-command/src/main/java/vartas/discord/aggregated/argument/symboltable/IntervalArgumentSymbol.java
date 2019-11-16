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

package vartas.discord.aggregated.argument.symboltable;

import vartas.chart.Interval;
import vartas.chart.interval._symboltable.IntervalNameSymbol;
import vartas.discord.argument._ast.ASTIntervalArgumentEntry;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.util.Optional;

public class IntervalArgumentSymbol extends ArgumentSymbol {
    protected ArgumentVisitor visitor;

    protected Interval interval;

    public IntervalArgumentSymbol(String name) {
        super(name);

        visitor = new IntervalArgumentVisitor();
    }

    public Optional<Interval> accept(){
        interval = null;
        getAstNode().ifPresent(ast -> ast.accept(visitor));
        return Optional.ofNullable(interval);
    }

    private class IntervalArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void handle(ASTIntervalArgumentEntry node){
            IntervalNameSymbol symbol = new IntervalNameSymbol(node.getIntervalName().getName());
            symbol.setAstNode(node.getIntervalName());
            interval = symbol.getInterval();
        }
    }
}
