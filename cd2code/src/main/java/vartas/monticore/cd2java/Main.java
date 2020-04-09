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

package vartas.monticore.cd2java;

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.io.paths.ModelPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.monticore.cd2code.CDGenerator;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code.Errors;
import vartas.monticore.cd2code.GeneratorError;
import vartas.monticore.cd2code._symboltable.CD2CodeGlobalScope;
import vartas.monticore.cd2code._symboltable.CD2CodeLanguage;
import vartas.monticore.cd2code._symboltable.CD2CodeModelLoader;
import vartas.monticore.cd2code.prettyprint.CD2CodePrettyPrinter;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class.getSimpleName());
    private static final String JAVA_FILE_EXTENSION = "java";
    private static final GlobalExtensionManagement GLEX = new GlobalExtensionManagement();
    private static final CD2CodeLanguage LANGUAGE = new CD2CodeLanguage();

    public static void main(String[] args){
        Path MODEL_PATH = Paths.get(args[0]);
        Path TEMPLATE_PATH = Paths.get(args[1]);
        Path OUTPUT_PATH = Paths.get(args[2]);
        Path SOURCES_PATH = Paths.get(args[3]);
        String CLASS_DIAGRAM = args[4];

        LOG.info("Executing CD Generator.");
        LOG.info("Template Path: {}", TEMPLATE_PATH);
        LOG.info("Output Path: {}", OUTPUT_PATH);
        LOG.info("Sources Path: {}", SOURCES_PATH);
        LOG.info("Class Diagram: {}", CLASS_DIAGRAM);
        LOG.info("Create Symbol Table");

        //Load the symbol table
        ModelPath modelPath = new ModelPath(MODEL_PATH);
        CD2CodeGlobalScope scope = new CD2CodeGlobalScope(modelPath, LANGUAGE);

        LOG.info("Parsing Class Diagram.");
        //Parse the class diagram
        ASTCDCompilationUnit cdCompilationUnit = parse(scope, modelPath, CLASS_DIAGRAM);

        LOG.info("Initializing Glex.");
        CDGeneratorHelper generatorHelper = new CD2JavaGeneratorHelper(cdCompilationUnit, GLEX, SOURCES_PATH);
        GLEX.setGlobalValue("cdPrinter", new CD2CodePrettyPrinter());
        GLEX.setGlobalValue("cdGenHelper", generatorHelper);

        LOG.info("Initializing Generator Setup.");
        //Initialize the generator
        GeneratorSetup GENERATOR_SETUP = new GeneratorSetup();
        GENERATOR_SETUP.setDefaultFileExtension(JAVA_FILE_EXTENSION);
        GENERATOR_SETUP.setAdditionalTemplatePaths(Collections.singletonList(TEMPLATE_PATH.toFile()));
        GENERATOR_SETUP.setOutputDirectory(OUTPUT_PATH.toFile());
        GENERATOR_SETUP.setTracing(false);
        GENERATOR_SETUP.setGlex(GLEX);

        LOG.info("Generate Code.");
        generate(GENERATOR_SETUP, generatorHelper);
    }

    public static void generate(@Nonnull GeneratorSetup generatorSetup, @Nonnull CDGeneratorHelper generatorHelper){
        CDTransformerChain transformerChain = new CDTransformerChain(generatorHelper);
        List<ASTCDCompilationUnit> asts = transformerChain.get();

        CDGenerator cdGenerator = new CDGenerator(generatorSetup, generatorHelper);

        for(ASTCDCompilationUnit ast : asts)
            cdGenerator.generate(ast);
    }

    public static ASTCDCompilationUnit parse(@Nonnull CD2CodeGlobalScope scope, @Nonnull ModelPath modelPath, @Nonnull String qualifiedName){
        CD2CodeModelLoader modelLoader = new CD2CodeModelLoader(LANGUAGE);
        List<ASTCDCompilationUnit> models = modelLoader.loadModelsIntoScope(qualifiedName, modelPath, scope);

        if(models.size() == 0)
            throw GeneratorError.of(Errors.NO_MATCHING_MODEL_FOUND);
        else if(models.size() > 1)
            throw GeneratorError.of(Errors.MULTIPLE_MODELS_FOUND);
        else
            return models.get(0);
    }
}
