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

package vartas.monticore.cd4code;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDParameter;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDFieldSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.codegen.mc2cd.TransformationHelper;
import de.monticore.io.paths.IterablePath;
import de.monticore.types.mcbasictypes._ast.ASTMCPrimitiveType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mcfullgenerictypes._visitor.MCFullGenericTypesVisitor;
import de.monticore.utils.Names;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CDGeneratorHelper {
    private final Path sourcesPath;

    public CDGeneratorHelper(){
        this(Paths.get("src", "main", "java"));
    }
    public CDGeneratorHelper(Path sourcesPath){
        this.sourcesPath = sourcesPath;
    }
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
    public static final String FACTORY_PACKAGE = "_" + FACTORY_MODULE;
    @Nonnull
    public static final String VISITOR_MODULE = "visitor";
    @Nonnull
    public static final String VISITOR_PACKAGE = "_" + VISITOR_MODULE;
    @Nonnull
    public static final String JSON_MODULE = "json";
    @Nonnull
    public static final String JSON_PACKAGE = "_" + JSON_MODULE;
    @Nonnull
    public static final String DECORATOR_MODULE = "decorator";
    @Nonnull
    public static final String INITIALIZER_MODULE = "initializer";
    //----------------------------------------------------------------------------------------------------------------//
    //
    //        Generator Parameters
    //
    //----------------------------------------------------------------------------------------------------------------//
    /**
     * When generating the JSON de-serializer, the attribute names are used keys for the JSON entries. In case the JSON
     * file has to be compatible with 3rd-party applications, it is possible to annotate the attributes with this label
     * to use a separate key.
     */
    @Nonnull
    public static final String JSON_KEY = "key";
    @Nonnull
    public static final String JSON_IGNORE = "ignore";
    @Nonnull
    public static final String CONTAINER_LABEL = "container";
    @Nonnull
    public static final String TEMPLATE_LABEL = "template";
    @Nonnull
    public static final String HANDWRITTEN_FILE_POSTFIX = "TOP";
    @Nonnull
    public static final String DEFAULT_FILE_EXTENSION = "java";

    public IterablePath getSourcesPath(){
        return IterablePath.fromPaths(Collections.singletonList(sourcesPath), DEFAULT_FILE_EXTENSION);
    }

    public boolean existsHandwrittenClass(String qualifiedName){
        return TransformationHelper.existsHandwrittenClass(getSourcesPath(), qualifiedName);
    }

    public boolean existsHandwrittenClass(CDTypeSymbol symbol){
        return existsHandwrittenClass(Names.getQualifiedName(symbol.getPackageName(), symbol.getName()));
    }

    public boolean existsHandwrittenClass(ASTCDType node){
        return existsHandwrittenClass(node.getSymbol());
    }

    public static boolean isPrimitive(ASTCDParameter node){
        return isPrimitive(node.getMCType());
    }

    public static boolean isPrimitive(ASTCDAttribute node){
        return isPrimitive(node.getMCType());
    }

    public static boolean isPrimitive(ASTMCType node){
        AtomicBoolean result = new AtomicBoolean(false);

        MCFullGenericTypesVisitor visitor = new MCFullGenericTypesVisitor() {
            @Override
            public void visit(ASTMCPrimitiveType node){
                result.set(true);
            }
        };

        node.accept(visitor);

        return result.get();
    }

    public static boolean isContainer(ASTCDType node){
        return node.isPresentSymbol() && isContainer(node.getSymbol());
    }

    public static boolean isContainer(ASTCDAttribute node){
        return !isPrimitive(node) && isContainer(node.getSymbol().getType().lazyLoadDelegate());
    }

    public static boolean isContainer(CDTypeSymbol symbol){
        return symbol.getStereotype(CONTAINER_LABEL).isPresent();
    }

    public static boolean inLocalScope(ASTCDType node, CDTypeSymbol symbol){
        return node.getEnclosingScope().getLocalCDTypeSymbols().contains(symbol);
    }

    public static boolean inLocalScope(ASTCDType node, CDFieldSymbol symbol){
        return inLocalScope(node, symbol.getType().lazyLoadDelegate());
    }

    public static boolean inLocalScope(ASTCDType node, ASTCDAttribute ast){
        return inLocalScope(node, ast.getSymbol());
    }

    public static Set<CDTypeSymbol> getSuperTypesTransitive(CDTypeSymbol symbol){
        Set<CDTypeSymbol> symbols = new HashSet<>();
        Deque<CDTypeSymbol> remaining = new LinkedList<>();

        remaining.add(symbol);
        while(!remaining.isEmpty()){
            symbol = remaining.remove();
            for(CDTypeSymbol supertype : symbol.getSuperTypes()){
                //Avoid recursive loops caused by self-references
                if(!symbols.contains(supertype)) {
                    symbols.add(supertype);
                    remaining.add(supertype);
                }
            }
        }

        return symbols;
    }

    public static Set<String> getImports(CDDefinitionSymbol symbol){
        //Add local imports
        Set<String> imports = new HashSet<>(symbol.getImports());

        //Add all inherited imports
        for(CDTypeSymbol type : symbol.getTypes())
            for(CDTypeSymbol supertype : getSuperTypesTransitive(type))
                imports.addAll((getImports(supertype)));

        return imports;
    }

    public static Set<String> getImports(CDTypeSymbol symbol){
        Set<String> imports = new HashSet<>();

        //CDType -> CDDefinition::SpannedScope -> CDArtifact::SpannedScope
        for(CDDefinitionSymbol definition : symbol.getEnclosingScope().getEnclosingScope().getLocalCDDefinitionSymbols())
            imports.addAll(definition.getImports());

        return imports;
    }
}
