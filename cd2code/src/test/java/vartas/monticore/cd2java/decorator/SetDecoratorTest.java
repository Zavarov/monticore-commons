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

public class SetDecoratorTest extends AbstractDecoratorTest {
    @BeforeEach
    public void setUp(){
        parseCDClass("Set","vartas.monticore.cd2java.SetCD");
        super.setUp();
    }

    @ParameterizedTest
    @CsvSource(value = {
            //Collection
            "             boolean : addSet            : 'Integer'",
            "             boolean : addAllSet         : Collection<? extends Integer>",
            "                void : clearSet          : ''",
            "             boolean : containsSet       : 'Object'",
            "             boolean : containsAllSet    : 'Collection<?>'",
            "             boolean : equalsSet         : 'Object'",
            "                void : forEachSet        : 'Consumer<? super Integer>'",
            "                 int : hashCodeSet       : ''",
            "             boolean : isEmptySet        : ''",
            "   Iterator<Integer> : iteratorSet       : ''",
            "     Stream<Integer> : parallelStreamSet : ''",
            "             boolean : removeSet         : 'Object'",
            "             boolean : removeAllSet      : 'Collection<?>'",
            "             boolean : removeIfSet       : 'Predicate<? super Integer>'",
            "             boolean : retainAllSet      : 'Collection<?>'",
            "                 int : sizeSet           : ''",
            "Spliterator<Integer> : spliteratorSet    : ''",
            "            Object[] : toArraySet        : ''",
            "           Integer[] : toArraySet        : 'IntFunction<Integer[]>'",
            "           Integer[] : toArraySet        : 'Integer[]'"
    }, delimiter = ':')
    public void testParse(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        ASTCDMethod cdMethod = getMethod(cdDecoratedType, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();

        assertThat(CDGeneratorHelper.prettyprint(mcReturnType)).isEqualTo(returnName);
    }
}
