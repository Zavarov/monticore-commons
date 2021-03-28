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

package vartas.monticore.cd4code.decorator;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4analysis._symboltable.Stereotype;
import de.monticore.cd.cd4code._visitor.CD4CodeVisitor;
import de.monticore.cd.facade.CDMethodFacade;
import de.monticore.cd.facade.CDModifier;
import de.monticore.cd.facade.CDParameterFacade;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.utils.Names;
import de.se_rwth.commons.Joiners;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.monticore.cd4code.CDGeneratorHelper;
import vartas.monticore.cd4code.CDMethodCalculator;
import vartas.monticore.cd4code.CDMethodComparator;
import vartas.monticore.cd4code.preprocessor.CDProcess;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The decorator
 */
public class DecoratorCreator extends AbstractCreator<ASTCDDefinition, ASTCDDefinition> implements CD4CodeVisitor {
    private final Multimap<ASTCDType, ASTCDMethod> calculatedMethods = HashMultimap.create();
    /**
     * This class' Logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * Creates a fresh decorator instance.
     * @param glex the {@link GlobalExtensionManagement} binding the templates to the method hook points.
     */
    private DecoratorCreator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
    }

    public static ASTCDDefinition create(ASTCDDefinition node, GlobalExtensionManagement glex){
        return new DecoratorCreator(glex).decorate(node);
    }

    @Override
    public ASTCDDefinition decorate(ASTCDDefinition node) {
        //Calculate methods
        node.forEachCDEnums(type -> type.accept(getRealThis()));
        node.forEachCDClasss(type -> type.accept(getRealThis()));
        node.forEachCDInterfaces(type -> type.accept(getRealThis()));

        //Add methods. Has to be done separately, in case types reference each other.
        //In that case, they would include the decorated methods in the calculation.
        node.forEachCDEnums(type -> type.addAllCDMethods(calculatedMethods.get(type)));
        node.forEachCDClasss(type -> type.addAllCDMethods(calculatedMethods.get(type)));
        node.forEachCDInterfaces(type -> type.addAllCDMethods(calculatedMethods.get(type)));

        return node;
    }

    @Override
    public void handle(ASTCDClass node){
        log.debug("Decorating {}.", node.getName());
        CDTypeDecorator decorator = new CDTypeDecorator(glex);
        node.accept(decorator);
        calculatedMethods.putAll(node, decorator.methods);
    }
    @Override
    public void handle(ASTCDInterface node){
        log.debug("Decorating {}.", node.getName());
        CDTypeDecorator decorator = new CDTypeDecorator(glex);
        node.accept(decorator);
        calculatedMethods.putAll(node, decorator.methods);
    }
    @Override
    public void handle(ASTCDEnum node){
        log.debug("Decorating {}.", node.getName());
        CDTypeDecorator decorator = new CDTypeDecorator(glex);
        node.accept(decorator);
        calculatedMethods.putAll(node, decorator.methods);
    }

    private static class CDTypeDecorator extends CDProcess{
        /**
         * This class' Logger.
         */
        private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
        /**
         * Contains all newly generated methods for all attributes.
         */
        private final Set<ASTCDMethod> methods = new TreeSet<>(new CDMethodComparator());
        private final CDMethodCalculator methodCalculator = new CDMethodCalculator();
        private final List<ASTMCQualifiedType> qualifiedTypes = new ArrayList<>();

        public CDTypeDecorator(GlobalExtensionManagement glex){
            super(glex);
        }

        /**
         * Clears the qualified types of previously encountered attributes.
         * @param node The currently processed {@link ASTCDAttribute}.
         */
        @Override
        public void visit(ASTCDAttribute node){
            qualifiedTypes.clear();
        }

        /**
         * Counts one of the qualified generic types of the current {@link ASTCDAttribute}.
         * @param node One of the qualified types of the current {@link ASTCDAttribute}.
         */
        @Override
        public void visit(ASTMCQualifiedType node){
            qualifiedTypes.add(node);
        }

        /**
         * Generates delegator methods for all functions that are accessible via the attribute. In order
         * to avoid any collisions, the name of the attribute is attached to the newly generated method.<br>
         * e.g. <code>value.toString()</code> will result in a method called <code>toStringValue()</code>.
         * @param node One of the attributes in the current {@link ASTCDType}.
         */
        @Override
        public void endVisit(ASTCDAttribute node){
            log.debug("Decorating {}", node.getName());
            CDAttributeDecorator decorator = new CDAttributeDecorator(glex, node);

            if(!CDGeneratorHelper.isPrimitive(node))
                methodCalculator.apply(node.getSymbol().getType().lazyLoadDelegate().getAstNode(), qualifiedTypes).forEach(method -> method.accept(decorator));

            //Default getter and setter have priority
            methods.add(buildGetter(node));
            methods.add(buildSetter(node));
            methods.addAll(decorator.methods);
        }

        /**
         * Constructs the generic getter method.
         * @param ast one {@link ASTCDAttribute} in an {@link ASTCDType}.
         * @return the getter method for the corresponding {@link ASTCDAttribute}.
         */
        private ASTCDMethod buildGetter(ASTCDAttribute ast){
            //Create method
            ASTCDMethod cdMethod = CDMethodFacade.getInstance().createMethod(
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
            ASTCDMethod cdMethod = CDMethodFacade.getInstance().createMethod(
                    CDModifier.PUBLIC,
                    "set"+StringUtils.capitalize(ast.getName()),
                    CDParameterFacade.getInstance().createParameter(ast)
            );
            //Bind to template
            String templateName = Names.getQualifiedName(CDGeneratorHelper.DECORATOR_MODULE, "Set");
            glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint(templateName, ast, cdMethod));

            return cdMethod;
        }
    }

    /**
     * This class is used to generate all methods associated with an {@link ASTCDAttribute}.
     */
    private static class CDAttributeDecorator extends CDProcess {
        private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
        private final Set<ASTCDMethod> methods = new TreeSet<>(new CDMethodComparator());
        private final ASTCDAttribute attribute;

        public CDAttributeDecorator(GlobalExtensionManagement glex, ASTCDAttribute attribute){
            super(glex);
            this.attribute = attribute;
        }
        /**
         * Transform the method to fit into the class.
         * @param node one of the attributes' {@link ASTCDMethod}.
         */
        @Override
        public void visit(ASTCDMethod node){
            log.debug("Decorating {}.", node.getName());
            node = node.deepClone();
            renameMethod(node);
            makePublic(node);
            setTemplate(node);
            methods.add(node);
        }

        /**
         * Change the attributes' method to an unique name.
         * @param node one of the generated {@link ASTCDMethod}.
         */
        private void renameMethod(ASTCDMethod node){
            //e.g:
            //String content;
            //content.size() -> sizeContent()
            node.setName(node.getName() + StringUtils.capitalize(attribute.getName()));
        }

        /**
         * Makes the method public. This is required when decorating interfaces, where the methods are implicitly
         * public, even if no modifier has been specified.
         * @param node one of the generated {@link ASTCDMethod}.
         */
        private void makePublic(ASTCDMethod node){
            node.setModifier(CDModifier.PUBLIC.build());
        }

        /**
         * Binds the {@link ASTCDMethod} to its corresponding template.
         * @param ast one of the generated {@link ASTCDMethod}.
         */
        private void setTemplate(ASTCDMethod ast){
            //Default template for all methods
            String templateName = Joiners.DOT.join(CDGeneratorHelper.DECORATOR_MODULE, "Decorator");

            //Some methods (especially hand-written ones may use a custom template)
            if(ast.isPresentSymbol()){
                Stereotype stereotype = ast.getSymbol().getStereotype(CDGeneratorHelper.TEMPLATE_LABEL);
                if(stereotype != null){
                    templateName = stereotype.getValue();
                }
            }

            glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, ast, new TemplateHookPoint(templateName, attribute, ast));
        }
    }
}
