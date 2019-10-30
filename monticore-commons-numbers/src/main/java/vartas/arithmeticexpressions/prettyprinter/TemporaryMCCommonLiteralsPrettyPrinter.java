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

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.literals.mccommonliterals._ast.ASTSignedBasicDoubleLiteral;
import de.monticore.literals.mccommonliterals._ast.ASTSignedBasicFloatLiteral;
import de.monticore.literals.mccommonliterals._ast.ASTSignedBasicLongLiteral;
import de.monticore.literals.mccommonliterals._ast.ASTSignedNatLiteral;
import de.monticore.prettyprint.IndentPrinter;

/**
 * This class is a temporary solution for the ${@link MCCommonLiteralsPrettyPrinter},
 * due to a bug regarding negative numbers and some quirks with respect to signed doubles.
 * #TODO Remove once MontiCore has been patched.
 */
public class TemporaryMCCommonLiteralsPrettyPrinter extends MCCommonLiteralsPrettyPrinter {
    public TemporaryMCCommonLiteralsPrettyPrinter(IndentPrinter printer) {
        super(printer);
    }

    @Override
    public void visit(ASTSignedNatLiteral node){
        if(node.isNegative())
            printer.print("-");
        printer.print(node.getDigits());
    }

    @Override
    public void visit(ASTSignedBasicLongLiteral node){
        if(node.isNegative())
            printer.print("-");
        printer.print(node.getDigits());
        printer.print("L");
    }

    @Override
    public void visit(ASTSignedBasicFloatLiteral node){
        if(node.isNegative())
            printer.print("-");
        printer.print(node.getPre());
        printer.print(".");
        printer.print(node.getPost());
        printer.print("F");
    }

    @Override
    public void visit(ASTSignedBasicDoubleLiteral node){
        if(node.isNegative())
            printer.print("-");
        printer.print(node.getPre());
        printer.print(".");
        printer.print(node.getPost());
    }
}
