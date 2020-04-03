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

package vartas.monticore.cd2code.creator;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types.mccollectiontypes._ast.ASTMCListType;
import de.monticore.types.mccollectiontypes._ast.ASTMCMapType;
import de.monticore.types.mccollectiontypes._ast.ASTMCOptionalType;
import de.monticore.types.mccollectiontypes._ast.ASTMCSetType;
import org.apache.commons.lang3.StringUtils;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code.DecoratorHelper;
import vartas.monticore.cd2code._visitor.CD2CodeInheritanceVisitor;
import vartas.monticore.cd2code.prettyprint.CD2CodePrettyPrinter;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._ast.ASTMCCacheType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class VisitorCreator extends AbstractCreator<ASTCDDefinition, ASTCDInterface> {
    public static final String VISIT = "void visit(%s %s);";
    public static final String END_VISIT = "void endVisit(%s %s);";
    public static final String TRAVERSE = "void traverse(%s %s);";
    public static final String HANDLE = "void handle(%s %s);";

    @Nonnull
    private final CDDefinitionSymbol cdDefinitionSymbol;

    public VisitorCreator(@Nonnull GlobalExtensionManagement glex, @Nonnull CDDefinitionSymbol cdDefinitionSymbol){
        super(glex);
        this.cdDefinitionSymbol = cdDefinitionSymbol;
    }

    public static ASTCDInterface create(ASTCDDefinition cdDefinition, GlobalExtensionManagement glex){
        return new VisitorCreator(glex, cdDefinition.getSymbol()).decorate(cdDefinition);
    }

    @Override
    public ASTCDInterface decorate(ASTCDDefinition cdDefinition) {
        ASTCDInterface cdInterface = CD4AnalysisMill.cDInterfaceBuilder()
                .setName(cdDefinition.getName()+"Visitor")
                .build();

        cdDefinition.streamCDClasss().map(this::createMethods).forEach(cdInterface::addAllCDMethods);

        return cdInterface;
    }

    protected List<ASTCDMethod> createMethods(ASTCDClass cdClass){
        return Arrays.asList(
                createHandle(cdClass),
                createVisit(cdClass),
                createTraverse(cdClass),
                createEndVisit(cdClass)
        );
    }

    protected ASTCDMethod createVisit(ASTCDClass cdClass){
        String className = cdClass.getName();
        String varName = StringUtils.uncapitalize(className);
        String signature = String.format(VISIT, className, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_TEMPLATE, cdMethod, new TemplateHookPoint("visitor.Visit", cdClass));
        return cdMethod;
    }

    protected ASTCDMethod createEndVisit(ASTCDClass cdClass){
        String className = cdClass.getName();
        String varName = StringUtils.uncapitalize(className);
        String signature = String.format(END_VISIT, className, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_TEMPLATE, cdMethod, new TemplateHookPoint("visitor.EndVisit", cdClass));
        return cdMethod;
    }

    protected ASTCDMethod createTraverse(ASTCDClass cdClass){
        String className = cdClass.getName();
        String varName = StringUtils.uncapitalize(className);
        String signature = String.format(TRAVERSE, className, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_TEMPLATE, cdMethod, new TemplateHookPoint("visitor.Traverse", cdClass));

        //Link the templates for the individual attributes
        for(ASTCDAttribute cdAttribute : cdClass.getCDAttributeList())
            cdAttribute.accept(new TraverseVisitor(cdClass, cdAttribute));

        return cdMethod;
    }

    private ASTCDMethod createHandle(ASTCDClass cdClass){
        String className = cdClass.getName();
        String varName = StringUtils.uncapitalize(className);
        String signature = String.format(HANDLE, className, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_TEMPLATE, cdMethod, new TemplateHookPoint("visitor.Handle", cdMethod));
        return cdMethod;
    }

    private class TraverseVisitor implements CD2CodeInheritanceVisitor {
        private final ASTCDAttribute cdAttribute;
        private final ASTCDClass cdClass;

        private TraverseVisitor(ASTCDClass cdClass, ASTCDAttribute cdAttribute){
            this.cdClass = cdClass;
            this.cdAttribute = cdAttribute;
        }

        private boolean isPresentGenericArgument(int index){
            String mcElementTypeName = DecoratorHelper.getMCTypeArgumentName(cdAttribute, index);
            return cdDefinitionSymbol.getType(mcElementTypeName).isPresent();
        }

        private boolean isPresent(){
            String mcElementTypeName = new CD2CodePrettyPrinter().prettyprint(cdAttribute.getMCType());
            return cdDefinitionSymbol.getType(mcElementTypeName).isPresent();
        }

        @Override
        public void handle(ASTMCListType ast){
            if(isPresentGenericArgument(0))
                replaceTemplate(
                        CDGeneratorHelper.ATTRIBUTE_TEMPLATE,
                        cdAttribute,
                        new TemplateHookPoint("visitor.traverse.Collection", cdClass, cdAttribute)
                );
        }
        @Override
        public void handle(ASTMCSetType ast){
            if(isPresentGenericArgument(0))
                replaceTemplate(
                        CDGeneratorHelper.ATTRIBUTE_TEMPLATE,
                        cdAttribute,
                        new TemplateHookPoint("visitor.traverse.Collection", cdClass, cdAttribute)
                );
        }
        @Override
        public void handle(ASTMCOptionalType ast){
            if(isPresentGenericArgument(0))
                replaceTemplate(
                        CDGeneratorHelper.ATTRIBUTE_TEMPLATE,
                        cdAttribute,
                        new TemplateHookPoint("visitor.traverse.Optional", cdClass, cdAttribute)
                );
        }
        @Override
        public void handle(ASTMCMapType ast){
            boolean iterateKeys = isPresentGenericArgument(0);
            boolean iterateValues = isPresentGenericArgument(1);
            if(iterateKeys || iterateValues)
                replaceTemplate(
                        CDGeneratorHelper.ATTRIBUTE_TEMPLATE,
                        cdAttribute,
                        new TemplateHookPoint("visitor.traverse.Map", cdClass, cdAttribute, iterateKeys, iterateValues)
                );
        }
        @Override
        public void handle(ASTMCCacheType ast){
            boolean iterateKeys = isPresentGenericArgument(0);
            boolean iterateValues = isPresentGenericArgument(1);
            if(iterateKeys || iterateValues)
                replaceTemplate(
                        CDGeneratorHelper.ATTRIBUTE_TEMPLATE,
                        cdAttribute,
                        new TemplateHookPoint("visitor.traverse.Cache", cdClass, cdAttribute, iterateKeys, iterateValues)
                );
        }
        @Override
        public void visit(ASTMCObjectType ast){
            if(isPresent())
                replaceTemplate(
                        CDGeneratorHelper.ATTRIBUTE_TEMPLATE,
                        cdAttribute,
                        new TemplateHookPoint("visitor.traverse.Singleton", cdClass, cdAttribute)
                );
        }
    }
}
