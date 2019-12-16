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

package vartas.discord.bot.status.visitor;

import vartas.MonticoreEscapeUtils;
import vartas.discord.bot.entities.Status;
import vartas.discord.bot.status._ast.ASTStatus;
import vartas.discord.bot.status._ast.ASTStatusArtifact;

public class StatusVisitor implements vartas.discord.bot.status._visitor.StatusVisitor {
    protected Status status;
    public Status accept(ASTStatusArtifact artifact, Status status){
        this.status = status;
        artifact.accept(getRealThis());
        return status;
    }
    @Override
    public void handle(ASTStatus node){
        status.add(MonticoreEscapeUtils.unescapeMonticore(node.getStringLiteral().getValue()));
    }
}
