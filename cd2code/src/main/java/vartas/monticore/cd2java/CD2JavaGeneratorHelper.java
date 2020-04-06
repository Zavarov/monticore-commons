/*
 * Copyright (c) 2020 Zavarov
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

package vartas.monticore.cd2java;

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.io.paths.IterablePath;
import vartas.monticore.cd2code.CDGeneratorHelper;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CD2JavaGeneratorHelper extends CDGeneratorHelper {
    public static Path SOURCES_DIRECTORY = Paths.get("src","main", "java");
    public static IterablePath SOURCES = IterablePath.from(SOURCES_DIRECTORY.toFile(), "java");

    public CD2JavaGeneratorHelper(@Nonnull ASTCDCompilationUnit ast) {
        super(ast);
    }
}
