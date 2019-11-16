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

package vartas.reddit.submission;

import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.Files;
import vartas.reddit.SubmissionInterface;
import vartas.reddit.submission._ast.ASTSubmissionArtifact;
import vartas.reddit.submission._parser.SubmissionParser;
import vartas.reddit.submission._symboltable.SubmissionArtifactScope;
import vartas.reddit.submission._symboltable.SubmissionScope;
import vartas.reddit.submission._symboltable.SubmissionSymbolTableCreator;
import vartas.reddit.submission.prettyprint.SubmissionPrettyPrinter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
/**
 * This class provides utility functions for an easy transformation between text files
 * and instances of {@link SubmissionInterface}.
 */
public class SubmissionHelper {
    /**
     * Converts the submissions into an by the grammar accepted format and stores them on the disk.
     * @param submissions A collection of all submissions that are stored.
     * @param target The target of the submission file.
     */
    public static void store(Collection<SubmissionInterface> submissions, File target){
        try {
            SubmissionPrettyPrinter printer = new SubmissionPrettyPrinter(new IndentPrinter());

            StringBuilder fileContent = new StringBuilder();
            submissions.stream().map(printer::prettyprint).forEach(fileContent::append);

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
     * @param filePath The path of the submission file.
     * @return A list of all submission instances in the file.
     */
    public static List<SubmissionInterface> parse(String filePath) throws IllegalArgumentException{
        ASTSubmissionArtifact ast = parseArtifact(filePath);
        buildSymbolTable(ast);
        return new ArrayList<>(ast.getSubmissionList());
    }

    private static ASTSubmissionArtifact parseArtifact(String filePath){
        try{
            SubmissionParser parser = new SubmissionParser();
            Optional<ASTSubmissionArtifact> submission = parser.parse(filePath);
            if(!submission.isPresent())
                throw new IllegalArgumentException("The submission file couldn't be parsed.");

            return submission.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static SubmissionArtifactScope buildSymbolTable(ASTSubmissionArtifact ast){
        SubmissionScope scope = new SubmissionScope(true);
        SubmissionSymbolTableCreator symbolTableCreator = new SubmissionSymbolTableCreator(scope);
        return symbolTableCreator.createFromAST(ast);
    }
}