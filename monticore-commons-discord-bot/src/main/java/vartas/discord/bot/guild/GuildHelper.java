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

package vartas.discord.bot.guild;

import de.monticore.io.paths.ModelPath;
import vartas.discord.bot.guild._ast.ASTGuildArtifact;
import vartas.discord.bot.guild._parser.GuildParser;
import vartas.discord.bot.guild._symboltable.GuildGlobalScope;
import vartas.discord.bot.guild._symboltable.GuildLanguage;
import vartas.discord.bot.guild._symboltable.GuildSymbolTableCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public abstract class GuildHelper {
    public static GuildConfiguration parse(String filePath, File reference){
        ASTGuildArtifact ast = parse(filePath);
        return new GuildConfiguration(ast, reference);
    }

    private static ASTGuildArtifact parse(String filePath){
        try{
            GuildSymbolTableCreator symbolTableCreator = createSymbolTableCreator();

            GuildParser parser = new GuildParser();
            Optional<ASTGuildArtifact> config = parser.parse(filePath);
            if(parser.hasErrors())
                throw new IllegalArgumentException("The parser encountered errors while parsing "+filePath);
            if(!config.isPresent())
                throw new IllegalArgumentException("The guild configuration file couldn't be parsed");

            ASTGuildArtifact ast = config.get();
            symbolTableCreator.createFromAST(ast);

            return ast;
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static GuildGlobalScope createGlobalScope(){
        ModelPath path = new ModelPath(Paths.get(""));
        GuildLanguage language = new GuildLanguage();
        return new GuildGlobalScope(path, language);
    }

    private static GuildSymbolTableCreator createSymbolTableCreator(){
        GuildGlobalScope globalScope = createGlobalScope();

        return new GuildSymbolTableCreator(globalScope);
    }
}
