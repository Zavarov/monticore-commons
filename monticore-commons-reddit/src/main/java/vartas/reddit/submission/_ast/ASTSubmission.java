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

package vartas.reddit.submission._ast;

import de.monticore.literals.mccommonliterals._ast.ASTSignedNatLiteral;
import vartas.reddit.SubmissionInterface;
import vartas.reddit.submission._symboltable.*;

import java.util.Date;
import java.util.Optional;

import static vartas.reddit.MonticoreEscapeUtils.unescapeMonticore;

public class ASTSubmission extends ASTSubmissionTOP implements SubmissionInterface {
    protected  ASTSubmission (){

    }

    public String getAuthor() {
        Optional<AuthorLiteralSymbol> symbol = getSpannedScope().resolveAuthorLiteral("author");

        return unescapeMonticore(symbol.get().getAstNode().get().getStringLiteral().getValue());
    }

    public String getId() {
        Optional<IdLiteralSymbol> symbol = getSpannedScope().resolveIdLiteral("id");

        return unescapeMonticore(symbol.get().getAstNode().get().getStringLiteral().getValue());
    }

    public Optional<String> getLinkFlairText() {
        Optional<LinkFlairTextLiteralSymbol> symbol = getSpannedScope().resolveLinkFlairTextLiteral("linkFlairText");

        if(symbol.isPresent())
            return Optional.of(unescapeMonticore(symbol.get().getAstNode().get().getStringLiteral().getValue()));
        else
            return Optional.empty();
    }

    public String getSubreddit() {
        Optional<SubredditLiteralSymbol> symbol = getSpannedScope().resolveSubredditLiteral("subreddit");

        return symbol.get().getAstNode().get().getStringLiteral().getValue();
    }

    public boolean isNsfw() {
        Optional<NsfwLiteralSymbol> symbol = getSpannedScope().resolveNsfwLiteral("nsfw");

        return symbol.get().getAstNode().get().getBooleanLiteral().getValue();
    }

    public boolean isSpoiler() {
        Optional<SpoilerLiteralSymbol> symbol = getSpannedScope().resolveSpoilerLiteral("spoiler");

        return symbol.get().getAstNode().get().getBooleanLiteral().getValue();
    }

    public int getScore() {
        Optional<ScoreLiteralSymbol> symbol = getSpannedScope().resolveScoreLiteral("score");

        //getSource is bugged in 5.3.0
        ASTSignedNatLiteral ast = symbol.get().getAstNode().get().getSignedNatLiteral();

        return Integer.parseInt(ast.isNegative() ? "-" + ast.getDigits() : ast.getDigits());
    }

    public String getTitle() {
        Optional<TitleLiteralSymbol> symbol = getSpannedScope().resolveTitleLiteral("title");

        return unescapeMonticore(symbol.get().getAstNode().get().getStringLiteral().getValue());
    }

    public Date getCreated() {
        Optional<CreatedLiteralSymbol> symbol = getSpannedScope().resolveCreatedLiteral("created");

        return new Date(symbol.get().getAstNode().get().getBasicLongLiteral().getValue());
    }

    public Optional<String> getSelfText() {
        Optional<SelfTextLiteralSymbol> symbol = getSpannedScope().resolveSelfTextLiteral("selfText");

        if(symbol.isPresent())
            return Optional.of(unescapeMonticore(symbol.get().getAstNode().get().getStringLiteral().getValue()));
        else
            return Optional.empty();
    }

    public Optional<String> getThumbnail() {
        Optional<ThumbnailLiteralSymbol> symbol = getSpannedScope().resolveThumbnailLiteral("thumbnail");

        if(symbol.isPresent())
            return Optional.of(unescapeMonticore(symbol.get().getAstNode().get().getStringLiteral().getValue()));
        else
            return Optional.empty();
    }

    public String getUrl() {
        Optional<UrlLiteralSymbol> symbol = getSpannedScope().resolveUrlLiteral("url");

        return unescapeMonticore(symbol.get().getAstNode().get().getStringLiteral().getValue());
    }

    @Override
    public String getPermalink(){
        Optional<PermalinkLiteralSymbol> symbol = getSpannedScope().resolvePermalinkLiteral("permalink");

        return unescapeMonticore(symbol.get().getAstNode().get().getStringLiteral().getValue());
    }
    /**
     * @return a hash code based on the id of the submission.
     */
    @Override
    public int hashCode(){
        return getId().hashCode();
    }
    /**
     * Two submissions are equal if they have the same id.
     * @param o an object this submission is compared to.
     * @return true if the object is a submission with the same id.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof SubmissionInterface){
            SubmissionInterface submission = (SubmissionInterface)o;
            return submission.getId().equals(this.getId());
        }else{
            return false;
        }
    }
}
