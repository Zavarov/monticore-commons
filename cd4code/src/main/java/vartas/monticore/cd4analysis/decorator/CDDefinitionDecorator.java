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

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4code._visitor.CD4CodeVisitor;
import de.monticore.codegen.cd2java.AbstractCreator;

import java.util.ArrayList;
import java.util.List;


public class CDDefinitionDecorator extends AbstractCreator<ASTCDDefinition, ASTCDDefinition> implements CD4CodeVisitor {
    private final CDAttributeDecorator attributeDecorator = new CDAttributeDecorator();
    private final List<ASTCDMethod> methodList = new ArrayList<>();

    @Override
    public ASTCDDefinition decorate(ASTCDDefinition ast) {
        ast = ast.deepClone();
        ast.accept(getRealThis());
        return ast;
    }

    @Override
    public void visit(ASTCDAttribute ast){
        methodList.addAll(attributeDecorator.decorate(ast));
    }

    @Override
    public void endVisit(ASTCDClass ast){
        ast.addAllCDMethods(methodList);
    }

    @Override
    public void endVisit(ASTCDEnum ast){
        ast.addAllCDMethods(methodList);
    }

    @Override
    public void endVisit(ASTCDInterface ast){
        ast.addAllCDMethods(methodList);
    }
}
