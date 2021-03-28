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

package zav.mc.math.prettyprint;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.expressions.expressionsbasis._ast.ASTExpressionsBasisNode;
import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.MCBasicsPrettyPrinter;
import zav.mc.math._ast.ASTMethodExpression;
import zav.mc.math._ast.ASTPowExpression;
import zav.mc.math._visitor.MathDelegatorVisitor;
import zav.mc.math._visitor.MathInheritanceVisitor;
import zav.mc.math._visitor.MathVisitor;

public class ArithmeticExpressionsPrettyPrinter extends MathDelegatorVisitor {
    private final IndentPrinter printer;
    public ArithmeticExpressionsPrettyPrinter(IndentPrinter printer){
        this.printer = printer;

        setMathVisitor(new MathSubPrettyPrinter());
        setCommonExpressionsVisitor(new CommonExpressionsPrettyPrinter(printer));
        setExpressionsBasisVisitor(new ExpressionsBasisPrettyPrinter(printer));
        setMCBasicsVisitor(new MCBasicsPrettyPrinter(printer));
        setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
    }

    public String prettyprint(ASTExpressionsBasisNode node){
        node.accept(getRealThis());
        String content = printer.getContent();
        printer.clearBuffer();
        return content;
    }

    private class MathSubPrettyPrinter implements MathInheritanceVisitor {
        private MathVisitor realThis = this;

        @Override
        public MathVisitor getRealThis(){
            return realThis;
        }

        @Override
        public void setRealThis(MathVisitor realThis){
            this.realThis = realThis;
        }

        @Override
        public void handle(ASTPowExpression node){
            node.getLeft().accept(getRealThis());
            printer.print(" ^ ");
            node.getRight().accept(getRealThis());
        }

        @Override
        public void visit(ASTMethodExpression node){
            printer.print(node.getName());
        }
    }
}
