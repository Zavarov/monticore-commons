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

package vartas.arithmeticexpressions.prettyprinter;

import de.monticore.expressions.expressionsbasis._ast.ASTExpressionsBasisNode;
import de.monticore.expressions.prettyprint2.CommonExpressionsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.MCBasicsPrettyPrinter;
import vartas.arithmeticexpressions._ast.ASTArithmeticExpressionsNode;
import vartas.arithmeticexpressions._ast.ASTAtArgument;
import vartas.arithmeticexpressions._ast.ASTBracketArgument;
import vartas.arithmeticexpressions._visitor.ArithmeticExpressionsDelegatorVisitor;
import vartas.arithmeticexpressions._visitor.ArithmeticExpressionsVisitor;
import vartas.numbers.prettyprinter.NumbersPrettyPrinter;

public class ArithmeticExpressionPrettyPrinter extends ArithmeticExpressionsDelegatorVisitor {
    private IndentPrinter printer;
    public ArithmeticExpressionPrettyPrinter(IndentPrinter printer){
        this.printer = printer;

        setArithmeticExpressionsVisitor(new ArithmeticExpressionSublanguagePrettyPrinter(printer));
        setCommonExpressionsVisitor(new CommonExpressionsPrettyPrinter(printer));
        setMCBasicsVisitor(new MCBasicsPrettyPrinter(printer));
        setNumbersVisitor(new NumbersPrettyPrinter(printer));
    }

    public String prettyprint(ASTArithmeticExpressionsNode node){
        node.accept(getRealThis());
        String content = printer.getContent();
        printer.clearBuffer();
        return content;
    }

    public String prettyprint(ASTExpressionsBasisNode node){
        node.accept(getRealThis());
        String content = printer.getContent();
        printer.clearBuffer();
        return content;
    }

    private static class ArithmeticExpressionSublanguagePrettyPrinter implements ArithmeticExpressionsVisitor {
        private ArithmeticExpressionsVisitor realThis;
        private IndentPrinter printer;

        private ArithmeticExpressionSublanguagePrettyPrinter(IndentPrinter printer){
            this.printer = printer;
            this.realThis = this;
        }

        @Override
        public ArithmeticExpressionsVisitor getRealThis(){
            return realThis;
        }

        @Override
        public void setRealThis(ArithmeticExpressionsVisitor realThis){
            this.realThis = realThis;
        }

        @Override
        public void visit(ASTAtArgument node){
            printer.print("@");
        }

        @Override
        public void visit(ASTBracketArgument node){
            printer.print("(");
        }

        @Override
        public void endVisit(ASTBracketArgument node){
            printer.print(")");
        }
    }
}
