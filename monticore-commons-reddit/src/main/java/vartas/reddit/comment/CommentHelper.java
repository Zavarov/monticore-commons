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

import de.monticore.io.paths.ModelPath;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.Files;
import vartas.reddit.CommentInterface;
import vartas.reddit.comment._ast.ASTCommentArtifact;
import vartas.reddit.comment._parser.CommentParser;
import vartas.reddit.comment._symboltable.CommentGlobalScope;
import vartas.reddit.comment._symboltable.CommentLanguage;
import vartas.reddit.comment._symboltable.CommentSymbolTableCreator;
import vartas.reddit.comment.prettyprint.CommentPrettyPrinter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class CommentHelper {

    public static void store(Collection<CommentInterface> comments, File target){
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

    public static List<CommentInterface> parse(String filePath){
        try{
            CommentSymbolTableCreator symbolTableCreator = createSymbolTableCreator();

            CommentParser parser = new CommentParser();
            Optional<ASTCommentArtifact> comments = parser.parse(filePath);
            if(parser.hasErrors())
                throw new IllegalArgumentException("The parser encountered errors while parsing "+filePath);
            if(!comments.isPresent())
                throw new IllegalArgumentException("The comment file couldn't be parsed");

            ASTCommentArtifact ast = comments.get();
            symbolTableCreator.createFromAST(ast);

            return new ArrayList<>(ast.getCommentList());
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static CommentGlobalScope createGlobalScope(){
        ModelPath path = new ModelPath(Paths.get(""));
        CommentLanguage language = new CommentLanguage();
        return new CommentGlobalScope(path, language);
    }

    private static CommentSymbolTableCreator createSymbolTableCreator(){
        CommentGlobalScope globalScope = createGlobalScope();

        return new CommentSymbolTableCreator(globalScope);
    }
}
