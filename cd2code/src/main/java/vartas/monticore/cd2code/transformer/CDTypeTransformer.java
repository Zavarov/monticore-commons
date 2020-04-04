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

package vartas.monticore.cd2code.transformer;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.codegen.cd2java.AbstractTransformer;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mccollectiontypes._ast.ASTMCListType;
import de.monticore.types.mccollectiontypes._ast.ASTMCMapType;
import de.monticore.types.mccollectiontypes._ast.ASTMCOptionalType;
import de.monticore.types.mccollectiontypes._ast.ASTMCSetType;
import vartas.monticore.cd2code._visitor.CD2CodeInheritanceVisitor;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;
import vartas.monticore.cd2code.decorator.*;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._ast.ASTMCCacheType;

import javax.annotation.Nonnull;

public class CDTypeTransformer extends AbstractTransformer<ASTCDType> {
    private final ASTCDInterface cdVisitor;

    public CDTypeTransformer(@Nonnull GlobalExtensionManagement glex, @Nonnull ASTCDInterface cdVisitor){
        super(glex);
        this.cdVisitor = cdVisitor;
    }

    public static ASTCDType apply(ASTCDType originalType, GlobalExtensionManagement glex, ASTCDInterface cdVisitor){
        CDTypeTransformer visitor = new CDTypeTransformer(glex, cdVisitor);

        ASTCDType decoratedType = originalType.deepClone();
        return visitor.decorate(originalType, decoratedType);
    }

    @Override
    public ASTCDType decorate(ASTCDType originalType, ASTCDType decoratedType) {
        decoratedType.accept(new CDTypeVisitor(originalType));
        return decoratedType;
    }

    private class CDTypeVisitor implements CD2CodeVisitor {
        private final ASTCDType originalType;

        public CDTypeVisitor(ASTCDType originalType){
            this.originalType = originalType;
        }

        @Override
        public void visit(ASTCDClass ast){
            //Visitor
            ast.addAllCDMethods(new VisitorDecorator(glex).decorate(cdVisitor));
            //Decorator for attributes
            for(ASTCDAttribute cdAttribute : originalType.getCDAttributeList())
                cdAttribute.accept(new CDAttributeVisitor(cdAttribute, ast));
        }

        @Override
        public void visit(ASTCDEnum ast){
            //Visitor
            ast.addAllCDMethods(new VisitorDecorator(glex).decorate(cdVisitor));
        }
    }

    private class CDAttributeVisitor implements CD2CodeInheritanceVisitor {
        private final ASTCDAttribute cdAttribute;
        private final ASTCDClass cdClass;

        private CDAttributeVisitor(ASTCDAttribute cdAttribute, ASTCDClass cdClass){
            this.cdAttribute = cdAttribute;
            this.cdClass = cdClass;
        }

        @Override
        public void handle(ASTMCListType ast){
            cdClass.addAllCDMethods(new ListDecorator(glex).decorate(cdAttribute));
            cdClass.addAllCDMethods(new CollectionDecorator(glex).decorate(cdAttribute));
            cdClass.addAllCDMethods(new CoreDecorator(glex).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCSetType ast){
            cdClass.addAllCDMethods(new CollectionDecorator(glex).decorate(cdAttribute));
            cdClass.addAllCDMethods(new CoreDecorator(glex).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCMapType ast){
            cdClass.addAllCDMethods(new MapDecorator(glex).decorate(cdAttribute));
            cdClass.addAllCDMethods(new CoreDecorator(glex).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCOptionalType ast){
            cdClass.addAllCDMethods(new OptionalDecorator(glex).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCCacheType ast){
            cdClass.addAllCDMethods(new CacheDecorator(glex).decorate(cdAttribute));
            cdClass.addAllCDMethods(new CoreDecorator(glex).decorate(cdAttribute));
        }

        @Override
        public void visit(ASTMCType ast){
            cdClass.addAllCDMethods(new CoreDecorator(glex).decorate(cdAttribute));
        }
    }
}
