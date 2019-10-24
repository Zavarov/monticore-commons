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

package vartas.discord.bot.config._ast;

import vartas.discord.bot.config._symboltable.*;

import java.util.Optional;

public class ASTConfigArtifact extends ASTConfigArtifactTOP{
    protected ASTConfigArtifact(){
        super();
    }

    public int getStatusMessageUpdateInterval(){
        Optional<StatusMessageUpdateIntervalSymbol> symbol = getEnclosingScope().resolveStatusMessageUpdateInterval("statusMessageUpdateInterval");
        return symbol.get().getAstNode().get().getNatLiteral().getValue();
    }

    public int getInteractiveMessageLifetime(){
        Optional<InteractiveMessageLifetimeSymbol> symbol = getEnclosingScope().resolveInteractiveMessageLifetime("interactiveMessageLifetime");
        return symbol.get().getAstNode().get().getNatLiteral().getValue();
    }

    public int getDiscordShards(){
        Optional<DiscordShardsSymbol> symbol = getEnclosingScope().resolveDiscordShards("discordShards");
        return symbol.get().getAstNode().get().getNatLiteral().getValue();
    }

    public int getActivityUpdateInterval(){
        Optional<ActivityUpdateIntervalSymbol> symbol = getEnclosingScope().resolveActivityUpdateInterval("activityUpdateInterval");
        return symbol.get().getAstNode().get().getNatLiteral().getValue();
    }

    public String getInviteSupportServer(){
        Optional<InviteSupportServerSymbol> symbol = getEnclosingScope().resolveInviteSupportServer("inviteSupportServer");
        return symbol.get().getAstNode().get().getStringLiteral().getValue();
    }

    public String getBotName(){
        Optional<BotNameSymbol> symbol = getEnclosingScope().resolveBotName("botName");
        return symbol.get().getAstNode().get().getStringLiteral().getValue();
    }

    public String getGlobalPrefix(){
        Optional<GlobalPrefixSymbol> symbol = getEnclosingScope().resolveGlobalPrefix("globalPrefix");
        return symbol.get().getAstNode().get().getStringLiteral().getValue();
    }

    public String getWikiLink(){
        Optional<WikiLinkSymbol> symbol = getEnclosingScope().resolveWikiLink("wikiLink");
        return symbol.get().getAstNode().get().getStringLiteral().getValue();
    }

    public int getImageWidth(){
        Optional<ImageWidthSymbol> symbol = getEnclosingScope().resolveImageWidth("imageWidth");
        return symbol.get().getAstNode().get().getNatLiteral().getValue();
    }

    public int getImageHeight(){
        Optional<ImageHeightSymbol> symbol = getEnclosingScope().resolveImageHeight("imageHeight");
        return symbol.get().getAstNode().get().getNatLiteral().getValue();
    }

    public String getDiscordToken(){
        Optional<DiscordTokenSymbol> symbol = getEnclosingScope().resolveDiscordToken("discordToken");
        return symbol.get().getAstNode().get().getStringLiteral().getValue();
    }

    public String getRedditAccount(){
        Optional<RedditAccountSymbol> symbol = getEnclosingScope().resolveRedditAccount("redditAccount");
        return symbol.get().getAstNode().get().getStringLiteral().getValue();
    }

    public String getRedditId(){
        Optional<RedditIdSymbol> symbol = getEnclosingScope().resolveRedditId("redditId");
        return symbol.get().getAstNode().get().getStringLiteral().getValue();
    }

    public String getRedditSecret(){
        Optional<RedditSecretSymbol> symbol = getEnclosingScope().resolveRedditSecret("redditSecret");
        return symbol.get().getAstNode().get().getStringLiteral().getValue();
    }
}