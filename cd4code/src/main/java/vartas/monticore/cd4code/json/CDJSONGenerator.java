package vartas.monticore.cd4code.json;

import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4code.CD4CodeMill;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CDJSONGenerator extends CDPreprocessorGenerator {
    private final CD4CodeSymbolTableCreatorDelegator stc;

    public CDJSONGenerator(@Nonnull GeneratorSetup generatorSetup, @Nonnull CDGeneratorHelper generatorHelper, @Nonnull CD4CodeGlobalScope globalScope) {
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
        List<String> imports = new ArrayList<>(ast.getSymbol().getImports());

        //Required by the attributes
        imports.add("java.lang.String.String");
        //Required by OfJson
        imports.add("java.io.IOException.IOException");
        imports.add("java.nio.file.Path.Path");
        imports.add("org.json.JSONObject.JSONObject");

        return imports.stream().map(this::buildMCImportStatement).collect(Collectors.toUnmodifiableList());
    }

    private ASTMCImportStatement buildParentMCImportStatement(ASTCDDefinition ast){
        String qualifiedName = Joiners.DOT.join(ast.getSymbol().getPackageName(), ast.getName(), "*");

        return buildMCImportStatement(qualifiedName, true);
    }

    private List<String> buildPackage(ASTCDDefinition ast){
        List<String> packageList = new ArrayList<>();
        Splitters.DOT.split(ast.getSymbol().getPackageName()).forEach(packageList::add);
        packageList.add(CDGeneratorHelper.JSON_PACKAGE);
        return packageList;
    }

    private ASTCDCompilationUnit buildCDCompilationUnit(ASTCDDefinition ast){
        return CD4CodeMill.cDCompilationUnitBuilder()
                .setCDDefinition(buildCDDefinition(ast))
                .addAllMCImportStatements(buildMCImportStatements(ast))
                .addMCImportStatement(buildParentMCImportStatement(ast))
                .addAllPackage(buildPackage(ast))
                .build();
    }

    private ASTCDDefinition buildCDDefinition(ASTCDDefinition ast){
        return CD4CodeMill.cDDefinitionBuilder()
                .setName("JSON"+ast.getName())
                .addAllCDClasss(buildJsonTypes(ast))
                .build();
    }

    private List<ASTCDClass> buildJsonTypes(ASTCDDefinition ast){
        List<ASTCDClass> factories = new ArrayList<>();

        ast.getCDClassList().stream().map(this::buildJsonType).forEach(factories::add);
        ast.getCDInterfaceList().stream().map(this::buildJsonType).forEach(factories::add);
        ast.getCDEnumList().stream().map(this::buildJsonType).forEach(factories::add);

        return factories;
    }

    private ASTCDClass buildJsonType(ASTCDType ast){
        return JSONCreator.create(ast, generatorSetup.getGlex());
    }
}
