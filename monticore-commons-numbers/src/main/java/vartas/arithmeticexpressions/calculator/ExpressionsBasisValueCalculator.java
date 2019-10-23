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
import de.monticore.expressions.expressionsbasis._ast.ASTLiteralExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.expressions.expressionsbasis._visitor.ExpressionsBasisVisitor;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class ExpressionsBasisValueCalculator implements ExpressionsBasisVisitor {
    private Map<ASTNode, BigDecimal> values;
    private ExpressionsBasisVisitor realThis;

    public ExpressionsBasisValueCalculator(Map<ASTNode, BigDecimal> values) {
        this.values = values;
        this.realThis = this;
    }

    @Override
    public ExpressionsBasisVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void setRealThis(ExpressionsBasisVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public void visit(ASTNameExpression name) {
        switch (name.getName().toLowerCase(Locale.ENGLISH)) {
            case "pi": {
                values.put(name, BigDecimal.valueOf(Math.PI));
                break;
            }
            case "e": {
                values.put(name, BigDecimal.valueOf(Math.E));
                break;
            }
            default: {
                throw new IllegalArgumentException("There is no variable with name " + name.getName());
            }
        }
    }

    @Override
    public void endVisit(ASTLiteralExpression node) {
        checkArgument(values.containsKey(node.getLiteral()));

        values.put(node, values.get(node.getLiteral()));
    }
}
