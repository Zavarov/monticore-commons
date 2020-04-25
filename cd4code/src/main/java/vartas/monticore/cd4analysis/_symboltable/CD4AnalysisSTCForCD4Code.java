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

package vartas.monticore.cd4analysis._symboltable;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDParameter;
import de.monticore.cd.cd4analysis._symboltable.CDFieldSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDMethOrConstrSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbolLoader;
import de.monticore.cd.cd4analysis._symboltable.ICD4AnalysisScope;
import de.monticore.cd.cd4code.CD4CodePrettyPrinterDelegator;
import de.se_rwth.commons.Joiners;

import java.util.Deque;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class patches the generation of the CD4Code symbol table when dealing with fully generic types.
 */
public class CD4AnalysisSTCForCD4Code extends de.monticore.cd.cd4code._symboltable.CD4AnalysisSTCForCD4Code {
    protected CD4CodePrettyPrinterDelegator printer = new CD4CodePrettyPrinterDelegator();
    protected Pattern QUALIFIED_NAME = Pattern.compile("(\\w+\\.)*\\w+");

    public CD4AnalysisSTCForCD4Code(Deque<? extends ICD4AnalysisScope> scopeStack) {
        super(scopeStack);
    }

    @Override
    public void initialize_CDAttribute(CDFieldSymbol fieldSymbol, ASTCDAttribute astAttribute){
        String typeName = printer.prettyprint(astAttribute.getMCType());
        String qualifiedName;
        //TODO find a better way to extract the qualified name
        Matcher matcher = QUALIFIED_NAME.matcher(typeName);
        if(matcher.find()) {
            qualifiedName = matcher.group();
            CDTypeSymbolLoader typeReference = new CDTypeSymbolLoader(qualifiedName, this.getCurrentScope().orElseThrow());
            fieldSymbol.setType(typeReference);
        }else{
            throw new IllegalArgumentException("The type doesn't start with a qualified name.");
        }
    }

    @Override
    protected void initialize_CDParameter(CDFieldSymbol fieldSymbol, ASTCDParameter astParameter) {
        //TODO Find a better way to prettyprint the MCType
        CDTypeSymbolLoader paramTypeSymbol = new CDTypeSymbolLoader(printer.prettyprint(astParameter.getMCType()), this.getCurrentScope().orElseThrow());
        //TODO How to deal with wildcards?
        //this.addTypeArgumentsToTypeSymbol(paramTypeSymbol, astParameter.getMCType());
        fieldSymbol.setType(paramTypeSymbol);
    }

    @Override
    public void setReturnTypeOfMethod(CDMethOrConstrSymbol methodSymbol, ASTCDMethod astMethod) {
        //TODO
        //Fails when we have nested generic return types.
    }

    @Override
    public void visit(ASTCDCompilationUnit ast){
        super.visit(ast);
    }

    @Override
    public void endVisit(ASTCDCompilationUnit ast){
        //Provide the package name for all sub-symbols
        CD4CodeSpanningSymbolMock spanningSymbolMock = new CD4CodeSpanningSymbolMock(ast.getCDDefinition().getName());
        spanningSymbolMock.setPackageName(Joiners.DOT.join(ast.getPackageList()));
        Optional<ICD4AnalysisScope> scope = getCurrentScope();
        getCurrentScope().orElseThrow().setSpanningSymbol(spanningSymbolMock);

        //Link to the spanning symbol before leaving the artifact scope
        super.endVisit(ast);
    }
}
