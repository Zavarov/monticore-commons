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

import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import de.monticore.literals.mccommonliterals._visitor.MCCommonLiteralsVisitor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;
import vartas.discord.argument._ast.ASTArgumentType;
import vartas.discord.argument._ast.ASTExpressionArgument;
import vartas.discord.argument._visitor.ArgumentDelegatorVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;
import vartas.discord.parameter._symboltable.GuildParameterSymbol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class GuildParameter2ArgumentSymbol extends GuildParameterSymbol implements Parameter2ArgumentInterface<Guild>{
    protected ASTArgumentType argument;
    protected ArgumentDelegatorVisitor visitor;

    protected Guild guild;
    protected JDA jda;

    public GuildParameter2ArgumentSymbol(String name, ASTArgumentType argument) {
        super(name);
        this.argument = argument;

        visitor = new ArgumentDelegatorVisitor();
        visitor.setArgumentVisitor(new ExpressionArgumentVisitor());
        visitor.setMCCommonLiteralsVisitor(new LiteralsArgumentVisitor());
    }

    @Override
    public String getQualifiedResolvedName(){
        return User.class.getCanonicalName();
    }

    @Override
    public Optional<Guild> resolve(Message context){
        jda = context.getJDA();

        argument.accept(visitor);
        return Optional.ofNullable(guild);
    }

    /**
     * This class evaluates the arithmetic expression inside the argument.
     */
    private class ExpressionArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void traverse(ASTExpressionArgument ast){
            BigDecimal value = ArithmeticExpressionsValueCalculator.valueOf(ast.getExpression());
            guild = jda.getGuildById(value.longValueExact());
        }
    }

    /**
     * This class evaluates the name of the guild inside the argument.
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
            List<Guild> guilds = jda.getGuildsByName(ast.getValue(), false);
            if(guilds.size() == 1)
                guild = guilds.get(0);
        }
    }
}
