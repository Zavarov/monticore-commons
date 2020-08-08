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

package vartas.monticore.arithmeticexpressions.prettyprint;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.prettyprint.IndentPrinter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ArithmeticExpressionsPrettyPrinterTest extends AbstractTest {
    public static ArithmeticExpressionsPrettyPrinter prettyPrinter;

    @BeforeAll
    public static void setUpAll(){
        IndentPrinter printer = new IndentPrinter();
        prettyPrinter = new ArithmeticExpressionsPrettyPrinter(printer);
    }

    @Nested
    public class PrettyPrinter{
        @ParameterizedTest
        @ValueSource(strings = {
                "sin(pi)",
                "cos(-1.234)",
                "1 ^ 3",
                "cos(-1)",
                "cos(-1L)",
                "cos(-1.234F)",
                "cos(-1.234)"
        })
        public void testPrettyPrintFunction(String argument){
            ASTExpression expression = parse(argument);

            String value = prettyPrinter.prettyprint(expression);

            assertThat(argument).isEqualTo(value);
        }
    }
}
