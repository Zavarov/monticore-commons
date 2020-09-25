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

package vartas.monticore.cd4code._symboltable;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4analysis._symboltable.*;
import de.monticore.cd.cd4code.CD4CodePrettyPrinterDelegator;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mccollectiontypes._ast.ASTMCBasicTypeArgument;
import de.monticore.types.mccollectiontypes._ast.ASTMCGenericType;
import de.monticore.types.mccollectiontypes._ast.ASTMCPrimitiveTypeArgument;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import de.monticore.types.mcfullgenerictypes._ast.ASTMCWildcardTypeArgument;
import de.monticore.types.mcsimplegenerictypes._ast.ASTMCCustomTypeArgument;
import de.monticore.types.prettyprint.MCCollectionTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
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
    public void addStereotypes(CDMethOrConstrSymbol methodSymbol, ASTCDStereotype astStereotype) {
        if (astStereotype != null) {
            for(ASTCDStereoValue value : astStereotype.getValueList()) {
                //TODO Used to be (getName, getName)
                methodSymbol.addStereotype(new Stereotype(value.getName(), value.getValue()));
            }
        }

    }

    @Override
    public void initialize_CDAttribute(CDFieldSymbol fieldSymbol, ASTCDAttribute astAttribute){
        ASTMCType astType = astAttribute.getMCType();

        //TODO Print the type, even if they're generic
        String typeName = printer.prettyprint(astAttribute.getMCType());
        Matcher matcher = QUALIFIED_NAME.matcher(typeName);
        if(matcher.find())
            typeName = matcher.group();
        else
            return;

        CDTypeSymbolSurrogate typeReference = new CDTypeSymbolSurrogate(typeName, this.getCurrentScope().orElseThrow());
        fieldSymbol.setType(typeReference);
        this.addTypeArgumentsToTypeSymbol(typeReference, astType);
        if (astAttribute.isPresentModifier()) {
            ASTModifier astModifier = astAttribute.getModifier();
            fieldSymbol.setIsDerived(astModifier.isDerived());
            fieldSymbol.setIsStatic(astModifier.isStatic());
            fieldSymbol.setIsFinal(astModifier.isFinal());
            if (astModifier.isProtected()) {
                fieldSymbol.setIsProtected(true);
            } else if (astModifier.isPrivate()) {
                fieldSymbol.setIsPrivate(true);
            } else {
                fieldSymbol.setIsPublic(true);
            }

            if (astModifier.isPresentStereotype()) {
                for (ASTCDStereoValue stereoValue : astModifier.getStereotype().getValueList()) {
                    Stereotype stereotype = new Stereotype(stereoValue.getName(), stereoValue.getName());
                    fieldSymbol.addStereotype(stereotype);
                }
            }
        }
    }

    @Override
    protected void initialize_CDParameter(CDFieldSymbol symbol, ASTCDParameter ast) {
        //TODO Print the type, even if they're generic
        CDTypeSymbolSurrogate paramTypeSymbol = new CDTypeSymbolSurrogate(printer.prettyprint(ast.getMCType()), this.getCurrentScope().orElseThrow());
        this.addTypeArgumentsToTypeSymbol(paramTypeSymbol, ast.getMCType());
        symbol.setType(paramTypeSymbol);
        symbol.setIsParameter(true);
        symbol.setIsPrivate(true);
    }

    @Override
    public void addTypeArgumentsToTypeSymbol(CDTypeSymbolSurrogate typeReference, ASTMCType astType) {
        if (astType instanceof ASTMCGenericType) {
            ASTMCGenericType astmcGenericType = (ASTMCGenericType)astType;
            if (astmcGenericType.getMCTypeArgumentList().isEmpty()) {
                return;
            }

            List<CDTypeSymbolSurrogate> actualTypeArguments = new ArrayList<>();

            for(Iterator<ASTMCTypeArgument> var5 = astmcGenericType.getMCTypeArgumentList().iterator(); var5.hasNext(); typeReference.setActualTypeArguments(actualTypeArguments)) {
                ASTMCTypeArgument astTypeArgument = var5.next();
                CDTypeSymbolSurrogate typeArgumentSymbolReference;
                if (astTypeArgument instanceof ASTMCBasicTypeArgument) {
                    ASTMCBasicTypeArgument astmcBasicTypeArgument = (ASTMCBasicTypeArgument)astTypeArgument;
                    if (astmcBasicTypeArgument.getMCQualifiedType() != null) {
                        ASTMCQualifiedType astTypeNoBound = astmcBasicTypeArgument.getMCQualifiedType();
                        typeArgumentSymbolReference = new CDTypeSymbolSurrogate(astTypeNoBound.printType(new MCCollectionTypesPrettyPrinter(new IndentPrinter())), this.getCurrentScope().orElseThrow());
                        this.addTypeArgumentsToTypeSymbol(typeArgumentSymbolReference, astTypeNoBound);
                        actualTypeArguments.add(typeArgumentSymbolReference);
                    } else {
                        Log.error("0xU0401 Unknown type argument " + astTypeArgument + " of type " + typeReference);
                    }
                } else if (astTypeArgument instanceof ASTMCPrimitiveTypeArgument) {
                    ASTMCPrimitiveTypeArgument astmcPrimitiveTypeArgument = (ASTMCPrimitiveTypeArgument)astTypeArgument;
                    if (astmcPrimitiveTypeArgument.getMCPrimitiveType() != null) {
                        ASTMCType astTypeNoBound = astmcPrimitiveTypeArgument.getMCPrimitiveType();
                        typeArgumentSymbolReference = new CDTypeSymbolSurrogate(astTypeNoBound.printType(new MCCollectionTypesPrettyPrinter(new IndentPrinter())), this.getCurrentScope().orElseThrow());
                        this.addTypeArgumentsToTypeSymbol(typeArgumentSymbolReference, astTypeNoBound);
                        actualTypeArguments.add(typeArgumentSymbolReference);
                    }
                } else if(astTypeArgument instanceof ASTMCWildcardTypeArgument) {
                    //TODO Believe it or not, wildcards *do* exist
                    ASTMCWildcardTypeArgument astmcWildcardTypeArgument = (ASTMCWildcardTypeArgument) astTypeArgument;
                    if (astmcWildcardTypeArgument.isPresentLowerBound()) {
                        ASTMCType astTypeLowerBound = astmcWildcardTypeArgument.getLowerBound();
                        typeArgumentSymbolReference = new CDTypeSymbolSurrogate(astTypeLowerBound.printType(new MCFullGenericTypesPrettyPrinter(new IndentPrinter())), this.getCurrentScope().orElseThrow());
                        this.addTypeArgumentsToTypeSymbol(typeArgumentSymbolReference, astTypeLowerBound);
                        actualTypeArguments.add(typeArgumentSymbolReference);
                    } else if (astmcWildcardTypeArgument.isPresentUpperBound()) {
                        ASTMCType astTypeUpperBound = astmcWildcardTypeArgument.getUpperBound();
                        typeArgumentSymbolReference = new CDTypeSymbolSurrogate(astTypeUpperBound.printType(new MCFullGenericTypesPrettyPrinter(new IndentPrinter())), this.getCurrentScope().orElseThrow());
                        this.addTypeArgumentsToTypeSymbol(typeArgumentSymbolReference, astTypeUpperBound);
                        actualTypeArguments.add(typeArgumentSymbolReference);
                    }
                } else if (astTypeArgument instanceof ASTMCCustomTypeArgument){
                    ASTMCCustomTypeArgument astmcCustomTypeArgument = (ASTMCCustomTypeArgument) astTypeArgument;
                    ASTMCType astCustomType = astmcCustomTypeArgument.getMCType();
                    typeArgumentSymbolReference = new CDTypeSymbolSurrogate(astCustomType.printType(new MCFullGenericTypesPrettyPrinter(new IndentPrinter())), (ICD4AnalysisScope)this.getCurrentScope().get());
                    this.addTypeArgumentsToTypeSymbol(typeArgumentSymbolReference, astCustomType);
                    actualTypeArguments.add(typeArgumentSymbolReference);
                } else {
                    Log.error("0xU0401 Unknown type argument " + astTypeArgument + " of type " + typeReference);
                }
            }
        }

    }

    @Override
    public void setReturnTypeOfMethod(CDMethOrConstrSymbol methodSymbol, ASTCDMethod astMethod) {
        //TODO Fails when we have nested generic return types.
    }

    /*
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
    */
}
