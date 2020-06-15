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

package vartas.monticore.cd4java.creator;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._ast.ASTCDInterface;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import vartas.monticore.cd4analysis.creator.VisitorCreator;
import vartas.monticore.cd4java.BasicCDTest;

public class VisitorCreatorTest extends BasicCDTest {
    ASTCDInterface visitor;
    ASTCDDefinition cdDefinition;
    CDDefinitionSymbol cdDefinitionSymbol;

    @BeforeEach
    public void setUp(){
        super.setUp();
        cdDefinitionSymbol = globalScope.resolveCDDefinition("vartas.monticore.cd4code.Visitor").orElseThrow();
        cdDefinition = cdDefinitionSymbol.getAstNode();
        visitor = VisitorCreator.create(cdDefinition, new GlobalExtensionManagement());
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
        getMethod(visitor, "visit", parameter);
        getMethod(visitor, "endVisit", parameter);
        getMethod(visitor, "traverse", parameter);
        getMethod(visitor, "handle", parameter);
    }
}