package vartas.monticore.cd4code.json;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import vartas.monticore.cd4code.BasicCDGeneratorTest;
import vartas.monticore.cd4code.decorator.CDDecoratorGenerator;

public class CDJSONCreatorTest extends BasicCDGeneratorTest {
    CDJSONGenerator generator;

    @BeforeEach
    public void setUp(){
        super.setUp();

        generator = new CDJSONGenerator(
                setup,
                helper,
                globalScope
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "vartas.monticore.cd4code.json.JSON"
    })
    public void testGenerate(String modelPath){
        CDDecoratorGenerator decorator;
        decorator = new CDDecoratorGenerator(setup ,helper, null);

        ASTCDDefinition node = globalScope.resolveCDDefinition(modelPath).map(CDDefinitionSymbol::getAstNode).orElseThrow();
        generator.generate(node);
        decorator.generate(node);
    }
}