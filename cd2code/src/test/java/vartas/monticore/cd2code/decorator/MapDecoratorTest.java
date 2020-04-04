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
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "clearMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testCompute(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "computeMap","Integer","BiFunction<? super Integer,? super String,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testComputeIfAbsent(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "computeIfAbsentMap","Integer","Function<? super Integer,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testComputeIfPresent(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "computeIfPresentMap","Integer","BiFunction<? super Integer,? super String,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testContainsKey(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "containsKeyMap","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testContainsValue(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "containsValueMap","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testEntrySet(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "entrySetMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Set<Map.Entry<Integer,String>>");
    }

    @Test
    public void testEquals(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "equalsMap","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testForEach(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "forEachMap","BiConsumer<? super Integer,? super String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testGet(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "getMap","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testGetOrDefault(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "getOrDefaultMap","Object", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testHashCode(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "hashCodeMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }

    @Test
    public void testIsEmpty(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "isEmptyMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testKeySet(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "keySetMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Set<Integer>");
    }

    @Test
    public void testMerge(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "mergeMap", "Integer", "String", "BiFunction<? super String,? super String,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testPut(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "putMap", "Integer", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testPutAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "putAllMap", "Map<? extends Integer,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testPutIfAbsent(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "putMap", "Integer", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testRemoveKey(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "removeMap", "Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testRemoveKeyValue(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "removeMap", "Object", "Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testReplaceKey(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "replaceMap", "Integer", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("String");
    }

    @Test
    public void testReplaceKeyValue(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "replaceMap", "Integer", "String", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testReplaceAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "replaceAllMap", "BiFunction<? super Integer,? super String,? extends String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testSize(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "sizeMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }

    @Test
    public void testValues(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "valuesMap");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Collection<String>");
    }
}
