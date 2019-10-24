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

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import org.assertj.core.data.Percentage;
import org.junit.Test;
import vartas.AbstractTest;
import vartas.arithmeticexpressions._parser.ArithmeticExpressionsParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ArithmeticExpressionsValueCalculatorTest extends AbstractTest {
    private Percentage precision = Percentage.withPercentage(10e-15);

    private ASTExpression parse(String expression){
        try{
            ArithmeticExpressionsParser parser = new ArithmeticExpressionsParser();

            Optional<ASTExpression> optional = parser.parse_String(expression);
            if(parser.hasErrors())
                throw new IllegalArgumentException();
                //fail("The parser encountered errors while parsing "+expression);
            if(!optional.isPresent())
                throw new IllegalArgumentException();
                //fail("The expression couldn't be parsed");

            return optional.get();
        }catch(IOException e){
            //fail(e.getMessage());
            //return null;
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal valueOf(String expression){
        return ArithmeticExpressionsValueCalculator.valueOf(parse(expression));
    }

    //***------------------------------------------------------------------------------------------------------------***
    //***---------------------------------------------  Literals  ---------------------------------------------------***
    //***------------------------------------------------------------------------------------------------------------***

    @Test
    public void testDecimal(){
        assertThat(valueOf("1").doubleValue()).isCloseTo(1.0, precision);
    }

    @Test
    public void testSignedDecimal(){
        assertThat(valueOf("-1").doubleValue()).isCloseTo(-1.0, precision);
    }

    @Test
    public void testFloatingPoint(){
        assertThat(valueOf("1.0").doubleValue()).isCloseTo(1.0, precision);
    }

    @Test
    public void testSignedFloatingPoint(){
        assertThat(valueOf("-1.0").doubleValue()).isCloseTo(-1.0, precision);
    }
    //***------------------------------------------------------------------------------------------------------------***
    //***------------------------------------------  Expressions-----------------------------------------------------***
    //***------------------------------------------------------------------------------------------------------------***

    @Test
    public void testPi(){
        assertThat(valueOf("pi").doubleValue()).isCloseTo(Math.PI, precision);
    }

    @Test
    public void testE(){
        assertThat(valueOf("e").doubleValue()).isCloseTo(Math.E, precision);
    }

    @Test
    public void testMultiplication(){
        assertThat(valueOf("1.5*3").doubleValue()).isCloseTo(4.5, precision);
    }

    @Test
    public void testDivision(){
        assertThat(valueOf("3/2").doubleValue()).isCloseTo(1.5, precision);
        assertThat(valueOf("3/2.0").doubleValue()).isCloseTo(1.5, precision);
        assertThat(valueOf("3.0/2").doubleValue()).isCloseTo(1.5, precision);
    }

    @Test
    public void testModulo(){
        assertThat(valueOf("5%3").doubleValue()).isCloseTo(2.0, precision);
    }

    @Test
    public void testAddition(){
        assertThat(valueOf("7+3.33").doubleValue()).isCloseTo(10.33, precision);
    }

    @Test
    public void testSubtraction(){
        assertThat(valueOf("7-3.33").doubleValue()).isCloseTo(7-3.33, precision);
    }

    @Test
    public void testPower(){
        assertThat(valueOf("2^3").doubleValue()).isCloseTo(8.0, precision);
    }

    @Test
    public void testRandomNumber(){
        assertThat(valueOf("random(1,2)").doubleValue()).isBetween(1.0, 5.0);
    }

    @Test
    public void testAbsolute(){
        assertThat(valueOf("abs@-1").doubleValue()).isCloseTo(1.0, precision);
    }

    @Test
    public void testACos(){
        assertThat(valueOf("acos@1.0").doubleValue()).isCloseTo(Math.acos(1.0), precision);
    }

    @Test
    public void testASin(){
        assertThat(valueOf("asin@1.0").doubleValue()).isCloseTo(Math.asin(1.0), precision);
    }

    @Test
    public void testATan(){
        assertThat(valueOf("atan@1.0").doubleValue()).isCloseTo(Math.atan(1.0), precision);
    }

    @Test
    public void testCeil(){
        assertThat(valueOf("ceil@1.5").doubleValue()).isCloseTo(Math.ceil(1.5), precision);
    }

    @Test
    public void testCos(){
        assertThat(valueOf("cos@1.0").doubleValue()).isCloseTo(Math.cos(1.0), precision);
    }

    @Test
    public void testFloor(){
        assertThat(valueOf("floor@1.5").doubleValue()).isCloseTo(Math.floor(1.5), precision);
    }

    @Test
    public void testLog(){
        assertThat(valueOf("log@1.5").doubleValue()).isCloseTo(Math.log10(1.5), precision);
    }

    @Test
    public void testLn(){
        assertThat(valueOf("ln@1.5").doubleValue()).isCloseTo(Math.log(1.5), precision);
    }

    @Test
    public void testMax(){
        assertThat(valueOf("max(-1, 2)").doubleValue()).isCloseTo(Math.max(-1, 2), precision);
    }

    @Test
    public void testMin(){
        assertThat(valueOf("min(-1, 2)").doubleValue()).isCloseTo(Math.min(-1, 2), precision);
    }

    @Test
    public void testSin(){
        assertThat(valueOf("sin@1.5").doubleValue()).isCloseTo(Math.sin(1.5), precision);
    }

    @Test
    public void testSqrt(){
        assertThat(valueOf("sqrt@1.5").doubleValue()).isCloseTo(Math.sqrt(1.5), precision);
    }

    @Test
    public void testTan(){
        assertThat(valueOf("tan@1.5").doubleValue()).isCloseTo(Math.tan(1.5), precision);
    }
}
