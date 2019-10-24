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

package vartas.discord.bot.rank._ast;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import vartas.discord.bot.rank.RankType;

public class ASTRankArtifact extends ASTRankArtifactTOP{
    protected ASTRankArtifact(){
        super();
    }

    public Multimap<Long, RankType> getRanks(){
        Multimap<Long, RankType> permissions = HashMultimap.create();

        for(ASTRank rank : getRankList())
            for(ASTRankType type : rank.getRankTypeList())
                permissions.put(rank.getBasicLongLiteral().getValue(), type.getRankType());

        return permissions;
    }
}
