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
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.facade.CDModifier;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import vartas.monticore.cd2code.CDGeneratorHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Nonnull
public class FactoryCreator extends AbstractCreator<ASTCDClass, ASTCDClass> {
    public FactoryCreator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
    }

    @Nonnull
    public static ASTCDClass create
    (
            @Nonnull ASTCDClass cdClass,
            @Nonnull GlobalExtensionManagement glex
    )
    {
        return new FactoryCreator(glex).decorate(cdClass);
    }

    @Nonnull
    @Override
    public ASTCDClass decorate(@Nonnull ASTCDClass cdClass) {
        //Ignore a possible TOP class;
        cdClass = cdClass.deepClone();

        ASTCDClass cdFactoryClass = CD4AnalysisMill.cDClassBuilder()
                .setName(cdClass.getName()+"Factory")
                .setModifier(CDModifier.PUBLIC.build())
                .build();

        cdFactoryClass.addAllCDMethods(createMethods(cdClass));

        return cdFactoryClass;
    }

    @Nonnull
    protected List<ASTCDMethod> createMethods(@Nonnull ASTCDClass cdClass){
        return Arrays.asList(
                createCreate(cdClass),
                createCreateSupplier(cdClass)
        );
    }

    private List<ASTCDParameter> getCDParameters(ASTCDClass cdClass){
        List<ASTCDParameter> cdParameters = new ArrayList<>();

        for(ASTCDType cdType : getTransitiveTypes(cdClass))
            cdParameters.addAll(getCDParameters(cdType));

        return cdParameters;
    }

    private List<ASTCDParameter> getCDParameters(ASTCDType cdType){
        List<ASTCDParameter> cdParameters = new ArrayList<>();

        for(ASTCDAttribute cdAttribute : cdType.getCDAttributeList())
            if(CDGeneratorHelper.isMandatory(cdAttribute))
                cdParameters.add(getCDParameterFacade().createParameter(cdAttribute));

        return cdParameters;
    }

    private List<ASTCDType> getTransitiveTypes(ASTCDType cdType){
        List<ASTCDType> cdTypes = new ArrayList<>();
        cdTypes.add(cdType);

        if(cdType.isPresentSymbol())
            for (CDTypeSymbol cdTypeSymbol : cdType.getSymbol().getSuperTypes())
                if (cdTypeSymbol.isPresentAstNode())
                    cdTypes.addAll(getTransitiveTypes(cdTypeSymbol.getAstNode()));
        return cdTypes;
    }



    @Nonnull
    protected ASTCDMethod createCreate(@Nonnull ASTCDClass cdClass){
        List<ASTCDParameter> cdParameters = getCDParameters(cdClass);

        ASTMCType mcReturnType = getMCTypeFacade().createQualifiedType(cdClass.getName());

        ASTCDMethod cdMethod = getCDMethodFacade().createMethod(
                CDModifier.PUBLIC_STATIC,
                mcReturnType,
                "create",
                cdParameters
        );

        replaceTemplate(CDGeneratorHelper.METHOD_TEMPLATE, cdMethod, new TemplateHookPoint("factory.Create", cdClass, cdMethod));
        return cdMethod;
    }

    @Nonnull
    protected ASTCDMethod createCreateSupplier(@Nonnull ASTCDClass cdClass){
        //Accept a supplier as the first argument
        ASTMCType mcSupplierType = getMCTypeFacade().createBasicGenericTypeOf("Supplier", cdClass.getName());

        List<ASTCDParameter> cdParameters = new ArrayList<>();
        cdParameters.add(getCDParameterFacade().createParameter(mcSupplierType, "supplier"));
        cdParameters.addAll(getCDParameters(cdClass));

        ASTMCType mcReturnType = getMCTypeFacade().createQualifiedType(cdClass.getName());

        ASTCDMethod cdMethod = getCDMethodFacade().createMethod(
                CDModifier.PUBLIC_STATIC,
                mcReturnType,
                "create",
                cdParameters
        );

        replaceTemplate(CDGeneratorHelper.METHOD_TEMPLATE, cdMethod, new TemplateHookPoint("factory.CreateSupplier", cdClass, cdMethod));
        return cdMethod;
    }
}
