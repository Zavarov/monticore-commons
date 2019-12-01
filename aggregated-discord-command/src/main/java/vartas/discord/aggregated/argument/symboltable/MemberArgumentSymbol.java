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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._ast.ASTExpressionArgumentEntry;
import vartas.discord.argument._ast.ASTUserArgumentEntry;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class MemberArgumentSymbol extends ArgumentSymbol {
    protected ArgumentVisitor visitor;

    protected Member member;
    protected Guild guild;

    public MemberArgumentSymbol(String name) {
        super(name);

        visitor = new MemberArgumentVisitor();
    }

    public Optional<Member> accept(Message context){
        member = null;

        checkNotNull(context.getGuild());
        guild = context.getGuild();

        getAstNode().accept(visitor);
        return Optional.ofNullable(member);
    }

    private class MemberArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void handle(ASTExpressionArgumentEntry ast){
            Optional<BigDecimal> valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getExpression());
            valueOpt.ifPresent(value -> member = guild.getMemberById(value.longValueExact()));
        }

        @Override
        public void handle(ASTUserArgumentEntry ast){
            member = guild.getMemberById(ast.getUser().getId().getSource());
        }

        @Override
        public void handle(ASTStringLiteral ast){
            List<Member> members = guild.getMembersByName(ast.getValue(), false);
            //Member via ID has precedence
            if(members.size() == 1 && member == null)
                member = members.get(0);
        }
    }
}
