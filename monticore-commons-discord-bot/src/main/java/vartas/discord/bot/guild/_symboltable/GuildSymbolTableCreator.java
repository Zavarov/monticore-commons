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

package vartas.discord.bot.guild._symboltable;

import vartas.discord.bot.guild._ast.ASTGuildArtifact;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class GuildSymbolTableCreator extends GuildSymbolTableCreatorTOP{

    public GuildSymbolTableCreator(IGuildScope enclosingScope) {
        super(enclosingScope);
    }

    public GuildSymbolTableCreator(final Deque<? extends IGuildScope> scopeStack) {
        super(scopeStack);
    }

    @Override
    public GuildArtifactScope createFromAST(ASTGuildArtifact rootNode) {
        requireNonNull(rootNode);

        final GuildArtifactScope artifactScope = new GuildArtifactScope(Optional.empty(), "", new ArrayList<>());
        putOnStack(artifactScope);

        rootNode.accept(this);

        return artifactScope;
    }

    @Override
    public void visit(ASTGuildArtifact node){
        super.visit(node);
        getCurrentScope().ifPresent(node::setEnclosingScope);
    }
}
