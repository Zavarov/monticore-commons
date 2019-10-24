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

package vartas.discord.aggregated.parameter.symboltable;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import de.monticore.literals.mccommonliterals._visitor.MCCommonLiteralsVisitor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import vartas.arithmeticexpressions._visitor.ArithmeticExpressionsInheritanceVisitor;
import vartas.arithmeticexpressions._visitor.ArithmeticExpressionsVisitor;
import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._ast.ASTArgumentType;
import vartas.discord.argument._visitor.ArgumentDelegatorVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;
import vartas.discord.parameter._symboltable.RoleParameterSymbol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class RoleParameter2ArgumentSymbol extends RoleParameterSymbol implements Parameter2ArgumentInterface<Role>{
    protected ASTArgumentType argument;
    protected ArgumentDelegatorVisitor visitor;

    protected Role role;
    protected Guild guild;

    public RoleParameter2ArgumentSymbol(String name, ASTArgumentType argument) {
        super(name);
        this.argument = argument;

        visitor = new ArgumentDelegatorVisitor();
        visitor.setArgumentVisitor(new ContextSensitiveArgumentVisitor());
        visitor.setMCCommonLiteralsVisitor(new LiteralsArgumentVisitor());
        visitor.setArithmeticExpressionsVisitor(new ExpressionArgumentVisitor());
    }

    @Override
    public String getQualifiedResolvedName(){
        return Member.class.getCanonicalName();
    }

    @Override
    public Optional<Role> resolve(Message context){
        checkNotNull(context.getGuild());
        guild = context.getGuild();

        argument.accept(visitor);
        return Optional.ofNullable(role);
    }

    /**
     * This class evaluates the id of the role by using the arithmetic expression inside the argument.
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
            role = guild.getRoleById(value.longValueExact());
        }
    }

    /**
     * This class evaluates the name of the role inside the argument.
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
            List<Role> roles = guild.getRolesByName(ast.getValue(), false);
            if(roles.size() == 1)
                role = roles.get(0);
        }
    }
}