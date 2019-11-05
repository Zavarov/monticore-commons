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

import de.se_rwth.commons.logging.Log;
import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._ast.ASTDateArgumentEntry;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

public class DateArgumentSymbol extends ArgumentSymbol {
    protected ArgumentVisitor visitor;

    protected Date date;
    protected SimpleDateFormat dateFormat;

    public DateArgumentSymbol(String name) {
        super(name);

        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.visitor = new DateArgumentVisitor();
    }

    public Optional<Date> accept(){
        date = null;
        getAstNode().ifPresent(ast -> ast.accept(visitor));
        return Optional.ofNullable(date);
    }

    private class DateArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void handle(ASTDateArgumentEntry ast){
            try{
                Optional<BigDecimal> valueOpt;
                int day;
                int month;
                int year;

                valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getDay());
                if(!valueOpt.isPresent())
                    return;
                day = valueOpt.get().intValueExact();

                valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getMonth());
                if(!valueOpt.isPresent())
                    return;
                month = valueOpt.get().intValueExact();

                valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getYear());
                if(!valueOpt.isPresent())
                    return;
                year = valueOpt.get().intValueExact();

                date = dateFormat.parse(String.format("%2d-%2d-%4d", day, month, year));
            }catch(ParseException e){
                Log.error(e.getMessage());
            }
        }
    }
}
