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

package vartas.discord.bot.configuration;

import vartas.discord.bot.configuration._ast.ASTConfigurationArtifact;
import vartas.discord.bot.configuration._parser.ConfigurationParser;
import vartas.discord.bot.configuration.cocos.ConfigurationCoCos;

import java.io.IOException;
import java.util.Optional;

public abstract class ConfigurationHelper {
    public static ASTConfigurationArtifact parse(String filePath) throws IllegalArgumentException{
        ASTConfigurationArtifact ast = parseArtifact(filePath);
        checkCoCos(ast);
        return ast;
    }

    private static ASTConfigurationArtifact parseArtifact(String filePath){
        try{
            ConfigurationParser parser = new ConfigurationParser();
            Optional<ASTConfigurationArtifact> Configuration = parser.parse(filePath);
            if(Configuration.isEmpty())
                throw new IllegalArgumentException("The Configuration configuration file couldn't be parsed");

            return Configuration.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static void checkCoCos(ASTConfigurationArtifact ast){
        ConfigurationCoCos.getCheckerForAllCoCos().checkAll(ast);
    }
}
