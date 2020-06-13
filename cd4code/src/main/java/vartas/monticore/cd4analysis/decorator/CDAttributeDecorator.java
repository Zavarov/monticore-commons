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

package vartas.monticore.cd4analysis.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDField;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.cd.facade.CDModifier;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import de.monticore.utils.Names;
import de.se_rwth.commons.Joiners;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.monticore.cd4analysis.CDGeneratorHelper;
import vartas.monticore.cd4analysis.CDMethodComparator;
import vartas.monticore.cd4analysis.calculator.CDMethodCalculator;

import java.util.*;

/**
 * This decorator generates decorator methods for all methods of its attributes,
 * in addition to basic getter and setter methods.
 * The available methods are provided via a class diagram.
 */
public class CDAttributeDecorator extends AbstractCreator<ASTCDAttribute, Set<ASTCDMethod>> implements CD4CodeInheritanceVisitor {
    /**
     * This class' Logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
    /**
     * Contains all methods that have been introduced by decoration the individual attributes..
     */
    private final Set<ASTCDMethod> methods = new TreeSet<>(new CDMethodComparator());
    /**
     * Contains the (possible) generic arguments of the individual attributes.
     */
    private final List<ASTMCTypeArgument> typeArguments = new ArrayList<>();
    /**
     * A helper class for loading the type associated with the attribute and retrieving its methods,
     * specified in the respective class diagram.
     */
    private final CDMethodCalculator cdMethodCalculator = new CDMethodCalculator();

    /**
     * Creates a fresh decorator instance.
     * @param glex the {@link GlobalExtensionManagement} binding the templates to the method hook points.
     */
    public CDAttributeDecorator(GlobalExtensionManagement glex){
        super(glex);
    }

    /**
     * Derives all methods for the specific attribute.
     * @param ast one {@link ASTCDAttribute} in an {@link ASTCDType}.
     * @return a {@link Set} containng all derived methods.
     */
    @Override
    public Set<ASTCDMethod> decorate(ASTCDAttribute ast) {
        log.debug("Visiting attribute {}.", ast.getName());

        methods.clear();
        ast.accept(getRealThis());
        return methods;
    }

    /**
     * Whenever a new attribute is visited, the buffer containing all generic argument types has to be cleared.
     * This buffer is used to derive the methods of the embedded types.
     * @param ast one {@link ASTCDAttribute} in an {@link ASTCDType}.
     */
    @Override
    public void visit(ASTCDAttribute ast){
        typeArguments.clear();
    }

    /**
     * Visits the generic arguments of an {@link ASTCDAttribute}.
     * @param ast one of the generic arguments of an {@link ASTCDAttribute}.
     */
    @Override
    public void visit(ASTMCTypeArgument ast){
        typeArguments.add(ast);
    }

    /**
     * After all generic arguments have been extracted, calculate the methods.<br>
     * It will use the methods of the argument type, as well as generic getter and setter.
     * In case there are collisions, the more specific method is used.
     * @param ast one {@link ASTCDAttribute} in an {@link ASTCDType}.
     */
    @Override
    public void endVisit(ASTCDAttribute ast){
        log.debug("Visiting attribute {}.", ast.getName());

        //Fails if there is no class diagram associated with the type
        Optional<CDTypeSymbol> typeSymbolOptional = ast.getSymbol().getType().loadSymbol();
        if(typeSymbolOptional.isPresent()){
            CDTypeSymbol typeSymbol = typeSymbolOptional.get();
            log.debug("Type of attribute {} successfully loaded as {}.", ast.getName(), typeSymbol.getName());

            loadMethods(ast, typeSymbol.getAstNode());
            for(CDTypeSymbol superType : typeSymbol.getSuperTypesTransitive())
                loadMethods(ast, superType.getAstNode());
        }
        methods.add(buildGetter(ast));
        methods.add(buildSetter(ast));
    }

    /**
     * Constructs the generic getter method.
     * @param ast one {@link ASTCDAttribute} in an {@link ASTCDType}.
     * @return the getter method for the corresponding {@link ASTCDAttribute}.
     */
    private ASTCDMethod buildGetter(ASTCDAttribute ast){
        //Create method
        ASTCDMethod cdMethod = getCDMethodFacade().createMethod(
                CDModifier.PUBLIC,
                ast.getMCType(),
                "get"+StringUtils.capitalize(ast.getName())
        );
        //Bind to template
        String templateName = Names.getQualifiedName(CDGeneratorHelper.DECORATOR_MODULE, "Get");
        glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint(templateName, ast, cdMethod));

        return cdMethod;
    }

    /**
     * Constructs the generic setter method.
     * @param ast one {@link ASTCDAttribute} in an {@link ASTCDType}.
     * @return the setter method for the corresponding {@link ASTCDAttribute}.
     */
    private ASTCDMethod buildSetter(ASTCDAttribute ast){
        //Create method
        ASTCDMethod cdMethod = getCDMethodFacade().createMethod(
                CDModifier.PUBLIC,
                "set"+StringUtils.capitalize(ast.getName()),
                getCDParameterFacade().createParameter(ast)
        );
        //Bind to template
        String templateName = Names.getQualifiedName(CDGeneratorHelper.DECORATOR_MODULE, "Set");
        glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint(templateName, ast, cdMethod));

        return cdMethod;
    }

    /**
     * Generates all methods for the corresponding {@link ASTCDAttribute}.
     * @param attribute one of the attribute in the {@link ASTCDType}.
     * @param type one of the types in the class diagram.
     */
    private void loadMethods(ASTCDAttribute attribute, ASTCDType type){
        CDAttributeVisitor cdAttributeVisitor = new CDAttributeVisitor(attribute, type);
        for(ASTCDMethod cdMethod : cdMethodCalculator.apply(type, typeArguments)){
            log.debug("Visiting method {} of type {}.", cdMethod.getName(), type.getName());
            //No symbol -> Not from class diagram -> Handwritten template
            if(cdMethod.isPresentSymbol() && cdMethod.getSymbol().isIsPublic())
                cdMethod.accept(cdAttributeVisitor);
        }
    }


    /**
     * This class is used to generate all methods associated with an {@link ASTCDAttribute}.
     */
    private class CDAttributeVisitor implements CD4CodeInheritanceVisitor {
        /**
         * The attribute name is used as an unique identifier when deriving the new method name.
         */
        private final String fieldName;
        /**
         * The type is used when deriving the template.
         */
        private final ASTCDType cdType;
        /**
         * The field is used as an argument for the template.
         */
        private final ASTCDField cdField;

        /**
         * Creates a new visitor instance.
         * @param cdField one of the fields in the {@link ASTCDType}.
         * @param cdType one of the types in the class diagram.
         */
        public CDAttributeVisitor(ASTCDField cdField, ASTCDType cdType){
            this.cdField = cdField;
            this.cdType = cdType;
            this.fieldName = StringUtils.capitalize(cdField.getName());
        }

        /**
         * Transform the method to fit into the class.
         * @param ast one of the attributes' {@link ASTCDMethod}.
         */
        @Override
        public void visit(ASTCDMethod ast){
            ast = ast.deepClone();
            bindToTemplate(ast);
            renameMethod(ast);
            makePublic(ast);
            methods.add(ast);
        }

        /**
         * Make all generated methods public.
         * @param ast one of the generated {@link ASTCDMethod}.
         */
        private void makePublic(ASTCDMethod ast){
            ast.setModifier(CDModifier.PUBLIC.build());
        }

        /**
         * Change the attributes' method to an unique name.
         * @param ast one of the generated {@link ASTCDMethod}.
         */
        private void renameMethod(ASTCDMethod ast){
            //e.g:
            //String content;
            //content.size() -> sizeContent()
            ast.setName(ast.getName() + fieldName);
        }

        /**
         * Binds the {@link ASTCDMethod} to its corresponding template.
         * @param ast one of the generated {@link ASTCDMethod}.
         */
        private void bindToTemplate(ASTCDMethod ast){
            String moduleName = CDGeneratorHelper.DECORATOR_MODULE;
            String packageName = cdType.getSymbol().getPackageName();
            String typeName = cdType.getName();
            String qualifiedName = Names.getQualifiedName(packageName, typeName);
            String methodName = StringUtils.capitalize(ast.getName());
            String templateName = Joiners.DOT.join(moduleName, qualifiedName, methodName);

            log.debug("Binding method {} of type {} to {}", ast.getName(), cdType.getName(), templateName);
            glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, ast, new TemplateHookPoint(templateName, cdField, ast));
        }
    }
}
