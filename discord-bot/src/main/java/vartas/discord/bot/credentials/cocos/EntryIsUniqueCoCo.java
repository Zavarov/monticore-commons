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

package vartas.discord.bot.credentials.cocos;

import de.se_rwth.commons.logging.Log;
import vartas.discord.bot.credentials._ast.ASTCredentialsArtifact;
import vartas.discord.bot.credentials._cocos.CredentialsASTCredentialsArtifactCoCo;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntryIsUniqueCoCo implements CredentialsASTCredentialsArtifactCoCo {
    public static final String ERROR_MESSAGE = "The configuration '%s' appears more than once.";
    @Override
    public void check(ASTCredentialsArtifact node) {
        Map<String, Long> frequency = node
                .getCredentialsNameList()
                .stream()
                .map(key -> key.getCredentials().name())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for(Map.Entry<String, Long> entry : frequency.entrySet())
            if(entry.getValue() > 1)
                Log.error(String.format(ERROR_MESSAGE, entry.getKey()));
    }
}