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
import org.apache.commons.io.FileUtils;
import org.atteo.evo.inflector.English;
import vartas.discord.aggregated.generator.CommandBuilderGenerator;
import vartas.discord.aggregated.generator.CommandGenerator;
import vartas.discord.aggregated.generator.CommandGeneratorHelper;
import vartas.discord.bot.CommunicatorInterface;
import vartas.discord.bot.EnvironmentInterface;
import vartas.discord.command.CommandHelper;
import vartas.discord.command._ast.ASTCommandArtifact;
import vartas.discord.command._cocos.CommandCoCoChecker;
import vartas.discord.command._symboltable.CommandGlobalScope;
import vartas.discord.command._symboltable.CommandLanguage;
import vartas.discord.command.cocos.CommandCoCos;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Main {
    private static final String TEMPLATE_EXTENSION = "ftl";

    private static final String TARGET_EXTENSION = "java";

    private static final GlobalExtensionManagement GLEX = new GlobalExtensionManagement();

    private static final GeneratorSetup SETUP = new GeneratorSetup();

    private static final GeneratorEngine GENERATOR = new GeneratorEngine(SETUP);

    static{
        GLEX.defineGlobalVar("Helper", new CommandGeneratorHelper());
        GLEX.defineGlobalVar("English", new English());
        GLEX.defineGlobalVar("Ordinal", new RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.ORDINAL));
        GLEX.defineGlobalVar("Communicator", CommunicatorInterface.class.getCanonicalName());
        GLEX.defineGlobalVar("Environment", EnvironmentInterface.class.getCanonicalName());

        SETUP.setGlex(GLEX);
    }

    /**
     * Generates the models specified in the arguments.
     * There are at least five arguments required for this method:
     * <ul>
     *     <li>The path to the models</li>
     *     <li>The path to the templates</li>
     *     <li>The directory of the handwritten sources</li>
     *     <li>The target directory of the generated sources</li>
     *     <li>The package name of the command builder</li>
     *     <li>An external class for the communicator</li>
     *     <li>An external class for the environment</li>
     * </ul>
     * The argument for the communicator and environment are optional.
     * By default, {@link CommunicatorInterface} and {@link EnvironmentInterface} are used.
     *
     * @param args the arguments for generating the commands.
     */
    public static void main(String[] args){
        Preconditions.checkArgument(args.length >= 5, "Please provide at least 3 arguments.");
        Preconditions.checkArgument(new File(args[0]).exists(), args[0]+": Please make sure that the model file exists");
        Preconditions.checkArgument(new File(args[1]).exists(), args[1]+": Please make sure that the template file exists");
        Preconditions.checkArgument(new File(args[2]).exists(), args[2]+": Please make sure that the source file exists");

        File modelFolder = new File(args[0]).getAbsoluteFile();
        File templateFolder = new File(args[1]).getAbsoluteFile();
        File targetDirectory = new File(args[2]).getAbsoluteFile();
        File outputDirectory = new File(args[3]).getAbsoluteFile();
        String packageName = args[4];

        if(args.length >= 6)
            GLEX.changeGlobalVar("Communicator", args[5]);
        if(args.length >= 7)
            GLEX.changeGlobalVar("Environment", args[6]);

        IterablePath templatePath = IterablePath.from(templateFolder, TEMPLATE_EXTENSION);
        IterablePath targetPath = IterablePath.from(targetDirectory, TARGET_EXTENSION);
        SETUP.setAdditionalTemplatePaths(templatePath.getPaths().stream().map(Path::toFile).collect(Collectors.toList()));
        SETUP.setOutputDirectory(outputDirectory);

        CommandGlobalScope scope = createGlobalScope();

        List<ASTCommandArtifact> models = parseModels(modelFolder, scope);

        CommandCoCoChecker checker = CommandCoCos.getCheckerForAllCoCos();

        models.forEach(checker::checkAll);

        generateCommands(models, scope, targetPath);
        generateCommandBuilder(models, scope, packageName);
    }

    private static void generateCommandBuilder(List<ASTCommandArtifact> models, CommandGlobalScope scope, String packageName){
        CommandBuilderGenerator.generate(models, GENERATOR, packageName);
    }


    private static void generateCommands(Collection<ASTCommandArtifact> models, CommandGlobalScope scope, IterablePath targetPath){
        models.forEach(model -> generateCommands(model, scope, targetPath));
    }

    private static void generateCommands(ASTCommandArtifact ast, CommandGlobalScope scope, IterablePath targetPath){
        CommandGenerator.generate(ast, GENERATOR, targetPath);
    }

    public static List<ASTCommandArtifact> parseModels(File modelFolder, CommandGlobalScope scope){
        return FileUtils.listFiles(modelFolder, new String[]{ CommandLanguage.COMMAND_FILE_ENDING}, false)
            .stream()
            .map(file -> CommandHelper.parse(scope, file.getPath()))
            .collect(Collectors.toList());
    }

    public static CommandGlobalScope createGlobalScope(){
        ModelPath path = new ModelPath(Paths.get(""));
        CommandLanguage language = new CommandLanguage();
        return new CommandGlobalScope(path, language);
    }
}
