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

package vartas.discord.bot.guild._ast;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.monticore.literals.mccommonliterals._ast.ASTBasicLongLiteral;
import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import vartas.discord.bot.guild._symboltable.FilterSymbol;
import vartas.discord.bot.guild._symboltable.PrefixSymbol;
import vartas.discord.bot.guild._symboltable.SubredditSymbol;
import vartas.discord.bot.guild._symboltable.TagSymbol;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ASTGuildArtifact extends ASTGuildArtifactTOP{
    protected ASTGuildArtifact(){
        super();
    }

    public Optional<String> getPrefix(){
        Optional<PrefixSymbol> symbol = getEnclosingScope().resolvePrefix("prefix");
        if(symbol.isPresent())
            return Optional.of(symbol.get().getAstNode().get().getPrefix().getValue());
        else
            return Optional.empty();
    }

    public Multimap<String, Long> getSubredditFeeds(){
        Collection<SubredditSymbol> symbols = getEnclosingScope().resolveSubredditMany("subreddit");
        Multimap<String, Long> target = HashMultimap.create();

        for(SubredditSymbol symbol : symbols){
            ASTSubreddit ast = symbol.getAstNode().get();
            for(ASTBasicLongLiteral textchannel : ast.getTextchannelList())
                target.put(ast.getSubreddit().getValue(), textchannel.getValue());
        }

        return target;
    }

    public Multimap<String, Long> getRoleGroups(){
        Collection<TagSymbol> symbols = getEnclosingScope().resolveTagMany("tag");
        Multimap<String, Long> target = HashMultimap.create();

        for(TagSymbol symbol : symbols){
            ASTTag ast = symbol.getAstNode().get();
            for(ASTBasicLongLiteral role : ast.getRoleList())
                target.put(ast.getTag().getValue(), role.getValue());
        }

        return target;
    }

    public Set<String> getFilter(){
        Optional<FilterSymbol> symbol = getEnclosingScope().resolveFilter("filter");

        if(symbol.isPresent())
            return symbol.get().getAstNode().get().getExpressionList().stream().map(ASTStringLiteral::getValue).collect(Collectors.toSet());
        else
            return Collections.emptySet();
    }
}
