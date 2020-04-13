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

package vartas.monticore.arithmeticexpressions.calculator;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.AbstractTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArithmeticExpressionsValueCalculatorTest extends AbstractTest {
    private static Percentage precision = Percentage.withPercentage(10e-15);

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class ArithmeticExpressionsValueCalculator{
        @ParameterizedTest
        @MethodSource("getArguments")
        public void testExpression(Pair<String, ? extends Number> argument){
            String expression = argument.getKey();
            double expected = argument.getValue().doubleValue();
            assertThat(valueOf(expression).doubleValue()).isCloseTo(expected, precision);
        }

        List<Pair<String, ? extends Number>> getArguments(){
            return Arrays.asList(
                    Pair.of("tan(1.5)", Math.tan(1.5)),
                    Pair.of("sqrt(1.5)", Math.sqrt(1.5)),
                    Pair.of("sin@1.5", Math.sin(1.5)),
                    Pair.of("min(-1,2)", Math.min(-1, 2)),
                    Pair.of("max(-1,2)", Math.max(-1, 2)),
                    Pair.of("max(-1,2)", Math.max(-1, 2)),
                    Pair.of("ln(1.5)", Math.log(1.5)),
                    Pair.of("log(1.5)", Math.log10(1.5)),
                    Pair.of("floor@1.5", Math.floor(1.5)),
                    Pair.of("cos@1.5", Math.cos(1.5)),
                    Pair.of("ceil@1.5", Math.ceil(1.5)),
                    Pair.of("atan@1.5", Math.atan(1.5)),
                    Pair.of("asin@1.0", Math.asin(1.0)),
                    Pair.of("acos@1.0", Math.acos(1.0)),
                    Pair.of("abs@1.0", Math.abs(-1)),
                    Pair.of("random(1,1)", 1),
                    Pair.of("2^3", Math.pow(2, 3)),
                    Pair.of("7-3.33", 7-3.33),
                    Pair.of("7+3.33", 7+3.33),
                    Pair.of("5%3", 5%3),
                    Pair.of("3/2", 1.5),
                    Pair.of("3/2.0", 1.5),
                    Pair.of("3.0/2", 1.5),
                    Pair.of("1.5*3", 1.5*3),
                    Pair.of("e", Math.E),
                    Pair.of("pi", Math.PI),
                    Pair.of("1.0", 1.0),
                    Pair.of("-1.0", -1.0),
                    Pair.of("1.0F", 1.0),
                    Pair.of("-1.0F", -1.0),
                    Pair.of("1", 1),
                    Pair.of("-1", -1),
                    Pair.of("1L", 1),
                    Pair.of("-1L", -1)
            );
        }
    }

    @Nested
    public class ArithmeticExpressionsInvalidValueCalculator{
        @ParameterizedTest
        @ValueSource(strings = {
                "x",
                "false",
                "@x",
                "x^1",
                "1^x",
                "random(x,1)",
                "random(1,x)",
                "random(99999999,1)",
                "random(1,99999999)",
                "abs(x)",
                "acos(x)",
                "asin(x)",
                "atan(x)",
                "ceil(x)",
                "cos(x)",
                "floor(x)",
                "log(x)",
                "ln(x)",
                "max(x,1)",
                "max(1,x)",
                "min(x,1)",
                "min(1,x)",
                "sin(x)",
                "sqrt(x)",
                "tan(x)",
                "asin(1.5)",
                "acos(1.5)"
        })
        public void testExpression(String argument){
            assertThat(valueOfOpt(argument)).isNotPresent();
        }
    }
}
