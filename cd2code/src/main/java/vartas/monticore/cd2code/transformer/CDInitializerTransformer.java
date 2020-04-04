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
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.StringHookPoint;
import de.monticore.types.mccollectiontypes._ast.ASTMCListType;
import de.monticore.types.mccollectiontypes._ast.ASTMCMapType;
import de.monticore.types.mccollectiontypes._ast.ASTMCSetType;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code.DecoratorHelper;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._ast.ASTMCCacheType;

public class CDInitializerTransformer implements CD2CodeVisitor {
    private final GlobalExtensionManagement glex;
    private final ASTCDAttribute cdAttribute;
    public CDInitializerTransformer(GlobalExtensionManagement glex,ASTCDAttribute cdAttribute){
        this.glex = glex;
        this.cdAttribute = cdAttribute;
    }

    public static void apply(ASTCDType cdType, GlobalExtensionManagement glex){
        cdType.getCDAttributeList().forEach(cdAttribute -> apply(cdAttribute, glex));
    }

    public static void apply(ASTCDAttribute cdAttribute, GlobalExtensionManagement glex){
        CDInitializerTransformer visitor = new CDInitializerTransformer(glex, cdAttribute);
        cdAttribute.accept(visitor);
    }

    @Override
    public void handle(ASTMCSetType ast){
        glex.replaceTemplate(CDGeneratorHelper.VALUE_TEMPLATE, cdAttribute, new StringHookPoint("= new HashSet<>()"));
    }

    @Override
    public void handle(ASTMCListType ast){
        glex.replaceTemplate(CDGeneratorHelper.VALUE_TEMPLATE, cdAttribute, new StringHookPoint("= new ArrayList<>()"));
    }

    @Override
    public void handle(ASTMCMapType ast){
        glex.replaceTemplate(CDGeneratorHelper.VALUE_TEMPLATE, cdAttribute, new StringHookPoint("= new HashMap<>()"));
    }

    @Override
    public void handle(ASTMCCacheType ast){
        if(CDGeneratorHelper.hasStereoValue(cdAttribute, DecoratorHelper.CACHED_STEREOVALUE)) {
            String duration = CDGeneratorHelper.getStereoValueValues(cdAttribute, DecoratorHelper.CACHED_STEREOVALUE).get(0);
            String signature = String.format("= CacheBuilder.newBuilder().expireAfterAccess(Duration.parse(\"%s\")).build()", duration);
            glex.replaceTemplate(CDGeneratorHelper.VALUE_TEMPLATE, cdAttribute, new StringHookPoint(signature));
        }else{
            String signature = "= CacheBuilder.newBuilder().build()";
            glex.replaceTemplate(CDGeneratorHelper.VALUE_TEMPLATE, cdAttribute, new StringHookPoint(signature));
        }
    }
}
