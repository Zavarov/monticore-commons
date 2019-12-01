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

package vartas.discord.bot.config.visitor;

import vartas.MonticoreEscapeUtils;
import vartas.discord.bot.config._ast.ASTConfigArtifact;
import vartas.discord.bot.config._ast.ASTIntegerConfig;
import vartas.discord.bot.config._ast.ASTStringConfig;
import vartas.discord.bot.config._visitor.ConfigVisitor;
import vartas.discord.bot.entities.BotConfig;

public class BotConfigVisitor implements ConfigVisitor {
    protected BotConfig config;

    public void accept(ASTConfigArtifact artifact, BotConfig config){
        this.config = config;
        artifact.accept(getRealThis());
    }
    @Override
    public void handle(ASTIntegerConfig node){
        config.setType(node.getType(), node.getNatLiteral().getValue());
    }
    @Override
    public void handle(ASTStringConfig node){
        config.setType(node.getType(), MonticoreEscapeUtils.unescapeMonticore(node.getStringLiteral().getValue()));
    }
}
