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

import net.dv8tion.jda.api.OnlineStatus;
import vartas.discord.argument._ast.ASTOnlineStatusArgumentEntry;
import vartas.discord.argument._symboltable.ArgumentSymbol;
import vartas.discord.argument._visitor.ArgumentVisitor;
import vartas.discord.argument.visitor.ContextSensitiveArgumentVisitor;
import vartas.discord.onlinestatus._symboltable.OnlineStatusNameSymbol;

import java.util.Optional;

public class OnlineStatusArgumentSymbol extends ArgumentSymbol {
    protected ArgumentVisitor visitor;

    protected OnlineStatus onlineStatus;

    public OnlineStatusArgumentSymbol(String name) {
        super(name);

        visitor = new OnlineStatusArgumentVisitor();
    }

    public Optional<OnlineStatus> accept(){
        onlineStatus = null;
        getAstNode().accept(visitor);
        return Optional.ofNullable(onlineStatus);
    }

    private class OnlineStatusArgumentVisitor extends ContextSensitiveArgumentVisitor {
        @Override
        public void handle(ASTOnlineStatusArgumentEntry node){
            OnlineStatusNameSymbol symbol = new OnlineStatusNameSymbol(node.getOnlineStatusName().getName());
            symbol.setAstNode(node.getOnlineStatusName());
            onlineStatus = symbol.getOnlineStatus();
        }
    }
}
