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

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Message;
import vartas.discord.argument._ast.ASTArgumentType;
import vartas.discord.argument._visitor.ArgumentDelegatorVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;
import vartas.discord.onlinestatus._ast.ASTOnlineStatus;
import vartas.discord.onlinestatus._visitor.OnlineStatusInheritanceVisitor;
import vartas.discord.onlinestatus._visitor.OnlineStatusVisitor;
import vartas.discord.parameter._symboltable.OnlineStatusParameterSymbol;

import java.util.Optional;

public class OnlineStatusParameter2ArgumentSymbol extends OnlineStatusParameterSymbol implements Parameter2ArgumentInterface<OnlineStatus>{
    protected ASTArgumentType argument;
    protected ArgumentDelegatorVisitor visitor;

    protected OnlineStatus onlineStatus;

    public OnlineStatusParameter2ArgumentSymbol(String name, ASTArgumentType argument) {
        super(name);
        this.argument = argument;

        visitor = new ArgumentDelegatorVisitor();
        visitor.setArgumentVisitor(new ContextSensitiveArgumentVisitor());
        visitor.setOnlineStatusVisitor(new OnlineStatusArgumentVisitor());
    }

    @Override
    public String getQualifiedResolvedName(){
        return OnlineStatus.class.getCanonicalName();
    }

    @Override
    public Optional<OnlineStatus> resolve(Message context){
        argument.accept(visitor);
        return Optional.ofNullable(onlineStatus);
    }

    /**
     * This class evaluates the online status inside the argument.
     */
    private class OnlineStatusArgumentVisitor implements OnlineStatusInheritanceVisitor {
        OnlineStatusVisitor realThis = this;

        @Override
        public void setRealThis(OnlineStatusVisitor realThis){
            this.realThis = realThis;
        }

        @Override
        public OnlineStatusVisitor getRealThis(){
            return realThis;
        }

        @Override
        public void visit(ASTOnlineStatus ast){
            onlineStatus = ast.getOnlineStatusType();
        }
    }
}
