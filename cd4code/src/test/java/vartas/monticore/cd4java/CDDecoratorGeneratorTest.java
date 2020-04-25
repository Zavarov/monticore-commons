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

package vartas.monticore.cd4java;

import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4code.CD4CodePrettyPrinterDelegator;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.io.paths.IterablePath;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import vartas.monticore.cd4analysis.CDDecoratorGenerator;
import vartas.monticore.cd4analysis.CDGeneratorHelper;

import java.util.Collections;

public class CDDecoratorGeneratorTest extends BasicCDTest{
    GlobalExtensionManagement glex;
    GeneratorSetup setup;
    CDGeneratorHelper helper;
    CDDecoratorGenerator generator;

    @BeforeEach
    public void setUp(){
        super.setUp();
        glex = new GlobalExtensionManagement();
        glex.setGlobalValue("cdPrinter", new CD4CodePrettyPrinterDelegator());
        glex.setGlobalValue("mcPrinter", new MCFullGenericTypesPrettyPrinter(new IndentPrinter()));

        setup = new GeneratorSetup();
        setup.setAdditionalTemplatePaths(Collections.singletonList(TEMPLATE_PATH.toFile()));
        setup.setDefaultFileExtension("java");
        setup.setGlex(glex);
        setup.setHandcodedPath(IterablePath.from(SOURCES_PATH.toFile(), "java"));
        setup.setOutputDirectory(OUTPUT_PATH.toFile());
        setup.setTracing(false);

        helper = new CDGeneratorHelper();

        generator = new CDDecoratorGenerator(
                setup,
                helper
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "vartas.monticore.cd4code.Cache",
            "vartas.monticore.cd4code.List",
            "vartas.monticore.cd4code.Map",
            "vartas.monticore.cd4code.Optional",
    })
    public void testGenerate(String modelPath){
        CDDefinitionSymbol cdDefinitionSymbol = globalScope.resolveCDDefinition(modelPath).orElseThrow();
        generator.generate(cdDefinitionSymbol);
    }
}
