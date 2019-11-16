/*
 * Copyright (c) 2019 Zavarov
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

package vartas.arithmeticexpressions.prettyprint;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.prettyprint.IndentPrinter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import vartas.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ArithmeticExpressionsPrettyPrinterTest extends AbstractTest {
    public static ArithmeticExpressionsPrettyPrinter prettyPrinter;

    @BeforeClass
    public static void setUp(){
        IndentPrinter printer = new IndentPrinter();
        prettyPrinter = new ArithmeticExpressionsPrettyPrinter(printer);
    }

    @RunWith(Parameterized.class)
    public static class PrettyPrinter{
        @Parameters
        public static Object[] data() {
            return new Object[] {
                    "sin(pi)",
                    "cos@-1.234",
                    "1 ^ 3",
                    "cos@-1",
                    "cos@-1L",
                    "cos@-1.234F",
                    "cos@-1.234"
            };
        }

        @Parameter
        public String argument;

        @Test
        public void testPrettyPrintFunction(){
            ASTExpression expression = parse(argument);

            String value = prettyPrinter.prettyprint(expression);

            assertThat(argument).isEqualTo(value);
        }
    }
}
