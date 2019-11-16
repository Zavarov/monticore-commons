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
import de.monticore.generating.GeneratorEngine;
import de.monticore.generating.GeneratorSetup;
import vartas.discord.command._ast.ASTCommandArtifact;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class CommandBuilderGenerator {
    public static void generate(ASTCommandArtifact ast, GeneratorEngine generator, GeneratorSetup setup, Path sourcePath, String packageName){
        generate(Collections.singletonList(ast), generator, setup, sourcePath, packageName);
    }
    public static void generate(List<ASTCommandArtifact> asts, GeneratorEngine generator, GeneratorSetup setup, Path sourcePath, String packageName){
        List<String> packageList = Arrays.asList(packageName.split("\\."));

        String fileName =
                Joiner.on(FileSystems.getDefault().getSeparator()).join(packageList) +
                        FileSystems.getDefault().getSeparator() +
                        "MontiCoreCommandBuilder"+
                        "."+setup.getDefaultFileExtension();

        Path targetPath = sourcePath.resolve(fileName);

        generator.generateNoA("builder.CommandBuilder", targetPath.toAbsolutePath(), asts, packageName);
    }
}
