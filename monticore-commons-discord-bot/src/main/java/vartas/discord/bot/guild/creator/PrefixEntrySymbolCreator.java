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
import vartas.discord.bot.guild._ast.ASTPrefixEntry;
import vartas.discord.bot.guild._ast.GuildNodeFactory;
import vartas.discord.bot.guild._symboltable.IGuildScope;
import vartas.discord.bot.guild._symboltable.PrefixEntrySymbol;

public abstract class PrefixEntrySymbolCreator {
    public static PrefixEntrySymbol create(IGuildScope enclosingScope, String value){
        PrefixEntrySymbol result = new PrefixEntrySymbol(value);

        //Register this symbol in the enclosing scope
        result.setEnclosingScope(enclosingScope);
        enclosingScope.add(result);

        //Set the AST of this symbol
        ASTPrefixEntry ast = createAst(value);
        result.setAstNode(ast);

        return result;
    }

    protected static ASTPrefixEntry createAst(String value){
        ASTPrefixEntry ast = GuildNodeFactory.createASTPrefixEntry();

        ast.setStringLiteral(createValue(value));

        return ast;
    }

    protected static ASTStringLiteral createValue(String value){
        ASTStringLiteral ast = MCCommonLiteralsNodeFactory.createASTStringLiteral();

        ast.setSource(value);

        return ast;
    }
}