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

package vartas.monticore.cd2java.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import vartas.monticore.cd2code.CDGeneratorHelper;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Applies the Decorator pattern for all attributes of a {@link ASTCDClass CDClass} that are not instances of
 * {@link Optional}. Since this class is not serializable, the methods access the value instead.
 * This decorator provides getter and setter methods.
 */
public class CoreDecorator extends AbstractMethodDecorator<ASTCDAttribute>{
    /**
     * This method returns the internal attribute.
     */
    private static final String CORE_GET = "<<Nonnull>> public %2$s get%1$s();";
    /**
     * This method replaces the internal attribute with a new instance.
     */
    private static final String CORE_SET = "public void set%s(%s t);";

    public CoreDecorator(@Nonnull GlobalExtensionManagement glex) {
        super(glex);
        TEMPLATES.put(CORE_GET,"decorator.core.Get");
        TEMPLATES.put(CORE_SET,"decorator.core.Set");
    }

    @Override
    @Nonnull
    public LinkedHashMap<String, String> getMethodSignatures(@Nonnull ASTCDAttribute cdAttribute) {
        LinkedHashMap<String, String> signatures = new LinkedHashMap<>();

        //Type
        String typeName = CDGeneratorHelper.prettyprint(cdAttribute.getMCType());

        //T get()
        signatures.put(CORE_GET, String.format
                (
                        CORE_GET,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeName
                )
        );
        //void set(T t)
        signatures.put(CORE_SET, String.format
                (
                        CORE_SET,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeName
                )
        );
        return signatures;
    }
}
