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

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.generating.templateengine.StringHookPoint;
import de.monticore.types.mccollectiontypes._ast.ASTMCListType;
import de.monticore.types.mccollectiontypes._ast.ASTMCMapType;
import de.monticore.types.mccollectiontypes._ast.ASTMCSetType;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._ast.ASTMCCacheType;

public class CDInitializerTemplate implements CDConsumerTemplate<ASTCDCompilationUnit> {
    private final CDGeneratorHelper generatorHelper;

    public CDInitializerTemplate(CDGeneratorHelper generatorHelper){
        this.generatorHelper = generatorHelper;
    }

    @Override
    public void visit(ASTCDAttribute ast){
        ast.accept(new CDAttributeVisitor());
    }

    private class CDAttributeVisitor implements CD2CodeVisitor {
        private ASTCDAttribute cdAttribute;

        @Override
        public void visit(ASTCDAttribute ast){
            cdAttribute = ast;
        }

        @Override
        public void handle(ASTMCSetType ast) {
            generatorHelper.getGlex().replaceTemplate(CDGeneratorHelper.VALUE_TEMPLATE, cdAttribute, new StringHookPoint("= new HashSet<>()"));
        }

        @Override
        public void handle(ASTMCListType ast) {
            generatorHelper.getGlex().replaceTemplate(CDGeneratorHelper.VALUE_TEMPLATE, cdAttribute, new StringHookPoint("= new ArrayList<>()"));
        }

        @Override
        public void handle(ASTMCMapType ast) {
            generatorHelper.getGlex().replaceTemplate(CDGeneratorHelper.VALUE_TEMPLATE, cdAttribute, new StringHookPoint("= new HashMap<>()"));
        }

        @Override
        public void handle(ASTMCCacheType ast) {
            if (CDGeneratorHelper.hasStereoValue(cdAttribute, CDGeneratorHelper.CACHED_STEREOVALUE)) {
                String duration = CDGeneratorHelper.getStereoValueValues(cdAttribute, CDGeneratorHelper.CACHED_STEREOVALUE).get(0);
                String signature = String.format("= CacheBuilder.newBuilder().expireAfterAccess(Duration.parse(\"%s\")).build()", duration);
                generatorHelper.getGlex().replaceTemplate(CDGeneratorHelper.VALUE_TEMPLATE, cdAttribute, new StringHookPoint(signature));
            } else {
                String signature = "= CacheBuilder.newBuilder().build()";
                generatorHelper.getGlex().replaceTemplate(CDGeneratorHelper.VALUE_TEMPLATE, cdAttribute, new StringHookPoint(signature));
            }
        }
    }
}
