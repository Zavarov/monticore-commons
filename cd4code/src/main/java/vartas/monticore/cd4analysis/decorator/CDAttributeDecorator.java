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
import vartas.monticore.cd4analysis.calculator.CDMethodCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * This decorator attempts to give access to all methods of a given {@link ASTCDAttribute}.<br>
 * The available methods are provided via a class diagram.
 */
public class CDAttributeDecorator extends AbstractCreator<ASTCDAttribute, List<ASTCDMethod>> implements CD4CodeInheritanceVisitor {
    /**
     * This class' Logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
    /**
     * Contains all methods that have been introduced by decoration the individual attributes..
     */
    private final List<ASTCDMethod> cdMethodList = new ArrayList<>();
    /**
     * Contains the (possible) generic arguments of the individual attributes.
     */
    private final List<ASTMCTypeArgument> typeArguments = new ArrayList<>();
    /**
     * A helper class for loading the type associated with the attribute and retrieving its methods,
     * specified in the respective class diagram.
     */
    private final CDMethodCalculator cdMethodCalculator = new CDMethodCalculator();

    public CDAttributeDecorator(GlobalExtensionManagement glex){
        super(glex);
    }

    @Override
    public List<ASTCDMethod> decorate(ASTCDAttribute ast) {
        log.debug("Visiting attribute {}.", ast.getName());

        cdMethodList.clear();
        ast.accept(getRealThis());
        return cdMethodList;
    }

    @Override
    public void visit(ASTCDAttribute ast){
        typeArguments.clear();
    }

    @Override
    public void visit(ASTMCTypeArgument ast){
        typeArguments.add(ast);
    }

    @Override
    public void endVisit(ASTCDAttribute ast){
        log.debug("Visiting attribute {}.", ast.getName());

        //Fails if there is no class diagram associated with the type
        ast.getSymbol().getType().loadSymbol().ifPresent(typeSymbol -> {
            log.debug("Type of attribute {} successfully loaded as {}.", ast.getName(), typeSymbol.getName());

            loadMethods(ast, typeSymbol.getAstNode());
            for(CDTypeSymbol superType : typeSymbol.getSuperTypesTransitive())
                loadMethods(ast, superType.getAstNode());
        });
    }

    private void loadMethods(ASTCDAttribute attribute, ASTCDType type){
        CDAttributeVisitor cdAttributeVisitor = new CDAttributeVisitor(attribute, type);
        for(ASTCDMethod cdMethod : cdMethodCalculator.apply(type, typeArguments)){
            log.debug("Visiting method {} of type {}.", cdMethod.getName(), type.getName());
            cdMethod.accept(cdAttributeVisitor);
        }
    }


    private class CDAttributeVisitor implements CD4CodeInheritanceVisitor {
        private final String fieldName;
        private final ASTCDType cdType;
        private final ASTCDField cdField;

        public CDAttributeVisitor(ASTCDField cdField, ASTCDType cdType){
            this.cdField = cdField;
            this.cdType = cdType;
            this.fieldName = StringUtils.capitalize(cdField.getName());
        }

        @Override
        public void visit(ASTCDMethod ast){
            ast = ast.deepClone();
            bindToTemplate(ast);
            renameMethod(ast);
            makePublic(ast);
            cdMethodList.add(ast);
        }

        private void makePublic(ASTCDMethod ast){
            ast.setModifier(CDModifier.PUBLIC.build());
        }

        private void renameMethod(ASTCDMethod ast){
            //e.g:
            //String content;
            //content.size() -> sizeContent()
            ast.setName(ast.getName() + fieldName);
        }

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
