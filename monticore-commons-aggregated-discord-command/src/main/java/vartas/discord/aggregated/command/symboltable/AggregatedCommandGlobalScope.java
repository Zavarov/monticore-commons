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

package vartas.discord.aggregated.command.symboltable;

import de.monticore.io.paths.ModelPath;
import vartas.discord.aggregated.parameter.symboltable.*;
import vartas.discord.argument._ast.ASTArgumentType;
import vartas.discord.command._symboltable.CommandGlobalScope;
import vartas.discord.command._symboltable.CommandLanguage;

public class AggregatedCommandGlobalScope extends CommandGlobalScope {
    public AggregatedCommandGlobalScope(ModelPath modelPath, CommandLanguage commandLanguage) {
        super(modelPath, commandLanguage);
    }

    public DateParameter2ArgumentSymbol resolveDateParameter(String symbolName, ASTArgumentType argument) {
        return new DateParameter2ArgumentSymbol(symbolName, argument);
    }

    public ExpressionParameter2ArgumentSymbol resolveExpressionParameter(String symbolName, ASTArgumentType argument) {
        return new ExpressionParameter2ArgumentSymbol(symbolName, argument);
    }

    public GuildParameter2ArgumentSymbol resolveGuildParameter(String symbolName, ASTArgumentType argument) {
        return new GuildParameter2ArgumentSymbol(symbolName, argument);
    }

    public IntervalParameter2ArgumentSymbol resolveIntervalParameter(String symbolName, ASTArgumentType argument) {
        return new IntervalParameter2ArgumentSymbol(symbolName, argument);
    }

    public MemberParameter2ArgumentSymbol resolveMemberParameter(String symbolName, ASTArgumentType argument) {
        return new MemberParameter2ArgumentSymbol(symbolName, argument);
    }

    public OnlineStatusParameter2ArgumentSymbol resolveOnlineStatusParameter(String symbolName, ASTArgumentType argument) {
        return new OnlineStatusParameter2ArgumentSymbol(symbolName, argument);
    }

    public RoleParameter2ArgumentSymbol resolveRoleParameter(String symbolName, ASTArgumentType argument) {
        return new RoleParameter2ArgumentSymbol(symbolName, argument);
    }

    public StringParameter2ArgumentSymbol resolveStringParameter(String symbolName, ASTArgumentType argument) {
        return new StringParameter2ArgumentSymbol(symbolName, argument);
    }

    public TextChannelParameter2ArgumentSymbol resolveTextChannelParameter(String symbolName, ASTArgumentType argument) {
        return new TextChannelParameter2ArgumentSymbol(symbolName, argument);
    }

    public UserParameter2ArgumentSymbol resolveUserParameter(String symbolName, ASTArgumentType argument) {
        return new UserParameter2ArgumentSymbol(symbolName, argument);
    }

    public MessageParameter2ArgumentSymbol resolveMessageParameter(String symbolName, ASTArgumentType argument) {
        return new MessageParameter2ArgumentSymbol(symbolName, argument);
    }
}
