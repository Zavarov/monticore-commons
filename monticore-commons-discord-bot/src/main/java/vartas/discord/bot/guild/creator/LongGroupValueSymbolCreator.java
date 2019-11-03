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
import vartas.discord.bot.guild._ast.ASTLongGroupValue;
import vartas.discord.bot.guild._ast.GuildNodeFactory;
import vartas.discord.bot.guild._symboltable.IGuildScope;
import vartas.discord.bot.guild._symboltable.LongGroupValueSymbol;

public abstract class LongGroupValueSymbolCreator {
    public static LongGroupValueSymbol create(IGuildScope enclosingScope, String type, String value){
        LongGroupValueSymbol result = new LongGroupValueSymbol(value);

        //Set the scopes for the symbol
        result.setEnclosingScope(enclosingScope);

        //Register result in the enclosing scope
        enclosingScope.add(result);

        //Set the AST of the symbol
        ASTLongGroupValue ast = createAst(type, value);
        result.setAstNode(ast);

        return result;
    }

    protected static ASTLongGroupValue createAst(String type, String value){
        ASTLongGroupValue ast = GuildNodeFactory.createASTLongGroupValue();

        ast.setType(type);
        ast.setValue(createValue(value));

        return ast;
    }

    protected static ASTBasicLongLiteral createValue(String value){
        ASTBasicLongLiteral ast = MCCommonLiteralsNodeFactory.createASTBasicLongLiteral();

        ast.setDigits(value);

        return ast;
    }
}
