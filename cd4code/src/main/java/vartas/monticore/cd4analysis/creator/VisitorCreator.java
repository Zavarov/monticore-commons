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

package vartas.monticore.cd4analysis.creator;

import com.google.common.collect.Lists;
import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.cd.facade.CDModifier;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.codegen.mc2cd.TransformationHelper;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mccollectiontypes._ast.ASTMCGenericType;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.monticore.utils.Names;
import de.se_rwth.commons.Joiners;
import org.apache.commons.lang3.StringUtils;
import vartas.monticore.cd4analysis.CDGeneratorHelper;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Applies the Visitor pattern on a given class diagram.
 * For each type, a <code>visit</code>, <code>endVisit</code>, <code>traverse</code> and <code>handle</code> method is
 * generated. The visitor traverses the types in a top-down fashion. It will first visit the current class
 * and then continue with all of the classes attributes. If one of them is associated with a type in the class diagram,
 * the visitor is applied on it. In case of container classes, the visitor is applied on all contained elements.<br>
 * <code>visit</code> will access the elements in inorder, while <code>endVisit</code> accesses them in postorder.
 * @link https://en.wikipedia.org/wiki/Visitor_pattern Visitor
 */
public class VisitorCreator extends AbstractCreator<ASTCDDefinition, ASTCDInterface> {
    /**
     * Signature of the visit method.<br>
     * This method will be called in a top-down fashion for all nodes in the AST.
     */
    public static final String VISIT = "void visit(%s %s);";
    /**
     * Signature of the endVisit method.<br>
     * This method will be called in a botton-up fashion for all nodes in the AST.
     */
    public static final String END_VISIT = "void endVisit(%s %s);";
    /**
     * Signature of the traverse method.<br>
     * This method will apply the visitor on all child nodes of the current node.
     */
    public static final String TRAVERSE = "void traverse(%s %s);";
    /**
     * Signature of the handle method.<br>
     * This method is the entry point for the accept method.
     */
    public static final String HANDLE = "void handle(%s %s);";
    /**
     * Signature of the accept method.<br>
     * This method is the hook point used to apply the visitor.
     */
    public static final String ACCEPT = "public void accept(%s visitor);";

    /**
     * The definition symbol is stored locally to have easy access to the package.<br>
     * This information is required when binding the accept method to the types of the class diagram.
     */
    @Nonnull
    private final CDDefinitionSymbol cdDefinitionSymbol;

    /**
     * The generator helper is required to check for handwritten files.
     */
    @Nonnull
    private CDGeneratorHelper generatorHelper;

    /**
     * Creates a new instance of the visitor creator.
     * @param cdDefinitionSymbol the {@link CDDefinitionSymbol} of the class diagram.
     * @param glex the {@link GlobalExtensionManagement} binding the templates to the hook points.
     * @param generatorHelper the generator helper instance.
     */
    private VisitorCreator(@Nonnull CDDefinitionSymbol cdDefinitionSymbol, @Nonnull GlobalExtensionManagement glex, @Nonnull CDGeneratorHelper generatorHelper){
        super(glex);
        this.cdDefinitionSymbol = cdDefinitionSymbol;
        this.generatorHelper = generatorHelper;
    }

    /**
     * Applies the visitor pattern on all types in the class diagram and creates a new visitor interface.
     * @param cdDefinition the {@link ASTCDDefinition} of the class diagram.
     * @param glex the {@link GlobalExtensionManagement} binding the templates to the hook points.
     * @param generatorHelper the generator helper instance.
     * @return the visitor interface for the provided class diagram.
     */
    public static ASTCDInterface create(ASTCDDefinition cdDefinition, GlobalExtensionManagement glex, CDGeneratorHelper generatorHelper){
        return new VisitorCreator(cdDefinition.getSymbol(), glex, generatorHelper).decorate(cdDefinition);
    }

    /**
     * Creates the visitor and all methods that are associated with it. This does both include the methods of the
     * visitor itself, responsible for visiting the individual nodes of the syntax tree, but also the <code>accept</code>
     * method of the nodes themselves, representing the hook point for custom visitors.
     * @param cdDefinition the {@link ASTCDDefinition} of the class diagram.
     * @return the visitor interface for the provided class diagram.
     */
    @Override
    public ASTCDInterface decorate(ASTCDDefinition cdDefinition) {
        ASTCDInterface cdInterface = buildVisitor(cdDefinition);

        buildAcceptor(cdDefinition, cdInterface);

        cdDefinition.streamCDClasss().map(this::createMethods).forEach(cdInterface::addAllCDMethods);
        cdDefinition.streamCDEnums().map(this::createMethods).forEach(cdInterface::addAllCDMethods);
        cdDefinition.streamCDInterfaces().map(this::createMethods).forEach(cdInterface::addAllCDMethods);

        return cdInterface;
    }

    /**
     * Generates the <code>accept</code> method for all types in the provided class diagram.
     * @param ast the {@link ASTCDDefinition} of the class diagram.
     */
    private void buildAcceptor(ASTCDDefinition ast, ASTCDInterface visitor){
        ast.accept(new CDDefinitionVisitor(visitor));
    }

    /**
     * Builds the frame of the visitor interface. The name for it is derived from name of the class diagram,
     * followed by <code>Visitor</code>.
     * @param ast the {@link ASTCDDefinition} of the class diagram.
     * @return an empty visitor interface.
     */
    private ASTCDInterface buildVisitor(ASTCDDefinition ast){
        return CD4AnalysisMill.cDInterfaceBuilder()
                .setName(ast.getName()+"Visitor")
                .setModifier(CDModifier.PUBLIC.build())
                .build();
    }

    /**
     * Creates the methods <code>visit</code>, <code>endVisit</code>, <code>traverse</code> and <code>handle</code>
     * for the provided {@link ASTCDType}.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return a list of all methods for the associated visitor.
     */
    protected List<ASTCDMethod> createMethods(ASTCDType cdType){
        return Arrays.asList(
                createHandle(cdType),
                createVisit(cdType),
                createTraverse(cdType),
                createEndVisit(cdType)
        );
    }

    /**
     * Creates the <code>visit</code> method for the associated {@link ASTCDType}
     * and binds the corresponding template to it.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return the <code>visit</code> method for the associated {@link ASTCDType}.
     */
    protected ASTCDMethod createVisit(ASTCDType cdType){
        String typeName = getTypeName(cdType);
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(VISIT, typeName, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.Visit", cdType));
        return cdMethod;
    }

    /**
     * Creates the <code>endVisit</code> method for the associated {@link ASTCDType}
     * and binds the corresponding template to it.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return the <code>endVisit</code> method for the associated {@link ASTCDType}.
     */
    protected ASTCDMethod createEndVisit(ASTCDType cdType){
        String typeName = getTypeName(cdType);
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(END_VISIT, typeName, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.EndVisit", cdType));
        return cdMethod;
    }

    /**
     * Creates the <code>handle</code> method for the associated {@link ASTCDType}
     * and binds the corresponding template to it.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return the <code>handle</code> method for the associated {@link ASTCDType}.
     */
    protected ASTCDMethod createHandle(ASTCDType cdType){
        String typeName = getTypeName(cdType);
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(HANDLE, typeName, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.Handle", cdMethod));
        return cdMethod;
    }

    /**
     * Creates the <code>traverse</code> method for the associated {@link ASTCDType}
     * and binds the corresponding template to it.<br>
     * This method is also responsible for traversing the child nodes of an individual node.
     * In case those are wrapped inside a container class, the corresponding accessor is used, provided by one of
     * the templates. Otherwise the <code>accept</code> method of the type is used to apply the visitor.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return the <code>traverse</code> method for the associated {@link ASTCDType}.
     */
    protected ASTCDMethod createTraverse(ASTCDType cdType){
        CDAttributeVisitor visitor = new CDAttributeVisitor();
        String typeName = getTypeName(cdType);
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(TRAVERSE, typeName, varName);

        //Link to the method body
        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.Traverse", cdType));

        //Link the individual attributes to their respective visitor template
        for(ASTCDAttribute cdAttribute : cdType.getCDAttributeList()){
            Optional<CDTypeSymbol> cdTypeSymbol = cdAttribute.getSymbol().getType().loadSymbol();
            //The accessor for containers has to be provided. (e.g. via iterator and whatnot)
            if(cdTypeSymbol.flatMap(symbol -> symbol.getStereotype(CDGeneratorHelper.CONTAINER_LABEL)).isPresent())
                replaceTemplate(CDGeneratorHelper.ATTRIBUTE_HOOK, cdAttribute, new TemplateHookPoint(computeTemplate(cdTypeSymbol.get()), cdMethod.getCDParameter(0), cdAttribute, visitor.accept(cdAttribute)));
            //isPresent -> Type in local class diagram -> Has generated "accept" method
            else if(cdTypeSymbol.isPresent())
                replaceTemplate(CDGeneratorHelper.ATTRIBUTE_HOOK, cdAttribute, new TemplateHookPoint(computeDefaultTemplate(), cdMethod.getCDParameter(0), cdAttribute));
        }

        return cdMethod;
    }

    /**
     * In case a handwritten class is detected and the TOP mechanism is applied, the visitor
     * has to refer to the TOP class instead.
     * @param ast one of the types in the class diagram.
     * @return the name of the type the visitor is handling.
     */
    private String getTypeName(ASTCDType ast){
        String qualifiedName = Names.getQualifiedName(ast.getSymbol().getPackageName(), ast.getName());

        if(TransformationHelper.existsHandwrittenClass(generatorHelper.getSourcesPath(), qualifiedName)) {
            //Rename the class
            return ast.getName() + CDGeneratorHelper.HANDWRITTEN_FILE_POSTFIX;
        }else{
            return ast.getName();
        }
    }

    /**
     * The <code>accept</code> method is specified in <code>Accept.ftl</code>
     * @return the template path for the accept method.
     */
    private String computeAcceptorTemplate(){
        return computeTemplate("Accept");
    }

    /**
     * The <code>accept</code> method is specified in <code>Default.ftl</code>
     * @return the template path for the accept method.
     */
    private String computeDefaultTemplate(){
        return computeTemplate("Default");
    }

    /**
     * Computes the qualified name of the provided template in the visitor module.
     * @param qualifiedName the name of the template file.
     * @return the qualfied template path for the accept method.
     */
    private String computeTemplate(String qualifiedName){
        return Joiners.DOT.join(CDGeneratorHelper.VISITOR_MODULE, qualifiedName);
    }

    /**
     * Computes the qualified name of the provided {@link CDTypeSymbol} in the package of the class diagram.
     * @param cdSymbol the {@link CDTypeSymbol} associated with the class diagram.
     * @return the qualified name of the symbol.
     */
    private String computeTemplate(CDTypeSymbol cdSymbol){
        return computeTemplate(Joiners.DOT.join(cdSymbol.getPackageName(), cdSymbol.getName()));
    }

    /**
     * This class is used to determine the arguments of container types
     * and whether or not they can be accessed by the visitor.
     */
    private class CDAttributeVisitor implements CD4CodeInheritanceVisitor{
        /**
         * There is no elegant way to get the {@link CDTypeSymbol} of an {@link ASTMCTypeArgument}. Hence why
         * we reduce it to a string first, before resolving it.
         */
        private MCFullGenericTypesPrettyPrinter printer = new MCFullGenericTypesPrettyPrinter(new IndentPrinter());
        /**
         * A bitmap indicating whether one of the container arguments is associated with an internal class diagram.
         * If so, we can access it via one of the templates.
         */
        private LinkedHashMap<ASTMCTypeArgument, Boolean> argumentTypes = new LinkedHashMap<>();

        /**
         * Associates each of the generic arguments of the provided {@link ASTCDAttribute} with whether there
         * exists a respective class diagram for them. We assume that the {@link ASTCDAttribute} is a container class.
         * @param ast an {@link ASTCDAttribute} in an {@link ASTCDType}.
         * @return an immutable bitmap associating each generic container argument with whether their exists an
         * corresponding class diagram.
         */
        public List<Map.Entry<ASTMCTypeArgument, Boolean>> accept(ASTCDAttribute ast){
            this.argumentTypes.clear();
            ast.accept(getRealThis());
            return Lists.newArrayList(argumentTypes.entrySet());
        }

        /**
         * In case the provided attribute belongs to a container class, their type will match {@link ASTMCGenericType}.
         * In any case, this method checks if any of the generic arguments has a corresponding class diagram and
         * stores the result in {@link #argumentTypes}
         * @param ast the type of the provided {@link ASTCDAttribute}.
         */
        @Override
        public void visit(ASTMCGenericType ast){
            for(ASTMCTypeArgument argument : ast.getMCTypeArgumentList()){
                //TODO Use Type symbols instead of a pretty printer
                String qualifiedName = argument.printType(printer);
                Optional<CDTypeSymbol> typeSymbol = cdDefinitionSymbol.getType(qualifiedName);
                //isPresent() == True <-> The symbol is in the local scope -> The symbol has an accept method
                argumentTypes.put(argument, typeSymbol.isPresent());
            }
        }
    }

    /**
     * Adds the "accept" method to all types that are associated with the visitor.
     */
    private class CDDefinitionVisitor implements CD4CodeInheritanceVisitor{
        /**
         * The <code>accept</code> method. Its body is independent of the class, so we can reuse it.
         */
        private final ASTCDMethod cdAccept;
        /**
         * The parameter of the <code>accept</code> method. I.e. the visitor.
         */
        private final ASTCDParameter cdParameter;
        /**
         * The visitor of the class diagram.
         */
        private final ASTCDInterface cdVisitor;

        /**
         * Creates a new visitor instance.
         * @param cdVisitor the visitor of the class diagram.
         */
        public CDDefinitionVisitor(ASTCDInterface cdVisitor){
            this.cdAccept = getCDMethodFacade().createMethodByDefinition(String.format(ACCEPT, cdVisitor.getName()));
            this.cdParameter = cdAccept.getCDParameter(0);
            this.cdVisitor = cdVisitor;
        }

        /**
         * Adds the qualified name of the visitor interface to the class diagram.<br>
         * This way, every class in this class diagram, every type inside will automatically
         * import it.
         * @param ast the {@link ASTCDDefinition} associated with the class diagram.
         */
        public void visit(ASTCDDefinition ast){
            String importName = Joiners.DOT.join(
                    cdDefinitionSymbol.getPackageName(),
                    CDGeneratorHelper.VISITOR_MODULE,
                    cdParameter.getName(),
                    cdVisitor.getName()
            );

            ast.getSymbol().addImport(importName);
        }

        /**
         * Binds the <code>accept</code> method to its corresponding template.<br>
         * For some unknown reason, the type itself doesn't have methods, so we have to visit the encapsulated types.
         * @param ast an arbitrary {@link ASTCDType} in the provided class diagram.
         */
        public void visit(ASTCDType ast){
            glex.replaceTemplate(CDGeneratorHelper.METHOD_HOOK, new TemplateHookPoint(computeAcceptorTemplate(), cdParameter));
        }

        /**
         * Adds the <code>accept</code> method to the {@link ASTCDClass}.
         * @param ast an {@link ASTCDClass} in the provided class diagram.
         */
        public void visit(ASTCDClass ast){
            ast.addCDMethod(cdAccept);
        }

        /**
         * Adds the <code>accept</code> method to the {@link ASTCDEnum}.
         * @param ast an {@link ASTCDEnum} in the provided class diagram.
         */
        public void visit(ASTCDEnum ast){
            ast.addCDMethod(cdAccept);
        }

        /**
         * Adds the <code>accept</code> method to the {@link ASTCDInterface}.
         * @param ast an {@link ASTCDInterface} in the provided class diagram.
         */
        public void visit(ASTCDInterface ast){
            ast.addCDMethod(cdAccept);
        }
    }
}