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

import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import de.monticore.literals.mccommonliterals._ast.MCCommonLiteralsNodeFactory;
import vartas.discord.bot.guild._ast.ASTSubredditGroupEntry;
import vartas.discord.bot.guild._ast.GuildNodeFactory;
import vartas.discord.bot.guild._symboltable.GuildScope;
import vartas.discord.bot.guild._symboltable.IGuildScope;
import vartas.discord.bot.guild._symboltable.SubredditGroupEntrySymbol;

public abstract class SubredditGroupEntrySymbolCreator {
    public static SubredditGroupEntrySymbol create(IGuildScope enclosingScope, String group){
        SubredditGroupEntrySymbol result = new SubredditGroupEntrySymbol(group);

        //Set the scopes for the symbol
        IGuildScope spannedScope = new GuildScope();
        result.setSpannedScope(spannedScope);
        result.setEnclosingScope(enclosingScope);

        //Register result in the enclosing scope
        enclosingScope.add(result);
        enclosingScope.addSubScope(spannedScope);

        //Set the AST of this symbol
        ASTSubredditGroupEntry ast = createAst(group);
        result.setAstNode(ast);
        return result;
    }

    protected static ASTSubredditGroupEntry createAst(String group){
        ASTSubredditGroupEntry ast = GuildNodeFactory.createASTSubredditGroupEntry();

        ast.setStringLiteral(createGroup(group));

        return ast;
    }

    protected static ASTStringLiteral createGroup(String value){
        ASTStringLiteral ast = MCCommonLiteralsNodeFactory.createASTStringLiteral();

        ast.setSource(value);

        return ast;
    }
}
