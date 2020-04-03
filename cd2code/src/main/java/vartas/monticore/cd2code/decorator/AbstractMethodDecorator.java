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

package vartas.monticore.cd2code.decorator;

import com.google.common.collect.Maps;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import vartas.monticore.cd2code.CDGeneratorHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Nonnull
public abstract class AbstractMethodDecorator<T> extends AbstractCreator<T, Collection<ASTCDMethod>> {
    /**
     * This map associates all methods with their respective template.
     * In this {@link Map}, the methods are represented by an unique identifier. This is usually the signature template,
     * but can also have any other representation.
     */
    @Nonnull
    protected final Map<String, String> TEMPLATES = new HashMap<>();

    /**
     * Creates an empty creator.<br>
     * All classes extending this creator should specify the templates they use in their respective constructor.
     * @param glex the global extension management of the generator.
     */
    public AbstractMethodDecorator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
    }

    /**
     * Creates methods for all signatures specified in {@link #TEMPLATES} and registers
     * their templates in the {@link GlobalExtensionManagement}.
     * @param cdEntity the reference node for the methods.
     * @return All created methods.
     */
    @Override
    public Collection<ASTCDMethod> decorate(@Nonnull T cdEntity) {
        Map<String, ASTCDMethod> methods = getCDMethods(cdEntity);

        methods.forEach((signature, cdMethod) -> replaceTemplate
                (
                    CDGeneratorHelper.METHOD_TEMPLATE,
                    cdMethod,
                    new TemplateHookPoint(TEMPLATES.get(signature), cdEntity, cdMethod)
                )
        );

        return methods.values();
    }

    /**
     * Computes the methods for the associated entity.
     * @param cdEntity the reference node for the signatures.
     * @return a {@link Map} associating each method identifier with its {@link ASTCDMethod CDMethod}.
     */
    @Nonnull
    private LinkedHashMap<String, ASTCDMethod> getCDMethods(@Nonnull T cdEntity){
        LinkedHashMap<String, String> signatures = getMethodSignatures(cdEntity);
        return new LinkedHashMap<>(Maps.transformValues(signatures,getCDMethodFacade()::createMethodByDefinition));
    }

    /**
     * Computes the method signatures for the associated entity.
     * @param cdEntity the reference node for the signatures.
     * @return a {@link Map} associating each method identifier with its signature.
     */
    @Nonnull
    public abstract LinkedHashMap<String, String> getMethodSignatures(@Nonnull T cdEntity);
}
