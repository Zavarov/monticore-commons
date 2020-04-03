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

public class CacheDecoratorTest extends AbstractDecoratorTest {
    @BeforeEach
    public void setUp(){
        super.setUp();
        parseCDClass("Cache","vartas","monticore","cd2code","decorator","CacheDecorator");
    }

    @Test
    public void testAsMap(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "asMapCache");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("ConcurrentMap<String,A>");
    }

    @Test
    public void testCleanUp(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "cleanUpCache");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testGet(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "getCache", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A");
    }

    @Test
    public void testGetLoader(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "getCache", "String", "Callable<? extends A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A");
    }

    @Test
    public void testGetAllPresent(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "getAllPresentCache", "Iterable<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("ImmutableMap<String,A>");
    }

    @Test
    public void testGetIfPresent(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "getIfPresentCache", "Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A");
    }

    @Test
    public void testInvalidate(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "invalidateCache", "Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testInvalidateAll(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "invalidateAllCache");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testInvalidateAllKeys(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "invalidateAllCache", "Iterable<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testPut(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "putCache", "String","A");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testPutAll(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "putAllCache", "Map<? extends String,? extends A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testSize(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "sizeCache");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("long");
    }

    @Test
    public void testStats(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "statsCache");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("CacheStats");
    }

    @Test
    public void testValues(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "valuesCache");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Collection<A>");
    }
}
