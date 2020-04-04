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

public class OptionalDecoratorTest extends AbstractDecoratorTest {
    @BeforeEach
    public void setUp(){
        super.setUp();
        parseCDClass("Optional","vartas","monticore","cd2code","decorator","OptionalDecorator");
    }

    @Test
    public void testGet(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "getOptional");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("Optional<String>");
    }

    @Test
    public void testSetOptional(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "setOptional", "Optional<String>");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testSet(){
        ASTCDMethod cdMethod = getMethod(cdDecoratedClass, "setOptional", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }
}
