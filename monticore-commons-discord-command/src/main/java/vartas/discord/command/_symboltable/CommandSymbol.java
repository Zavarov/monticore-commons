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

package vartas.discord.command._symboltable;

import net.dv8tion.jda.api.Permission;
import vartas.discord.bot.rank.RankType;
import vartas.discord.bot.rank._ast.ASTRankType;
import vartas.discord.command._ast.ASTClassNameAttribute;
import vartas.discord.command._ast.ASTParameterAttribute;
import vartas.discord.command._ast.ASTPermissionAttribute;
import vartas.discord.command._ast.ASTRankAttribute;
import vartas.discord.parameter._ast.ASTParameter;
import vartas.discord.permission._ast.ASTPermission;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class
CommandSymbol extends CommandSymbolTOP{
    public CommandSymbol(String name){
        super(name);
    }

    public String getClassName(){
        return getSpannedScope().getLocalClassNameAttributeSymbols()
                .stream()
                .map(ClassNameAttributeSymbol::getAstNode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ASTClassNameAttribute::getValue)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("This command doesn't have a class name specified."));
    }

    public List<RankType> getValidRanks(){
        return getSpannedScope().getLocalRankAttributeSymbols()
                .stream()
                .map(RankAttributeSymbol::getAstNode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ASTRankAttribute::getRankTypeList)
                .flatMap(Collection::stream)
                .map(ASTRankType::getRankType)
                .collect(Collectors.toList());
    }

    public List<Permission> getRequiredPermissions(){
        return getSpannedScope().getLocalPermissionAttributeSymbols()
                .stream()
                .map(PermissionAttributeSymbol::getAstNode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ASTPermissionAttribute::getPermissionList)
                .flatMap(Collection::stream)
                .map(ASTPermission::getPermissionType)
                .collect(Collectors.toList());
    }

    public List<ASTParameter> getParameters(){
        return getSpannedScope().getLocalParameterAttributeSymbols()
                .stream()
                .map(ParameterAttributeSymbol::getAstNode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ASTParameterAttribute::getParameterList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public boolean requiresGuild(){
        return !getSpannedScope().getLocalGuildRestrictionSymbols().isEmpty();
    }

    public boolean requiresAttachment(){
        return !getSpannedScope().getLocalAttachmentRestrictionSymbols().isEmpty();
    }
}
