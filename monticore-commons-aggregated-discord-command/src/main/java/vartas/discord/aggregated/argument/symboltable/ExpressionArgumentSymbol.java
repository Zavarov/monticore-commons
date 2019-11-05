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
import vartas.discord.argument._ast.ASTExpressionArgumentEntry;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.math.BigDecimal;
import java.util.Optional;

public class ExpressionArgumentSymbol extends ArgumentSymbol {
    protected ArgumentVisitor visitor;

    protected BigDecimal value;

    public ExpressionArgumentSymbol(String name) {
        super(name);

        this.visitor = new ExpressionArgumentVisitor();
    }

    public Optional<BigDecimal> accept(){
        value = null;
        getAstNode().ifPresent(ast -> ast.accept(visitor));
        return Optional.ofNullable(value);
    }

    private class ExpressionArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void handle(ASTExpressionArgumentEntry ast){
            Optional<BigDecimal> valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getExpression());
            valueOpt.ifPresent(bigDecimal -> value = bigDecimal);
        }
    }
}
