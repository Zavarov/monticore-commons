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

package vartas.reddit.comment;

import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.Files;
import vartas.reddit.Comment;
import vartas.reddit.comment._ast.ASTCommentArtifact;
import vartas.reddit.comment._parser.CommentParser;
import vartas.reddit.comment.prettyprint.CommentPrettyPrinter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This class provides utility functions for an easy transformation between text files
 * and instances of {@link Comment}.
 */
public abstract class CommentHelper {
    private static final CommentParser parser = new CommentParser();
    /**
     * Converts the comments into an by the grammar accepted format and stores them on the disk.
     * @param comments A collection of all comments that are stored.
     * @param target The target of the comment file.
     */
    public static void store(Collection<Comment> comments, File target){
        try {
            CommentPrettyPrinter printer = new CommentPrettyPrinter(new IndentPrinter());

            StringBuilder fileContent = new StringBuilder();
            comments.stream().map(printer::prettyprint).forEach(fileContent::append);

            if(!target.getParentFile().exists())
                java.nio.file.Files.createDirectories(target.getParentFile().toPath());
            if(!target.exists())
                java.nio.file.Files.createFile(target.toPath());

            Files.writeToTextFile(new StringReader(fileContent.toString()), target);
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param filePath The path of the comment file.
     * @return An immutable list of all comments in the file.
     */
    public static List<Comment> parse(String filePath){
        return List.copyOf(parseArtifact(filePath).getCommentList());
    }

    private static ASTCommentArtifact parseArtifact(String filePath){
        try{
            Optional<ASTCommentArtifact> comment = parser.parse(filePath);
            if(comment.isEmpty())
                throw new IllegalArgumentException("The comment file couldn't be parsed.");

            return comment.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }
}
