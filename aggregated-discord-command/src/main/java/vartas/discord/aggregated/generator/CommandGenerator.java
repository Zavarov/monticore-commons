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
import vartas.discord.command._ast.ASTCommand;
import vartas.discord.command._ast.ASTCommandArtifact;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class CommandGenerator {
    public static void generate(List<ASTCommandArtifact> artifacts, GeneratorEngine generator, GeneratorSetup setup, Path targetPath, Path sourcePath){
        for(ASTCommandArtifact artifact : artifacts)
            generate(artifact, generator, setup, targetPath, sourcePath);
    }

    public static void generate(ASTCommandArtifact artifact, GeneratorEngine generator, GeneratorSetup setup, Path targetPath, Path sourcePath){
        for(ASTCommand command : artifact.getCommandList())
            generate(artifact, command, generator, setup, targetPath, sourcePath);
    }

    private static void generate(ASTCommandArtifact artifact, ASTCommand command, GeneratorEngine generator, GeneratorSetup setup, Path targetPath, Path sourcePath){
        //The relative path to the package directory
        Path packagePath = CommandGeneratorHelper.getPackagePath(artifact);
        //The package name is Java format (i.e X.Y.Z instead of X/Y/Z)
        String packageName = CommandGeneratorHelper.getPackageName(artifact);
        //The name of the command in the package
        String className = command.getSpannedScope().getLocalClassAttributeSymbols().get(0).getName();
        //The path to the handwritten file
        Path sourceFilePath = sourcePath.resolve(packagePath).resolve(className + "." + setup.getDefaultFileExtension());
        //If a handwritten class exists, apply the TOP mechanism
        boolean fileExists = Files.exists(sourceFilePath);
        if(fileExists)
            className += "TOP";
        //The path to the generated file
        Path targetFilePath = targetPath.resolve(packagePath).resolve(className + "." + setup.getDefaultFileExtension());

        generator.generate("command.Command", targetFilePath.toAbsolutePath(), command, packageName, fileExists);
    }
}
