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

package vartas.monticore.cd2java.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import vartas.monticore.CSV2StringArray;
import vartas.monticore.cd2code.CDGeneratorHelper;

import static org.assertj.core.api.Assertions.assertThat;

public class MapDecoratorTest extends AbstractDecoratorTest {
    @BeforeEach
    public void setUp(){
        parseCDClass("Map","vartas.monticore.cd2java.MapCD");
        super.setUp();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "                          void : clearMap            : ''",
            "                        String : computeMap          : 'Integer, BiFunction<? super Integer,? super String,? extends String>'",
            "                        String : computeIfAbsentMap  : 'Integer, Function<? super Integer,? extends String>'",
            "                        String : computeIfPresentMap : 'Integer, BiFunction<? super Integer,? super String,? extends String>'",
            "                       boolean : containsKeyMap      : 'Object'",
            "                       boolean : containsValueMap    : 'Object'",
            "Set<Map.Entry<Integer,String>> : entrySetMap         : ''",
            "                       boolean : equalsMap           : 'Object'",
            "                          void : forEachMap          : 'BiConsumer<? super Integer,? super String>'",
            "                        String : getMap              : 'Object'",
            "                        String : getOrDefaultMap     : 'Object, String'",
            "                           int : hashCodeMap         : ''",
            "                       boolean : isEmptyMap          : ''",
            "                  Set<Integer> : keySetMap           : ''",
            "                        String : mergeMap            : 'Integer, String, BiFunction<? super String,? super String,? extends String>'",
            "                        String : putMap              : 'Integer, String'",
            "                          void : putAllMap           : 'Map<? extends Integer,? extends String>'",
            "                        String : removeMap           : 'Object'",
            "                       boolean : removeMap           : 'Object, Object'",
            "                        String : replaceMap          : 'Integer, String'",
            "                       boolean : replaceMap          : 'Integer, String, String'",
            "                          void : replaceAllMap       : 'BiFunction<? super Integer,? super String,? extends String>'",
            "                           int : sizeMap             : ''",
            "            Collection<String> : valuesMap           : ''",


    }, delimiter = ':')
    public void testParse(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        ASTCDMethod cdMethod = getMethod(cdDecoratedType, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();

        assertThat(CDGeneratorHelper.prettyprint(mcReturnType)).isEqualTo(returnName);
    }
}
