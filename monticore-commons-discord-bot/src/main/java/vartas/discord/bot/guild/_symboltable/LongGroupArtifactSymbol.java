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
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LongGroupArtifactSymbol extends LongGroupArtifactSymbolTOP{
    public LongGroupArtifactSymbol(String name) {
        super(name);
    }

    private TextChannel resolveAsTextChannel(LongGroupValueSymbol symbol, Guild reference){
        return reference.getTextChannelById(symbol.getName());
    }

    private Role resolveAsRole(LongGroupValueSymbol symbol, Guild reference){
        return reference.getRoleById(symbol.getName());
    }

    private Stream<LongGroupValueSymbol> resolveAsValueStream(){
        return getSpannedScope().getLocalLongGroupValueSymbols().stream();
    }

    /**
     * @param reference a guild reference the text channels are supposedly in.
     * @return a collection of all text channels matching the internal ids.
     */
    public Collection<TextChannel> resolveAsTextChannels(Guild reference){
        return resolveAsValueStream()
                .map(symbol -> resolveAsTextChannel(symbol, reference))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * @param reference a guild reference the roles are supposedly in.
     * @return a collection of all roles matching the internal ids.
     */
    public Collection<Role> resolveAsRoles(Guild reference){
        return resolveAsValueStream()
                .map(symbol -> resolveAsRole(symbol, reference))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Checks if all ids in the group can be resolved as roles and removes those,
     * for which this is not the case.
     * @param guild the guild the text channels are in.
     * @return a list of all symbols that couldn't be resolved.
     */
    public List<LongGroupValueSymbol> validateRoles(Guild guild){
        return resolveAsValueStream()
                .filter(value -> resolveAsRole(value, guild) == null)
                .collect(Collectors.toList());
    }

    /**
     * Checks if all ids in the group can be resolved as text channels and removes those,
     * for which this is not the case.
     * @param guild the guild the text channels are in.
     * @return a list of all symbols that couldn't be resolved.
     */
    public List<LongGroupValueSymbol> validateTextChannels(Guild guild){
        return resolveAsValueStream()
                .filter(value -> resolveAsTextChannel(value, guild) == null)
                .collect(Collectors.toList());
    }
}
