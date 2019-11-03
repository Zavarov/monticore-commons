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

package vartas.discord.bot.guild.cocos;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import de.se_rwth.commons.logging.Log;
import vartas.discord.bot.guild._ast.ASTGuildArtifact;
import vartas.discord.bot.guild._ast.ASTIdentifier;
import vartas.discord.bot.guild._ast.ASTStringEntry;
import vartas.discord.bot.guild._cocos.GuildASTGuildArtifactCoCo;
import vartas.discord.bot.guild._visitor.GuildVisitor;

public class PrefixIsUniqueCoCo implements GuildASTGuildArtifactCoCo, GuildVisitor {
    public static final String ERROR_MESSAGE = "The prefix identifier appears more than once.";
    Multimap<ASTIdentifier, ASTStringEntry> map;

    @Override
    public void check(ASTGuildArtifact node) {
        map = LinkedListMultimap.create();

        node.accept(this);

        if(map.get(ASTIdentifier.PREFIX).size() > 1)
            Log.error(ERROR_MESSAGE);
    }

    @Override
    public void visit(ASTStringEntry node){
        map.put(node.getIdentifier(), node);
    }
}
