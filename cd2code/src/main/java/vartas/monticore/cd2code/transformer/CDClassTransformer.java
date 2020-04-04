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

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDInterface;
import de.monticore.codegen.cd2java.AbstractTransformer;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.types.mccollectiontypes._ast.ASTMCListType;
import de.monticore.types.mccollectiontypes._ast.ASTMCMapType;
import de.monticore.types.mccollectiontypes._ast.ASTMCOptionalType;
import de.monticore.types.mccollectiontypes._ast.ASTMCSetType;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;
import vartas.monticore.cd2code.decorator.*;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._ast.ASTMCCacheType;

import javax.annotation.Nonnull;

public class CDClassTransformer extends AbstractTransformer<ASTCDClass> implements CD2CodeVisitor{
    private final ASTCDInterface cdVisitor;
    public CDClassTransformer(@Nonnull GlobalExtensionManagement glex, @Nonnull ASTCDInterface cdVisitor){
        super(glex);
        this.cdVisitor = cdVisitor;
    }

    public static ASTCDClass apply(ASTCDClass cdClass, GlobalExtensionManagement glex, ASTCDInterface cdVisitor){
        CDClassTransformer visitor = new CDClassTransformer(glex, cdVisitor);

        ASTCDClass transformedClass = cdClass.deepClone();
        CDClassTransformer transformer = new CDClassTransformer(glex, cdVisitor);
        return visitor.decorate(cdClass, transformedClass);
    }

    @Override
    public ASTCDClass decorate(ASTCDClass originalClass, ASTCDClass transformedClass) {
        originalClass.accept(new CDClassVisitor(transformedClass));

        for(ASTCDAttribute cdAttribute : originalClass.getCDAttributeList())
            cdAttribute.accept(new CDAttributeVisitor(cdAttribute, transformedClass));

        return transformedClass;
    }

    private class CDClassVisitor implements CD2CodeVisitor {
        private final ASTCDClass cdClass;

        private CDClassVisitor(ASTCDClass cdClass){
            this.cdClass = cdClass;
        }

        @Override
        public void visit(ASTCDClass ast){
            cdClass.addAllCDMethods(new VisitorDecorator(glex).decorate(cdVisitor));
        }

        @Override
        public void visit(ASTCDAttribute ast){
            //We store the reference type of optionals, so conventional methods won't work
            //if(!MCCollectionTypesHelper.isOptional(ast.getMCType()))
            //    cdClass.addAllCDMethods(new CoreDecorator(glex).decorate(ast));
        }


    }

    private class CDAttributeVisitor implements CD2CodeVisitor {
        private final ASTCDAttribute cdAttribute;
        private final ASTCDClass cdClass;

        private CDAttributeVisitor(ASTCDAttribute cdAttribute, ASTCDClass cdClass){
            this.cdAttribute = cdAttribute;
            this.cdClass = cdClass;
        }

        @Override
        public void handle(ASTMCListType ast){
            cdClass.addAllCDMethods(new CollectionDecorator(glex).decorate(cdAttribute));
            cdClass.addAllCDMethods(new ListDecorator(glex).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCSetType ast){
            cdClass.addAllCDMethods(new CollectionDecorator(glex).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCMapType ast){
            cdClass.addAllCDMethods(new MapDecorator(glex).decorate(cdAttribute));
        }

        @Override
        public void handle(ASTMCOptionalType ast){
            cdClass.addAllCDMethods(new OptionalDecorator(glex).decorate(cdAttribute));
        }

        @Override
        public void visit(ASTMCCacheType ast){
            cdClass.addAllCDMethods(new CacheDecorator(glex).decorate(cdAttribute));
        }
    }
}
