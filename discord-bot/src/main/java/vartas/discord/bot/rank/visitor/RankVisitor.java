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

package vartas.discord.bot.rank.visitor;

import vartas.discord.bot.entities.Rank;
import vartas.discord.bot.rank._ast.ASTRankArtifact;
import vartas.discord.bot.rank._ast.ASTRankName;
import vartas.discord.bot.rank._ast.ASTUserWithRank;

public class RankVisitor implements vartas.discord.bot.rank._visitor.RankVisitor {
    protected Rank rank;
    public void accept(ASTRankArtifact artifact, Rank rank){
        this.rank = rank;
        artifact.accept(getRealThis());
    }

    public void handle(ASTUserWithRank node){
        long user = Long.parseUnsignedLong(node.getName());
        for(ASTRankName rank : node.getRankNameList()){
            this.rank.add(user, rank.getRank());
        }
    }
}
