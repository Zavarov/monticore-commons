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

package vartas.monticore.cd4analysis;

import de.monticore.io.paths.IterablePath;

import javax.annotation.Nonnull;
import java.nio.file.Paths;
import java.util.Collections;

public class CDGeneratorHelper {
    //----------------------------------------------------------------------------------------------------------------//
    //
    //        Core Templates
    //
    //----------------------------------------------------------------------------------------------------------------//
    @Nonnull
    public static final String CLASS_TEMPLATE = "core.Class";
    @Nonnull
    public static final String ENUM_TEMPLATE = "core.Enum";
    @Nonnull
    public static final String INTERFACE_TEMPLATE = "core.Interface";
    //----------------------------------------------------------------------------------------------------------------//
    //
    //        Hook Points
    //
    //----------------------------------------------------------------------------------------------------------------//
    @Nonnull
    public static final String ANNOTATION_HOOK = "hook.Annotation";
    @Nonnull
    public static final String CONSTRUCTOR_HOOK = "hook.Constructor";
    @Nonnull
    public static final String PACKAGE_HOOK = "hook.Package";
    @Nonnull
    public static final String IMPORT_HOOK = "hook.Import";
    @Nonnull
    public static final String METHOD_HOOK = "hook.Method";
    @Nonnull
    public static final String VALUE_HOOK = "hook.Value";
    @Nonnull
    public static final String ATTRIBUTE_HOOK = "hook.Attribute";
    //----------------------------------------------------------------------------------------------------------------//
    //
    //        Modules
    //
    //----------------------------------------------------------------------------------------------------------------//
    @Nonnull
    public static final String FACTORY_MODULE = "factory";
    @Nonnull
    public static final String VISITOR_MODULE = "visitor";
    @Nonnull
    public static final String DECORATOR_MODULE = "decorator";
    @Nonnull
    public static final String INITIALIZER_MODULE = "initializer";
    //----------------------------------------------------------------------------------------------------------------//
    //
    //        Generator Parameters
    //
    //----------------------------------------------------------------------------------------------------------------//
    @Nonnull
    public static final String CONTAINER_LABEL = "container";
    @Nonnull
    public static final String HANDWRITTEN_FILE_POSTFIX = "TOP";
    @Nonnull
    public static final String DEFAULT_FILE_EXTENSION = "java";
    @Nonnull
    public static final IterablePath SOURCES_PATH = IterablePath.fromPaths(
            Collections.singletonList(Paths.get("src","main", "java")),
            "java"
    );

}
