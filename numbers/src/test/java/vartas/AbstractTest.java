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

package vartas;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import vartas.monticore.arithmeticexpressions._parser.ArithmeticExpressionsParser;
import vartas.monticore.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractTest {
    public static ASTExpression parse(String expression){
        try{
            ArithmeticExpressionsParser parser = new ArithmeticExpressionsParser();

            Optional<ASTExpression> optional = parser.parse_String(expression);
            if(parser.hasErrors())
                throw new IllegalArgumentException();
            //fail("The parser encountered errors while parsing "+expression);
            if(optional.isEmpty())
                throw new IllegalArgumentException();
            //fail("The expression couldn't be parsed");

            return optional.get();
        }catch(IOException e){
            //fail(e.getMessage());
            //return null;
            throw new IllegalArgumentException();
        }
    }

    public static Optional<BigDecimal> valueOfOpt(String expression){
        return ArithmeticExpressionsValueCalculator.valueOf(parse(expression));
    }

    public static BigDecimal valueOf(String expression){
        Optional<BigDecimal> valueOpt = valueOfOpt(expression);
        assertThat(valueOpt).isPresent();
        return valueOpt.get();
    }
}
