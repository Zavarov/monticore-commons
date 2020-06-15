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

package vartas.monticore.cd4analysis;

import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4code.CD4CodePrettyPrinterDelegator;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.io.paths.IterablePath;
import de.monticore.io.paths.ModelPath;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import vartas.monticore.cd4analysis._symboltable.CD4CodeGlobalScope;
import vartas.monticore.cd4analysis._symboltable.CD4CodeLanguage;

import java.nio.file.Path;
import java.util.Collections;

public class Main {
    private static Path MODEL_PATH;
    private static Path TEMPLATE_PATH;
    private static Path OUTPUT_PATH;
    private static Path SOURCES_PATH;
    private static Path CLASSES_PATH;
    private static String MODEL;

    public static void main(String[] args){
        assert args.length >= 6;

        //Setup environment
        MODEL_PATH = Path.of(args[0]);
        TEMPLATE_PATH = Path.of(args[1]);
        OUTPUT_PATH = Path.of(args[2]);
        SOURCES_PATH = Path.of(args[3]);
        CLASSES_PATH = Path.of(args[4]);
        MODEL = args[5];

        CD4CodeLanguage language = new CD4CodeLanguage();
        ModelPath modelPath = new ModelPath(MODEL_PATH, CLASSES_PATH);
        CD4CodeGlobalScope globalScope = new CD4CodeGlobalScope(modelPath, language);

        //Setup generator
        GlobalExtensionManagement glex = new GlobalExtensionManagement();
        glex.setGlobalValue("cdPrinter", new CD4CodePrettyPrinterDelegator());
        glex.setGlobalValue("mcPrinter", new MCFullGenericTypesPrettyPrinter(new IndentPrinter()));

        GeneratorSetup setup = new GeneratorSetup();
        setup.setAdditionalTemplatePaths(Collections.singletonList(TEMPLATE_PATH.toFile()));
        setup.setDefaultFileExtension("java");
        setup.setGlex(glex);
        setup.setHandcodedPath(IterablePath.from(SOURCES_PATH.toFile(), "java"));
        setup.setOutputDirectory(OUTPUT_PATH.toFile());
        setup.setTracing(false);

        CDGeneratorHelper helper = new CDGeneratorHelper();

        //Generate
        CDDefinitionSymbol cdDefinitionSymbol = globalScope.resolveCDDefinition(MODEL).orElseThrow();

        CDFactoryGenerator factoryGenerator = new CDFactoryGenerator(setup, helper, globalScope);
        CDVisitorGenerator visitorGenerator = new CDVisitorGenerator(setup, helper, globalScope);
        CDDecoratorGenerator decoratorGenerator = new CDDecoratorGenerator(setup, helper);

        factoryGenerator.generate(cdDefinitionSymbol);
        visitorGenerator.generate(cdDefinitionSymbol);
        decoratorGenerator.generate(cdDefinitionSymbol);
    }
}
