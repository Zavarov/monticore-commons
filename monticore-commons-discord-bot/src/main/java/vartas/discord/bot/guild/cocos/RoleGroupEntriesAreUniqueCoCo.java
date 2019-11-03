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

import de.monticore.literals.mccommonliterals._ast.ASTBasicLongLiteral;
import de.se_rwth.commons.logging.Log;
import vartas.discord.bot.guild._ast.ASTGuildArtifact;
import vartas.discord.bot.guild._ast.ASTIdentifier;
import vartas.discord.bot.guild._ast.ASTLongGroupEntry;
import vartas.discord.bot.guild._ast.ASTLongGroupValue;
import vartas.discord.bot.guild._cocos.GuildASTGuildArtifactCoCo;
import vartas.discord.bot.guild._visitor.GuildVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoleGroupEntriesAreUniqueCoCo implements GuildASTGuildArtifactCoCo, GuildVisitor {
    public static final String ERROR_MESSAGE = "The role with id '%s' appears more than one group.";
    List<ASTBasicLongLiteral> list;

    @Override
    public void check(ASTGuildArtifact node) {
        list = new ArrayList<>();

        node.accept(getRealThis());

        Map<String, Long> frequency = list
                .stream()
                .map(ASTBasicLongLiteral::getDigits)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for(Map.Entry<String, Long> entry : frequency.entrySet())
            if(entry.getValue() > 1)
                Log.error(String.format(ERROR_MESSAGE, entry.getKey()));
    }

    @Override
    public void handle(ASTLongGroupEntry node){
        if(node.getIdentifier() == ASTIdentifier.ROLEGROUP)
            node.getLongGroupArtifact().accept(getRealThis());
    }

    @Override
    public void visit(ASTLongGroupValue node){
        list.add(node.getValue());
    }
}
