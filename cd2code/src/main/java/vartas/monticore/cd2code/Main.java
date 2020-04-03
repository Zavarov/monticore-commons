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
import de.monticore.codegen.cd2java.CoreTemplates;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.StringHookPoint;
import de.monticore.io.paths.ModelPath;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.se_rwth.commons.Joiners;
import vartas.monticore.cd2code._symboltable.CD2CodeGlobalScope;
import vartas.monticore.cd2code._symboltable.CD2CodeLanguage;
import vartas.monticore.cd2code._symboltable.CD2CodeModelLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class Main {
    private static final String JAVA_FILE_EXTENSION = "java";
    private static CD2CodeLanguage LANGUAGE = new CD2CodeLanguage();
    private static ModelPath MODEL_PATH;
    private static CD2CodeGlobalScope SCOPE;

    public static void main(String[] args){
        Path TEMPLATE_PATH = Paths.get(args[1]);
        Path OUTPUT_PATH = Paths.get(args[2]);
        String CLASS_DIAGRAM = args[3];

        //Load the symbol table
        MODEL_PATH = new ModelPath(Paths.get(args[0]));
        SCOPE = new CD2CodeGlobalScope(MODEL_PATH, LANGUAGE);

        //Parse the class diagram
        ASTCDCompilationUnit cdCompilationUnit = parse(CLASS_DIAGRAM);
        String importString = cdCompilationUnit.getMCImportStatementList().stream().map(ASTMCImportStatement::printType).reduce((u, v) -> u + "\n" + v).orElse("");

        //Initialize the GLEX
        GlobalExtensionManagement GLEX = new GlobalExtensionManagement();
        GLEX.replaceTemplate(CDGeneratorHelper.PACKAGE_TEMPLATE, CoreTemplates.createPackageHookPoint(cdCompilationUnit.getPackageList()));
        GLEX.replaceTemplate(CDGeneratorHelper.IMPORT_TEMPLATE, new StringHookPoint(importString));

        //Initialize the generator
        GeneratorSetup GENERATOR_SETUP = new GeneratorSetup();
        GENERATOR_SETUP.setDefaultFileExtension(JAVA_FILE_EXTENSION);
        GENERATOR_SETUP.setAdditionalTemplatePaths(Collections.singletonList(TEMPLATE_PATH.toFile()));
        GENERATOR_SETUP.setOutputDirectory(OUTPUT_PATH.toFile());
        GENERATOR_SETUP.setTracing(false);
        GENERATOR_SETUP.setGlex(GLEX);

        //Generate the source code
        CDGenerator.generate(GENERATOR_SETUP, cdCompilationUnit);
    }

    private static ASTCDCompilationUnit parse(String... names){
        String qualifiedName = Joiners.DOT.join(names);

        CD2CodeModelLoader modelLoader = new CD2CodeModelLoader(LANGUAGE);
        List<ASTCDCompilationUnit> models = modelLoader.loadModelsIntoScope(qualifiedName, MODEL_PATH, SCOPE);

        if(models.size() == 0)
            throw GeneratorError.of(Errors.NO_MATCHING_MODEL_FOUND);
        else if(models.size() > 1)
            throw GeneratorError.of(Errors.MULTIPLE_MODELS_FOUND);
        else
            return models.get(0);
    }
}
