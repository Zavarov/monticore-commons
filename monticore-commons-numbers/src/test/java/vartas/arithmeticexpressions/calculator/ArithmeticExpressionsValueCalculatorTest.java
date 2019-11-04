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

package vartas.arithmeticexpressions.calculator;

import org.assertj.core.data.Percentage;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import vartas.AbstractTest;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ArithmeticExpressionsValueCalculatorTest extends AbstractTest {
    private static Percentage precision = Percentage.withPercentage(10e-15);

    @RunWith(Parameterized.class)
    public static class ArithmeticExpressionsValueCalculator{
        @Parameters
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][] {
                    { "tan(1.5)", Math.tan(1.5) },
                    { "tan@1.5", Math.tan(1.5) },
                    { "sqrt@1.5", Math.sqrt(1.5) },
                    { "sin@1.5", Math.sin(1.5) },
                    { "min(-1,2)", Math.min(-1,2)},
                    { "max(-1,2)", Math.max(-1,2)},
                    { "ln@1.5", Math.log(1.5) },
                    { "log@1.5", Math.log10(1.5) },
                    { "floor@1.5", Math.floor(1.5) },
                    { "cos@1.5", Math.cos(1.5) },
                    { "ceil@1.5", Math.ceil(1.5) },
                    { "atan@1.5", Math.atan(1.5) },
                    { "asin@1.0", Math.asin(1.0) },
                    { "acos@1.0", Math.acos(1.0) },
                    { "abs@-1", Math.abs(-1) },
                    { "random(1,1)", 1 },
                    { "2^3", Math.pow(2,3) },
                    { "7-3.33", 7-3.33 },
                    { "7+3.33", 7+3.33 },
                    { "5%3", 5%3 },
                    { "3/2", 1.5 },
                    { "3/2.0", 1.5 },
                    { "3.0/2", 1.5 },
                    { "1.5*3", 1.5*3 },
                    { "e", Math.E },
                    { "pi", Math.PI },
                    { "1.0", 1.0 },
                    { "-1.0", -1.0 },
                    { "1.0F", 1.0 },
                    { "-1.0F", -1.0 },
                    { "1", 1 },
                    { "-1", -1 },
                    { "1L", 1  },
                    { "-1L", -1 },
            });
        }

        @Parameter
        public String arg;
        @Parameter(1)
        public double expected;

        @Test
        public void testExpression(){
            assertThat(valueOf(arg).doubleValue()).isCloseTo(expected, precision);
        }
    }

    @RunWith(Parameterized.class)
    public static class ArithmeticExpressionsInvalidValueCalculator{
        @Parameters
        public static Object[] data() {
            return new Object[] {
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
            };
        }

        @Parameter
        public String arg;

        @Test
        public void testExpression(){
            assertThat(valueOfOpt(arg)).isNotPresent();
        }
    }
}
