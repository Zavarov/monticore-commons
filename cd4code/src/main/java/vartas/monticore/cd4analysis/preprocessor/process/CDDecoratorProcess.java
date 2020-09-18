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

package vartas.monticore.cd4analysis.preprocessor.process;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4code.CD4CodePrettyPrinterDelegator;
import de.monticore.cd.facade.CDMethodFacade;
import de.monticore.cd.facade.CDModifier;
import de.monticore.cd.facade.CDParameterFacade;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.utils.Names;
import de.se_rwth.commons.Joiners;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.monticore.cd4analysis.CDGeneratorHelper;
import vartas.monticore.cd4analysis.CDMethodCalculator;
import vartas.monticore.cd4analysis.CDMethodComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The decorator
 */
public class CDDecoratorProcess extends CDProcess {
    /**
     * This class' Logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * Creates a fresh decorator instance.
     * @param glex the {@link GlobalExtensionManagement} binding the templates to the method hook points.
     */
    public CDDecoratorProcess(GlobalExtensionManagement glex){
        super(glex);
    }

    @Override
    public void handle(ASTCDClass node){
        log.debug("Decorating {}.", node.getName());
        CDTypeDecorator decorator = new CDTypeDecorator(glex);
        node.accept(decorator);
        node.addAllCDMethods(decorator.methods);
    }
    @Override
    public void handle(ASTCDInterface node){
        log.debug("Decorating {}.", node.getName());
        CDTypeDecorator decorator = new CDTypeDecorator(glex);
        node.accept(decorator);
        node.addAllCDMethods(decorator.methods);
    }
    @Override
    public void handle(ASTCDEnum node){
        log.debug("Decorating {}.", node.getName());
        CDTypeDecorator decorator = new CDTypeDecorator(glex);
        node.accept(decorator);
        node.addAllCDMethods(decorator.methods);
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
         * @param node One of the qualified types of teh current {@link ASTCDAttribute}.
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

            node.getSymbol().getType().loadSymbol().map(CDTypeSymbol::getAstNode).ifPresent(type ->
                methodCalculator.apply(type, qualifiedTypes).forEach(method -> method.accept(decorator))
            );

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
         * Binds the {@link ASTCDMethod} to its corresponding template.
         * @param ast one of the generated {@link ASTCDMethod}.
         */
        private void setTemplate(ASTCDMethod ast){
            String templateName = Joiners.DOT.join(CDGeneratorHelper.DECORATOR_MODULE, "Decorator");
            glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, ast, new TemplateHookPoint(templateName, ast));
        }
    }
}