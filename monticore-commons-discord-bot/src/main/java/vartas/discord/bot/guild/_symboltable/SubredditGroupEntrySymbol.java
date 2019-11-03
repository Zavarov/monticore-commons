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

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubredditGroupEntrySymbol extends SubredditGroupEntrySymbolTOP{
    public SubredditGroupEntrySymbol(String name) {
        super(name);
    }

    private Stream<LongGroupElementSymbol> resolveAsValueStream(){
        return getSpannedScope().getLocalLongGroupElementSymbols().stream();
    }

    /**
     * @param reference a guild reference the text channels are supposedly in.
     * @return a collection of all text channels matching the internal ids.
     */
    public Collection<TextChannel> resolve(Guild reference){
        return resolveAsValueStream()
                .map(symbol -> reference.getTextChannelById(symbol.getName()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Checks if all ids in the group can be resolved as text channels and removes those,
     * for which this is not the case.
     * @param guild the guild the text channels are in.
     * @return a list of all symbols that couldn't be resolved.
     */
    public List<LongGroupElementSymbol> validate(Guild guild){
        return resolveAsValueStream()
                .filter(value -> guild.getTextChannelById(value.getName()) == null)
                .collect(Collectors.toList());
    }
}
