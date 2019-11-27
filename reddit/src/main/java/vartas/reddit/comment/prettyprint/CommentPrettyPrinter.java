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

package vartas.reddit.comment.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import vartas.reddit.Comment;

import java.time.ZoneOffset;

import static vartas.reddit.MonticoreEscapeUtils.escapeMonticore;

/**
 * This class transforms the {@link vartas.reddit.comment._ast.ASTComment} back
 * into a String that is accepted by the comment language.
 */
public class CommentPrettyPrinter {
    IndentPrinter printer;

    public CommentPrettyPrinter(IndentPrinter printer){
        this.printer = printer;
    }

    public String prettyprint(Comment comment){
        printer.clearBuffer();

        printer.addLine("comment {");

        addAuthor(comment);
        addId(comment);
        addScore(comment);
        addSubreddit(comment);
        addSubmission(comment);
        addSubmissionTitle(comment);
        addCreated(comment);

        printer.addLine("}");

        return printer.getContent();
    }

    private void addAuthor(Comment comment){
        printer.addLine(String.format("author = \"%s\"", escapeMonticore(comment.getAuthor())));
    }

    private void addId(Comment comment){
        printer.addLine(String.format("id = \"%s\"", escapeMonticore(comment.getId())));
    }

    private void addSubreddit(Comment comment){
        printer.addLine(String.format("subreddit = \"%s\"", escapeMonticore(comment.getSubreddit())));
    }

    private void addScore(Comment comment){
        printer.addLine(String.format("score = %d", comment.getScore()));
    }

    private void addSubmissionTitle(Comment comment){
        printer.addLine(String.format("submissionTitle = \"%s\"", escapeMonticore(comment.getSubmissionTitle())));
    }

    private void addSubmission(Comment comment){
        printer.addLine(String.format("submission = \"%s\"", escapeMonticore(comment.getSubmission())));
    }

    private void addCreated(Comment comment){
        printer.addLine(String.format("created = %dL", comment.getCreated().toEpochSecond(ZoneOffset.UTC)));
    }
}
