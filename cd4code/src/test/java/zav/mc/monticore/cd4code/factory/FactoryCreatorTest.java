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

package zav.mc.monticore.cd4code.factory;

import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import zav.mc.cd4code.factory.FactoryCreator;
import zav.mc.monticore.BasicCDTest;
import zav.mc.monticore.CSV2StringArray;

public class FactoryCreatorTest extends BasicCDTest {
    ASTCDClass factory;
    ASTCDClass cdClass;
    ASTCDDefinition cdDefinition;
    CDDefinitionSymbol cdDefinitionSymbol;

    @BeforeEach
    public void setUp(){
        super.setUp();
        cdDefinitionSymbol = globalScope.resolveCDDefinition("vartas.monticore.cd4code.factory.Factory").orElseThrow();
        cdDefinition = cdDefinitionSymbol.getAstNode();
        cdClass = cdDefinition.getCDClass(0);
        factory = FactoryCreator.create(cdClass, new GlobalExtensionManagement());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'Supplier<? extends Essay> : String : String : List<String>'",
            "'Supplier<? extends Essay> : String : String'",
            "'String : String : List<String>'",
            "'String : String"
    })
    public void testParse(@ConvertWith(CSV2StringArray.class) String[] parameters){
        getCDMethod(factory, "create", parameters);
    }
}
