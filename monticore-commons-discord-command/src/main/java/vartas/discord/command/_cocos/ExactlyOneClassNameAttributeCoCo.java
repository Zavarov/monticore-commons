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

package vartas.discord.command._cocos;

import de.se_rwth.commons.logging.Log;
import vartas.discord.command._ast.ASTClassNameAttribute;
import vartas.discord.command._ast.ASTCommand;
import vartas.discord.command._visitor.CommandVisitor;

public class ExactlyOneClassNameAttributeCoCo implements CommandASTCommandCoCo, CommandVisitor {
    private int counter;
    public static final String ERROR_MESSAGE = "%s: The command must have exactly one class name attribute.";
    @Override
    public void check(ASTCommand node) {
        counter = 0;
        node.accept(getRealThis());

        if(counter != 1)
            Log.error(String.format(ERROR_MESSAGE, node.getCommandSymbol().getClassName()));
    }

    @Override
    public void visit(ASTClassNameAttribute node){
        counter++;
    }
}