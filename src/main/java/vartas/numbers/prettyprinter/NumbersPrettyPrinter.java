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

package vartas.numbers.prettyprinter;

import de.monticore.prettyprint.IndentPrinter;
import vartas.numbers._ast.ASTDecimalNumber;
import vartas.numbers._ast.ASTFloatingPointNumber;
import vartas.numbers._ast.ASTSignedDecimalNumber;
import vartas.numbers._ast.ASTSignedFloatingPointNumber;
import vartas.numbers._visitor.NumbersVisitor;

public class NumbersPrettyPrinter implements NumbersVisitor {
    private IndentPrinter printer;
    private NumbersVisitor realThis;

    public NumbersPrettyPrinter(IndentPrinter printer){
        this.realThis = this;
        this.printer = printer;
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
    public void handle(ASTDecimalNumber node){
        printer.print(node.getSource());
    }

    @Override
    public void handle(ASTSignedDecimalNumber node){
        printer.print(node.getSource());
    }

    @Override
    public void handle(ASTFloatingPointNumber node){
        printer.print(node.getPre().getSource());
        printer.print(".");
        printer.print(node.getPost());
    }

    @Override
    public void handle(ASTSignedFloatingPointNumber node){
        printer.print(node.getPre().getSource());
        printer.print(".");
        printer.print(node.getPost());
    }
}
