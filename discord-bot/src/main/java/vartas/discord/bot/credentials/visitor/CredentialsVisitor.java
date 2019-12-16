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

package vartas.discord.bot.credentials.visitor;

import vartas.MonticoreEscapeUtils;
import vartas.discord.bot.credentials._ast.ASTCredentialsArtifact;
import vartas.discord.bot.credentials._ast.ASTIntegerCredentials;
import vartas.discord.bot.credentials._ast.ASTStringCredentials;
import vartas.discord.bot.entities.Credentials;

public class CredentialsVisitor implements vartas.discord.bot.credentials._visitor.CredentialsVisitor {
    protected Credentials credentials;

    public void accept(ASTCredentialsArtifact artifact, Credentials credentials){
        this.credentials = credentials;
        artifact.accept(getRealThis());
    }
    @Override
    public void handle(ASTIntegerCredentials node){
        credentials.setType(node.getType(), node.getNatLiteral().getValue());
    }
    @Override
    public void handle(ASTStringCredentials node){
        credentials.setType(node.getType(), MonticoreEscapeUtils.unescapeMonticore(node.getStringLiteral().getValue()));
    }
}
