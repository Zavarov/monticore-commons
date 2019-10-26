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

package vartas.discord.bot.rank;

import vartas.discord.bot.rank._ast.ASTRankArtifact;
import vartas.discord.bot.rank._parser.RankParser;
import vartas.discord.bot.rank._symboltable.RankArtifactScope;
import vartas.discord.bot.rank._symboltable.RankGlobalScope;
import vartas.discord.bot.rank._symboltable.RankSymbolTableCreatorDelegator;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public abstract class RankHelper {
    public static RankConfiguration parse(RankGlobalScope scope, String filePath, File reference) throws IllegalArgumentException{
        ASTRankArtifact ast = parseArtifact(filePath);
        buildSymbolTable(scope, ast);
        return new RankConfiguration(ast, reference);
    }

    private static ASTRankArtifact parseArtifact(String filePath){
        try{
            RankParser parser = new RankParser();
            Optional<ASTRankArtifact> Ranks = parser.parse(filePath);
            if(parser.hasErrors())
                throw new IllegalArgumentException("The parser encountered errors while parsing "+filePath);
            if(!Ranks.isPresent())
                throw new IllegalArgumentException("The guild configuration file couldn't be parsed");

            return Ranks.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static void buildSymbolTable(RankGlobalScope scope, ASTRankArtifact ast){
        RankSymbolTableCreatorDelegator symbolTableCreator = createSymbolTableCreator(scope);
        RankArtifactScope artifactScope = symbolTableCreator.createFromAST(ast);
        scope.addSubScope(artifactScope);
    }

    private static RankSymbolTableCreatorDelegator createSymbolTableCreator(RankGlobalScope scope){
        return scope.getRankLanguage().getSymbolTableCreator(scope);
    }
}
