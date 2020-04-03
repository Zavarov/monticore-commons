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

package vartas.monticore.cd2code.types.cd2codecollectiontypes.prettyprint;

import de.monticore.prettyprint.IndentPrinter;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._ast.ASTMCCacheType;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._visitor.CD2CodeCollectionTypesVisitor;

public class CD2CodeCollectionPrettyPrinter implements CD2CodeCollectionTypesVisitor {
    private CD2CodeCollectionTypesVisitor realThis = this;
    private final IndentPrinter printer;
    public CD2CodeCollectionPrettyPrinter(IndentPrinter printer){
        this.printer = printer;
    }

    @Override
    public void handle(ASTMCCacheType ast){
        printer.print(ast.getName(0));
        printer.print("<");
        ast.getKey().accept(getRealThis());
        printer.print(",");
        ast.getValue().accept(getRealThis());
        printer.print(">");
    }

    @Override
    public CD2CodeCollectionTypesVisitor getRealThis(){
        return realThis;
    }

    @Override
    public void setRealThis(CD2CodeCollectionTypesVisitor realThis){
        this.realThis = realThis;
    }
}
