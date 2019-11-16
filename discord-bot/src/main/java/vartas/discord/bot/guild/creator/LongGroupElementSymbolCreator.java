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
import vartas.discord.bot.guild._ast.ASTLongGroupElement;
import vartas.discord.bot.guild._ast.GuildNodeFactory;
import vartas.discord.bot.guild._symboltable.IGuildScope;
import vartas.discord.bot.guild._symboltable.LongGroupElementSymbol;

public abstract class LongGroupElementSymbolCreator {
    public static LongGroupElementSymbol create(IGuildScope enclosingScope, String type, String value){
        LongGroupElementSymbol result = new LongGroupElementSymbol(value);

        //Register result in the enclosing scope
        result.setEnclosingScope(enclosingScope);
        enclosingScope.add(result);

        //Set the AST of this symbol
        ASTLongGroupElement ast = createAst(type, value);
        result.setAstNode(ast);

        return result;
    }

    protected static ASTLongGroupElement createAst(String type, String value){
        ASTLongGroupElement ast = GuildNodeFactory.createASTLongGroupElement();

        ast.setType(type);
        ast.setElement(createElement(value));

        return ast;
    }

    protected static ASTBasicLongLiteral createElement(String value){
        ASTBasicLongLiteral ast = MCCommonLiteralsNodeFactory.createASTBasicLongLiteral();

        ast.setDigits(value);

        return ast;
    }
}
