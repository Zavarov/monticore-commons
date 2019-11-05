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

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._ast.ASTExpressionArgumentEntry;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.math.BigDecimal;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class MessageArgumentSymbol extends ArgumentSymbol {
    protected ArgumentVisitor visitor;

    protected Message message;
    protected TextChannel channel;

    public MessageArgumentSymbol(String name) {
        super(name);

        visitor = new MessageArgumentVisitor();
    }

    public Optional<Message> accept(Message context){
        message = null;

        checkNotNull(context.getTextChannel());
        channel = context.getTextChannel();

        getAstNode().ifPresent(ast -> ast.accept(visitor));
        return Optional.ofNullable(message);
    }

    private class MessageArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void handle(ASTExpressionArgumentEntry ast){
            Optional<BigDecimal> valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getExpression());
            try {
                valueOpt.ifPresent(value -> message = channel.retrieveMessageById(value.longValueExact()).complete());
            }catch(ErrorResponseException e){
                //The message id was invalid
                if(e.getErrorResponse() != ErrorResponse.UNKNOWN_MESSAGE)
                    throw e;
            }
        }
    }
}
