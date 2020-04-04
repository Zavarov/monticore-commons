/*
 * Copyright (c) 2020 Zavarov
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

package vartas.monticore.cd2code;

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.se_rwth.commons.Joiners;
import org.junit.jupiter.api.Test;
import vartas.monticore.cd2code._parser.CD2CodeParser;
import vartas.monticore.cd2code._symboltable.CD2CodeModelLoader;
import vartas.monticore.cd2code._symboltable.CD2CodeSymbolTableCreatorDelegator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ParseCDTest extends BasicCDTest{

    @Test
    public ASTCDCompilationUnit parseX(String... names){
        String qualifiedName = Joiners.DOT.join(names);

        CD2CodeModelLoader modelLoader = new CD2CodeModelLoader(cdLanguage);

        List<ASTCDCompilationUnit> models = modelLoader.loadModels(qualifiedName, cdModelPath);

        if(models.size() == 0)
            throw GeneratorError.of(Errors.NO_MATCHING_MODEL_FOUND);
        else if(models.size() > 1)
            throw GeneratorError.of(Errors.MULTIPLE_MODELS_FOUND);
        else
            return models.get(0);
    }

    @Test
    public ASTCDCompilationUnit parseSTC(String... names) {
        Path modelPath = MODEL_PATH.resolve(Paths.get("", names));

        CD2CodeParser parser = new CD2CodeParser();
        try {
            ASTCDCompilationUnit ast = parser.parse(modelPath.toString()).orElseThrow();
            CD2CodeSymbolTableCreatorDelegator stc = cdLanguage.getSymbolTableCreator(cdGlobalScope);
            stc.createFromAST(ast);
            return ast;

        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    public void testParseCache(){
        parseCDClass("Browser","vartas","monticore","cd2code","BrowserCD");
    }

    @Test
    public void testParseInstant(){
        parseCDClass("Person","vartas","monticore","cd2code","PersonCD");
    }

    @Test
    public void testParseURL(){
        parseCDClass("Website","vartas","monticore","cd2code","InternetCD");
    }

    @Test
    public void testParseParameter(){
        parseCDClass("Database","vartas","monticore","cd2code","DatabaseCD");
    }
}
