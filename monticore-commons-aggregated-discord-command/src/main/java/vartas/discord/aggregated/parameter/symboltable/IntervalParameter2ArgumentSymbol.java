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

package vartas.discord.aggregated.parameter.symboltable;

import net.dv8tion.jda.api.entities.Message;
import vartas.chart.Interval;
import vartas.chart.interval._ast.ASTInterval;
import vartas.chart.interval._visitor.IntervalInheritanceVisitor;
import vartas.chart.interval._visitor.IntervalVisitor;
import vartas.discord.argument._ast.ASTArgumentType;
import vartas.discord.argument._visitor.ArgumentDelegatorVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;
import vartas.discord.parameter._symboltable.IntervalParameterSymbol;

import java.util.Optional;

public class IntervalParameter2ArgumentSymbol extends IntervalParameterSymbol implements Parameter2ArgumentInterface<Interval> {
    protected ASTArgumentType argument;
    protected ArgumentDelegatorVisitor visitor;

    protected Interval interval;

    public IntervalParameter2ArgumentSymbol(String name, ASTArgumentType argument) {
        super(name);
        this.argument = argument;

        visitor = new ArgumentDelegatorVisitor();
        visitor.setArgumentVisitor(new ContextSensitiveArgumentVisitor());
        visitor.setIntervalVisitor(new IntervalArgumentVisitor());
    }

    @Override
    public String getQualifiedResolvedName(){
        return Interval.class.getCanonicalName();
    }

    @Override
    public Optional<Interval> resolve(Message context){
        argument.accept(visitor);
        return Optional.ofNullable(interval);
    }

    /**
     * This class evaluates the interval inside the argument.
     */
    private class IntervalArgumentVisitor implements IntervalInheritanceVisitor {
        IntervalVisitor realThis = this;

        @Override
        public void setRealThis(IntervalVisitor realThis){
            this.realThis = realThis;
        }

        @Override
        public IntervalVisitor getRealThis(){
            return realThis;
        }

        @Override
        public void visit(ASTInterval ast){
            interval = ast.getIntervalType();
        }
    }
}
