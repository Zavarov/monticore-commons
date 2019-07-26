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

package vartas.discord.entity.prettyprinter;

import de.monticore.prettyprint.IndentPrinter;
import vartas.discord.entity._ast.ASTEntityNode;
import vartas.discord.entity._ast.ASTRole;
import vartas.discord.entity._ast.ASTTextChannel;
import vartas.discord.entity._ast.ASTUser;
import vartas.discord.entity._visitor.EntityVisitor;

public class EntityPrettyPrinter implements EntityVisitor{
    private IndentPrinter printer;
    private EntityVisitor realThis;

    public EntityPrettyPrinter(IndentPrinter printer){
        this.realThis = this;
        this.printer = printer;
    }

    @Override
    public EntityVisitor getRealThis(){
        return realThis;
    }

    @Override
    public void setRealThis(EntityVisitor realThis){
        this.realThis = realThis;
    }

    public String prettyprint(ASTEntityNode node){
        printer.clearBuffer();
        node.accept(getRealThis());
        return printer.getContent();
    }

    @Override
    public void handle(ASTUser node){
        printer.print("<@");
        if(node.isSemicolon())
            printer.print("!");
        printer.print(node.getId());
        printer.print(">");
    }

    @Override
    public void handle(ASTTextChannel node){
        printer.print("<#");
        printer.print(node.getId());
        printer.print(">");
    }

    @Override
    public void handle(ASTRole node){
        printer.print("<@&");
        printer.print(node.getId());
        printer.print(">");
    }
}