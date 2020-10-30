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

package vartas.monticore.cd4code.visitor;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._ast.ASTCDInterface;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import vartas.monticore.BasicCDTest;
import vartas.monticore.cd4code.CDGeneratorHelper;

import static org.assertj.core.api.Assertions.assertThat;

public class VisitorCreatorTest extends BasicCDTest {
    ASTCDInterface visitor;
    ASTCDDefinition cdDefinition;
    CDDefinitionSymbol cdDefinitionSymbol;

    @BeforeEach
    public void setUp(){
        super.setUp();
        cdDefinitionSymbol = globalScope.resolveCDDefinition("vartas.monticore.cd4code.visitor.Visitor").orElseThrow();
        cdDefinition = cdDefinitionSymbol.getAstNode();
        visitor = VisitorCreator.create(cdDefinition, new GlobalExtensionManagement(), new CDGeneratorHelper());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Member",
            "Message",
            "Guild",
            "TextChannel",
            "Role"
    })
    public void testVisitorMethods(String parameter){
        getCDMethod(visitor, "visit", parameter);
        getCDMethod(visitor, "endVisit", parameter);
        getCDMethod(visitor, "traverse", parameter);
        getCDMethod(visitor, "handle", parameter);
        getCDMethod(visitor, "walkUpFrom", parameter);
        getCDMethod(visitor, "endWalkUpFrom", parameter);
    }

    @Test
    public void testGetRealThisMethod(){
        for(CDTypeSymbol symbol : cdDefinitionSymbol.getTypes()) {
            ASTCDMethod cdMethod = getCDMethod(symbol.getAstNode(), "getRealThis");

            ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();
            assertThat(printer.prettyprint(mcReturnType)).isEqualTo(symbol.getName());
        }
    }

    @Test
    public void testAcceptMethod(){
        for(CDTypeSymbol symbol : cdDefinitionSymbol.getTypes()) {
            ASTCDMethod cdMethod = getCDMethod(symbol.getAstNode(), "accept", visitor.getName());

            assertThat(cdMethod.getMCReturnType().isPresentMCVoidType());
        }
    }
}
