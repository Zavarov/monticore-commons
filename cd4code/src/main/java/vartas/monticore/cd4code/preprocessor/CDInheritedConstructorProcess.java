package vartas.monticore.cd4code.preprocessor;

import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDConstructor;
import de.monticore.cd.cd4analysis._ast.ASTCDEnum;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4code._visitor.CD4CodeVisitor;
import de.monticore.cd.facade.CDConstructorFacade;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import vartas.monticore.cd4code.CDGeneratorHelper;

import java.util.ArrayList;
import java.util.List;

public class CDInheritedConstructorProcess extends CDProcess{
    public CDInheritedConstructorProcess(GlobalExtensionManagement glex) {
        super(glex);
    }

    @Override
    public void visit(ASTCDClass ast){
        ast.addAllCDConstructors(fromParent(ast));
    }

    @Override
    public void visit(ASTCDEnum ast){
        ast.addAllCDConstructors(fromParent(ast));
    }

    private List<ASTCDConstructor> fromParent(ASTCDType target){
        List<ASTCDConstructor> constructors = new ArrayList<>();

        CD4CodeVisitor visitor = new CD4CodeVisitor() {
            @Override
            public void visit(ASTCDConstructor ast){
                //Only copy accessible constructors
                if(ast.getModifier().isPrivate())
                    return;

                CDConstructorFacade facade = CDConstructorFacade.getInstance();
                ASTCDConstructor constructor = facade.createConstructor(ast.getModifier(), target.getName(), ast.getCDParameterList());
                TemplateHookPoint template = new TemplateHookPoint("initializer.CallParentConstructor", constructor.getCDParameterList());
                glex.replaceTemplate(CDGeneratorHelper.CONSTRUCTOR_HOOK, constructor, template);
                constructors.add(constructor);
            }
        };

        target.getSymbol().getSuperTypes().stream().map(CDTypeSymbol::getAstNode).forEach(node -> node.accept(visitor));

        return constructors;
    }
}
