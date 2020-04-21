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

package vartas.monticore.cd4java.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import vartas.monticore.cd4java.CSV2StringArray;

import static org.assertj.core.api.Assertions.assertThat;

public class CDObjectTest extends AbstractCDDecoratorTest {
    ASTCDDefinition cdDefinition;
    CDDefinitionSymbol cdDefinitionSymbol;

    @BeforeEach
    public void setUp(){
        super.setUp();
        cdDefinitionSymbol = globalScope.resolveCDDefinition("vartas.monticore.cd4code.Optional").orElseThrow();
        cdDefinition = cdDefinitionSymbol.getAstNode();
    }

    @ParameterizedTest
    @CsvSource(value = {
            " boolean : equalsNickname    : 'Object'",
            "Class<?> : getClassNickname  : ''",
            "     int : hashCodeNickname  : ''",
            "    void : notifyNickname    : ''",
            "    void : notifyAllNickname : ''",
            "  String : toStringNickname  : ''",
            "    void : waitNickname      : ''",
            "    void : waitNickname      : 'long'",
            "    void : waitNickname      : 'long, int'"


    }, delimiter = ':')
    public void testParse(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        ASTCDDefinition ast = cdDefinitionDecorator.decorate(cdDefinition);
        ASTCDType cdClass = getCDType(ast, "User");
        ASTCDMethod cdMethod = getMethod(cdClass, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();

        assertThat(printer.prettyprint(mcReturnType)).isEqualTo(returnName);
    }
}
