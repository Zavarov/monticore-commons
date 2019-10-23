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
import de.monticore.literals.mccommonliterals._ast.*;
import de.monticore.literals.mccommonliterals._visitor.MCCommonLiteralsVisitor;

import java.math.BigDecimal;
import java.util.Map;

public class MCCommonLiteralsValueCalculator implements MCCommonLiteralsVisitor {
    private Map<ASTNode, BigDecimal> values;
    private MCCommonLiteralsVisitor realThis;

    public MCCommonLiteralsValueCalculator(Map<ASTNode, BigDecimal> values) {
        this.values = values;
        this.realThis = this;
    }

    @Override
    public MCCommonLiteralsVisitor getRealThis() {
        return realThis;
    }

    @Override
    public void setRealThis(MCCommonLiteralsVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public void visit(ASTNatLiteral node){
        values.put(node, new BigDecimal(node.getSource()));
    }

    @Override
    public void visit(ASTSignedNatLiteral node){
        //getSource() is bugged in 5.3.0 for negative numbers
        String source = (node.isNegative() ? "-" : "") + node.getDigits();
        values.put(node, new BigDecimal(source));
    }

    @Override
    public void visit(ASTBasicLongLiteral node){
        //BigDecimal doesn't like the L at the end
        String source = node.getDigits();

        values.put(node, new BigDecimal(source));
    }

    @Override
    public void visit(ASTSignedBasicLongLiteral node){
        //getSource() is bugged in 5.3.0 for negative numbers
        String source = (node.isNegative() ? "-" : "") + node.getDigits();
        values.put(node, new BigDecimal(source));
    }

    @Override
    public void visit(ASTBasicFloatLiteral node){
        //BigDecimal doesn't like the F at the end
        String source = node.getPre() + "." + node.getPost();

        values.put(node, new BigDecimal(source));
    }

    @Override
    public void visit(ASTSignedBasicFloatLiteral node){
        //getSource() is bugged in 5.3.0 for negative numbers
        String source = (node.isNegative() ? "-" : "") + node.getPre() + "." + node.getPost();
        values.put(node, new BigDecimal(source));
    }

    @Override
    public void visit(ASTBasicDoubleLiteral node){
        //BigDecimal doesn't like the D at the end
        String source = node.getPre() + "." + node.getPost();

        values.put(node, new BigDecimal(source));
    }

    @Override
    public void visit(ASTSignedBasicDoubleLiteral node){
        //getSource() is bugged in 5.3.0 for negative numbers
        String source = (node.isNegative() ? "-" : "") + node.getPre() + "." + node.getPost();

        values.put(node, new BigDecimal(source));
    }
}
