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
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.cd.cd4code._visitor.CD4CodeVisitor;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class CDDefinitionDecorator extends AbstractCreator<ASTCDDefinition, ASTCDDefinition> implements CD4CodeInheritanceVisitor {
    private final CDAttributeDecorator attributeDecorator;
    private List<ASTCDMethod> methodList;
    /**
     * Generates methods can't be added by the time a single type has been handled, in case other types refer to it.
     * Doing so would cause conflicts when binding the respective templates to the new methods, hence why it has do
     * be delayed until all types have been visited.
     */
    private List<Runnable> restActions = new ArrayList<>();

    public CDDefinitionDecorator(GlobalExtensionManagement glex){
        super(glex);
        this.attributeDecorator = new CDAttributeDecorator(glex);
    }

    @Override
    public ASTCDDefinition decorate(ASTCDDefinition ast) {
        ast.accept(getRealThis());
        return ast;
    }

    @Override
    public void visit(ASTCDDefinition ast){
        restActions.clear();
    }

    @Override
    public void visit(ASTCDType ast){
        methodList = new ArrayList<>();
    }

    @Override
    public void visit(ASTCDAttribute ast){
        methodList.addAll(attributeDecorator.decorate(ast));
    }

    @Override
    public void endVisit(ASTCDType ast){
        ASTCDMethodConsumer consumer = new ASTCDMethodConsumer(ast);

        for(ASTCDMethod method : methodList)
            restActions.add(() -> consumer.accept(method));
    }

    @Override
    public void endVisit(ASTCDDefinition ast){
        restActions.forEach(Runnable::run);
    }

    private static class ASTCDMethodConsumer implements Consumer<ASTCDMethod>, CD4CodeVisitor {
        private final ASTCDType ast;
        private ASTCDMethod cdMethod;

        public ASTCDMethodConsumer(ASTCDType ast){
            this.ast = ast;
        }

        @Override
        public void accept(ASTCDMethod cdMethod) {
            this.cdMethod = cdMethod;
            ast.accept(getRealThis());
            this.cdMethod = null;
        }

        @Override
        public void visit(ASTCDClass ast){
            ast.addCDMethod(cdMethod);
        }

        @Override
        public void visit(ASTCDEnum ast){
            ast.addCDMethod(cdMethod);
        }

        @Override
        public void visit(ASTCDInterface ast){
            ast.addCDMethod(cdMethod);
        }
    }
}
