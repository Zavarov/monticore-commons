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

import de.monticore.generating.GeneratorEngine;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.io.paths.IterablePath;
import de.monticore.io.paths.ModelPath;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.command.CommandHelper;
import vartas.discord.command._ast.ASTCommandArtifact;
import vartas.discord.command._symboltable.CommandGlobalScope;
import vartas.discord.command._symboltable.CommandLanguage;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class CommandGeneratorTest{
    private static final String OUTPUT_DIRECTORY = "target/generated-test-sources/monticore/sourcecode/";

    private static final String TEMPLATE_EXTENSION = "ftl";
    private static final String TEMPLATE_PATH = "src/main/resources/templates";

    private static final String TARGET_EXTENSION = "java";
    private static final String TARGET_PATH = "src/main/java";

    private CommandGlobalScope scope;
    private ASTCommandArtifact ast;
    private IterablePath targetPath;
    private IterablePath templatePath;
    private File outputDirectory;

    private GlobalExtensionManagement glex;
    private GeneratorSetup setup;
    private GeneratorEngine generator;

    @Before
    public void setUp(){
        File targetFolder = new File(TARGET_PATH).getAbsoluteFile();
        File templateFolder = new File(TEMPLATE_PATH).getAbsoluteFile();

        scope = new CommandGlobalScope(new ModelPath(Paths.get("")), new CommandLanguage());
        ast = CommandHelper.parse(scope, "src/test/resources/Command.cmd");
        targetPath = IterablePath.from(Collections.singletonList(targetFolder), TARGET_EXTENSION);
        templatePath = IterablePath.from(Collections.singletonList(templateFolder), TEMPLATE_EXTENSION);
        outputDirectory = new File(OUTPUT_DIRECTORY);

        glex = new GlobalExtensionManagement();
        glex.defineGlobalVar("helper", new CommandGeneratorHelper());

        setup = new GeneratorSetup();
        setup.setGlex(glex);
        setup.setOutputDirectory(outputDirectory);
        setup.setModelName(CommandGeneratorHelper.getPackage(ast));
        setup.setAdditionalTemplatePaths(templatePath.getPaths().stream().map(Path::toFile).collect(Collectors.toList()));

        generator = new GeneratorEngine(setup);
        CommandGenerator.generate(ast, generator, targetPath);
    }

    @Test
    public void testGenerate(){
        String packageName = CommandGeneratorHelper.getPackageFolder(ast);

        String qualifiedName = OUTPUT_DIRECTORY + FileSystems.getDefault().getSeparator() + packageName + FileSystems.getDefault().getSeparator();

        assertThat(new File(qualifiedName + "TestCommand."+TARGET_EXTENSION)).exists();
    }
}
