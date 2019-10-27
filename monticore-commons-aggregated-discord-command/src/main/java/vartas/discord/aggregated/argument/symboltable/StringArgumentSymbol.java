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
import de.monticore.literals.mccommonliterals._visitor.MCCommonLiteralsVisitor;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentDelegatorVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;

import java.util.Optional;

public class StringArgumentSymbol extends ArgumentSymbol {
    protected ArgumentDelegatorVisitor visitor;

    protected String value;

    public StringArgumentSymbol(String name) {
        super(name);

        visitor = new ArgumentDelegatorVisitor();
        visitor.setArgumentVisitor(new ContextSensitiveArgumentVisitor());
        visitor.setMCCommonLiteralsVisitor(new LiteralsArgumentVisitor());
    }

    public Optional<String> accept(){
        getAstNode().ifPresent(ast -> ast.accept(visitor));
        return Optional.ofNullable(value);
    }

    /**
     * This class evaluates the value of the string inside the argument.
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
            value = ast.getValue();
        }
    }
}
