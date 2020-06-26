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

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._ast.ASTCDInterface;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._ast.CD4AnalysisMill;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.facade.CDModifier;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import vartas.monticore.cd4analysis.CDGeneratorHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class InheritanceVisitorCreator extends VisitorCreator{

    protected InheritanceVisitorCreator(@Nonnull CDDefinitionSymbol cdDefinitionSymbol, @Nonnull GlobalExtensionManagement glex, @Nonnull CDGeneratorHelper generatorHelper){
        super(cdDefinitionSymbol, glex, generatorHelper);
    }

    public static ASTCDInterface create(ASTCDDefinition cdDefinition, GlobalExtensionManagement glex, CDGeneratorHelper generatorHelper){
        return new InheritanceVisitorCreator(cdDefinition.getSymbol(), glex, generatorHelper).decorate(cdDefinition);
    }

    @Override
    public ASTCDInterface decorate(ASTCDDefinition cdDefinition) {
        ASTCDInterface cdInterface = buildVisitor(cdDefinition);

        cdDefinition.streamCDClasss().map(this::createMethods).forEach(cdInterface::addAllCDMethods);
        cdDefinition.streamCDEnums().map(this::createMethods).forEach(cdInterface::addAllCDMethods);
        cdDefinition.streamCDInterfaces().map(this::createMethods).forEach(cdInterface::addAllCDMethods);

        return cdInterface;
    }

    @Override
    protected ASTCDInterface buildVisitor(ASTCDDefinition ast){
        return CD4AnalysisMill.cDInterfaceBuilder()
                .setName(ast.getName()+"InheritanceVisitor")
                .addInterface(getMCTypeFacade().createQualifiedType(ast.getName()+"Visitor"))
                .setModifier(CDModifier.PUBLIC.build())
                .build();
    }

    @Override
    protected List<String> computeSuperTypes(ASTCDType cdType){
        return cdType.getSymbol().getSuperTypesTransitive().stream().distinct().map(CDTypeSymbol::getAstNode).map(this::getTypeName).collect(Collectors.toList());
    }
}
