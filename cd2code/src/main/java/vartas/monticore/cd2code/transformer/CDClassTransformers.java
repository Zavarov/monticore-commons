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

import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDInterface;
import de.monticore.generating.templateengine.GlobalExtensionManagement;

public abstract class CDClassTransformers {
    public static ASTCDClass transform(ASTCDClass cdClass, ASTCDInterface cdVisitor, GlobalExtensionManagement glex){
        ASTCDClass transformedClass = cdClass.deepClone();

        CDClassTransformer transformer = new CDClassTransformer(glex, cdVisitor);
        transformer.decorate(cdClass, transformedClass);

        CDOptionalTransformer.apply(transformedClass, glex);
        CDInitializerTransformer.apply(transformedClass, glex);
        CDAnnotatorTransformer.apply(transformedClass, glex);

        return transformedClass;
    }
}
