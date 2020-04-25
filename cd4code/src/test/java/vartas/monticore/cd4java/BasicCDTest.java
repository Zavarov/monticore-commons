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

package vartas.monticore.cd4java;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDParameter;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.io.paths.ModelPath;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import org.junit.jupiter.api.BeforeEach;
import vartas.monticore.cd4analysis._symboltable.CD4CodeGlobalScope;
import vartas.monticore.cd4analysis._symboltable.CD4CodeLanguage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicCDTest {
    protected static final Path MODEL_PATH = Paths.get("src","main","models","cd4java");
    protected static final Path TEST_MODEL_PATH = Paths.get("src","test","models","cd4java");
    protected static final Path TEMPLATE_PATH = Paths.get("src","main","resources","templates");
    protected static final Path OUTPUT_PATH = Paths.get("target","generated-sources");
    protected static final Path SOURCES_PATH = Paths.get("src","main", "java");

    protected CD4CodeLanguage language = new CD4CodeLanguage();
    protected CD4CodeGlobalScope globalScope;
    protected ModelPath modelPath;

    @BeforeEach
    public void setUp(){
        modelPath = new ModelPath(TEST_MODEL_PATH, MODEL_PATH);
        language = new CD4CodeLanguage();
        globalScope = new CD4CodeGlobalScope(modelPath, language);
    }

    protected ASTCDType getCDType(ASTCDDefinition ast, String className){
        return ast.streamCDClasss().filter(cdClass -> cdClass.getName().equals(className)).findAny().orElseThrow();
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
