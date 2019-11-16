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

package vartas.discord.command;

import de.se_rwth.commons.Joiners;
import vartas.discord.command._ast.ASTCommandArtifact;
import vartas.discord.command._parser.CommandParser;
import vartas.discord.command._symboltable.CommandArtifactScope;
import vartas.discord.command._symboltable.CommandGlobalScope;
import vartas.discord.command._symboltable.CommandSymbolTableCreatorDelegator;
import vartas.discord.command.cocos.CommandCoCos;

import java.io.IOException;
import java.util.Optional;

public abstract class CommandHelper {
    public static ASTCommandArtifact parse(CommandGlobalScope scope, String filePath) throws IllegalArgumentException{
        ASTCommandArtifact ast = parseArtifact(filePath);
        buildSymbolTable(scope, ast);
        checkCoCos(ast);
        return ast;
    }

    private static void checkCoCos(ASTCommandArtifact ast){
        CommandCoCos.getCheckerForAllCoCos().checkAll(ast);
    }

    private static ASTCommandArtifact parseArtifact(String filePath){
        try{
            CommandParser parser = new CommandParser();
            Optional<ASTCommandArtifact> commands = parser.parse(filePath);
            if(!commands.isPresent())
                throw new IllegalArgumentException("The command file couldn't be parsed");

            return commands.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static void buildSymbolTable(CommandGlobalScope scope, ASTCommandArtifact ast){
        CommandSymbolTableCreatorDelegator symbolTableCreator = createSymbolTableCreator(scope);
        CommandArtifactScope artifactScope = symbolTableCreator.createFromAST(ast);
        scope.addSubScope(artifactScope);

        artifactScope.setPackageName(Joiners.DOT.join(ast.getPrefixList()));
    }

    private static CommandSymbolTableCreatorDelegator createSymbolTableCreator(CommandGlobalScope scope){
        return scope.getCommandLanguage().getSymbolTableCreator(scope);
    }
}