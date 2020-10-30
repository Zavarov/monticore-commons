package vartas.monticore.cd4code.json;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.monticore.BasicCDTest;

public class JSONCreatorTest extends BasicCDTest {
    ASTCDClass json;
    ASTCDClass cdClass;
    ASTCDDefinition cdDefinition;
    CDDefinitionSymbol cdDefinitionSymbol;

    @BeforeEach
    public void setUp(){
        super.setUp();
        cdDefinitionSymbol = globalScope.resolveCDDefinition("vartas.monticore.cd4code.json.JSON").orElseThrow();
        cdDefinition = cdDefinitionSymbol.getAstNode();
        cdClass = cdDefinition.getCDClass(0);
        json = JSONCreator.create(cdClass, new GlobalExtensionManagement());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Path",
            "String",
            "JSONObject"
    })
    public void testFromJson(String parameter){
        getCDMethod(json, "fromJson", cdClass.getName(), parameter);
    }

    @Test
    public void testToJson(){
        getCDMethod(json, "toJson", cdClass.getName(), "JSONObject");
    }

    @Test
    public void testFromAttribute(){
        for(ASTCDAttribute node : cdClass.getCDAttributeList())
            getCDMethod(json, "$from" + StringUtils.capitalize(node.getName()), "JSONObject", cdClass.getName());
    }

    @Test
    public void testToAttribute(){
        for(ASTCDAttribute node : cdClass.getCDAttributeList())
            getCDMethod(json, "$to" + StringUtils.capitalize(node.getName()), cdClass.getName(), "JSONObject");
    }
}
