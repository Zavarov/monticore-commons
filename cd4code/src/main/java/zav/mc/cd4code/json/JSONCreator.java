package zav.mc.cd4code.json;

import de.monticore.cd.cd4analysis.CD4AnalysisMill;
import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4analysis._symboltable.Stereotype;
import de.monticore.cd.cd4code._visitor.CD4CodeVisitor;
import de.monticore.cd.facade.CDModifier;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.StringHookPoint;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCPrimitiveType;
import de.monticore.types.mccollectiontypes._ast.ASTMCOptionalType;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import de.monticore.utils.Names;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import zav.mc.cd4code.CDCreator;
import zav.mc.cd4code.CDGeneratorHelper;
import zav.mc.cd4code.CDMethodComparator;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.*;

/**
 * This class is responsible for creating the JSON representation of a corresponding {@link ASTCDType}. The serialization
 * is done via a {@code ToJson} method.
 */
public class JSONCreator extends CDCreator<ASTCDType> {
    public static final String FROM_PATH_TEMPLATE = "FromPath";
    public static final String FROM_STRING_TEMPLATE = "FromString";
    public static final String FROM_JSON_TEMPLATE = "FromJson";
    public static final String TO_JSON_TEMPLATE = "ToJson";

    /**
     * The <code>from</code> submodule contains all templates that are used to initialize the attributes of a given
     * {@link ASTCDType} with the values contained in the JSON file. The library we use supports the extraction of
     * primitive types, as well as strings. More complex types, especially container like lists or maps have to be
     * implemented by the developer.
     */
    private static final String FROM_SUBMODULE = "from";
    public static final String FROM_BOOLEAN = Names.getQualifiedName(FROM_SUBMODULE, "Boolean");
    public static final String FROM_DOUBLE = Names.getQualifiedName(FROM_SUBMODULE, "Double");
    public static final String FROM_FLOAT = Names.getQualifiedName(FROM_SUBMODULE, "Float");
    public static final String FROM_INT = Names.getQualifiedName(FROM_SUBMODULE, "Int");
    public static final String FROM_LONG = Names.getQualifiedName(FROM_SUBMODULE, "Long");
    public static final String FROM_STRING = Names.getQualifiedName(FROM_SUBMODULE, "String");
    public static final String FROM_HANDWRITTEN = Names.getQualifiedName(FROM_SUBMODULE, "Handwritten");

    private static final String TO_SUBMODULE = "to";
    public static final String TO_NATIVE = Names.getQualifiedName(TO_SUBMODULE, "Native");
    public static final String TO_LOCAL = Names.getQualifiedName(TO_SUBMODULE, "Local");
    public static final String TO_HANDWRITTEN = Names.getQualifiedName(TO_SUBMODULE, "Handwritten");

    public static final String FROM_PATH_METHOD = "public static %1$s fromJson(%1$s target, Path path) throws IOException;";
    public static final String FROM_STRING_METHOD = "public static %1$s fromJson(%1$s target, String content);";
    public static final String FROM_JSON_METHOD = "public static %1$s fromJson(%1$s target, JSONObject source);";
    public static final String TO_JSON_METHOD = "public static JSONObject toJson(%s source, JSONObject target);";

    public static final String FROM_JSON_ATTRIBUTE_METHOD = "protected void $from%s(JSONObject source, %s target);";
    public static final String TO_JSON_ATTRIBUTE_METHOD = "protected void $to%s(%s source, JSONObject target);";

    private MCFullGenericTypesPrettyPrinter typePrinter = new MCFullGenericTypesPrettyPrinter(new IndentPrinter());

    private JSONCreator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
    }

    public static ASTCDClass create(ASTCDType ast, GlobalExtensionManagement glex){
        return new JSONCreator(glex).decorate(ast);
    }

    @Override
    public ASTCDClass decorate(ASTCDType ast) {
        ASTCDClass json = buildClass(ast);

        json.addAllCDMethods(buildMethods(ast));
        json.addAllCDAttributes(buildAttributes(ast));

        return json;
    }

    private ASTCDClass buildClass(ASTCDType ast){
        return CD4AnalysisMill.cDClassBuilder()
                .setName("JSON"+ast.getName())
                .setModifier(CDModifier.PUBLIC.build())
                .build();
    }

    private Set<ASTCDAttribute> buildAttributes(ASTCDType ast){
        Set<ASTCDAttribute> attributes = new HashSet<>();

        for(ASTCDAttribute attribute : getAttributes(ast)) {
            //Creates an attribute of the type: protected static final KEY = "key";
            //The key is the key used to store the value of the variable in the JSON object.
            ASTCDAttribute node = getCDAttributeFacade().createAttribute(
                    CDModifier.PROTECTED_STATIC_FINAL,
                    "String",
                    attribute.getName().toUpperCase(Locale.ENGLISH)
            );

            glex.replaceTemplate(
                    CDGeneratorHelper.VALUE_HOOK,
                    node,
                    new StringHookPoint(" = \"" + getKey(attribute) + "\"")
            );

            attributes.add(node);
        }

        return attributes;
    }

    private Set<ASTCDMethod> buildMethods(ASTCDType ast){
        //Duplicates will be created when there are no container attributes.
        Set<ASTCDMethod> methods = new TreeSet<>(new CDMethodComparator());

        methods.addAll(buildFromJson(ast));
        for(ASTCDAttribute attribute : getAttributes(ast))
            methods.add(buildFromJson(ast, attribute));

        methods.add(buildToJson(ast));
        for(ASTCDAttribute attribute : getAttributes(ast))
            methods.add(buildToJson(ast, attribute));

        return methods;
    }

    /**
     * Generates the static methods for deserializing instances of the given {@link ASTCDType}.<br>
     * There are three different methods, one for deserializing it from a {@link Path}, {@link String}
     * and {@link JSONObject}.
     * @param ast The {@link ASTCDType} that is deserialized.
     * @return The generated methods.
     */
    private Set<ASTCDMethod> buildFromJson(ASTCDType ast){
        Set<ASTCDMethod> methods = new HashSet<>();
        ASTCDMethod method;

        method = buildMethod(FROM_PATH_METHOD, ast.getName());
        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_PATH_TEMPLATE, method);
        methods.add(method);

        method = buildMethod(FROM_STRING_METHOD, ast.getName());
        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_STRING_TEMPLATE, method);
        methods.add(method);

        method = buildMethod(FROM_JSON_METHOD, ast.getName());
        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_JSON_TEMPLATE, ast, method, getAttributes(ast));
        methods.add(method);

        return methods;
    }

    /**
     * Generates the methods for deserializing the individual attributes of a given {@link  ASTCDType}.
     * @param type The {@link ASTCDType} containing the attributes.
     * @param attribute An {@link ASTCDAttribute} of a given {@link ASTCDType}.
     * @return The method that deserializes the given {@link ASTCDAttribute}.
     */
    private ASTCDMethod buildFromJson(ASTCDType type, ASTCDAttribute attribute){
        ASTCDMethod method;

        method = buildMethod(FROM_JSON_ATTRIBUTE_METHOD, StringUtils.capitalize(attribute.getName()), type.getName());
        setFromTemplate(method, attribute);

        return method;
    }

    private void setFromTemplate(ASTCDMethod method, ASTCDAttribute attribute){
        CD4CodeVisitor templateVisitor = new CD4CodeVisitor() {
            @Override
            public void visit(ASTMCPrimitiveType node){
                if(node.isBoolean()){
                    setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_BOOLEAN, getKey(attribute), attribute, false);
                }else if(node.isDouble()){
                    setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_DOUBLE, getKey(attribute), attribute, false);
                }else if(node.isFloat()){
                    setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_FLOAT, getKey(attribute), attribute, false);
                }else if(node.isInt()){
                    setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_INT, getKey(attribute), attribute, false);
                }else if(node.isLong()){
                    setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_LONG, getKey(attribute), attribute, false);
                }
            }

            @Override
            public void visit(ASTMCOptionalType node){
                switch(node.getMCTypeArgument().printType(typePrinter)){
                    case "String":
                        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_STRING, getKey(attribute), attribute, true);
                        break;
                    case "Boolean":
                        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_BOOLEAN, getKey(attribute), attribute, true);
                        break;
                    case "Double":
                        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_DOUBLE, getKey(attribute), attribute, true);
                        break;
                    case "Float":
                        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_FLOAT, getKey(attribute), attribute, true);
                        break;
                    case "Integer":
                        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_INT, getKey(attribute), attribute, true);
                        break;
                    case "Long":
                        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_LONG, getKey(attribute), attribute, true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void visit(ASTCDAttribute node){
                if(!CDGeneratorHelper.isPrimitive(node))
                    if(node.getSymbol().getType().lazyLoadDelegate().getFullName().equals("java.lang.String.String"))
                        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_STRING, getKey(attribute), attribute, false);
            }
        };

        //"Default" template that may be overwritten by more specific templates by the visitor
        setTemplate(method, CDGeneratorHelper.JSON_MODULE, FROM_HANDWRITTEN, attribute);
        //Use more specific templates for types that can be resolved directly
        attribute.accept(templateVisitor);
    }

    /**
     * Generates the static method for serializing instances of the given {@link ASTCDType}.
     * @param ast The {@link ASTCDType} that is serialized.
     * @return The generated method.
     */
    private ASTCDMethod buildToJson(ASTCDType ast){
        ASTCDMethod method;

        method = buildMethod(TO_JSON_METHOD, ast.getName());
        setTemplate(method, CDGeneratorHelper.JSON_MODULE, TO_JSON_TEMPLATE, ast, method, getAttributes(ast));

        return method;
    }

    /**
     * Generates the methods for serializing the individual attributes of a given {@link  ASTCDType}.
     * @param type The {@link ASTCDType} containing the attributes.
     * @param attribute An {@link ASTCDAttribute} of a given {@link ASTCDType}.
     * @return The method that serializes the given {@link ASTCDAttribute}.
     */
    private ASTCDMethod buildToJson(ASTCDType type, ASTCDAttribute attribute){
        ASTCDMethod method;

        method = buildMethod(TO_JSON_ATTRIBUTE_METHOD, StringUtils.capitalize(attribute.getName()), type.getName());
        setToTemplate(type, method, attribute);

        return method;
    }

    private void setToTemplate(ASTCDType type, ASTCDMethod method, ASTCDAttribute attribute){
        CD4CodeVisitor templateVisitor = new CD4CodeVisitor() {
            @Override
            public void visit(ASTMCPrimitiveType node){
                setTemplate(method, CDGeneratorHelper.JSON_MODULE, TO_NATIVE, getKey(attribute), attribute, false);
            }

            @Override
            public void visit(ASTMCOptionalType node){
                switch(node.getMCTypeArgument().printType(typePrinter)){
                    case "String":
                    case "Boolean":
                    case "Double":
                    case "Float":
                    case "Integer":
                    case "Long":
                        setTemplate(method, CDGeneratorHelper.JSON_MODULE, TO_NATIVE, getKey(attribute), attribute, true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void endVisit(ASTCDAttribute node){
                if(!CDGeneratorHelper.isPrimitive(node)) {
                    if(CDGeneratorHelper.inLocalScope(type, node))
                        setTemplate(method, CDGeneratorHelper.JSON_MODULE, TO_LOCAL, node.getSymbol().getType().lazyLoadDelegate(), getKey(attribute), attribute, false);
                    else if (node.getSymbol().getType().lazyLoadDelegate().getFullName().equals("java.lang.String.String"))
                        setTemplate(method, CDGeneratorHelper.JSON_MODULE, TO_NATIVE, getKey(attribute), attribute, false);
                }
            }
        };

        //"Default" template that may be overwritten by more specific templates by the visitor
        setTemplate(method, CDGeneratorHelper.JSON_MODULE, TO_HANDWRITTEN, attribute);
        //Use more specific templates for types that can be resolved directly
        attribute.accept(templateVisitor);
    }

    private String getKey(ASTCDAttribute node){
        return node.getSymbol().getStereotype(CDGeneratorHelper.JSON_KEY).map(Stereotype::getValue).orElse(node.getName());
    }

    private Set<ASTCDAttribute> getAttributes(ASTCDType node){
        //Avoid duplicates
        Set<ASTCDAttribute> attributes = new TreeSet<>(Comparator.comparing(ASTCDAttributeTOP::getName));

        //Get all attributes
        attributes.addAll(node.getCDAttributeList());
        for(CDTypeSymbol parent : node.getSymbol().getSuperTypes())
            attributes.addAll(getAttributes(parent.getAstNode()));
        //Filter attributes that are ignored;
        attributes.removeIf(ast -> ast.getSymbol().getStereotype(CDGeneratorHelper.JSON_IGNORE).isPresent());

        return attributes;
    }
}
