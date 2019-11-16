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

import de.monticore.prettyprint.IndentPrinter;
import vartas.discord.bot.rank._ast.ASTRankArtifact;
import vartas.discord.bot.rank._parser.RankParser;
import vartas.discord.bot.rank._symboltable.RankArtifactScope;
import vartas.discord.bot.rank._symboltable.RankSymbolTableCreator;
import vartas.discord.bot.rank.cocos.RankCoCos;
import vartas.discord.bot.rank.prettyprint.RankScopePrettyPrinter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

public abstract class RankHelper {
    public static void store(ASTRankArtifact ast) throws IOException {
        Path reference = ast.getReference();
        if(!reference.toFile().exists())
            Files.createDirectories(reference.getParent().toFile().toPath());
        if(!reference.toFile().exists())
            Files.createFile(reference);
        String content = new RankScopePrettyPrinter(new IndentPrinter()).prettyPrint(ast.getEnclosingScope());
        Files.write(reference, Collections.singletonList(content));
    }

    public static ASTRankArtifact parse(String filePath, Path reference) throws IllegalArgumentException{
        ASTRankArtifact ast = parseArtifact(filePath);
        buildSymbolTable(ast);
        setReference(ast, reference);
        checkCoCos(ast);
        return ast;
    }

    private static void checkCoCos(ASTRankArtifact ast){
        RankCoCos.getCheckerForAllCoCos().checkAll(ast);
    }

    private static void setReference(ASTRankArtifact ast, Path reference){
        ast.setReference(reference);
    }

    private static ASTRankArtifact parseArtifact(String filePath){
        try{
            RankParser parser = new RankParser();
            Optional<ASTRankArtifact> Ranks = parser.parse(filePath);
            if(!Ranks.isPresent())
                throw new IllegalArgumentException("The guild configuration file couldn't be parsed");

            return Ranks.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static RankArtifactScope buildSymbolTable(ASTRankArtifact ast){
        RankSymbolTableCreator symbolTableCreator = new RankSymbolTableCreator(new LinkedList<>());
        return symbolTableCreator.createFromAST(ast);
    }
}
