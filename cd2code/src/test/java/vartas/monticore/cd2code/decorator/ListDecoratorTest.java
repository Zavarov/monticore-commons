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
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "addList","A");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testAddAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "addAllList", "Collection<? extends A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testClear(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "clearList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testContains(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "containsList","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testContainsAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "containsAllList","Collection<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testEquals(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "equalsList", "Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testForEach(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "forEachList", "Consumer<? super A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testHashCode(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "hashCodeList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }

    @Test
    public void testIsEmpty(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "isEmptyList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testIterator(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "iteratorList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Iterator<A>");
    }

    @Test
    public void testParallelStream(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "parallelStreamList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Stream<A>");
    }

    @Test
    public void testRemove(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "removeList","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testRemoveAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "removeAllList","Collection<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testRemoveIf(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "removeIfList","Predicate<? super A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testRetainAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "retainAllList", "Collection<?>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }

    @Test
    public void testSize(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "sizeList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }

    @Test
    public void testSpliterator(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "spliteratorList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Spliterator<A>");
    }

    @Test
    public void testToArray(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "toArrayList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Object[]");
    }

    @Test
    public void testToIntFunctionArray(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "toArrayList","IntFunction<A[]>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A[]");
    }

    @Test
    public void testToGenericArray(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "toArrayList","A[]");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A[]");
    }
    //List
    @Test
    public void testAddList(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "addList","int","A");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }
    @Test
    public void testAddAllList(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "addAllList","int","Collection<? extends A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("boolean");
    }
    @Test
    public void testGet(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "getList","int");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A");
    }
    @Test
    public void testIndexOf(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "indexOfList","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }
    @Test
    public void testLastIndexOf(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "lastIndexOfList","Object");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("int");
    }
    @Test
    public void testListIterator(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "listIteratorList");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("ListIterator<A>");
    }
    @Test
    public void testListIteratorIndex(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "listIteratorList", "int");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("ListIterator<A>");
    }
    @Test
    public void testRemoveList(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "removeList", "int");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A");
    }
    @Test
    public void testReplaceAll(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "replaceAllList", "UnaryOperator<A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }
    @Test
    public void testSet(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "setList", "int","A");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("A");
    }
    @Test
    public void testSort(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "sortList", "Comparator<? super A>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }
    @Test
    public void testSubList(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "subListList", "int","int");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("List<A>");
    }
}
