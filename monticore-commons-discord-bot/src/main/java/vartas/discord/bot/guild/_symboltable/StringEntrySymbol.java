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

import java.util.Optional;
import java.util.regex.Pattern;

public class StringEntrySymbol extends StringEntrySymbolTOP{
    /**
     * Computing the pattern is an expensive task, so we keep the latest
     * compilation in memory instead.
     */
    protected Pattern pattern = Pattern.compile("");

    public StringEntrySymbol(String name) {
        super(name);
    }

    /**
     * @return the value of this symbol.
     */
    public Optional<String> resolveAsString(){
        return getSpannedScope().getLocalStringValueSymbols().stream().findAny().map(StringValueSymbol::getName);
    }

    /**
     * @return the latest pattern that has been compiled
     */
    public Pattern resolveAsPattern(){
        return pattern;
    }

    public void recompilePattern(){
        Optional<String> valueOpt = resolveAsString();
        if(valueOpt.isPresent()){
            String value = valueOpt.get();
            pattern = Pattern.compile(value);
        }
    }
}
