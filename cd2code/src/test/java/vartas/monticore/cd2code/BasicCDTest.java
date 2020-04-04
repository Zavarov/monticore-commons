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

package vartas.monticore.cd2code;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.codegen.cd2java.CoreTemplates;
import de.monticore.generating.GeneratorEngine;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.StringHookPoint;
import de.monticore.io.paths.ModelPath;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.se_rwth.commons.Joiners;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import vartas.monticore.cd2code._symboltable.CD2CodeGlobalScope;
import vartas.monticore.cd2code._symboltable.CD2CodeLanguage;
import vartas.monticore.cd2code._symboltable.CD2CodeModelLoader;
import vartas.monticore.cd2code.prettyprint.CD2CodePrettyPrinter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicCDTest {
    protected static final Path MODEL_PATH = Paths.get("src","test","resources");

    protected CD2CodeLanguage cdLanguage;
    protected ModelPath cdModelPath;
    protected CD2CodeGlobalScope cdGlobalScope;

    public Path OUTPUT_PATH = Paths.get("target","generated-sources");
    public Path PACKAGE_PATH = Paths.get("vartas","monticore","cd2code");
    public Path QUALIFIED_PATH = OUTPUT_PATH.resolve(PACKAGE_PATH);
    public Path TEMPLATE_PATH = Paths.get("src","main","resources","templates");
    public String JAVA_FILE_EXTENSION = "java";

    public ASTCDCompilationUnit cdCompilationUnit;
    public ASTCDDefinition cdDefinition;
    public ASTCDClass cdClass;
    public ASTCDClass cdTransformedClass;
    public CDGenerator cdGenerator;

    public GlobalExtensionManagement GLEX;
    public GeneratorSetup GENERATOR_SETUP;
    public GeneratorEngine GENERATOR_ENGINE;
    public CD2CodePrettyPrinter mcPrinter = new CD2CodePrettyPrinter();

    @BeforeAll
    public static void setUpAll(){
        //Log.enableFailQuick(false);
    }

    @BeforeEach
    public void setUp(){
        cdLanguage = new CD2CodeLanguage();
        cdModelPath = new ModelPath(MODEL_PATH);
        cdGlobalScope = new CD2CodeGlobalScope(cdModelPath, cdLanguage);
        GLEX = new GlobalExtensionManagement();
        GENERATOR_SETUP = new GeneratorSetup();
        GENERATOR_SETUP.setDefaultFileExtension(JAVA_FILE_EXTENSION);
        GENERATOR_SETUP.setAdditionalTemplatePaths(Collections.singletonList(TEMPLATE_PATH.toFile()));
        GENERATOR_SETUP.setGlex(GLEX);
        GENERATOR_SETUP.setTracing(false);
        GENERATOR_SETUP.setOutputDirectory(OUTPUT_PATH.toFile());
        GENERATOR_ENGINE = new GeneratorEngine(GENERATOR_SETUP);
    }

    protected ASTCDCompilationUnit parse(String... names){
        String qualifiedName = Joiners.DOT.join(names);

        CD2CodeModelLoader modelLoader = new CD2CodeModelLoader(cdLanguage);
        List<ASTCDCompilationUnit> models = modelLoader.loadModelsIntoScope(qualifiedName, cdModelPath, cdGlobalScope);

        if(models.size() == 0)
            throw GeneratorError.of(Errors.NO_MATCHING_MODEL_FOUND);
        else if(models.size() > 1)
            throw GeneratorError.of(Errors.MULTIPLE_MODELS_FOUND);
        else
            return models.get(0);
    }

    protected void parseCDClass(String className, String... names){
        cdCompilationUnit = parse(names);
        cdDefinition = cdCompilationUnit.getCDDefinition();
        cdGenerator = new CDGenerator(GENERATOR_SETUP ,cdCompilationUnit);

        String importString = cdCompilationUnit.getMCImportStatementList().stream().map(ASTMCImportStatement::printType).reduce((u, v) -> u + "\n" + v).orElse("");

        GLEX.replaceTemplate(CDGeneratorHelper.PACKAGE_TEMPLATE, CoreTemplates.createPackageHookPoint(cdCompilationUnit.getPackageList()));
        GLEX.replaceTemplate(CDGeneratorHelper.IMPORT_TEMPLATE, new StringHookPoint(importString));

        cdClass = cdDefinition.getCDClassList().stream().filter(cdClazz -> cdClazz.getName().equals(className)).findAny().orElseThrow();
        cdTransformedClass = cdGenerator.transform(cdClass);
    }

    protected ASTCDMethod getMethod(ASTCDType cdType, String methodName, String... parameterNames){
        List<ASTCDMethod> methods = cdType.getCDMethodList()
                .stream()
                .filter(method -> method.getName().equals(methodName))
                .filter(method -> compareParameters(method, parameterNames))
                .collect(Collectors.toList());

        if(methods.size() == 0)
            throw new IllegalArgumentException("A method with name '"+methodName+"' doesn't exist.");
        else if(methods.size() > 1)
            throw new IllegalArgumentException("Multiple methods with name '"+methodName+"' exist.");
        else return methods.get(0);
    }

    private boolean compareParameters(ASTCDMethod cdMethod, String... parameterNames){
        if(parameterNames.length != cdMethod.getCDParameterList().size())
            return false;

        ASTCDParameter cdParameter;
        MCFullGenericTypesPrettyPrinter printer = new MCFullGenericTypesPrettyPrinter(new IndentPrinter());
        for(int i = 0 ; i < parameterNames.length ; ++i){
            cdParameter = cdMethod.getCDParameter(i);

            if(!cdParameter.getMCType().printType(printer).equals(parameterNames[i]))
                return false;
        }
        return true;
    }
}
