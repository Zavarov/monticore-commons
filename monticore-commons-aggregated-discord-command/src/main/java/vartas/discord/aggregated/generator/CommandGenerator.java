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

import de.monticore.codegen.GeneratorHelper;
import de.monticore.generating.GeneratorEngine;
import de.monticore.io.paths.IterablePath;
import vartas.discord.command._ast.ASTCommand;
import vartas.discord.command._ast.ASTCommandArtifact;

import java.nio.file.Path;

public abstract class CommandGenerator {
    private CommandGenerator(){}

    private static GeneratorEngine generator;
    private static IterablePath targetPath;
    private static String packageName;

    public static void generate(ASTCommandArtifact ast, GeneratorEngine generator, IterablePath targetPath){
        CommandGenerator.packageName = CommandGeneratorHelper.getPackage(ast);
        CommandGenerator.generator = generator;
        CommandGenerator.targetPath = targetPath;

        ast.getCommandList().forEach(CommandGenerator::generate);
        ast.getCommandList().forEach(CommandGenerator::generateAbstract);
    }

    public static void generate(ASTCommand ast){
        String className = ast.getCommandSymbol().getClassName();
        String abstractClassName = "Abstract"+ast.getCommandSymbol().getClassName();

        boolean fileExists = GeneratorHelper.existsHandwrittenClass(className, packageName, targetPath);
        String fileName = GeneratorHelper.getSimpleTypeNameToGenerate(className, packageName, targetPath);
        Path path = CommandGeneratorHelper.getQualifiedPath(packageName, fileName);
        String abstractFileName = GeneratorHelper.getSimpleTypeNameToGenerate(abstractClassName, packageName, targetPath);

        generator.generate("command.Command", path, ast, packageName, fileExists, fileName, abstractFileName);
    }

    public static void generateAbstract(ASTCommand ast){
        String className = "Abstract" + ast.getCommandSymbol().getClassName();

        boolean fileExists = GeneratorHelper.existsHandwrittenClass(className, packageName, targetPath);
        String fileName = GeneratorHelper.getSimpleTypeNameToGenerate(className, packageName, targetPath);
        Path path = CommandGeneratorHelper.getQualifiedPath(packageName, fileName);

        generator.generate("command.AbstractCommand", path, ast, packageName, fileExists, fileName);
    }
}
