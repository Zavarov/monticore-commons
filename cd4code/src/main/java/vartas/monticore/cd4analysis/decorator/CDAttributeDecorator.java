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

package vartas.monticore.cd4analysis.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDField;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.monticore.cd4analysis.calculator.CDMethodCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * This decorator attempts to give access to all methods of a given {@link ASTCDAttribute}.<br>
 * The available methods are provided via a class diagram.
 */
public class CDAttributeDecorator extends AbstractCreator<ASTCDAttribute, List<ASTCDMethod>> implements CD4CodeInheritanceVisitor {
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
    private final List<ASTCDMethod> cdMethodList = new ArrayList<>();
    private final List<ASTMCTypeArgument> typeArguments = new ArrayList<>();
    private final CDMethodCalculator cdMethodCalculator = new CDMethodCalculator();

    @Override
    public List<ASTCDMethod> decorate(ASTCDAttribute ast) {
        log.debug("Visiting attribute {}.", ast.getName());

        cdMethodList.clear();
        ast.accept(getRealThis());
        return cdMethodList;
    }

    @Override
    public void visit(ASTCDAttribute ast){
        typeArguments.clear();
    }

    @Override
    public void visit(ASTMCTypeArgument ast){
        typeArguments.add(ast);
    }

    @Override
    public void endVisit(ASTCDAttribute ast){
        log.debug("Visiting attribute {}.", ast.getName());

        //Fails if there is no class diagram associated with the type
        ast.getSymbol().getType().loadSymbol().ifPresent(typeSymbol -> {
            log.debug("Type of attribute {} successfully loaded as {}.", ast.getName(), typeSymbol.getName());

            loadMethods(ast, typeSymbol);
            for(CDTypeSymbol superType : typeSymbol.getSuperTypesTransitive())
                loadMethods(ast, superType);
        });
    }

    private void loadMethods(ASTCDAttribute ast, CDTypeSymbol typeSymbol){
        CDAttributeVisitor cdAttributeVisitor = new CDAttributeVisitor(ast);
        for(ASTCDMethod cdMethod : cdMethodCalculator.apply(typeSymbol.getAstNode(), typeArguments)){
            log.debug("Visiting method {} of symbol {}.", cdMethod.getName(), typeSymbol.getName());
            cdMethod.accept(cdAttributeVisitor);
        }
    }


    private class CDAttributeVisitor implements CD4CodeInheritanceVisitor {
        private final String symbolName;

        public CDAttributeVisitor(ASTCDField ast){
            symbolName = StringUtils.capitalize(ast.getName());
        }

        @Override
        public void visit(ASTCDMethod ast){
            ast = ast.deepClone();
            //e.g:
            //String content;
            //content.size() -> sizeContent()
            ast.setName(ast.getName() + symbolName);
            cdMethodList.add(ast);
        }
    }
}
