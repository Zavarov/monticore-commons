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
import net.dv8tion.jda.api.entities.Role;
import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._ast.ASTExpressionArgumentEntry;
import vartas.discord.argument._ast.ASTRoleArgumentEntry;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class RoleArgumentSymbol extends ArgumentSymbol {
    protected ArgumentVisitor visitor;

    protected Role role;
    protected Guild guild;

    public RoleArgumentSymbol(String name) {
        super(name);

        visitor = new RoleArgumentVisitor();
    }

    public Optional<Role> accept(Message context){
        role = null;

        checkNotNull(context.getGuild());
        guild = context.getGuild();

        getAstNode().ifPresent(ast -> ast.accept(visitor));
        return Optional.ofNullable(role);
    }

    private class RoleArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void handle(ASTExpressionArgumentEntry ast){
            Optional<BigDecimal> valueOpt = ArithmeticExpressionsValueCalculator.valueOf(ast.getExpression());
            valueOpt.ifPresent(value -> role = guild.getRoleById(value.longValueExact()));
        }

        @Override
        public void handle(ASTRoleArgumentEntry ast){
            role = guild.getRoleById(ast.getRole().getId().getSource());
        }

        @Override
        public void handle(ASTStringLiteral ast){
            List<Role> roles = guild.getRolesByName(ast.getValue(), false);
            //Role via ID has precedence
            if(roles.size() == 1 && role == null)
                role = roles.get(0);
        }
    }
}