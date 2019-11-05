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

package vartas.discord.command.cocos;

import de.se_rwth.commons.logging.Log;
import vartas.discord.command._ast.ASTCommand;
import vartas.discord.command._cocos.CommandASTCommandCoCo;
import vartas.discord.command._visitor.CommandVisitor;
import vartas.discord.parameter._ast.ASTCardinality;
import vartas.discord.parameter._ast.ASTParameterVariable;

import java.util.ArrayList;
import java.util.List;

public class ManyOnlyAtLastParameterCoCo implements CommandASTCommandCoCo, CommandVisitor {
    public static final String ERROR_MESSAGE = "Only the last parameter can have the '+' cardinality.";
    protected List<Boolean> list;
    @Override
    public void check(ASTCommand node) {
        list = new ArrayList<>();

        node.accept(getRealThis());

        for(int i = 0 ; i < list.size() - 1 ; ++i){
            if (list.get(i)) {
                Log.error(ERROR_MESSAGE);
                break;
            }
        }
    }

    @Override
    public void visit(ASTParameterVariable node){
        if(node.isPresentCardinality() && node.getCardinality() == ASTCardinality.MANY)
            list.add(true);
        else
            list.add(false);
    }
}
