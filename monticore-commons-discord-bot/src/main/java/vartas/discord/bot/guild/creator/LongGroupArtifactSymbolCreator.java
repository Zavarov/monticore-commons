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

package vartas.discord.bot.guild.creator;

import vartas.discord.bot.guild._symboltable.GuildScope;
import vartas.discord.bot.guild._symboltable.IGuildScope;
import vartas.discord.bot.guild._symboltable.LongGroupArtifactSymbol;
import vartas.discord.bot.guild._symboltable.LongGroupValueSymbol;

public abstract class LongGroupArtifactSymbolCreator {
    public static LongGroupArtifactSymbol create(IGuildScope enclosingScope, String group, String type, String value){
        LongGroupArtifactSymbol result = new LongGroupArtifactSymbol(group);

        //Set the scopes for the symbol
        IGuildScope spannedScope = new GuildScope();
        result.setSpannedScope(spannedScope);
        result.setEnclosingScope(enclosingScope);

        //Register result in the enclosing scope
        enclosingScope.add(result);
        enclosingScope.addSubScope(spannedScope);

        //Create sub symbols
        LongGroupValueSymbol symbol = LongGroupValueSymbolCreator.create(spannedScope, type, value);
        spannedScope.add(symbol);

        return result;
    }
}
