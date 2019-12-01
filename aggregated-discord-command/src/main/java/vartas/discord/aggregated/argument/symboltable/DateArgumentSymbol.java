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

import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._ast.ASTDateArgumentEntry;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public class DateArgumentSymbol extends ArgumentSymbol {
    protected ArgumentVisitor visitor;

    protected LocalDate date;

    public DateArgumentSymbol(String name) {
        super(name);
        this.visitor = new DateArgumentVisitor();
    }

    public Optional<LocalDate> accept(){
        date = null;
        getAstNode().accept(visitor);
        return Optional.ofNullable(date);
    }

    private class DateArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void handle(ASTDateArgumentEntry ast){
            Optional<BigDecimal> valueOpt;
            int day;
            int month;
            int year;

            valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getDay());
            if(valueOpt.isEmpty())
                return;
            day = valueOpt.get().intValueExact();

            valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getMonth());
            if(valueOpt.isEmpty())
                return;
            month = valueOpt.get().intValueExact();

            valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getYear());
            if(valueOpt.isEmpty())
                return;
            year = valueOpt.get().intValueExact();

            date = LocalDate.of(year, month, day);
        }
    }
}
