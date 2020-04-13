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

package vartas.monticore.cd2java.template;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mccollectiontypes._ast.ASTMCListType;
import de.monticore.types.mccollectiontypes._ast.ASTMCMapType;
import de.monticore.types.mccollectiontypes._ast.ASTMCOptionalType;
import de.monticore.types.mccollectiontypes._ast.ASTMCSetType;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code._visitor.CD2CodeInheritanceVisitor;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._ast.ASTMCCacheType;
import vartas.monticore.cd2java.decorator.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class CDDecoratorTemplate implements CD2CodeInheritanceVisitor, UnaryOperator<ASTCDCompilationUnit> {
    private final CDGeneratorHelper generatorHelper;

    public CDDecoratorTemplate(CDGeneratorHelper generatorHelper){
        this.generatorHelper = generatorHelper;
    }

    @Override
    public ASTCDCompilationUnit apply(ASTCDCompilationUnit ast) {
        ast = ast.deepClone();
        ast.accept(this);
        return ast;
    }

    @Override
    public void handle(ASTCDClass ast){
        //Visitor
        ast.addAllCDMethods(new VisitorDecorator(generatorHelper.getGlex()).decorate(generatorHelper.getVisitor()));
        //Decorator
        CDDecoratorVisitor visitor = new CDDecoratorVisitor();
        for(ASTCDAttribute cdAttribute : ast.getCDAttributeList()) {
            cdAttribute.accept(visitor);
        }
        ast.addAllCDMethods(visitor.cdMethods);
    }

    @Override
    public void handle(ASTCDEnum ast){
        //Visitor
        ast.addAllCDMethods(new VisitorDecorator(generatorHelper.getGlex()).decorate(generatorHelper.getVisitor()));
    }

    @Override
    public void handle(ASTCDInterface ast){
        //Visitor
        ast.addAllCDMethods(new VisitorDecorator(generatorHelper.getGlex()).decorate(generatorHelper.getVisitor()));
    }

    private class CDDecoratorVisitor implements CD2CodeInheritanceVisitor {
        private final List<ASTCDMethod> cdMethods = new ArrayList<>();
        
        private ASTCDAttribute cdAttribute;

        @Override
        public void visit(ASTCDAttribute ast){
            cdAttribute = ast;
        }

        @Override
        public void handle(ASTMCListType ast){
            cdMethods.addAll(new ListDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
            cdMethods.addAll(new CollectionDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
            cdMethods.addAll(new CoreDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCSetType ast){
            cdMethods.addAll(new CollectionDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
            cdMethods.addAll(new CoreDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCMapType ast){
            cdMethods.addAll(new MapDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
            cdMethods.addAll(new CoreDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCOptionalType ast){
            cdMethods.addAll(new OptionalDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCCacheType ast){
            cdMethods.addAll(new CacheDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
            cdMethods.addAll(new CoreDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
        }

        @Override
        public void visit(ASTMCType ast){
            cdMethods.addAll(new CoreDecorator(generatorHelper.getGlex()).decorate(cdAttribute));
        }
    }
}
