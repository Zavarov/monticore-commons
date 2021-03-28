package zav.mc.cd4code._symboltable;

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import de.monticore.cd.cd4code._symboltable.CD4CodeSymbolTableCreatorDelegator;
import de.monticore.modelloader.AstProvider;

import java.util.List;

public class CD4CodeModelLoader extends de.monticore.cd.cd4code._symboltable.CD4CodeModelLoader {
    public CD4CodeModelLoader(AstProvider<ASTCDCompilationUnit> astProvider, CD4CodeSymbolTableCreatorDelegator symbolTableCreator, String modelFileExtension) {
        super(astProvider, symbolTableCreator, modelFileExtension);
    }

    @Override
    public void showWarningIfParsedModels(List<?> asts, String modelName){
        //TODO Would be great if the warning told us how the building process is supposed to look like
    }
}
