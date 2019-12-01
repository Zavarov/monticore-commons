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

package vartas.discord.aggregated;
import com.google.common.base.Preconditions;
import com.ibm.icu.text.RuleBasedNumberFormat;
import de.monticore.generating.GeneratorEngine;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.io.paths.IterablePath;
import de.monticore.io.paths.ModelPath;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FileUtils;
import org.atteo.evo.inflector.English;
import vartas.discord.aggregated.generator.*;
import vartas.discord.bot.entities.DiscordCommunicator;
import vartas.discord.bot.entities.DiscordEnvironment;
import vartas.discord.command.CommandHelper;
import vartas.discord.command._ast.ASTCommandArtifact;
import vartas.discord.command._cocos.CommandCoCoChecker;
import vartas.discord.command._symboltable.CommandGlobalScope;
import vartas.discord.command._symboltable.CommandLanguage;
import vartas.discord.command.cocos.CommandCoCos;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Main {

    protected static final Path TARGET_PATH = Paths.get("target/generated-sources/monticore/sourcecode/");
    protected static final Path SOURCE_PATH = Paths.get("src/main/java");

    protected static final String TEMPLATE_EXTENSION = "ftl";
    protected static final String TARGET_EXTENSION = "java";

    private static final GlobalExtensionManagement GLEX = new GlobalExtensionManagement();

    private static final GeneratorSetup SETUP = new GeneratorSetup();

    private static final GeneratorEngine GENERATOR = new GeneratorEngine(SETUP);

    static{
        Log.initWARN();

        GLEX.defineGlobalVar("Helper", new CommandGeneratorHelper());
        GLEX.defineGlobalVar("English", new English());
        GLEX.defineGlobalVar("Ordinal", new RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.ORDINAL));
        GLEX.defineGlobalVar("Communicator", DiscordCommunicator.class.getCanonicalName());
        GLEX.defineGlobalVar("Environment", DiscordEnvironment.class.getCanonicalName());
    }

    /**
     * Generates the models specified in the arguments.
     * There are at least two arguments required for this method:
     * <ul>
     *     <li>The path to the models</li>
     *     <li>The path to the templates</li>
     *     <li>The package of the builder</li>
     * </ul>
     *
     * @param args the arguments for generating the commands.
     */
    public static void main(String[] args){
        Preconditions.checkArgument(args.length >= 3, "Please provide at least 3 arguments.");
        Preconditions.checkArgument(new File(args[0]).exists(), args[0]+": Please make sure that the model file exists");
        Preconditions.checkArgument(new File(args[1]).exists(), args[1]+": Please make sure that the template file exists");

        File modelFolder = new File(args[0]).getAbsoluteFile();
        File templateFolder = new File(args[1]).getAbsoluteFile();

        IterablePath templatePath = IterablePath.from(templateFolder, TEMPLATE_EXTENSION);

        SETUP.setGlex(GLEX);
        SETUP.setOutputDirectory(TARGET_PATH.toFile());
        SETUP.setDefaultFileExtension(TARGET_EXTENSION);
        SETUP.setAdditionalTemplatePaths(Collections.singletonList(templateFolder));
        SETUP.setAdditionalTemplatePaths(templatePath.getPaths().stream().map(Path::toFile).collect(Collectors.toList()));

        CommandGlobalScope scope = createGlobalScope();
        List<ASTCommandArtifact> models = parseModels(modelFolder, scope);
        CommandCoCoChecker checker = CommandCoCos.getCheckerForAllCoCos();
        models.forEach(checker::checkAll);

        generate(models, args[2]);
    }

    private static void generate(List<ASTCommandArtifact> models, String builderPackage){
        CommandBuilderGenerator.generate(models, GENERATOR, SETUP, TARGET_PATH, builderPackage);
        CommandCreatorGenerator.generate(models, GENERATOR, SETUP, TARGET_PATH);
        AbstractCommandGenerator.generate(models, GENERATOR, SETUP, TARGET_PATH);
        CommandGenerator.generate(models, GENERATOR, SETUP, TARGET_PATH, SOURCE_PATH);
    }

    private static List<ASTCommandArtifact> parseModels(File modelFolder, CommandGlobalScope scope){
        return FileUtils.listFiles(modelFolder, new String[]{ CommandLanguage.COMMAND_FILE_ENDING}, false)
            .stream()
            .map(file -> CommandHelper.parse(scope, file.getPath()))
            .collect(Collectors.toList());
    }

    private static CommandGlobalScope createGlobalScope(){
        ModelPath path = new ModelPath(Paths.get(""));
        CommandLanguage language = new CommandLanguage();
        return new CommandGlobalScope(path, language);
    }
}
