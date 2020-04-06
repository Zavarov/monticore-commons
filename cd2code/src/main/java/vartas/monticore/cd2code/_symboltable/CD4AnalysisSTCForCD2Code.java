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

package vartas.monticore.cd2code._symboltable;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDParameter;
import de.monticore.cd.cd4analysis._symboltable.CDFieldSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDMethOrConstrSymbol;
import de.monticore.cd.cd4analysis._symboltable.ICD4AnalysisScope;

import java.util.Deque;

public class CD4AnalysisSTCForCD2Code extends CD4AnalysisSTCForCD2CodeTOP{
    public CD4AnalysisSTCForCD2Code(Deque<? extends ICD4AnalysisScope> scopeStack) {
        super(scopeStack);
    }

    @Override
    public void initialize_CDAttribute(CDFieldSymbol fieldSymbol, ASTCDAttribute astAttribute){
        //TODO
        //Ignore, since it will break the moment there are types that are neither generics, nor arrays
        //but also not collections. >.<
    }

    @Override
    protected void initialize_CDParameter(CDFieldSymbol symbol, ASTCDParameter ast) {
        //TODO
        //Ignore, since AstPrinter can't be applied to parameter types that are in a sub-language
    }

    @Override
    public void setReturnTypeOfMethod(CDMethOrConstrSymbol methodSymbol, ASTCDMethod astMethod) {
        //TODO
        //Ignore, since the return type might be more than just a simple generic one.
    }
}
