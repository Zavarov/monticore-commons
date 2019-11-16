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

package vartas.discord.bot.guild.visitor;

import vartas.discord.bot.guild._symboltable.*;
import vartas.discord.bot.guild._visitor.GuildSymbolVisitor;

import java.nio.file.Files;

public class SymbolRemover implements GuildSymbolVisitor {
    GuildSymbolVisitor realThis = this;
    @Override
    public void setRealThis(GuildSymbolVisitor realThis){
        this.realThis = realThis;
    }
    @Override
    public GuildSymbolVisitor getRealThis(){
        return realThis;
    }

    public static void remove(ICommonGuildSymbol symbol){
        SymbolRemover remover = new SymbolRemover();
        symbol.accept(remover.getRealThis());
    }

    @Override
    public void handle(GuildArtifactSymbol symbol){
        symbol.getEnclosingScope().remove(symbol);
        symbol.getEnclosingScope().removeSubScope(symbol.getSpannedScope());

        //Delete the symbol file
        try {
            Files.deleteIfExists(symbol.getReference());
        }catch(Exception e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void handle(BlacklistEntrySymbol symbol){
        symbol.getEnclosingScope().remove(symbol);
    }

    @Override
    public void handle(PrefixEntrySymbol symbol){
        symbol.getEnclosingScope().remove(symbol);
    }

    @Override
    public void handle(RoleGroupEntrySymbol symbol){
        symbol.getEnclosingScope().remove(symbol);
        symbol.getEnclosingScope().removeSubScope(symbol.getSpannedScope());
    }

    @Override
    public void handle(SubredditGroupEntrySymbol symbol){
        symbol.getEnclosingScope().remove(symbol);
        symbol.getEnclosingScope().removeSubScope(symbol.getSpannedScope());
    }

    @Override
    public void handle(LongGroupElementSymbol symbol){
        symbol.getEnclosingScope().remove(symbol);
    }
}
