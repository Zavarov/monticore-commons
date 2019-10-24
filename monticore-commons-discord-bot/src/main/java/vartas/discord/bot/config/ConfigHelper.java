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

import de.monticore.io.paths.ModelPath;
import vartas.discord.bot.config._ast.ASTConfigArtifact;
import vartas.discord.bot.config._parser.ConfigParser;
import vartas.discord.bot.config._symboltable.ConfigGlobalScope;
import vartas.discord.bot.config._symboltable.ConfigLanguage;
import vartas.discord.bot.config._symboltable.ConfigSymbolTableCreator;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public abstract class ConfigHelper {
    public static ASTConfigArtifact parse(String filePath){
        try{
            ConfigSymbolTableCreator symbolTableCreator = createSymbolTableCreator();

            ConfigParser parser = new ConfigParser();
            Optional<ASTConfigArtifact> config = parser.parse(filePath);
            if(parser.hasErrors())
                throw new IllegalArgumentException("The parser encountered errors while parsing "+filePath);
            if(!config.isPresent())
                throw new IllegalArgumentException("The config file couldn't be parsed");

            ASTConfigArtifact ast = config.get();
            symbolTableCreator.createFromAST(ast);

            return ast;
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static ConfigGlobalScope createGlobalScope(){
        ModelPath path = new ModelPath(Paths.get(""));
        ConfigLanguage language = new ConfigLanguage();
        return new ConfigGlobalScope(path, language);
    }

    private static ConfigSymbolTableCreator createSymbolTableCreator(){
        ConfigGlobalScope globalScope = createGlobalScope();

        return new ConfigSymbolTableCreator(globalScope);
    }
}
