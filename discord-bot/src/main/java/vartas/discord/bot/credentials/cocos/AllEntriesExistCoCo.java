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

import com.google.common.collect.Sets;
import de.se_rwth.commons.logging.Log;
import vartas.discord.bot.credentials._ast.ASTCredentials;
import vartas.discord.bot.credentials._ast.ASTCredentialsArtifact;
import vartas.discord.bot.credentials._cocos.CredentialsASTCredentialsArtifactCoCo;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AllEntriesExistCoCo implements CredentialsASTCredentialsArtifactCoCo {
    public static final String ERROR_MESSAGE = "The configuration '%s' is missing.";
    private static Set<String> expected ;

    static{
        expected = Arrays.stream(ASTCredentials.values())
                .map(ASTCredentials::name)
                .collect(Collectors.toSet());
    }

    @Override
    public void check(ASTCredentialsArtifact node) {
        Set<String> present = node
                .getCredentialsNameList()
                .stream()
                .map(key -> key.getCredentials().name())
                .collect(Collectors.toSet());

        if(!present.equals(expected))
            for(String unexpected : Sets.symmetricDifference(present, expected))
                Log.error(String.format(ERROR_MESSAGE, unexpected));
    }
}