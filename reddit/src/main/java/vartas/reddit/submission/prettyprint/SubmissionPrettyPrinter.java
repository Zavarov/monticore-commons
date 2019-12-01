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
import net.sourceforge.plantuml.StringUtils;
import vartas.reddit.Submission;
import vartas.reddit.submission._ast.ASTSubmissionEnum;

import java.time.ZoneOffset;

import static vartas.MonticoreEscapeUtils.escapeMonticore;
/**
 * This class transforms the {@link vartas.reddit.submission._ast.ASTSubmission} back
 * into a String that is accepted by the submission language.
 */
public class SubmissionPrettyPrinter{
    IndentPrinter printer;

    public SubmissionPrettyPrinter(IndentPrinter printer){
        this.printer = printer;
    }

    public String prettyprint(Submission submission){
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

    private void addAuthor(Submission submission){
        printer.addLine(String.format("%s = \"%s\"", prettyprint(ASTSubmissionEnum.AUTHOR), escapeMonticore(submission.getAuthor())));
    }

    private void addId(Submission submission){
        printer.addLine(String.format("%s = \"%s\"", prettyprint(ASTSubmissionEnum.ID), escapeMonticore(submission.getId())));
    }

    private void addLinkFlairText(Submission submission){
        submission.getLinkFlairText().ifPresent(
                text -> printer.addLine(String.format("%s = \"%s\"", prettyprint(ASTSubmissionEnum.LINKFLAIRTEXT), escapeMonticore(text)))
        );
    }

    private void addSubreddit(Submission submission){
        printer.addLine(String.format("%s = \"%s\"", prettyprint(ASTSubmissionEnum.SUBREDDIT), escapeMonticore(submission.getSubreddit())));
    }

    private void addNsfw(Submission submission){
        printer.addLine(String.format("%s = %b", prettyprint(ASTSubmissionEnum.NSFW), submission.isNsfw()));
    }

    private void addSpoiler(Submission submission){
        printer.addLine(String.format("%s = %b", prettyprint(ASTSubmissionEnum.SPOILER), submission.isSpoiler()));
    }

    private void addScore(Submission submission){
        printer.addLine(String.format("%s = %dL", prettyprint(ASTSubmissionEnum.SCORE), submission.getScore()));
    }

    private void addTitle(Submission submission){
        printer.addLine(String.format("%s = \"%s\"", prettyprint(ASTSubmissionEnum.TITLE), escapeMonticore(submission.getTitle())));
    }

    private void addCreated(Submission submission){
        printer.addLine(String.format("%s = \"%d\"", prettyprint(ASTSubmissionEnum.CREATED), submission.getCreated().toEpochSecond(ZoneOffset.UTC)));
    }

    private void addSelfText(Submission submission){
        submission.getSelfText().ifPresent(
                text -> printer.addLine(String.format("%s = \"%s\"", prettyprint(ASTSubmissionEnum.SELFTEXT), escapeMonticore(text)))
        );
    }

    private void addThumbnail(Submission submission){
        submission.getThumbnail().ifPresent(
                text -> printer.addLine(String.format("%s = \"%s\"", prettyprint(ASTSubmissionEnum.THUMBNAIL), escapeMonticore(text)))
        );
    }

    private void addUrl(Submission submission){
        printer.addLine(String.format("%s = \"%s\"", prettyprint(ASTSubmissionEnum.URL), escapeMonticore(submission.getUrl())));
    }

    private void addPermalink(Submission submission){
        printer.addLine(String.format("%s = \"%s\"", prettyprint(ASTSubmissionEnum.PERMALINK), escapeMonticore(submission.getPermalink())));
    }

    private String prettyprint(ASTSubmissionEnum key){
        return StringUtils.capitalize(key.name());
    }
}
