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
import java.util.List;

public abstract class CommandCreatorGenerator {
    public static void generate(List<ASTCommandArtifact> asts, GeneratorEngine generator, GeneratorSetup setup, Path sourcePath){
        for(ASTCommandArtifact ast : asts)
            generate(ast, generator, setup, sourcePath);
    }

    public static void generate(ASTCommandArtifact ast, GeneratorEngine generator, GeneratorSetup setup, Path sourcePath){
        String packageName = Joiner.on(".").join(ast.getPackageList());

        String fileName =
                Joiner.on(FileSystems.getDefault().getSeparator()).join(ast.getPackageList()) +
                FileSystems.getDefault().getSeparator() +
                "MontiCoreCommandCreator"+
                "."+setup.getDefaultFileExtension();

        Path targetPath = sourcePath.resolve(fileName);

        generator.generateNoA("creator.CommandCreator", targetPath.toAbsolutePath(), ast, packageName);
    }
}
