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

package vartas.discord.argument.visitor;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import org.assertj.core.data.Percentage;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import vartas.discord.argument._ast.ASTArgument;
import vartas.discord.argument._ast.ASTDateArgument;
import vartas.discord.call._parser.CallParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator.valueOf;

@RunWith(Enclosed.class)
public class ContextSensitiveArgumentVisitorTest {
    protected static Percentage precisision = Percentage.withPercentage(1e-15);
    protected static ASTArgument parse(String content) throws IOException {
        CallParser parser = new CallParser();
        return parser.parse_StringArgument(content).get();
    }

    public static class ContextSensitiveTest extends ContextSensitiveArgumentVisitor{
        @Test
        public void testSetRealThis(){
            super.setRealThis(null);
            assertThat(super.realThis).isNull();
        }
        @Test
        public void testGetRealThis(){
            assertThat(super.getRealThis()).isEqualTo(this);
        }
    }

    public static class ContextSensitiveDateTest extends ContextSensitiveArgumentVisitor{
        protected ASTArgument argument;

        @Before
        public void setUp() throws IOException {
            argument = parse("22-11-3333");
        }

        @Test
        public void testVisit(){
            argument.accept(getRealThis());
        }

        @Override
        public void visit(ASTDateArgument ast){
            assertThat(valueOf(ast.getDay()).intValueExact()).isEqualTo(22);
            assertThat(valueOf(ast.getMonth()).intValueExact()).isEqualTo(11);
            assertThat(valueOf(ast.getYear()).intValueExact()).isEqualTo(3333);
        }

        @Override
        public void visit(ASTExpression ast){
            assertThat(valueOf(ast).intValueExact()).isEqualTo(22-11-3333);
        }

        @Override
        public void visit(ASTStringLiteral ast){
            assertThat(ast.getValue()).isEqualTo("22-11-3333");
        }
    }

    @RunWith(Parameterized.class)
    public static class ContextSensitiveRawTextTest extends ContextSensitiveArgumentVisitor{
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    { "pi",   Math.PI },
                    { "e",    Math.E },
                    { "wololo", Double.NaN },
            });
        }

        @Parameter
        public String input;

        @Parameter(1)
        public double expected;

        protected ASTArgument argument;

        @Before
        public void setUp() throws IOException {
            argument = parse(input);
        }

        @Test
        public void testVisit(){
            argument.accept(getRealThis());
        }

        @Override
        public void visit(ASTExpression ast){
            assertThat(valueOf(ast).doubleValue()).isCloseTo(expected, precisision);
        }

        @Override
        public void visit(ASTStringLiteral ast){
            assertThat(ast.getValue()).isEqualTo(input);
        }
    }
}
