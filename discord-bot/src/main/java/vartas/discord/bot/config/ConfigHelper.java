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
import vartas.discord.bot.config._symboltable.ConfigSymbolTableCreator;
import vartas.discord.bot.config.cocos.ConfigCoCos;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

public abstract class ConfigHelper {
    public static ASTConfigArtifact parse(String filePath) throws IllegalArgumentException{
        ASTConfigArtifact ast = parseArtifact(filePath);
        buildSymbolTable(ast);
        checkCoCos(ast);
        return ast;
    }

    private static void checkCoCos(ASTConfigArtifact ast){
        ConfigCoCos.getCheckerForAllCoCos().checkAll(ast);
    }


    private static ASTConfigArtifact parseArtifact(String filePath){
        try{
            ConfigParser parser = new ConfigParser();
            Optional<ASTConfigArtifact> config = parser.parse(filePath);
            if(!config.isPresent())
                throw new IllegalArgumentException("The guild configuration file couldn't be parsed");

            return config.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static ConfigArtifactScope buildSymbolTable(ASTConfigArtifact ast){
        ConfigSymbolTableCreator symbolTableCreator = new ConfigSymbolTableCreator(new LinkedList<>());

        return symbolTableCreator.createFromAST(ast);
    }
}
