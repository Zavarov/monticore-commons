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

import de.monticore.io.paths.ModelPath;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.Files;
import vartas.reddit.SubmissionInterface;
import vartas.reddit.submission._ast.ASTSubmissionArtifact;
import vartas.reddit.submission._parser.SubmissionParser;
import vartas.reddit.submission._symboltable.SubmissionGlobalScope;
import vartas.reddit.submission._symboltable.SubmissionLanguage;
import vartas.reddit.submission._symboltable.SubmissionSymbolTableCreator;
import vartas.reddit.submission.prettyprint.SubmissionPrettyPrinter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SubmissionHelper {

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

    public static List<SubmissionInterface> parse(String filePath){
        try{
            SubmissionSymbolTableCreator symbolTableCreator = createSymbolTableCreator();

            SubmissionParser parser = new SubmissionParser();
            Optional<ASTSubmissionArtifact> comments = parser.parse(filePath);
            if(parser.hasErrors())
                throw new IllegalArgumentException("The parser encountered errors while parsing "+filePath);
            if(!comments.isPresent())
                throw new IllegalArgumentException("The comment file couldn't be parsed");

            ASTSubmissionArtifact ast = comments.get();
            symbolTableCreator.createFromAST(ast);

            return new ArrayList<>(ast.getSubmissionList());
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }

    private static SubmissionGlobalScope createGlobalScope(){
        ModelPath path = new ModelPath(Paths.get(""));
        SubmissionLanguage language = new SubmissionLanguage();
        return new SubmissionGlobalScope(path, language);
    }

    private static SubmissionSymbolTableCreator createSymbolTableCreator(){
        SubmissionGlobalScope globalScope = createGlobalScope();

        return new SubmissionSymbolTableCreator(globalScope);
    }
}
