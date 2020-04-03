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

public class ListDecoratorTest extends AbstractDecoratorTest {
    @BeforeEach
    public void setUp(){
        super.setUp();
        parseCDClass("List","vartas","monticore","cd2code","decorator","ListDecorator");
    }
    //Collection
    @Test
    public void testAdd(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "addList","A");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testAddAll(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "addAllList", "Collection<? extends A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testClear(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "clearList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testContains(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "containsList","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testContainsAll(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "containsAllList","Collection<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testEquals(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "equalsList", "Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testForEach(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "forEachList", "Consumer<? super A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testHashCode(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "hashCodeList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }

    @Test
    public void testIsEmpty(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "isEmptyList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testIterator(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "iteratorList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Iterator<A>");
    }

    @Test
    public void testParallelStream(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "parallelStreamList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Stream<A>");
    }

    @Test
    public void testRemove(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "removeList","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testRemoveAll(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "removeAllList","Collection<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testRemoveIf(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "removeIfList","Predicate<? super A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testRetainAll(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "retainAllList", "Collection<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testSize(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "sizeList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }

    @Test
    public void testSpliterator(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "spliteratorList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Spliterator<A>");
    }

    @Test
    public void testToArray(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "toArrayList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Object[]");
    }

    @Test
    public void testToIntFunctionArray(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "toArrayList","IntFunction<A[]>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A[]");
    }

    @Test
    public void testToGenericArray(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "toArrayList","A[]");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A[]");
    }
    //List
    @Test
    public void testAddList(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "addList","int","A");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }
    @Test
    public void testAddAllList(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "addAllList","int","Collection<? extends A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }
    @Test
    public void testGet(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "getList","int");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A");
    }
    @Test
    public void testIndexOf(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "indexOfList","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }
    @Test
    public void testLastIndexOf(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "lastIndexOfList","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }
    @Test
    public void testListIterator(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "listIteratorList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("ListIterator<A>");
    }
    @Test
    public void testListIteratorIndex(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "listIteratorList", "int");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("ListIterator<A>");
    }
    @Test
    public void testRemoveList(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "removeList", "int");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A");
    }
    @Test
    public void testReplaceAll(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "replaceAllList", "UnaryOperator<A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }
    @Test
    public void testSet(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "setList", "int","A");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A");
    }
    @Test
    public void testSort(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "sortList", "Comparator<? super A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }
    @Test
    public void testSubList(){
        ASTCDMethod cdMethod = getMethod(cdTransformedClass, "subListList", "int","int");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("List<A>");
    }
}
