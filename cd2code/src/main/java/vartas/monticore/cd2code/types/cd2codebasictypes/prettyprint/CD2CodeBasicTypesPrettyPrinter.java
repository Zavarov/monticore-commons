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

package vartas.monticore.cd2code.types.cd2codebasictypes.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import vartas.monticore.cd2code.types.cd2codebasictypes._ast.ASTMCInstantType;
import vartas.monticore.cd2code.types.cd2codebasictypes._ast.ASTMCURLType;
import vartas.monticore.cd2code.types.cd2codebasictypes._visitor.CD2CodeBasicTypesVisitor;

public class CD2CodeBasicTypesPrettyPrinter implements CD2CodeBasicTypesVisitor {
    private CD2CodeBasicTypesVisitor realThis = this;
    private final IndentPrinter printer;
    public CD2CodeBasicTypesPrettyPrinter(IndentPrinter printer){
        this.printer = printer;
    }

    @Override
    public void visit(ASTMCInstantType ast){
        this.printer.print(ast.getName());
    }

    @Override
    public void visit(ASTMCURLType ast){
        this.printer.print(ast.getName());
    }

    @Override
    public CD2CodeBasicTypesVisitor getRealThis(){
        return realThis;
    }

    @Override
    public void setRealThis(CD2CodeBasicTypesVisitor realThis){
        this.realThis = realThis;
    }
}
