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

package zav.mc.cd4code.visitor;

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._ast.ASTCDInterface;
import de.monticore.cd.cd4code.CD4CodeMill;
import de.monticore.generating.GeneratorSetup;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedNameBuilder;
import de.se_rwth.commons.Joiners;
import de.se_rwth.commons.Splitters;
import zav.mc.cd4code.CDGeneratorHelper;
import zav.mc.cd4code.CDPreprocessorGenerator;
import zav.mc.cd4code._symboltable.CD4CodeGlobalScope;
import zav.mc.cd4code._symboltable.CD4CodeSymbolTableCreatorDelegator;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CDVisitorGenerator extends CDPreprocessorGenerator {
    private final CD4CodeSymbolTableCreatorDelegator stc;
    public CDVisitorGenerator(@Nonnull GeneratorSetup generatorSetup, @Nonnull CDGeneratorHelper generatorHelper, @Nonnull CD4CodeGlobalScope globalScope) {
        super(
                generatorSetup,
                generatorHelper
        );
        stc = new CD4CodeSymbolTableCreatorDelegator(globalScope);
    }

    @Override
    public void generate(@Nonnull ASTCDDefinition node){
        ASTCDCompilationUnit ast = buildCDCompilationUnit(node);
        stc.createFromAST(ast);
        super.generate(ast.getCDDefinition());
    }

    private ASTMCQualifiedName buildMCQualifiedName(Iterable<String> parts){
        ASTMCQualifiedNameBuilder builder = CD4CodeMill.mCQualifiedNameBuilder();
        for(String part : parts)
            builder.addParts(part);
        return builder.build();
    }

    private ASTMCImportStatement buildMCImportStatement(Iterable<String> parts){
        return CD4CodeMill.mCImportStatementBuilder()
                .setMCQualifiedName(buildMCQualifiedName(parts))
                .setStar(true)
                .build();
    }

    private ASTMCImportStatement buildMCImportStatement(String importName){
        return buildMCImportStatement(Splitters.DOT.split(importName));
    }

    private ASTMCImportStatement buildParentMCImportStatement(ASTCDDefinition ast){
        String qualifiedName = Joiners.DOT.join(ast.getSymbol().getPackageName(), ast.getName(), "*");

        return buildMCImportStatement(qualifiedName);
    }

    private List<String> buildPackage(ASTCDDefinition ast){
        List<String> packageList = new ArrayList<>();
        Splitters.DOT.split(ast.getSymbol().getPackageName()).forEach(packageList::add);
        packageList.add(CDGeneratorHelper.VISITOR_PACKAGE);
        return packageList;
    }

    private ASTCDCompilationUnit buildCDCompilationUnit(ASTCDDefinition ast){
        return CD4CodeMill.cDCompilationUnitBuilder()
                .setCDDefinition(buildCDDefinition(ast))
                .addMCImportStatement(buildParentMCImportStatement(ast))
                .addAllPackage(buildPackage(ast))
                .build();
    }

    private ASTCDDefinition buildCDDefinition(ASTCDDefinition ast){
        return CD4CodeMill.cDDefinitionBuilder()
                .setName(ast.getName()+"Visitor")
                .addCDInterface(buildCDVisitor(ast))
                .build();
    }

    private ASTCDInterface buildCDVisitor(ASTCDDefinition ast){
        return VisitorCreator.create(ast, generatorSetup.getGlex(), generatorHelper);
    }
}
