package vartas.monticore.cd4analysis.preprocessor;

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import vartas.monticore.cd4analysis.CDGeneratorHelper;
import vartas.monticore.cd4analysis.preprocessor.process.*;

import java.util.function.Function;

public class CD2CDPreprocessor implements Function<ASTCDCompilationUnit, ASTCDCompilationUnit> {
    private final GlobalExtensionManagement glex;
    private final CDGeneratorHelper helper;

    public CD2CDPreprocessor(GlobalExtensionManagement glex, CDGeneratorHelper helper){
        this.glex = glex;
        this.helper = helper;
    }

    @Override
    public ASTCDCompilationUnit apply(ASTCDCompilationUnit node) {
        node = node.deepClone();

        node.accept(new CDDecoratorProcess(glex));
        node.accept(new CDHandleHandwrittenFilesProcess(glex, helper));
        node.accept(new CDInitializeContainerProcess(glex));
        node.accept(new CDSetImportsForTypesProcess(glex));
        node.accept(new CDSetPackageForTypesProcess(glex));

        return node;
    }
}
