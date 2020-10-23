package vartas.monticore.cd4code;

import de.monticore.cd.cd4analysis._ast.ASTCD4AnalysisNode;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.HookPoint;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.utils.Names;

import javax.annotation.Nonnull;

public abstract class CDCreator <T> extends AbstractCreator<ASTCDType, T> {
    protected CDCreator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
    }
    @Nonnull
    protected ASTCDMethod buildMethod(@Nonnull String signature, @Nonnull Object... arguments){
        String qualifiedSignature = String.format(signature, arguments);
        return getCDMethodFacade().createMethodByDefinition(qualifiedSignature);
    }

    protected void setTemplate(@Nonnull ASTCD4AnalysisNode node, @Nonnull String module, @Nonnull String template, @Nonnull Object... arguments){
        String qualifiedTemplate = Names.getQualifiedName(module, template);
        HookPoint hookPoint = new TemplateHookPoint(qualifiedTemplate, arguments);

        glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, node, hookPoint);
    }
}
