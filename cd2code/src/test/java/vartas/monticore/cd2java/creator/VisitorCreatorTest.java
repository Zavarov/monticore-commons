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

package vartas.monticore.cd2java.creator;

import de.monticore.cd.cd4analysis._ast.ASTCDInterface;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import vartas.monticore.CSV2StringArray;
import vartas.monticore.cd2code.BasicCDTest;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code.creator.VisitorCreator;

import static org.assertj.core.api.Assertions.assertThat;

public class VisitorCreatorTest extends BasicCDTest {
    private ASTCDInterface cdVisitorInterface;

    @BeforeEach
    public void setUp(){
        parseCDClass("V","vartas.monticore.cd2java.VisitorCD");
        cdVisitorInterface = VisitorCreator.create(cdDefinition, new GlobalExtensionManagement());
    }

    @ParameterizedTest
    @CsvSource({
            "void, visit, 'V'",
            "void, endVisit, 'V'",
            "void, traverse, 'V'",
            "void, handle, 'V'"
    })
    public void testParse(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        ASTCDMethod cdMethod = getMethod(cdVisitorInterface, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();

        assertThat(CDGeneratorHelper.prettyprint(mcReturnType)).isEqualTo(returnName);
    }
}
