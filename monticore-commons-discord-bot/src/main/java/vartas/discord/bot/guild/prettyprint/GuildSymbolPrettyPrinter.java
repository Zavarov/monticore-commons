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
import vartas.discord.bot.guild._symboltable.*;
import vartas.discord.bot.guild._visitor.GuildSymbolVisitor;

import java.util.Locale;

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
        printer.print("guild ");
        printer.print(symbol.getName());
        printer.print("L");
        printer.addLine(" {");
    }

    @Override
    public void traverse(GuildArtifactSymbol symbol){
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalStringEntrySymbols())
            entry.accept(getRealThis());
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalLongGroupEntrySymbols())
            entry.accept(getRealThis());
    }

    @Override
    public void endVisit(GuildArtifactSymbol symbol){
        printer.addLine("}");
    }

    @Override
    public void visit(StringEntrySymbol symbol){
        //Print identifier
        printer.print(symbol.getName().toLowerCase(Locale.ENGLISH));
        printer.print(" ");
    }

    @Override
    public void traverse(StringEntrySymbol symbol){
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalStringValueSymbols())
            entry.accept(getRealThis());
    }

    @Override
    public void endVisit(StringEntrySymbol symbol){
        printer.println();
    }

    @Override
    public void handle(StringValueSymbol symbol){
        printer.print("\"");
        printer.print(symbol.getName());
        printer.print("\"");

    }

    @Override
    public void visit(LongGroupEntrySymbol symbol){
        //Print identifier
        printer.print(symbol.getName().toLowerCase(Locale.ENGLISH));
        printer.print(" ");
    }

    @Override
    public void traverse(LongGroupEntrySymbol symbol){
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalLongGroupArtifactSymbols())
            entry.accept(getRealThis());
    }

    @Override
    public void visit(LongGroupArtifactSymbol symbol){
        //Print group name
        printer.print("\"");
        printer.print(symbol.getName());
        printer.print("\"");
        printer.addLine(" {");
    }

    @Override
    public void traverse(LongGroupArtifactSymbol symbol){
        for(ICommonGuildSymbol entry : symbol.getSpannedScope().getLocalLongGroupValueSymbols())
            entry.accept(getRealThis());
    }

    @Override
    public void endVisit(LongGroupArtifactSymbol symbol){
        printer.addLine("}");
    }

    @Override
    public void handle(LongGroupValueSymbol symbol){
        symbol.getAstNode().ifPresent(ast -> {
            //Print type
            if(ast.isPresentType()) {
                printer.print(ast.getType());
                printer.print(" : ");
            }
            //Print value
            ast.getValue().accept(prettyPrinter);
            printer.println();
        });
    }
}
