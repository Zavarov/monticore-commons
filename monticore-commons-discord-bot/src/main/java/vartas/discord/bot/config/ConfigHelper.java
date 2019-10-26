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

package vartas.discord.bot.config;

import vartas.discord.bot.config._ast.ASTConfigArtifact;
import vartas.discord.bot.config._parser.ConfigParser;
import vartas.discord.bot.config._symboltable.ConfigArtifactScope;
import vartas.discord.bot.config._symboltable.ConfigGlobalScope;
import vartas.discord.bot.config._symboltable.ConfigSymbolTableCreatorDelegator;

import java.io.IOException;
import java.util.Optional;

public abstract class ConfigHelper {
    public static ASTConfigArtifact parse(ConfigGlobalScope scope, String filePath) throws IllegalArgumentException{
        ASTConfigArtifact ast = parseArtifact(filePath);
        buildSymbolTable(scope, ast);
        return ast;
    }

    private static ASTConfigArtifact parseArtifact(String filePath){
        try{
            ConfigParser parser = new ConfigParser();
            Optional<ASTConfigArtifact> Configs = parser.parse(filePath);
            if(parser.hasErrors())
                throw new IllegalArgumentException("The parser encountered errors while parsing "+filePath);
            if(!Configs.isPresent())
                throw new IllegalArgumentException("The guild configuration file couldn't be parsed");

            return Configs.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static void buildSymbolTable(ConfigGlobalScope scope, ASTConfigArtifact ast){
        ConfigSymbolTableCreatorDelegator symbolTableCreator = createSymbolTableCreator(scope);
        ConfigArtifactScope artifactScope = symbolTableCreator.createFromAST(ast);
        scope.addSubScope(artifactScope);
    }

    private static ConfigSymbolTableCreatorDelegator createSymbolTableCreator(ConfigGlobalScope scope){
        return scope.getConfigLanguage().getSymbolTableCreator(scope);
    }
}
