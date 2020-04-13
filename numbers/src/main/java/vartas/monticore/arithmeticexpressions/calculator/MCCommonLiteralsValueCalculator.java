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
import de.monticore.literals.mccommonliterals._ast.ASTSignedBasicDoubleLiteral;
import de.monticore.literals.mccommonliterals._ast.ASTSignedBasicFloatLiteral;
import de.monticore.literals.mccommonliterals._ast.ASTSignedBasicLongLiteral;
import de.monticore.literals.mccommonliterals._ast.ASTSignedNatLiteral;
import de.monticore.literals.mccommonliterals._visitor.MCCommonLiteralsVisitor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

public class MCCommonLiteralsValueCalculator implements MCCommonLiteralsVisitor {
    private final Map<ASTNode, BigDecimal> values;
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
    public void visit(ASTSignedNatLiteral node){
        values.put(node, new BigDecimal(node.getSource()));
    }

    @Override
    public void visit(ASTSignedBasicLongLiteral node){
        values.put(node, new BigDecimal(StringUtils.chop(node.getSource())));
    }

    @Override
    public void visit(ASTSignedBasicFloatLiteral node){
        values.put(node, new BigDecimal(StringUtils.chop(node.getSource())));
    }

    @Override
    public void visit(ASTSignedBasicDoubleLiteral node){
        values.put(node, new BigDecimal(node.getSource()));
    }
}
