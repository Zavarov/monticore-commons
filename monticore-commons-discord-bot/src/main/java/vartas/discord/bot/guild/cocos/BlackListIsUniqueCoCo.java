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

import de.se_rwth.commons.logging.Log;
import vartas.discord.bot.guild._ast.ASTBlacklistEntry;
import vartas.discord.bot.guild._ast.ASTGuildArtifact;
import vartas.discord.bot.guild._cocos.GuildASTGuildArtifactCoCo;
import vartas.discord.bot.guild._visitor.GuildVisitor;

public class BlackListIsUniqueCoCo implements GuildASTGuildArtifactCoCo, GuildVisitor {
    public static final String ERROR_MESSAGE = "The blacklist identifier appears more than once.";
    protected int counter;

    @Override
    public void check(ASTGuildArtifact node) {
        counter = 0;

        node.accept(this);

        if(counter > 1)
            Log.error(ERROR_MESSAGE);
    }

    @Override
    public void visit(ASTBlacklistEntry node){
        ++counter;
    }
}
