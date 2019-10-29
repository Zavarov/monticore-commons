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
import vartas.discord.bot.guild._symboltable.GuildArtifactScope;
import vartas.discord.bot.guild._symboltable.GuildGlobalScope;
import vartas.discord.bot.guild._symboltable.GuildLanguage;
import vartas.discord.bot.guild._symboltable.GuildSymbolTableCreatorDelegator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public abstract class GuildHelper {
    private static GuildGlobalScope createGlobalScope(){
        ModelPath modelPath = new ModelPath(Paths.get(""));
        GuildLanguage language = new GuildLanguage();
        return new GuildGlobalScope(modelPath, language);
    }

    public static GuildConfiguration parse(String filePath, File reference) throws IllegalArgumentException{
        GuildGlobalScope scope = createGlobalScope();
        return parse(scope, filePath, reference);
    }

    public static GuildConfiguration parse(GuildGlobalScope scope, String filePath, File reference) throws IllegalArgumentException{
        ASTGuildArtifact ast = parseArtifact(filePath);
        buildSymbolTable(scope, ast);
        return new GuildConfiguration(ast, reference);
    }

    private static ASTGuildArtifact parseArtifact(String filePath){
        try{
            GuildParser parser = new GuildParser();
            Optional<ASTGuildArtifact> Guilds = parser.parse(filePath);
            if(parser.hasErrors())
                throw new IllegalArgumentException("The parser encountered errors while parsing "+filePath);
            if(!Guilds.isPresent())
                throw new IllegalArgumentException("The guild configuration file couldn't be parsed");

            return Guilds.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static void buildSymbolTable(GuildGlobalScope scope, ASTGuildArtifact ast){
        GuildSymbolTableCreatorDelegator symbolTableCreator = createSymbolTableCreator(scope);
        GuildArtifactScope artifactScope = symbolTableCreator.createFromAST(ast);
        scope.addSubScope(artifactScope);
    }

    private static GuildSymbolTableCreatorDelegator createSymbolTableCreator(GuildGlobalScope scope){
        return scope.getGuildLanguage().getSymbolTableCreator(scope);
    }
}
