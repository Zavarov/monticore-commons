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

import com.google.common.base.Joiner;
import de.monticore.codegen.GeneratorHelper;
import de.monticore.generating.GeneratorEngine;
import de.monticore.generating.GeneratorSetup;
import de.monticore.io.paths.IterablePath;
import vartas.discord.command._ast.ASTCommand;
import vartas.discord.command._ast.ASTCommandArtifact;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public abstract class CommandGenerator {
    public static void generate(List<ASTCommandArtifact> artifacts, GeneratorEngine generator, GeneratorSetup setup, Path sourcePath, Path referencePath){
        for(ASTCommandArtifact artifact : artifacts)
            generate(artifact, generator, setup, sourcePath, referencePath);
    }

    public static void generate(ASTCommandArtifact artifact, GeneratorEngine generator, GeneratorSetup setup, Path sourcePath, Path referencePath){
        String packageName = Joiner.on(".").join(artifact.getPackageList());

        for(ASTCommand command : artifact.getCommandList()) {
            String className = command.getSpannedScope().getLocalClassAttributeSymbols().get(0).getName();
            boolean fileExists = GeneratorHelper.existsHandwrittenClass(className, packageName, IterablePath.from(referencePath.toFile(), setup.getDefaultFileExtension()));

            if(fileExists)
                className += "TOP";

            String fileName =
                    Joiner.on(FileSystems.getDefault().getSeparator()).join(artifact.getPackageList()) +
                            FileSystems.getDefault().getSeparator() +
                            className +
                            "."+setup.getDefaultFileExtension();

            generate(command, generator, sourcePath, packageName, fileName, fileExists);
        }
    }

    private static void generate(ASTCommand command, GeneratorEngine generator, Path sourcePath, String packageName, String fileName, boolean fileExists){
        Path targetPath = sourcePath.resolve(fileName);
        generator.generate("command.Command", targetPath.toAbsolutePath(), command, packageName, fileExists);
    }
}
