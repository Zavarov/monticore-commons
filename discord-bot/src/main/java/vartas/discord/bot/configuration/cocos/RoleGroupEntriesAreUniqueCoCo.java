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

package vartas.discord.bot.configuration.cocos;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import de.se_rwth.commons.logging.Log;
import vartas.discord.bot.configuration._ast.ASTGuildArtifact;
import vartas.discord.bot.configuration._ast.ASTLongGroupElement;
import vartas.discord.bot.configuration._ast.ASTRoleGroupEntry;
import vartas.discord.bot.configuration._cocos.GuildASTGuildArtifactCoCo;
import vartas.discord.bot.configuration._visitor.GuildVisitor;

public class RoleGroupEntriesAreUniqueCoCo implements GuildASTGuildArtifactCoCo, GuildVisitor {
    public static final String ERROR_MESSAGE = "The role with id '%s' appears in multiple groups.";
    Multiset<String> roles;

    @Override
    public void check(ASTGuildArtifact node) {
        roles = HashMultiset.create();

        node.accept(getRealThis());

        for(Multiset.Entry<String> entry : roles.entrySet())
            if(entry.getCount() > 1)
                Log.error(String.format(ERROR_MESSAGE, entry.getElement()));
    }

    @Override
    public void handle(ASTRoleGroupEntry node){
        for(ASTLongGroupElement element : node.getLongGroup().getLongGroupElementList())
            roles.add(element.getName());
    }
}
