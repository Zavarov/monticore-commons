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

import de.monticore.prettyprint.IndentPrinter;
import vartas.discord.bot.guild._ast.ASTGuildArtifact;
import vartas.discord.bot.guild._parser.GuildParser;
import vartas.discord.bot.guild._symboltable.*;
import vartas.discord.bot.guild.cocos.GuildCoCos;
import vartas.discord.bot.guild.prettyprint.GuildSymbolPrettyPrinter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

public abstract class GuildHelper {
    public static void store(GuildArtifactSymbol symbol) throws IOException {
        if(!symbol.getReference().getParent().toFile().exists())
            Files.createDirectories(symbol.getReference().getParent());
        if(!symbol.getReference().toFile().exists())
            Files.createFile(symbol.getReference());
        String content = new GuildSymbolPrettyPrinter(new IndentPrinter()).prettyPrint(symbol);
        Files.write(symbol.getReference(), Collections.singletonList(content));
    }

    public static ASTGuildArtifact parse(GuildGlobalScope scope, String filePath, Path reference) throws IllegalArgumentException{
        ASTGuildArtifact ast = parseArtifact(filePath);

        buildSymbolTable(scope, ast);
        setReference(ast, reference);
        checkCoCos(ast);
        return ast;
    }

    private static ASTGuildArtifact parseArtifact(String filePath){
        try{
            GuildParser parser = new GuildParser();
            Optional<ASTGuildArtifact> guild = parser.parse(filePath);
            if(!guild.isPresent())
                throw new IllegalArgumentException("The guild configuration file couldn't be parsed");

            return guild.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static void checkCoCos(ASTGuildArtifact ast){
        GuildCoCos.getCheckerForAllCoCos().checkAll(ast);
    }

    private static void setReference(ASTGuildArtifact ast, Path reference){
        ast.getSymbol().setReference(reference);
    }

    private static GuildArtifactScope buildSymbolTable(GuildScope scope, ASTGuildArtifact ast){
        GuildSymbolTableCreator symbolTableCreator = new GuildSymbolTableCreator(scope);
        return symbolTableCreator.createFromAST(ast);
    }
}
