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

package vartas.monticore.cd2code.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MapDecoratorTest extends AbstractDecoratorTest {
    @BeforeEach
    public void setUp(){
        super.setUp();
        parseCDClass("Map","vartas","monticore","cd2code","decorator","MapDecorator");
    }

    @Test
    public void testClear(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "clearMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testCompute(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "computeMap","Integer","BiFunction<? super Integer,? super String,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testComputeIfAbsent(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "computeIfAbsentMap","Integer","Function<? super Integer,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testComputeIfPresent(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "computeIfPresentMap","Integer","BiFunction<? super Integer,? super String,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testContainsKey(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "containsKeyMap","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testContainsValue(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "containsValueMap","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testEntrySet(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "entrySetMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Set<Map.Entry<Integer,String>>");
    }

    @Test
    public void testEquals(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "equalsMap","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testForEach(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "forEachMap","BiConsumer<? super Integer,? super String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testGet(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "getMap","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testGetOrDefault(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "getOrDefaultMap","Object", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testHashCode(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "hashCodeMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }

    @Test
    public void testIsEmpty(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "isEmptyMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testKeySet(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "keySetMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Set<Integer>");
    }

    @Test
    public void testMerge(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "mergeMap", "Integer", "String", "BiFunction<? super String,? super String,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testPut(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "putMap", "Integer", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testPutAll(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "putAllMap", "Map<? extends Integer,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testPutIfAbsent(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "putMap", "Integer", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testRemoveKey(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "removeMap", "Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testRemoveKeyValue(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "removeMap", "Object", "Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testReplaceKey(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "replaceMap", "Integer", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testReplaceKeyValue(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "replaceMap", "Integer", "String", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testReplaceAll(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "replaceAllMap", "BiFunction<? super Integer,? super String,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testSize(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "sizeMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }

    @Test
    public void testValues(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "valuesMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Collection<String>");
    }
}
