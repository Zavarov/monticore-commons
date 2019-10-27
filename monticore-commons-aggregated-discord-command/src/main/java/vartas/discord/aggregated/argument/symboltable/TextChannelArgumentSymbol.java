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

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import de.monticore.literals.mccommonliterals._visitor.MCCommonLiteralsVisitor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import vartas.arithmeticexpressions._visitor.ArithmeticExpressionsInheritanceVisitor;
import vartas.arithmeticexpressions._visitor.ArithmeticExpressionsVisitor;
import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentDelegatorVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class TextChannelArgumentSymbol extends ArgumentSymbol {
    protected ArgumentDelegatorVisitor visitor;

    protected TextChannel channel;
    protected Guild guild;

    public TextChannelArgumentSymbol(String name) {
        super(name);

        visitor = new ArgumentDelegatorVisitor();
        visitor.setArgumentVisitor(new ContextSensitiveArgumentVisitor());
        visitor.setMCCommonLiteralsVisitor(new LiteralsArgumentVisitor());
        visitor.setArithmeticExpressionsVisitor(new ExpressionArgumentVisitor());
    }

    public Optional<TextChannel> accept(Message context){
        channel = null;

        checkNotNull(context.getGuild());
        guild = context.getGuild();

        getAstNode().ifPresent(ast -> ast.accept(visitor));
        return Optional.ofNullable(channel);
    }

    /**
     * This class evaluates the id of the channel by using the arithmetic expression inside the argument.
     */
    private class ExpressionArgumentVisitor implements ArithmeticExpressionsInheritanceVisitor {
        ArithmeticExpressionsVisitor realThis = this;

        @Override
        public void setRealThis(ArithmeticExpressionsVisitor realThis){
            this.realThis = realThis;
        }

        @Override
        public ArithmeticExpressionsVisitor getRealThis(){
            return realThis;
        }

        @Override
        public void visit(ASTExpression ast){
            BigDecimal value = ArithmeticExpressionsValueCalculator.valueOf(ast);
            channel = guild.getTextChannelById(value.longValueExact());
        }
    }

    /**
     * This class evaluates the name the channel inside the argument.
     * The id is evaluated via the expression.
     */
    private class LiteralsArgumentVisitor implements MCCommonLiteralsVisitor {
        MCCommonLiteralsVisitor realThis = this;

        @Override
        public void setRealThis(MCCommonLiteralsVisitor realThis){
            this.realThis = realThis;
        }

        @Override
        public MCCommonLiteralsVisitor getRealThis(){
            return realThis;
        }

        @Override
        public void visit(ASTStringLiteral ast){
            List<TextChannel> channels = guild.getTextChannelsByName(ast.getValue(), false);
            if(channels.size() == 1)
                channel = channels.get(0);
        }
    }
}
