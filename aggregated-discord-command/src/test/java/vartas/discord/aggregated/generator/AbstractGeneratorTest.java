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

package vartas.discord.aggregated.generator;

import com.ibm.icu.text.RuleBasedNumberFormat;
import de.monticore.generating.GeneratorEngine;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.io.paths.IterablePath;
import de.monticore.io.paths.ModelPath;
import de.se_rwth.commons.logging.Log;
import org.atteo.evo.inflector.English;
import org.junit.Before;
import org.junit.BeforeClass;
import vartas.discord.command.CommandHelper;
import vartas.discord.command._ast.ASTCommandArtifact;
import vartas.discord.command._symboltable.CommandGlobalScope;
import vartas.discord.command._symboltable.CommandLanguage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Collectors;

public abstract class AbstractGeneratorTest {
    protected static final Path OUTPUT_PATH = Paths.get("target/generated-test-sources/monticore/sourcecode/");
    protected static final Path TEMPLATE_PATH = Paths.get("src/main/resources/templates");
    protected static final Path REFERENCE_PATH = Paths.get("src/main/java");

    protected static final String TEMPLATE_EXTENSION = "ftl";
    protected static final String TARGET_EXTENSION = "java";

    protected CommandGlobalScope scope;
    protected ASTCommandArtifact ast;
    protected IterablePath targetPath;
    protected IterablePath templatePath;

    protected GlobalExtensionManagement glex;
    protected GeneratorSetup setup;
    protected GeneratorEngine generator;

    @Before
    public void initGenerator() {
        scope = new CommandGlobalScope(new ModelPath(Paths.get("")), new CommandLanguage());
        ast = CommandHelper.parse(scope, "src/test/resources/Command.cmd");

        targetPath = IterablePath.from(REFERENCE_PATH.toFile(), TARGET_EXTENSION);
        templatePath = IterablePath.from(TEMPLATE_PATH.toFile(), TEMPLATE_EXTENSION);

        glex = new GlobalExtensionManagement();
        glex.defineGlobalVar("Helper", new CommandGeneratorHelper());
        glex.defineGlobalVar("English", new English());
        glex.defineGlobalVar("Ordinal", new RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.ORDINAL));

        setup = new GeneratorSetup();
        setup.setGlex(glex);
        setup.setOutputDirectory(OUTPUT_PATH.toFile());
        setup.setModelName(CommandGeneratorHelper.getPackage(ast));
        setup.setAdditionalTemplatePaths(templatePath.getPaths().stream().map(Path::toFile).collect(Collectors.toList()));
        setup.setDefaultFileExtension(TARGET_EXTENSION);

        generator = new GeneratorEngine(setup);
    }

    @BeforeClass
    public static void initLog(){
        Log.initWARN();
    }
}
