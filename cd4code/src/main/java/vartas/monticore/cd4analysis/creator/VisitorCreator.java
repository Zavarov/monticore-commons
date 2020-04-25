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

package vartas.monticore.cd4analysis.creator;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.cd.facade.CDModifier;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mccollectiontypes._ast.ASTMCGenericType;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import org.apache.commons.lang3.StringUtils;
import vartas.monticore.cd4analysis.CDGeneratorHelper;

import javax.annotation.Nonnull;
import java.util.*;

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
        //Ignore possible TOP classes;
        cdDefinition = cdDefinition.deepClone();

        ASTCDInterface cdInterface = CD4AnalysisMill.cDInterfaceBuilder()
                .setName(cdDefinition.getName()+"Visitor")
                .setModifier(CDModifier.PUBLIC.build())
                .build();

        cdDefinition.streamCDClasss().map(this::createMethods).forEach(cdInterface::addAllCDMethods);
        cdDefinition.streamCDEnums().map(this::createMethods).forEach(cdInterface::addAllCDMethods);
        cdDefinition.streamCDInterfaces().map(this::createMethods).forEach(cdInterface::addAllCDMethods);

        return cdInterface;
    }

    protected List<ASTCDMethod> createMethods(ASTCDType cdType){
        return Arrays.asList(
                createHandle(cdType),
                createVisit(cdType),
                createTraverse(cdType),
                createEndVisit(cdType)
        );
    }

    protected ASTCDMethod createVisit(ASTCDType cdType){
        String typeName = cdType.getName();
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(VISIT, typeName, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.Visit", cdType));
        return cdMethod;
    }

    protected ASTCDMethod createEndVisit(ASTCDType cdType){
        String typeName = cdType.getName();
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(END_VISIT, typeName, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.EndVisit", cdType));
        return cdMethod;
    }

    private ASTCDMethod createHandle(ASTCDType cdType){
        String className = cdType.getName();
        String varName = StringUtils.uncapitalize(className);
        String signature = String.format(HANDLE, className, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.Handle", cdMethod));
        return cdMethod;
    }

    protected ASTCDMethod createTraverse(ASTCDType cdType){
        CDAttributeVisitor visitor = new CDAttributeVisitor();
        String typeName = cdType.getName();
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(TRAVERSE, typeName, varName);

        //Link to the method body
        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.Traverse", cdType));

        //Link the individual attributes to their respective visitor template
        for(ASTCDAttribute cdAttribute : cdType.getCDAttributeList()){
            Optional<CDTypeSymbol> cdTypeSymbol = cdAttribute.getSymbol().getType().loadSymbol();
            //The accessor for containers has to be provided. (e.g. via iterator and whatnot)
            if(cdTypeSymbol.map(symbol -> symbol.getStereotype("container")).isPresent())
                replaceTemplate(CDGeneratorHelper.ATTRIBUTE_HOOK, cdAttribute, new TemplateHookPoint(cdTypeSymbol.get().getFullName(), visitor.acccept(cdAttribute)));
            else
                replaceTemplate(CDGeneratorHelper.ATTRIBUTE_HOOK, cdAttribute, new TemplateHookPoint("visitor.Core"));
        }

        return cdMethod;
    }

    private class CDAttributeVisitor implements CD4CodeInheritanceVisitor{
        private MCFullGenericTypesPrettyPrinter printer = new MCFullGenericTypesPrettyPrinter(new IndentPrinter());
        private LinkedHashMap<ASTMCTypeArgument, Boolean> argumentTypes = new LinkedHashMap<>();

        public Map<ASTMCTypeArgument, Boolean> acccept(ASTCDAttribute ast){
            this.argumentTypes.clear();
            ast.accept(getRealThis());
            return Collections.unmodifiableMap(argumentTypes);
        }

        @Override
        public void visit(ASTMCGenericType ast){
            for(ASTMCTypeArgument argument : ast.getMCTypeArgumentList()){
                //TODO Use Type symbols instead of a pretty printer
                String qualifiedName = argument.printType(printer);
                Optional<CDTypeSymbol> typeSymbol = cdDefinitionSymbol.getType(qualifiedName);
                //True <-> The symbol is in the local scope -> The symbol has an accept method
                argumentTypes.put(argument, typeSymbol.isPresent());
            }
        }
    }
}
