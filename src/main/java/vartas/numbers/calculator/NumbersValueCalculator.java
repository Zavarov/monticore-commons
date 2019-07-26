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

package vartas.numbers.calculator;

import de.monticore.ast.ASTNode;
import vartas.numbers._ast.ASTDecimalNumber;
import vartas.numbers._ast.ASTFloatingPointNumber;
import vartas.numbers._ast.ASTSignedDecimalNumber;
import vartas.numbers._ast.ASTSignedFloatingPointNumber;
import vartas.numbers._visitor.NumbersVisitor;

import java.math.BigDecimal;
import java.util.Map;

public class NumbersValueCalculator implements NumbersVisitor {
    private Map<ASTNode, BigDecimal> values;
    private NumbersVisitor realThis;

    public NumbersValueCalculator(Map<ASTNode, BigDecimal> values){
        this.realThis = this;
        this.values = values;
    }

    @Override
    public NumbersVisitor getRealThis(){
        return realThis;
    }

    @Override
    public void setRealThis(NumbersVisitor realThis){
        this.realThis = realThis;
    }

    @Override
    public void visit(ASTSignedDecimalNumber node) {
        values.put(node, node.getValue());
    }

    @Override
    public void visit(ASTDecimalNumber node) {
        values.put(node, node.getValue());
    }

    @Override
    public void visit(ASTSignedFloatingPointNumber node) {
        values.put(node, node.getValue());
    }

    @Override
    public void visit(ASTFloatingPointNumber node) {
        values.put(node, node.getValue());
    }
}
