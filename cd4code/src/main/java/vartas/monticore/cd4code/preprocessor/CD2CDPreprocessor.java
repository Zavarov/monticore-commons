package vartas.monticore.cd4code.preprocessor;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import vartas.monticore.cd4code.CDGeneratorHelper;

import java.util.function.Consumer;

public class CD2CDPreprocessor implements Consumer<ASTCDDefinition> {
    private final GlobalExtensionManagement glex;
    private final CDGeneratorHelper helper;

    public CD2CDPreprocessor(GlobalExtensionManagement glex, CDGeneratorHelper helper){
        this.glex = glex;
        this.helper = helper;
    }

    @Override
    public void accept(ASTCDDefinition node) {
        node.accept(new CDHandleHandwrittenFilesProcess(glex, helper));
        node.accept(new CDInitializeContainerProcess(glex));
        node.accept(new CDSetImportsForTypesProcess(glex));
        node.accept(new CDSetPackageForTypesProcess(glex));
    }
}
