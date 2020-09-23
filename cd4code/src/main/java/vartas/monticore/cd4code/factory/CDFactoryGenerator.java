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

package vartas.monticore.cd4code.factory;

import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4code._ast.CD4CodeMill;
import de.monticore.generating.GeneratorSetup;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedNameBuilder;
import de.se_rwth.commons.Joiners;
import de.se_rwth.commons.Splitters;
import vartas.monticore.cd4code.CDGeneratorHelper;
import vartas.monticore.cd4code.CDPreprocessorGenerator;
import vartas.monticore.cd4code._symboltable.CD4CodeGlobalScope;
import vartas.monticore.cd4code._symboltable.CD4CodeSymbolTableCreatorDelegator;
import vartas.monticore.cd4code.preprocessor.CDSetImportsForTypesProcess;
import vartas.monticore.cd4code.preprocessor.CDSetPackageForTypesProcess;
import vartas.monticore.cd4code.preprocessor.CDHandleHandwrittenFilesProcess;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CDFactoryGenerator extends CDPreprocessorGenerator {
    private final CD4CodeSymbolTableCreatorDelegator stc;

    public CDFactoryGenerator(@Nonnull GeneratorSetup generatorSetup, @Nonnull CDGeneratorHelper generatorHelper, @Nonnull CD4CodeGlobalScope globalScope) {
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
            builder.addPart(part);
        return builder.build();
    }

    private ASTMCImportStatement buildMCImportStatement(Iterable<String> parts, boolean isStar){
        return CD4CodeMill.mCImportStatementBuilder()
                .setMCQualifiedName(buildMCQualifiedName(parts))
                .setStar(isStar)
                .build();
    }

    private ASTMCImportStatement buildMCImportStatement(String importName, boolean isStar){
        return buildMCImportStatement(Splitters.DOT.split(importName), isStar);
    }

    private ASTMCImportStatement buildMCImportStatement(String importName){
        return buildMCImportStatement(importName, false);
    }

    private List<ASTMCImportStatement> buildMCImportStatements(ASTCDDefinition ast){
        List<String> imports = new ArrayList<>();

        imports.add("java.util.function.Supplier.Supplier");
        imports.addAll(ast.getSymbol().getImports());

        return imports.stream().map(this::buildMCImportStatement).collect(Collectors.toUnmodifiableList());
    }

    private ASTMCImportStatement buildParentMCImportStatement(ASTCDDefinition ast){
        String qualifiedName = Joiners.DOT.join(ast.getSymbol().getPackageName(), ast.getName(), "*");

        return buildMCImportStatement(qualifiedName, true);
    }

    private List<String> buildPackage(ASTCDDefinition ast){
        List<String> packageList = new ArrayList<>();
        Splitters.DOT.split(ast.getSymbol().getPackageName()).forEach(packageList::add);
        packageList.add(CDGeneratorHelper.FACTORY_MODULE);
        return packageList;
    }

    private ASTCDCompilationUnit buildCDCompilationUnit(ASTCDDefinition ast){
        return CD4CodeMill.cDCompilationUnitBuilder()
                .setCDDefinition(buildCDDefinition(ast))
                .addAllMCImportStatements(buildMCImportStatements(ast))
                .addMCImportStatement(buildParentMCImportStatement(ast))
                .addAllPackages(buildPackage(ast))
                .build();
    }

    private ASTCDDefinition buildCDDefinition(ASTCDDefinition ast){
        return CD4CodeMill.cDDefinitionBuilder()
                .setName(ast.getName()+"Factory")
                .addAllCDClasss(buildCDFactories(ast))
                .build();
    }

    private List<ASTCDClass> buildCDFactories(ASTCDDefinition ast){
        List<ASTCDClass> factories = new ArrayList<>();

        ast.getCDClassList().stream().map(this::buildCDFactory).forEach(factories::add);
        ast.getCDInterfaceList().stream().map(this::buildCDFactory).forEach(factories::add);

        return factories;
    }

    private ASTCDClass buildCDFactory(ASTCDType ast){
        return FactoryCreator.create(ast, generatorSetup.getGlex());
    }
}
