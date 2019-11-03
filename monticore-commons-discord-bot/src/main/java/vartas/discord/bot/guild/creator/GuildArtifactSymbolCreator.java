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

package vartas.discord.bot.guild.creator;

import de.monticore.literals.mccommonliterals._ast.ASTBasicLongLiteral;
import de.monticore.literals.mccommonliterals._ast.MCCommonLiteralsNodeFactory;
import net.dv8tion.jda.api.entities.Guild;
import net.sourceforge.plantuml.Log;
import vartas.discord.bot.guild.GuildHelper;
import vartas.discord.bot.guild._ast.ASTGuildArtifact;
import vartas.discord.bot.guild._ast.GuildNodeFactory;
import vartas.discord.bot.guild._symboltable.GuildArtifactSymbol;
import vartas.discord.bot.guild._symboltable.GuildScope;
import vartas.discord.bot.guild._symboltable.IGuildScope;

import java.io.IOException;
import java.nio.file.Path;

public abstract class GuildArtifactSymbolCreator{
    public static GuildArtifactSymbol create(IGuildScope enclosingScope, Guild guild, Path reference){
        GuildArtifactSymbol result = new GuildArtifactSymbol(guild.getId());

        //Set the scopes for the symbol
        IGuildScope spannedScope = new GuildScope();
        result.setSpannedScope(spannedScope);
        result.setEnclosingScope(enclosingScope);

        //Register result in the enclosing scope
        enclosingScope.add(result);
        enclosingScope.addSubScope(spannedScope);

        //Create the symbol file
        result.setReference(reference);

        //Set the AST of this symbol
        ASTGuildArtifact ast = createAst(guild);
        result.setAstNode(ast);

        try {
            GuildHelper.store(result);
        } catch(IOException e) {
            Log.error(e.getMessage());
            throw new IllegalStateException(e);
        }

        return result;
    }

    protected static ASTGuildArtifact createAst(Guild guild){
        ASTGuildArtifact ast = GuildNodeFactory.createASTGuildArtifact();

        ast.setId(createValue(guild));

        return ast;
    }

    protected static ASTBasicLongLiteral createValue(Guild guild){
        ASTBasicLongLiteral ast = MCCommonLiteralsNodeFactory.createASTBasicLongLiteral();

        ast.setDigits(guild.getId());

        return ast;
    }
}