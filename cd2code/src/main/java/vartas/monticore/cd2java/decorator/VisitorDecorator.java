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

import de.monticore.cd.cd4analysis._ast.ASTCDInterface;
import de.monticore.generating.templateengine.GlobalExtensionManagement;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;

/**
 * Applies the Decorator pattern for a single visitor {@link ASTCDInterface CDInterface}.
 */
public class VisitorDecorator extends AbstractMethodDecorator<ASTCDInterface>{
    /**
     * This method applies the visitor pattern.
     */
    @Nonnull
    private static final String VISITOR_ACCEPT = "public void accept(" +
            "%s visitor" +
    ");";

    public VisitorDecorator(@Nonnull GlobalExtensionManagement glex) {
        super(glex);
        TEMPLATES.put(VISITOR_ACCEPT, "decorator.core.Accept");
    }

    @Nonnull
    @Override
    public LinkedHashMap<String, String> getMethodSignatures(@Nonnull ASTCDInterface cdVisitor) {
        LinkedHashMap<String, String> signatures = new LinkedHashMap<>();

        signatures.put(VISITOR_ACCEPT, String.format
                (
                        VISITOR_ACCEPT,
                        cdVisitor.getName()
                )
        );
        return signatures;
    }
}
