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

public class SetDecoratorTest extends AbstractDecoratorTest {
    @BeforeEach
    public void setUp(){
        super.setUp();
        parseCDClass("Set","vartas","monticore","cd2code","decorator","SetDecorator");
    }

    @Test
    public void testAdd(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "addSet","Integer");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testAddAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "addAllSet", "Collection<? extends Integer>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testClear(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "clearSet");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testContains(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "containsSet","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testContainsAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "containsAllSet","Collection<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testEquals(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "equalsSet", "Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testForEach(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "forEachSet", "Consumer<? super Integer>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testHashCode(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "hashCodeSet");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }

    @Test
    public void testIsEmpty(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "isEmptySet");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testIterator(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "iteratorSet");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Iterator<Integer>");
    }

    @Test
    public void testParallelStream(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "parallelStreamSet");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Stream<Integer>");
    }

    @Test
    public void testRemove(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "removeSet","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testRemoveAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "removeAllSet","Collection<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testRemoveIf(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "removeIfSet","Predicate<? super Integer>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testRetainAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "retainAllSet", "Collection<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testSize(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "sizeSet");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }

    @Test
    public void testSpliterator(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "spliteratorSet");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Spliterator<Integer>");
    }

    @Test
    public void testToArray(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "toArraySet");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Object[]");
    }

    @Test
    public void testToIntFunctionArray(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "toArraySet","IntFunction<Integer[]>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Integer[]");
    }

    @Test
    public void testToGenericArray(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "toArraySet","Integer[]");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Integer[]");
    }
}
