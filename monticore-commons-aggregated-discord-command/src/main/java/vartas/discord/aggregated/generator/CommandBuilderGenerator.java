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
import vartas.discord.command._ast.ASTCommandArtifact;

import java.nio.file.Path;
import java.util.List;

public class CommandBuilderGenerator {
    protected CommandBuilderGenerator(){}

    public static void generate(List<ASTCommandArtifact> asts, GeneratorEngine generator, String packageName){
        Path path = CommandGeneratorHelper.getQualifiedPath(packageName, "CommandBuilder");

        generator.generateNoA("CommandBuilder", path, asts, packageName);
    }
}