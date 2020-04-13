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

import de.monticore.ast.ASTNode;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import vartas.monticore.arithmeticexpressions._ast.*;
import vartas.monticore.arithmeticexpressions._visitor.ArithmeticExpressionsDelegatorVisitor;
import vartas.monticore.arithmeticexpressions._visitor.ArithmeticExpressionsVisitor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

public class ArithmeticExpressionsValueCalculator extends ArithmeticExpressionsDelegatorVisitor {
    /**
     * The maximum number of rolls that are allowed at once.
     */
    private static final long MAX_EYE = 1024;
    /**
     * The maximum number of eyes a die can have.
     */
    private static final long MAX_DICE = 256;
    /**
     * The number generator for the dice.
     */
    private static final Random rng = new Random();
    /**
     * Contains the value of each subexpression.
     */
    private final Map<ASTNode, BigDecimal> values = new HashMap<>();

    /**
     * @param expression the arithmetic expression that is going to be evaluated.
     * @return the value of the expression.
     */
    public static Optional<BigDecimal> valueOf(ASTExpression expression) throws IllegalStateException{
        try {
            ArithmeticExpressionsValueCalculator calculator = new ArithmeticExpressionsValueCalculator();
            expression.accept(calculator);
            return calculator.getValue(expression);
        }catch(IllegalArgumentException e){
            return Optional.empty();
        }
    }

    public Optional<BigDecimal> getValue(ASTExpression expression) {
        return values.containsKey(expression) ? Optional.of(values.get(expression)) : Optional.empty();
    }

    protected ArithmeticExpressionsValueCalculator(){
        setArithmeticExpressionsVisitor(new ArithmeticExpressionsValueCalculatorSubCalculator());
        setCommonExpressionsVisitor(new CommonExpressionsValueCalculator(values));
        setExpressionsBasisVisitor(new ExpressionsBasisValueCalculator(values));
        setMCCommonLiteralsVisitor(new MCCommonLiteralsValueCalculator(values));
    }

    private class ArithmeticExpressionsValueCalculatorSubCalculator implements ArithmeticExpressionsVisitor {
        private ArithmeticExpressionsVisitor realThis = this;

        @Override
        public ArithmeticExpressionsVisitor getRealThis(){
            return realThis;
        }

        @Override
        public void setRealThis(ArithmeticExpressionsVisitor realThis){
            this.realThis = realThis;
        }

        @Override
        public void endVisit(ASTLiteralExpression node){
            checkArgument(values.containsKey(node.getSignedLiteral()));

            values.put(node, values.get(node.getSignedLiteral()));
        }

        @Override
        public void endVisit(ASTAtExpression node){
            checkArgument(values.containsKey(node.getExpression()));

            values.put(node, values.get(node.getExpression()));
        }

        @Override
        public void endVisit(ASTPowExpression node) {
            checkArgument(values.containsKey(node.getLeft()));
            checkArgument(values.containsKey(node.getRight()));

            BigDecimal left = values.get(node.getLeft());
            BigDecimal right = values.get(node.getRight());

            values.put(node, BigDecimal.valueOf(Math.pow(left.doubleValue(), right.doubleValue())));
        }

        @Override
        public void endVisit(ASTRandomNumberExpression node) {
            checkArgument(values.containsKey(node.getDice()));
            checkArgument(values.containsKey(node.getEyes()));
            checkArgument(values.get(node.getDice()).intValueExact() <= MAX_DICE, "You can at most roll " + MAX_DICE + " dice.");
            checkArgument(values.get(node.getEyes()).intValueExact() <= MAX_EYE, "A die can have at most " + MAX_EYE + " eyes.");
            checkArgument(values.get(node.getDice()).intValueExact() > 0, "You need to roll at least one die");
            checkArgument(values.get(node.getEyes()).intValueExact() > 0, "A die needs to have at least one eye");

            int dice = values.get(node.getDice()).intValueExact();
            int eyes = values.get(node.getEyes()).intValueExact();
            BigDecimal result = BigDecimal.ZERO;

            for (int i = 0; i < dice; ++i) {
                //+1 since rng returns values from [0,eyes)
                result = result.add(BigDecimal.valueOf(rng.nextInt(eyes) + 1));
            }

            values.put(node, result);
        }

        @Override
        public void endVisit(ASTAbsExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, value.abs());
        }

        @Override
        public void endVisit(ASTACosExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.acos(value.doubleValue())));
        }

        @Override
        public void endVisit(ASTASinExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.asin(value.doubleValue())));
        }

        @Override
        public void endVisit(ASTATanExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.atan(value.doubleValue())));
        }

        @Override
        public void endVisit(ASTCeilExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.ceil(value.doubleValue())));
        }

        @Override
        public void endVisit(ASTCosExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.cos(value.doubleValue())));
        }

        @Override
        public void endVisit(ASTFloorExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.floor(value.doubleValue())));
        }

        @Override
        public void endVisit(ASTLogExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.log10(value.doubleValue())));
        }

        @Override
        public void endVisit(ASTLnExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.log(value.doubleValue())));
        }

        @Override
        public void endVisit(ASTMaxExpression node) {
            checkArgument(values.containsKey(node.getLeft()));
            checkArgument(values.containsKey(node.getRight()));

            BigDecimal left = values.get(node.getLeft());
            BigDecimal right = values.get(node.getRight());

            values.put(node, left.max(right));
        }

        @Override
        public void endVisit(ASTMinExpression node) {
            checkArgument(values.containsKey(node.getLeft()));
            checkArgument(values.containsKey(node.getRight()));

            BigDecimal left = values.get(node.getLeft());
            BigDecimal right = values.get(node.getRight());

            values.put(node, left.min(right));
        }

        @Override
        public void endVisit(ASTSinExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.sin(value.doubleValue())));
        }

        @Override
        public void endVisit(ASTSqrtExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.sqrt(value.doubleValue())));
        }

        @Override
        public void endVisit(ASTTanExpression node) {
            checkArgument(values.containsKey(node.getArgument()));

            BigDecimal value = values.get(node.getArgument());

            values.put(node, BigDecimal.valueOf(Math.tan(value.doubleValue())));
        }
    }
}
