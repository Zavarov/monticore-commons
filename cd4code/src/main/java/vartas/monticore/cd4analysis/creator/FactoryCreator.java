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
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.facade.CDModifier;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import de.se_rwth.commons.Joiners;
import vartas.monticore.cd4analysis.CDGeneratorHelper;
import vartas.monticore.cd4analysis.CDMethodComparator;

import javax.annotation.Nonnull;
import java.util.*;

public class FactoryCreator extends AbstractCreator<ASTCDType, ASTCDClass> {
    private static final String CREATE_TEMPLATE = Joiners.DOT.join(CDGeneratorHelper.FACTORY_MODULE, "Create");
    private static final String CREATE_SUPPLIER_TEMPLATE = Joiners.DOT.join(CDGeneratorHelper.FACTORY_MODULE, "CreateSupplier");

    private FactoryCreator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
    }

    public static ASTCDClass create(ASTCDType ast, GlobalExtensionManagement glex){
        return new FactoryCreator(glex).decorate(ast);
    }

    @Override
    public ASTCDClass decorate(ASTCDType ast) {
        ASTCDClass factory = buildFactory(ast);

        factory.addAllCDMethods(buildMethods(ast));

        return factory;
    }

    private ASTCDClass buildFactory(ASTCDType ast){
        return CD4AnalysisMill.cDClassBuilder()
                .setName(ast.getName()+"Factory")
                .setModifier(CDModifier.PUBLIC.build())
                .build();
    }

    private Set<ASTCDMethod> buildMethods(ASTCDType ast){
        //Duplicates will be created when there are no container attributes.
        Set<ASTCDMethod> methods = new TreeSet<>(new CDMethodComparator());
        ASTCDMethod method;

        //For non-abstract classes, create a new instance in the method body
        if(!ast.getSymbol().isIsAbstract()){
            method = buildMethod(ast, true);
            bindToTemplate(ast, method, CREATE_TEMPLATE);
            methods.add(method);

            method = buildMethod(ast, false);
            bindToTemplate(ast, method, CREATE_TEMPLATE);
            methods.add(method);
        }

        method = buildMethodSupplier(ast, true);
        bindToTemplate(ast, method, CREATE_SUPPLIER_TEMPLATE);
        methods.add(method);

        method = buildMethodSupplier(ast, false);
        bindToTemplate(ast, method, CREATE_SUPPLIER_TEMPLATE);
        methods.add(method);

        return methods;
    }

    private ASTCDMethod buildMethodSupplier(ASTCDType ast, boolean includeContainers){
        List<ASTCDParameter> parameters = new ArrayList<>();

        parameters.add(createSupplier(ast));
        for (ASTCDAttribute attribute : getAttributes(ast))
            //Containers are optional
            if(!(isContainer(attribute) && !includeContainers))
                parameters.add(getCDParameterFacade().createParameter(attribute));

        return buildMethod(ast, parameters);
    }

    private ASTCDMethod buildMethod(ASTCDType ast, boolean includeContainers) {
        List<ASTCDParameter> parameters = new ArrayList<>();

        for (ASTCDAttribute attribute : getAttributes(ast))
            //Containers are optional
            if (!(isContainer(attribute) && !includeContainers))
                parameters.add(getCDParameterFacade().createParameter(attribute));

        return buildMethod(ast, parameters);
    }

    private ASTCDMethod buildMethod(ASTCDType ast, List<ASTCDParameter> parameters) {
        ASTMCType returnType = getMCTypeFacade().createQualifiedType(ast.getName());
        ASTCDMethod method = getCDMethodFacade().createMethod(CDModifier.PUBLIC_STATIC, returnType, "create", parameters);
        String createTemplate = Joiners.DOT.join(CDGeneratorHelper.FACTORY_MODULE, "create");
        return method;
    }

    private void bindToTemplate(ASTCDType cdClass, ASTCDMethod cdMethod, String template){
        glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint(template, cdClass, cdMethod));
    }

    private List<ASTCDAttribute> getAttributes(ASTCDType ast){
        Set<ASTCDAttribute> attributes = new LinkedHashSet<>(ast.getCDAttributeList());

        for(CDTypeSymbol symbol : ast.getSymbol().getSuperTypesTransitive())
            attributes.addAll(symbol.getAstNode().getCDAttributeList());

        return new ArrayList<>(attributes);
    }

    private boolean isContainer(ASTCDAttribute ast){
        return ast.getSymbol()
                .getType()
                .loadSymbol()
                .flatMap(type -> type.getStereotype(CDGeneratorHelper.CONTAINER_LABEL))
                .isPresent();
    }

    private ASTCDParameter createSupplier(ASTCDType ast){
        ASTMCTypeArgument argument = getMCTypeFacade().createWildCardWithUpperBoundType(ast.getName());
        ASTMCType type = getMCTypeFacade().createBasicGenericTypeOf("Supplier", argument);

        return getCDParameterFacade().createParameter(type, "_factory"+ast.getName());
    }
}
