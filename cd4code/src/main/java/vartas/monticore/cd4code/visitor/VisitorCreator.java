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

package vartas.monticore.cd4code.visitor;

import com.google.common.collect.Lists;
import de.monticore.cd.cd4analysis.CD4AnalysisMill;
import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.cd.facade.CDModifier;
import de.monticore.codegen.cd2java.AbstractCreator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.types.mccollectiontypes._ast.ASTMCGenericType;
import de.monticore.types.mccollectiontypes._ast.ASTMCTypeArgument;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.se_rwth.commons.Joiners;
import org.apache.commons.lang3.StringUtils;
import vartas.monticore.cd4code.CDGeneratorHelper;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Applies the Visitor pattern on a given class diagram. For each type, the following methods are generated:
 * <ol>
 *     <li>visit</li>
 *     <li>walkUpFrom</li>
 *     <li>traverse</li>
 *     <li>handle</li>
 *     <li>endWalkUpFrom</li>
 *     <li>endVisit</li>
 * </ol>
 * The visitor will traverse all types in a top-down fashion. It will first visit the current class
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
     * Signature of the walkUpFrom method.<br>
     * By recursively calling <code>walkUpFrom</code> for all its supertypes, the visitor can also be used as an
     * inheritance visitor.
     */
    public static final String WALK_UP_FROM = "void walkUpFrom(%s %s);";
    /**
     * Signature of the endWalkUpFrom method.<br>
     * By recursively calling <code>endWalkUpFrom</code> for all its supertypes, the visitor can also be used as an
     * inheritance visitor.
     */
    public static final String END_WALK_UP_FROM = "void endWalkUpFrom(%s %s);";
    /**
     * Signature of the getRealThis method.<br>
     * When the TOP mechanism is applied, the <code>accept</code> method will only exist in the subclass. However,
     * an instance of the base class is required for the method, which this one provides. The MontiCore visitor
     * had exactly the same issue. However, the developers decided to explicitly cast each TOP class to its base class.
     */
    public static final String GET_REAL_THIS = "public %s getRealThis();";
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
    private final CDGeneratorHelper generatorHelper;

    /**
     * Creates a new instance of the visitor creator.
     * @param cdDefinitionSymbol the {@link CDDefinitionSymbol} of the class diagram.
     * @param glex the {@link GlobalExtensionManagement} binding the templates to the hook points.
     * @param generatorHelper the generator helper instance.
     */
    protected VisitorCreator(@Nonnull CDDefinitionSymbol cdDefinitionSymbol, @Nonnull GlobalExtensionManagement glex, @Nonnull CDGeneratorHelper generatorHelper){
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

        buildHookpoints(cdDefinition, cdInterface);

        cdDefinition.streamCDClasss().map(this::createMethods).forEach(cdInterface::addAllCDMethods);
        cdDefinition.streamCDEnums().map(this::createMethods).forEach(cdInterface::addAllCDMethods);
        cdDefinition.streamCDInterfaces().map(this::createMethods).forEach(cdInterface::addAllCDMethods);

        return cdInterface;
    }

    /**
     * Generates the <code>accept</code> method for all types in the provided class diagram.
     * @param ast the {@link ASTCDDefinition} of the class diagram.
     */
    private void buildHookpoints(ASTCDDefinition ast, ASTCDInterface visitor){
        ast.accept(new CDDefinitionVisitor(visitor));
    }

    /**
     * Builds the frame of the visitor interface. The name for it is derived from name of the class diagram,
     * followed by <code>Visitor</code>.
     * @param ast the {@link ASTCDDefinition} of the class diagram.
     * @return an empty visitor interface.
     */
    protected ASTCDInterface buildVisitor(ASTCDDefinition ast){
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
                createEndVisit(cdType),
                createWalkUpFrom(cdType),
                createEndWalkUpFrom(cdType)
        );
    }

    /**
     * Creates the <code>visit</code> method for the associated {@link ASTCDType}
     * and binds the corresponding template to it.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return the <code>visit</code> method for the associated {@link ASTCDType}.
     */
    protected ASTCDMethod createVisit(ASTCDType cdType){
        String typeName = cdType.getName();
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(VISIT, typeName, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.Visit"));
        return cdMethod;
    }

    /**
     * Creates the <code>endVisit</code> method for the associated {@link ASTCDType}
     * and binds the corresponding template to it.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return the <code>endVisit</code> method for the associated {@link ASTCDType}.
     */
    protected ASTCDMethod createEndVisit(ASTCDType cdType){
        String typeName = cdType.getName();
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(END_VISIT, typeName, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.EndVisit"));
        return cdMethod;
    }

    /**
     * Creates the <code>handle</code> method for the associated {@link ASTCDType} and binds the corresponding
     * template to it. This method first calls <code>walkUpFrom</code>, then <code>traverse</code> and
     * <code>endWalkUpFrom</code> for the current {@link ASTCDType}.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return the <code>handle</code> method for the associated {@link ASTCDType}.
     */
    protected ASTCDMethod createHandle(ASTCDType cdType){
        String typeName = cdType.getName();
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
        String typeName = cdType.getName();
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(TRAVERSE, typeName, varName);

        //Link to the method body
        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        List<ASTCDAttribute> cdAttributes = new ArrayList<>();

        //Link the individual attributes to their respective visitor template
        for(ASTCDAttribute cdAttribute : cdType.getCDAttributeList()) {
            //Primitive types can't be loaded
            if(CDGeneratorHelper.isPrimitive(cdAttribute.getMCType()))
                continue;

            CDTypeSymbol cdTypeSymbol = cdAttribute.getSymbol().getType().lazyLoadDelegate();

            //The accessor for containers has to be provided. (e.g. via iterator and whatnot)
            if (CDGeneratorHelper.isContainer(cdTypeSymbol)) {
                cdAttributes.add(cdAttribute);
                replaceTemplate(CDGeneratorHelper.ATTRIBUTE_HOOK, cdAttribute, new TemplateHookPoint(computeTemplate(cdTypeSymbol), cdMethod.getCDParameter(0), cdAttribute, visitor.accept(cdAttribute)));
            //The "accept" method is only generated for classes in the local CDDefinition
            } else if (CDGeneratorHelper.inLocalScope(cdType, cdTypeSymbol)) {
                cdAttributes.add(cdAttribute);
                replaceTemplate(CDGeneratorHelper.ATTRIBUTE_HOOK, cdAttribute, new TemplateHookPoint(computeDefaultTemplate(), cdMethod.getCDParameter(0), cdAttribute, visitor.accept(cdAttribute)));
            }
        }

        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.Traverse", cdType, cdAttributes));
        return cdMethod;
    }

    /**
     * Creates the <code>walkUpFrom</code> method for the associated {@link ASTCDType} and binds the corresponding
     * template to it. This method calls <code>walkUpFrom</code> for all its supertypes, before calling
     * <code>visit</code> for the current type.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return the <code>walkUpFrom</code> method for the associated {@link ASTCDType}.
     */
    protected ASTCDMethod createWalkUpFrom(ASTCDType cdType){
        String typeName = cdType.getName();
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(WALK_UP_FROM, typeName, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.WalkUpFrom", cdMethod, computeSuperTypes(cdType)));
        return cdMethod;
    }

    /**
     * Creates the <code>endWalkUpFrom</code> method for the associated {@link ASTCDType} and binds the corresponding
     * template to it. This method first calls <code>endVisit</code> for the current type, before continuing to call
     * <code>endWalkUpFrom</code> for all its supertypes.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return the <code>endWalkUpFrom</code> method for the associated {@link ASTCDType}.
     */
    protected ASTCDMethod createEndWalkUpFrom(ASTCDType cdType){
        String typeName = cdType.getName();
        String varName = StringUtils.uncapitalize(typeName);
        String signature = String.format(END_WALK_UP_FROM, typeName, varName);

        ASTCDMethod cdMethod = getCDMethodFacade().createMethodByDefinition(signature);
        replaceTemplate(CDGeneratorHelper.METHOD_HOOK, cdMethod, new TemplateHookPoint("visitor.EndWalkUpFrom", cdMethod, computeSuperTypes(cdType)));
        return cdMethod;
    }

    /**
     * Computes the effective name sof the super types of the provide {@link ASTCDType} in the order they appear.
     * Those names are relevant when calling the visit method for more than just the current type.
     * @param cdType one of the {@link ASTCDType} of the class diagram.
     * @return a list containing the effective names of all superypes of the provided {@link ASTCDType}.
     */
    protected List<String> computeSuperTypes(ASTCDType cdType){
        return cdType.getSymbol()
                .getSuperTypes()
                .stream()
                //Omit supertypes that aren't in the local scope and thus don't have an accept method
                .filter(cdSuperType -> cdType.getSymbol().getEnclosingScope().getLocalCDTypeSymbols().contains(cdSuperType))
                .map(CDTypeSymbol::getAstNode)
                .map(ASTCDType::getName)
                .collect(Collectors.toList());
    }

    /**
     * The <code>getRealThis</code> method is specified in <code>GetRealThis.ftl</code>
     * @return the template path for the getRealThis method.
     */
    private String computeGetRealThisTemplate(){
        return computeTemplate("GetRealThis");
    }

    /**
     * The <code>accept</code> method is specified in <code>Accept.ftl</code>
     * @return the template path for the accept method.
     */
    private String computeAcceptTemplate(){
        return computeTemplate("Accept");
    }

    /**
     * The <code>accept</code> method is specified in <code>Default.ftl</code> This is the default template
     * by traversing through a type by calling its <code>accept</code> method.
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
        private final MCFullGenericTypesPrettyPrinter printer = new MCFullGenericTypesPrettyPrinter(new IndentPrinter());
        /**
         * A bitmap indicating whether one of the container arguments is associated with an internal class diagram.
         * If so, we can access it via one of the templates.
         */
        private final LinkedHashMap<String, Boolean> argumentTypes = new LinkedHashMap<>();

        /**
         * Associates each of the generic arguments of the provided {@link ASTCDAttribute} with whether there
         * exists a respective class diagram for them. We assume that the {@link ASTCDAttribute} is a container class.
         * @param ast an {@link ASTCDAttribute} in an {@link ASTCDType}.
         * @return an immutable bitmap associating each generic container argument with whether their exists an
         * corresponding class diagram.
         */
        public List<Map.Entry<String, Boolean>> accept(ASTCDAttribute ast){
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
            for(ASTMCTypeArgument argument : ast.getMCTypeArgumentList())
                processType(argument.printType(printer));
        }

        /**
         * In case the provided attribute doesn't belong to a container class.<br>
         * In this case, the attribute should be visited if and only if its type is in the local class diagram.
         * @param ast the type of the provided {@link ASTCDAttribute}.
         */
        @Override
        public void visit(ASTMCQualifiedType ast){
            processType(ast.printType(printer));
        }

        //TODO Use Type symbols instead of a pretty printer
        private void processType(String qualifiedName){
            Optional<CDTypeSymbol> typeSymbol = cdDefinitionSymbol.getType(qualifiedName);
            //isPresent() == True <-> The symbol is in the local scope -> The symbol has an accept method
            argumentTypes.put(qualifiedName, typeSymbol.isPresent());
        }
    }

    /**
     * Applies all for the visitor required methods to each {@link ASTCDType} in the current {@link ASTCDDefinition}.
     * The pattern requires each class to have an <code>accept</code> method, in order to apply the visitor.
     * Due to the TOP mechanism, it is also necessary to implement an <code>getRealThis</code> method, to get
     * access to the superclass.
     */
    private class CDDefinitionVisitor implements CD4CodeInheritanceVisitor{
        /**
         * The <code>getRealThis</code> method. It returns a reference to the superclass in case the TOP mechanism
         * has been applied due to a handwritten file.
         */
        private ASTCDMethod cdGetRealThis;
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
            this.cdVisitor = cdVisitor;
            this.cdAccept = buildAccept();
            this.cdParameter = cdAccept.getCDParameter(0);  //Visitor class
        }

        /**
         * For each definition, only a single visitor class is generated. This method registers this visitor by both
         * declaring an import statement to it and by creating the corresponding <code>accept</code> method.
         * @param ast The current {@link ASTCDDefinition}
         */
        public void visit(ASTCDDefinition ast){
            handleImport(ast);
            handleAccept();
        }

        /**
         * Adds the qualified name of the visitor interface to the class diagram. Since every class contains the
         * <code>accept</code> method, it needs to contain a reference to the visitor class.
         * @param ast the {@link ASTCDDefinition} associated with the class diagram.
         */
        private void handleImport(ASTCDDefinition ast){
            String importName = Joiners.DOT.join(
                    cdDefinitionSymbol.getPackageName(),
                    CDGeneratorHelper.VISITOR_MODULE,
                    cdParameter.getName(),
                    cdVisitor.getName()
            );

            ast.getSymbol().addImport(importName);
        }

        private ASTCDMethod buildAccept(){
            return getCDMethodFacade().createMethodByDefinition(String.format(ACCEPT, cdVisitor.getName()));
        }

        /**
         * Binds the <code>accept</code> method to its corresponding template.<br>
         */
        private void handleAccept(){
            glex.replaceTemplate(
                    CDGeneratorHelper.METHOD_HOOK,
                    cdAccept,
                    new TemplateHookPoint(computeAcceptTemplate(), cdParameter)
            );
        }

        /**
         * Creates the <code>getRealThis</code> method for the corresponding {@link ASTCDType} and binds it to its
         * respective template. There are two cases that have to be considered: In case there exists a handwritten file
         * and the TOP mechanism has been applied, the <code>getRealThis</code> method has to be declared as abstract,
         * so that the superclass has to implement it. Otherwise the method is implemented by simply returning an
         * instance of itself.
         * @param ast The current {@link ASTCDType} associated with the method.
         */
        public void visit(ASTCDType ast){
            cdGetRealThis = buildGetRealThis(ast);
            handleGetRealThis(ast);
        }

        private ASTCDMethod buildGetRealThis(ASTCDType ast){
            return getCDMethodFacade().createMethodByDefinition(String.format(GET_REAL_THIS, ast.getName()));
        }

        private void handleGetRealThis(ASTCDType type){
            if(generatorHelper.existsHandwrittenClass(type)) {
                cdGetRealThis.getModifier().setAbstract(true);
            }else{
                glex.replaceTemplate(
                        CDGeneratorHelper.METHOD_HOOK,
                        cdGetRealThis,
                        new TemplateHookPoint(computeGetRealThisTemplate())
                );
            }
        }

        /**
         * Adds the <code>accept</code> and <code>getRealThis</code> method to the {@link ASTCDClass}.
         * @param ast An {@link ASTCDClass} in the provided class diagram.
         */
        public void endVisit(ASTCDClass ast){
            ast.addCDMethod(cdAccept);
            ast.addCDMethod(cdGetRealThis);
        }

        /**
         * Adds the <code>accept</code> and <code>getRealThis</code> method to the {@link ASTCDEnum}.
         * @param ast An {@link ASTCDEnum} in the provided class diagram.
         */
        public void endVisit(ASTCDEnum ast){
            ast.addCDMethod(cdAccept);
            ast.addCDMethod(cdGetRealThis);
        }

        /**
         * Adds the <code>accept</code> and <code>getRealThis</code> method to the {@link ASTCDInterface}.
         * @param ast An {@link ASTCDInterface} in the provided class diagram.
         */
        public void endVisit(ASTCDInterface ast){
            ast.addCDMethod(cdAccept);
            ast.addCDMethod(cdGetRealThis);
        }
    }
}