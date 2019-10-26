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

import net.dv8tion.jda.api.entities.Message;
import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._ast.ASTExpressionArgument;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentDelegatorVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.math.BigDecimal;
import java.util.Optional;

public class ExpressionArgumentSymbol extends ArgumentSymbol {
    protected ArgumentDelegatorVisitor visitor;

    protected BigDecimal value;

    public ExpressionArgumentSymbol(String name) {
        super(name);

        this.visitor = new ArgumentDelegatorVisitor();
        visitor.setArgumentVisitor(new ExpressionArgumentVisitor());
    }

    @Override
    public String getQualifiedResolvedName(){
        return BigDecimal.class.getCanonicalName();
    }

    @Override
    public Optional<BigDecimal> resolve(Message context){
        getAstNode().ifPresent(ast -> ast.accept(visitor));
        return Optional.ofNullable(value);
    }

    /**
     * This class evaluates the arithmetic expression inside the argument.
     */
    private class ExpressionArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void traverse(ASTExpressionArgument ast){
            value = ArithmeticExpressionsValueCalculator.valueOf(ast.getExpression());
        }
    }
}
