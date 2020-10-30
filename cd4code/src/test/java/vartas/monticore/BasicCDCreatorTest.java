package vartas.monticore;

import de.monticore.cd.cd4code.CD4CodePrettyPrinterDelegator;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.io.paths.IterablePath;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import org.junit.jupiter.api.BeforeEach;
import vartas.monticore.cd4code.CDGeneratorHelper;

import java.util.Collections;

public abstract class BasicCDCreatorTest extends BasicCDTest{
    protected GlobalExtensionManagement glex;
    protected GeneratorSetup setup;
    protected CDGeneratorHelper helper;

    @BeforeEach
    public void setUp(){
        super.setUp();
        glex = new GlobalExtensionManagement();
        glex.setGlobalValue("cdPrinter", new CD4CodePrettyPrinterDelegator());
        glex.setGlobalValue("mcPrinter", new MCFullGenericTypesPrettyPrinter(new IndentPrinter()));

        setup = new GeneratorSetup();
        setup.setAdditionalTemplatePaths(Collections.singletonList(TEMPLATE_PATH.toFile()));
        setup.setDefaultFileExtension("java");
        setup.setGlex(glex);
        setup.setHandcodedPath(IterablePath.from(SOURCES_PATH.toFile(), "java"));
        setup.setOutputDirectory(OUTPUT_PATH.toFile());
        setup.setTracing(false);

        helper = new CDGeneratorHelper();
    }
}
