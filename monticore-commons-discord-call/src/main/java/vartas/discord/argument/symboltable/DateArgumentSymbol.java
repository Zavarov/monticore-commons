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

package vartas.discord.argument.symboltable;

import de.se_rwth.commons.logging.Log;
import net.dv8tion.jda.api.entities.Message;
import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._ast.ASTDateArgument;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentDelegatorVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

public class DateArgumentSymbol extends ArgumentSymbol {
    protected ArgumentDelegatorVisitor visitor;

    protected Date date;
    private SimpleDateFormat dateFormat;

    public DateArgumentSymbol(String name) {
        super(name);

        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        this.visitor = new ArgumentDelegatorVisitor();
        visitor.setArgumentVisitor(new DateArgumentVisitor());
    }

    @Override
    public String getQualifiedResolvedName(){
        return Date.class.getCanonicalName();
    }

    @Override
    public Optional<Date> resolve(Message context){
        getAstNode().ifPresent(ast -> ast.accept(visitor));
        return Optional.ofNullable(date);
    }

    private class DateArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void visit(ASTDateArgument ast){
            try{
                int day = ArithmeticExpressionsValueCalculator.valueOf(ast.getDay()).intValueExact();
                int month = ArithmeticExpressionsValueCalculator.valueOf(ast.getMonth()).intValueExact();
                int year = ArithmeticExpressionsValueCalculator.valueOf(ast.getYear()).intValueExact();
                date = dateFormat.parse(String.format("%2d-%2d-%4d", day, month, year));
            }catch(ParseException e){
                Log.error(e.getMessage());
            }
        }
    }
}
