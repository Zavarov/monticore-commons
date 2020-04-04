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

package vartas.monticore.cd2code.transformer;

import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.codegen.mc2cd.TransformationHelper;
import de.monticore.io.paths.IterablePath;

public class CDHandwrittenClassTransformer {
    public static String TOP_POSTFIX = "TOP";

    public static void apply(IterablePath path, ASTCDType ast){
        if(TransformationHelper.existsHandwrittenClass(path, ast.getName()))
            ast.setName(ast.getName() + TOP_POSTFIX);
    }
}
