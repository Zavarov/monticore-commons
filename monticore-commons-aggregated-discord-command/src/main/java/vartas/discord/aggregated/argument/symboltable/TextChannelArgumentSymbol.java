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

import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._ast.ASTExpressionArgumentEntry;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class TextChannelArgumentSymbol extends ArgumentSymbol {
    protected ArgumentVisitor visitor;

    protected TextChannel channel;
    protected Guild guild;

    public TextChannelArgumentSymbol(String name) {
        super(name);

        visitor = new TextChannelArgumentVisitor();
    }

    public Optional<TextChannel> accept(Message context){
        channel = null;

        checkNotNull(context.getGuild());
        guild = context.getGuild();

        getAstNode().ifPresent(ast -> ast.accept(visitor));
        return Optional.ofNullable(channel);
    }

    private class TextChannelArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void handle(ASTExpressionArgumentEntry ast){
            Optional<BigDecimal> valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getExpression());
            valueOpt.ifPresent(value -> channel = guild.getTextChannelById(value.longValueExact()));
        }

        @Override
        public void handle(ASTStringLiteral ast){
            List<TextChannel> channels = guild.getTextChannelsByName(ast.getValue(), false);
            if(channels.size() == 1)
                channel = channels.get(0);
        }
    }
}
