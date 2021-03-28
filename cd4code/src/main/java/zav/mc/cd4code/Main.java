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

package zav.mc.cd4code;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4code.CD4CodePrettyPrinterDelegator;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.io.paths.IterablePath;
import de.monticore.io.paths.ModelPath;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import zav.mc.cd4code._symboltable.CD4CodeGlobalScope;
import zav.mc.cd4code.decorator.CDDecoratorGenerator;
import zav.mc.cd4code.factory.CDFactoryGenerator;
import zav.mc.cd4code.json.CDJSONGenerator;
import zav.mc.cd4code.visitor.CDVisitorGenerator;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Path MODELS_PATH;
    private static Path LOCAL_MODELS_PATH;
    private static Path TEMPLATES_PATH;
    private static Path LOCAL_TEMPLATES_PATH;
    private static Path SOURCES_PATH;
    private static Path OUTPUT_PATH;
    private static String MODEL;

    private static CD4CodeGlobalScope GLOBAL_SCOPE;
    private static GlobalExtensionManagement GLEX;
    private static GeneratorSetup GENERATOR_SETUP;
    private static CDGeneratorHelper GENERATOR_HELPER;

    private static CDFactoryGenerator FACTORY_GENERATOR;
    private static CDVisitorGenerator VISITOR_GENERATOR;
    private static CDDecoratorGenerator DECORATOR_GENERATOR;
    private static CDJSONGenerator JSON_GENERATOR;

    public static void main(String... args){
        processArguments(args);

        buildGlobalScope();
        buildGlex();
        buildGeneratorSetup();
        buildGeneratorHelper();
        buildGenerator();

        generate();
    }

    private static void processArguments(String[] args){
        assert args.length >= 7;
        // ------------------------------------------------
        // Contain the class diagrams
        // ------------------------------------------------
        MODELS_PATH = Path.of(args[0]);
        LOCAL_MODELS_PATH = Path.of(args[1]);
        // ------------------------------------------------
        // Contain the FreeMarker templates
        // ------------------------------------------------
        TEMPLATES_PATH = Path.of(args[2]);
        LOCAL_TEMPLATES_PATH = Path.of(args[3]);
        // ------------------------------------------------
        // The source directory for the handwritten files
        // ------------------------------------------------
        SOURCES_PATH = Path.of(args[4]);
        // ------------------------------------------------
        // The target directory for the generated files
        // ------------------------------------------------
        OUTPUT_PATH = Path.of(args[5]);
        MODEL = args[6];
    }

    private static void buildGlobalScope(){
        assert MODELS_PATH != null;
        assert LOCAL_MODELS_PATH != null;

        ModelPath modelPath = new ModelPath();

        if(MODELS_PATH.toFile().exists())
            modelPath.addEntry(MODELS_PATH);
        if(LOCAL_MODELS_PATH.toFile().exists())
            modelPath.addEntry(LOCAL_MODELS_PATH);

        GLOBAL_SCOPE = new CD4CodeGlobalScope(modelPath, "cd");
    }

    private static void buildGlex(){
        GLEX = new GlobalExtensionManagement();
        GLEX.setGlobalValue("cdPrinter", new CD4CodePrettyPrinterDelegator());
        GLEX.setGlobalValue("mcPrinter", new MCFullGenericTypesPrettyPrinter(new IndentPrinter()));
    }

    private static void buildGeneratorSetup(){
        assert TEMPLATES_PATH != null;
        assert LOCAL_TEMPLATES_PATH != null;
        assert SOURCES_PATH != null;
        assert OUTPUT_PATH != null;
        assert GLEX != null;

        List<File> templatePaths = new ArrayList<>();
        if(TEMPLATES_PATH.toFile().exists())
            templatePaths.add(TEMPLATES_PATH.toFile());
        if(LOCAL_TEMPLATES_PATH.toFile().exists())
            templatePaths.add(LOCAL_TEMPLATES_PATH.toFile());

        GENERATOR_SETUP = new GeneratorSetup();
        GENERATOR_SETUP.setAdditionalTemplatePaths(templatePaths);
        GENERATOR_SETUP.setDefaultFileExtension(CDGeneratorHelper.DEFAULT_FILE_EXTENSION);
        GENERATOR_SETUP.setGlex(GLEX);
        GENERATOR_SETUP.setHandcodedPath(IterablePath.from(SOURCES_PATH.toFile(), CDGeneratorHelper.DEFAULT_FILE_EXTENSION));
        GENERATOR_SETUP.setOutputDirectory(OUTPUT_PATH.toFile());
        GENERATOR_SETUP.setTracing(false);
    }

    private static void buildGeneratorHelper(){
        assert SOURCES_PATH != null;

        GENERATOR_HELPER = new CDGeneratorHelper(SOURCES_PATH);
    }

    private static void buildGenerator(){
        FACTORY_GENERATOR = new CDFactoryGenerator(GENERATOR_SETUP, GENERATOR_HELPER, GLOBAL_SCOPE);
        VISITOR_GENERATOR = new CDVisitorGenerator(GENERATOR_SETUP, GENERATOR_HELPER, GLOBAL_SCOPE);
        DECORATOR_GENERATOR = new CDDecoratorGenerator(GENERATOR_SETUP, GENERATOR_HELPER, GLOBAL_SCOPE);
        JSON_GENERATOR = new CDJSONGenerator(GENERATOR_SETUP, GENERATOR_HELPER, GLOBAL_SCOPE);
    }

    private static void generate(){
        assert GLOBAL_SCOPE != null;
        assert MODEL != null;
        assert FACTORY_GENERATOR != null;
        assert VISITOR_GENERATOR != null;
        assert DECORATOR_GENERATOR != null;

        CDDefinitionSymbol cdDefinitionSymbol = GLOBAL_SCOPE.resolveCDDefinition(MODEL).orElseThrow();
        ASTCDDefinition node = cdDefinitionSymbol.getAstNode();

        FACTORY_GENERATOR.generate(node);
        VISITOR_GENERATOR.generate(node);
        JSON_GENERATOR.generate(node);
        DECORATOR_GENERATOR.generate(node);
    }
}
