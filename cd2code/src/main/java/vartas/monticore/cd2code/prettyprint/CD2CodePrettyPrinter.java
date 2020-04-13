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

package vartas.monticore.cd2code.prettyprint;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.cd.cd4analysis._ast.ASTCD4AnalysisNode;
import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDParameter;
import de.monticore.cd.prettyprint.CDPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.MCBasicsPrettyPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCBasicTypesNode;
import de.monticore.types.mccollectiontypes._ast.ASTMCCollectionTypesNode;
import de.monticore.types.mcfullgenerictypes._ast.ASTMCFullGenericTypesNode;
import de.monticore.types.mcsimplegenerictypes._ast.ASTMCSimpleGenericTypesNode;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCCollectionTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCSimpleGenericTypesPrettyPrinter;
import vartas.monticore.cd2code._ast.ASTCD2CodeNode;
import vartas.monticore.cd2code._visitor.CD2CodeDelegatorVisitor;
import vartas.monticore.cd2code._visitor.CD2CodeVisitor;
import vartas.monticore.cd2code.types.cd2codebasictypes._ast.ASTCD2CodeBasicTypesNode;
import vartas.monticore.cd2code.types.cd2codebasictypes.prettyprint.CD2CodeBasicTypesPrettyPrinter;
import vartas.monticore.cd2code.types.cd2codecollectiontypes._ast.ASTCD2CodeCollectionTypesNode;
import vartas.monticore.cd2code.types.cd2codecollectiontypes.prettyprint.CD2CodeCollectionPrettyPrinter;

public class CD2CodePrettyPrinter extends CD2CodeDelegatorVisitor {
    private final IndentPrinter printer;
    public CD2CodePrettyPrinter(){
        this(new IndentPrinter());
    }

    public CD2CodePrettyPrinter(IndentPrinter printer){
        this.printer = printer;

        this.setCD2CodeVisitor(new InternalCD2CodePrettyPrinter());
        this.setCD2CodeBasicTypesVisitor(new CD2CodeBasicTypesPrettyPrinter(printer));
        this.setCD2CodeCollectionTypesVisitor(new CD2CodeCollectionPrettyPrinter(printer));
        this.setCD4AnalysisVisitor(new CDPrettyPrinter(printer));
        this.setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
        this.setMCBasicTypesVisitor(new MCBasicTypesPrettyPrinter(printer));
        this.setMCBasicsVisitor(new MCBasicsPrettyPrinter(printer));
        this.setMCCollectionTypesVisitor(new MCCollectionTypesPrettyPrinter(printer));
        this.setMCSimpleGenericTypesVisitor(new MCSimpleGenericTypesPrettyPrinter(printer));
        this.setMCFullGenericTypesVisitor(new MCFullGenericTypesPrettyPrinter(printer));
    }

    public String printType(ASTCDParameter ast){
        return prettyprint(ast.getMCType());
    }

    public String printType(ASTCDAttribute ast){
        return prettyprint(ast.getMCType());
    }

    public String printReturnType(ASTCDMethod ast){
        return prettyprint(ast.getMCReturnType());
    }

    public String prettyprint(ASTMCBasicTypesNode ast){
        printer.clearBuffer();
        ast.accept(this);
        return printer.getContent();
    }

    public String prettyprint(ASTMCCollectionTypesNode ast){
        printer.clearBuffer();
        ast.accept(this);
        return printer.getContent();
    }

    public String prettyprint(ASTMCSimpleGenericTypesNode ast){
        printer.clearBuffer();
        ast.accept(this);
        return printer.getContent();
    }

    public String prettyprint(ASTMCFullGenericTypesNode ast){
        printer.clearBuffer();
        ast.accept(this);
        return printer.getContent();
    }

    public String prettyprint(ASTCD2CodeNode ast){
        printer.clearBuffer();
        ast.accept(this);
        return printer.getContent();
    }

    public String prettyprint(ASTCD2CodeBasicTypesNode ast){
        printer.clearBuffer();
        ast.accept(this);
        return printer.getContent();
    }

    public String prettyprint(ASTCD2CodeCollectionTypesNode ast){
        printer.clearBuffer();
        ast.accept(this);
        return printer.getContent();
    }

    public String prettyprint(ASTCD4AnalysisNode ast){
        printer.clearBuffer();
        ast.accept(this);
        return printer.getContent();
    }

    private static class InternalCD2CodePrettyPrinter implements CD2CodeVisitor{
        private CD2CodeVisitor realThis = this;

        @Override
        public CD2CodeVisitor getRealThis(){
            return realThis;
        }

        @Override
        public void setRealThis(CD2CodeVisitor realThis){
            this.realThis = realThis;
        }
    }
}
