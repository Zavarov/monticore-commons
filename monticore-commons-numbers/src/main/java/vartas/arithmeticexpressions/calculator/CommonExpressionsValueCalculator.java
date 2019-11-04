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

import de.monticore.ast.ASTNode;
import de.monticore.expressions.commonexpressions._ast.*;
import de.monticore.expressions.commonexpressions._visitor.CommonExpressionsVisitor;
import de.monticore.prettyprint.IndentPrinter;
import vartas.arithmeticexpressions.prettyprint.ArithmeticExpressionsPrettyPrinter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class CommonExpressionsValueCalculator implements CommonExpressionsVisitor {
    private Map<ASTNode, BigDecimal> values;
    private CommonExpressionsVisitor realThis;

    public CommonExpressionsValueCalculator(Map<ASTNode, BigDecimal> values) {
        this.values = values;
        this.realThis = this;
    }

    @Override
    public CommonExpressionsVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void setRealThis(CommonExpressionsVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public void endVisit(ASTBracketExpression node){
        checkArgument(values.containsKey(node.getExpression()));

        values.put(node, values.get(node.getExpression()));
    }

    @Override
    public void visit(ASTCallExpression node) {
        ArithmeticExpressionsPrettyPrinter prettyPrinter = new ArithmeticExpressionsPrettyPrinter(new IndentPrinter());

        throw new IllegalArgumentException("The expression " + prettyPrinter.prettyprint(node) + " is not defined.");
    }

    @Override
    public void visit(ASTFieldAccessExpression node) {
        throw new IllegalArgumentException("Qualified names are not a allowed.");
    }

    @Override
    public void visit(ASTBooleanNotExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTLogicalNotExpression node) {
        throw new IllegalArgumentException("Logical operators are not a allowed.");
    }

    @Override
    public void visit(ASTLessEqualExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTGreaterEqualExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTLessThanExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTGreaterThanExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTEqualsExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTNotEqualsExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTBooleanAndOpExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTBooleanOrOpExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void visit(ASTConditionalExpression node) {
        throw new IllegalArgumentException("Boolean operators are not a allowed.");
    }

    @Override
    public void endVisit(ASTMultExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.multiply(right));
    }

    @Override
    public void endVisit(ASTDivideExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.divide(right, 15, RoundingMode.HALF_EVEN));
    }

    @Override
    public void endVisit(ASTModuloExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.remainder(right));
    }

    @Override
    public void endVisit(ASTPlusExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.add(right));
    }

    @Override
    public void endVisit(ASTMinusExpression node) {
        checkArgument(values.containsKey(node.getLeft()));
        checkArgument(values.containsKey(node.getRight()));

        BigDecimal left = values.get(node.getLeft());
        BigDecimal right = values.get(node.getRight());

        values.put(node, left.subtract(right));
    }
}