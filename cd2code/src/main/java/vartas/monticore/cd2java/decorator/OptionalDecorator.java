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
 * Applies the Decorator pattern for all attributes of a {@link ASTCDClass CDClass} that are instances
 * of {@link Optional}. Since that class is not serializable, we will only store the value. {@link Optional#empty()}
 * is represented by {@code null}.
 */
public class OptionalDecorator extends AbstractMethodDecorator<ASTCDAttribute>{
    /**
     * This method returns the internal attribute.
     */
    @Nonnull
    private static final String GET = "<<Nonnull>> public Optional<%2$s> get%1$s();";
    /**
     * This method replaces the internal attribute with a new instance.
     */
    @Nonnull
    private static final String SET_OPTIONAL = "public void set%s(Optional<%s> e);";
    /**
     * This method replaces the internal attribute with a new instance.
     * Can't be null.
     */
    @Nonnull
    private static final String SET = "public void set%s(%s e);";

    public OptionalDecorator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
        TEMPLATES.put(GET, "decorator.optional.Get");
        TEMPLATES.put(SET_OPTIONAL, "decorator.optional.SetOptional");
        TEMPLATES.put(SET, "decorator.optional.Set");
    }

    @Nonnull
    @Override
    public LinkedHashMap<String, String> getMethodSignatures(@Nonnull ASTCDAttribute cdAttribute){
        LinkedHashMap<String, String> signatures = new LinkedHashMap<>();

        //Element
        String typeArgumentName = CDGeneratorHelper.getMCTypeArgumentName(cdAttribute, 0);

        //Optional<E> get()
        signatures.put(GET, String.format
                (
                        GET,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //void set(Optional<E> e)
        signatures.put(SET_OPTIONAL, String.format
                (
                        SET_OPTIONAL,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //void set(E e)
        signatures.put(SET, String.format
                (
                        SET,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        return signatures;
    }
}
