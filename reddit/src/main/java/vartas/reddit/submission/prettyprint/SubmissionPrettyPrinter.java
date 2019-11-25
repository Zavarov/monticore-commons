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

package vartas.reddit.submission.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import vartas.reddit.SubmissionInterface;

import java.time.ZoneOffset;

import static vartas.reddit.MonticoreEscapeUtils.escapeMonticore;
/**
 * This class transforms the {@link vartas.reddit.submission._ast.ASTSubmission} back
 * into a String that is accepted by the submission language.
 */
public class SubmissionPrettyPrinter{
    IndentPrinter printer;

    public SubmissionPrettyPrinter(IndentPrinter printer){
        this.printer = printer;
    }

    public String prettyprint(SubmissionInterface submission){
        printer.clearBuffer();

        printer.addLine("submission {");

        addAuthor(submission);
        addId(submission);
        addLinkFlairText(submission);
        addSubreddit(submission);
        addNsfw(submission);
        addSpoiler(submission);
        addScore(submission);
        addTitle(submission);
        addCreated(submission);
        addSelfText(submission);
        addThumbnail(submission);
        addUrl(submission);
        addPermalink(submission);

        printer.addLine("}");

        return printer.getContent();
    }

    private void addAuthor(SubmissionInterface submission){
        printer.addLine(String.format("author = \"%s\"", escapeMonticore(submission.getAuthor())));
    }

    private void addId(SubmissionInterface submission){
        printer.addLine(String.format("id = \"%s\"", escapeMonticore(submission.getId())));
    }

    private void addLinkFlairText(SubmissionInterface submission){
        submission.getLinkFlairText().ifPresent(
                text -> printer.addLine(String.format("linkFlairText = \"%s\"", escapeMonticore(text)))
        );
    }

    private void addSubreddit(SubmissionInterface submission){
        printer.addLine(String.format("subreddit = \"%s\"", escapeMonticore(submission.getSubreddit())));
    }

    private void addNsfw(SubmissionInterface submission){
        printer.addLine(String.format("nsfw = %b", submission.isNsfw()));
    }

    private void addSpoiler(SubmissionInterface submission){
        printer.addLine(String.format("spoiler = %b", submission.isSpoiler()));
    }

    private void addScore(SubmissionInterface submission){
        printer.addLine(String.format("score = %d", submission.getScore()));
    }

    private void addTitle(SubmissionInterface submission){
        printer.addLine(String.format("title = \"%s\"", escapeMonticore(submission.getTitle())));
    }

    private void addCreated(SubmissionInterface submission){
        printer.addLine(String.format("created = %dL", submission.getCreated().toEpochSecond(ZoneOffset.UTC)));
    }

    private void addSelfText(SubmissionInterface submission){
        submission.getSelfText().ifPresent(
                text -> printer.addLine(String.format("selfText = \"%s\"", escapeMonticore(text)))
        );
    }

    private void addThumbnail(SubmissionInterface submission){
        submission.getThumbnail().ifPresent(
                text -> printer.addLine(String.format("thumbnail = \"%s\"", escapeMonticore(text)))
        );
    }

    private void addUrl(SubmissionInterface submission){
        printer.addLine(String.format("url = \"%s\"", escapeMonticore(submission.getUrl())));
    }

    private void addPermalink(SubmissionInterface submission){
        printer.addLine(String.format("permalink = \"%s\"", escapeMonticore(submission.getPermalink())));
    }
}
