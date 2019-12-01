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

package vartas.discord.bot.guild.prettyprint;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import vartas.discord.bot.guild._ast.*;
import vartas.discord.bot.guild._symboltable.*;
import vartas.discord.bot.guild._visitor.GuildSymbolVisitor;

public class GuildSymbolPrettyPrinter implements GuildSymbolVisitor {
    protected GuildSymbolVisitor realThis = this;
    protected IndentPrinter printer;
    protected MCCommonLiteralsPrettyPrinter prettyPrinter;

    public GuildSymbolPrettyPrinter(IndentPrinter printer){
        this.printer = printer;
        this.prettyPrinter = new MCCommonLiteralsPrettyPrinter(printer);
    }

    @Override
    public void setRealThis(GuildSymbolVisitor realThis){
        this.realThis = realThis;
    }

    @Override
    public GuildSymbolVisitor getRealThis(){
        return realThis;
    }

    public String prettyPrint(ICommonGuildSymbol symbol){
        symbol.accept(getRealThis());

        String content = printer.getContent();
        printer.clearBuffer();
        return content;
    }

    @Override
    public void visit(GuildArtifactSymbol symbol){
        printer.print("guild");
        printer.print(" ");
        symbol.getAstNode().getId().accept(prettyPrinter);
        printer.print(" ");
        printer.addLine("{");
    }

    @Override
    public void traverse(GuildArtifactSymbol symbol){
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalPrefixEntrySymbols())
            entry.accept(getRealThis());
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalBlacklistEntrySymbols())
            entry.accept(getRealThis());
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalSubredditGroupEntrySymbols())
            entry.accept(getRealThis());
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalRoleGroupEntrySymbols())
            entry.accept(getRealThis());
    }

    @Override
    public void endVisit(GuildArtifactSymbol symbol){
        printer.addLine("}");
    }

    @Override
    public void handle(BlacklistEntrySymbol symbol){
        ASTBlacklistEntry ast = symbol.getAstNode();
        printer.print("blacklist");
        printer.print(" ");
        ast.getStringLiteral().accept(prettyPrinter);
        printer.println();
    }

    @Override
    public void handle(PrefixEntrySymbol symbol){
        ASTPrefixEntry ast = symbol.getAstNode();
        printer.print("prefix");
        printer.print(" ");
        ast.getStringLiteral().accept(prettyPrinter);
        printer.println();
    }

    @Override
    public void visit(RoleGroupEntrySymbol symbol){
        ASTRoleGroupEntry ast = symbol.getAstNode();
        printer.print("rolegroup");
        printer.print(" ");
        ast.getStringLiteral().accept(prettyPrinter);
        printer.print(" ");
        printer.addLine("{");
    }

    @Override
    public void traverse(RoleGroupEntrySymbol symbol){
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalLongGroupElementSymbols())
            entry.accept(getRealThis());
    }

    @Override
    public void endVisit(RoleGroupEntrySymbol symbol){
        printer.addLine("}");
    }

    @Override
    public void visit(SubredditGroupEntrySymbol symbol){
        ASTSubredditGroupEntry ast = symbol.getAstNode();
        printer.print("subreddit");
        printer.print(" ");
        ast.getStringLiteral().accept(prettyPrinter);
        printer.print(" ");
        printer.addLine("{");
    }

    @Override
    public void traverse(SubredditGroupEntrySymbol symbol){
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalLongGroupElementSymbols())
            entry.accept(getRealThis());
    }

    @Override
    public void endVisit(SubredditGroupEntrySymbol symbol){
        printer.addLine("}");
    }

    @Override
    public void handle(LongGroupElementSymbol symbol){
        ASTLongGroupElement ast = symbol.getAstNode();
        //Print type
        if(ast.isPresentType()) {
            printer.print(ast.getType());
            printer.print(" : ");
        }
        //Print element
        ast.getElement().accept(prettyPrinter);
        printer.println();
    }
}
