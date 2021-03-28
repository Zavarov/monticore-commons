package zav.mc.monticore.cd4code.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import zav.mc.cd4code.decorator.DecoratorCreator;
import zav.mc.monticore.CSV2StringArray;
import zav.mc.monticore.cd4code.BasicCDGeneratorTest;

public class DecoratorCreatorTest extends BasicCDGeneratorTest {
    @BeforeEach
    public void setUp(){
        super.setUp();
        //Log.enableFailQuick(false);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "ConcurrentMap<URL,Cookie> : asMapCookies         : ''",
            "                     void : cleanUpCookies       : ''",
            "                   Cookie : getCookies           : 'URL : Callable<? extends Cookie>'",
            " ImmutableMap<URL,Cookie> : getAllPresentCookies : 'Iterable<?>'",
            "                   Cookie : getIfPresentCookies  : 'Object'",
            "                     void : invalidateCookies    : 'Object'",
            "                     void : invalidateAllCookies : ''",
            "                     void : invalidateAllCookies : 'Iterable<?>'",
            "                     void : putCookies           : 'URL : Cookie'",
            "                     void : putAllCookies        : 'Map<? extends URL,? extends Cookie>'",
            "                     long : sizeCookies          : ''",
            "               CacheStats : statsCookies         : ''",
            "                   Cookie : getCookies           : 'URL'",
            "                   Cookie : getUncheckedCookies  : 'URL'",
            "                 Set<URL> : keysCookies          : ''",
            "       Collection<Cookie> : valuesCookies        : ''",


    }, delimiter = ':')
    public void testDecorateCache(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        CDDefinitionSymbol symbol = getCDDefinitionSymbol("vartas.monticore.cd4code.decorator.Cache");
        ASTCDDefinition ast = DecoratorCreator.create(symbol.getAstNode(), glex);

        ASTCDType cdClass = getCDType(ast, "Browser");
        ASTCDMethod cdMethod = getCDMethod(cdClass, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();
        Assertions.assertThat(printer.prettyprint(mcReturnType)).isEqualTo(returnName);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "       boolean : addValues            : 'String'",
            "       boolean : addAllValues         : 'Collection<? extends String>'",
            "          void : clearValues          : ''",
            "       boolean : containsValues       : 'Object'",
            "       boolean : containsAllValues    : 'Collection<?>'",
            "       boolean : isEmptyValues        : ''",
            "Stream<String> : parallelStreamValues : ''",
            "       boolean : removeValues         : 'Object'",
            "       boolean : removeAllValues      : 'Collection<?>'",
            "       boolean : removeIfValues       : 'Predicate<? super String>'",
            "       boolean : retainAllValues      : 'Collection<?>'",
            "           int : sizeValues           : ''",
            "Stream<String> : streamValues         : ''",
            "      Object[] : toArrayValues        : ''",
            "      String[] : toArrayValues        : 'IntFunction<String[]>'",
            "      String[] : toArrayValues        : 'String[]'"
    }, delimiter = ':')
    public void testDecorateCollection(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        CDDefinitionSymbol symbol = getCDDefinitionSymbol("vartas.monticore.cd4code.decorator.List");
        ASTCDDefinition ast = DecoratorCreator.create(symbol.getAstNode(), glex);

        ASTCDType cdClass = getCDType(ast, "Database");
        ASTCDMethod cdMethod = getCDMethod(cdClass, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();
        Assertions.assertThat(printer.prettyprint(mcReturnType)).isEqualTo(returnName);
    }


    @ParameterizedTest
    @CsvSource(value = {
            "                void : forEachValues     : 'Consumer<? super String>'",
            "    Iterator<String> : iteratorValues    : ''",
            " Spliterator<String> : spliteratorValues : ''"
    }, delimiter = ':')
    public void testDecorateIterable(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        CDDefinitionSymbol symbol = getCDDefinitionSymbol("vartas.monticore.cd4code.decorator.List");
        ASTCDDefinition ast = DecoratorCreator.create(symbol.getAstNode(), glex);

        ASTCDType cdClass = getCDType(ast, "Database");
        ASTCDMethod cdMethod = getCDMethod(cdClass, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();
        Assertions.assertThat(printer.prettyprint(mcReturnType)).isEqualTo(returnName);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "                void : addValues          : 'int : String'",
            "             boolean : addAllValues       : 'int : Collection<? extends String>'",
            "              String : getValues          : 'int'",
            "                 int : indexOfValues      : 'Object'",
            "                 int : lastIndexOfValues  : 'Object'",
            "ListIterator<String> : listIteratorValues : ''",
            "ListIterator<String> : listIteratorValues : 'int'",
            "              String : removeValues       : 'int'",
            "                void : replaceAllValues   : 'UnaryOperator<String>'",
            "              String : setValues          : 'int : String'",
            "                void : sortValues         : 'Comparator<? super String>'",
            "        List<String> : subListValues      : 'int : int'"
    }, delimiter = ':')
    public void testDecorateList(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        CDDefinitionSymbol symbol = getCDDefinitionSymbol("vartas.monticore.cd4code.decorator.List");
        ASTCDDefinition ast = DecoratorCreator.create(symbol.getAstNode(), glex);

        ASTCDType cdClass = getCDType(ast, "Database");
        ASTCDMethod cdMethod = getCDMethod(cdClass, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();
        Assertions.assertThat(printer.prettyprint(mcReturnType)).isEqualTo(returnName);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "                         void : clearSubmissions            : ''",
            "                   Submission : computeSubmissions          : 'ID : BiFunction<? super ID,? super Submission,? extends Submission>'",
            "                   Submission : computeIfAbsentSubmissions  : 'ID : Function<? super ID,? extends Submission>'",
            "                   Submission : computeIfPresentSubmissions : 'ID : BiFunction<? super ID,? super Submission,? extends Submission>'",
            "                      boolean : containsKeySubmissions      : 'Object'",
            "                      boolean : containsValueSubmissions    : 'Object'",
            "Set<Map.Entry<ID,Submission>> : entrySetSubmissions         : ''",
            "                      boolean : equalsSubmissions           : 'Object'",
            "                         void : forEachSubmissions          : 'BiConsumer<? super ID,? super Submission>'",
            "                   Submission : getSubmissions              : 'Object'",
            "                   Submission : getOrDefaultSubmissions     : 'Object : Submission'",
            "                      boolean : isEmptySubmissions          : ''",
            "                      Set<ID> : keySetSubmissions           : ''",
            "                   Submission : mergeSubmissions            : 'ID : Submission : BiFunction<? super Submission,? super Submission,? extends Submission>'",
            "                   Submission : putSubmissions              : 'ID : Submission'",
            "                         void : putAllSubmissions           : 'Map<? extends ID,? extends Submission>'",
            "                   Submission : putIfAbsentSubmissions      : 'ID : Submission'",
            "                   Submission : removeSubmissions           : 'Object'",
            "                      boolean : removeSubmissions           : 'Object : Object'",
            "                   Submission : replaceSubmissions          : 'ID : Submission'",
            "                      boolean : replaceSubmissions          : 'ID : Submission : Submission'",
            "                         void : replaceAllSubmissions       : 'BiFunction<? super ID,? super Submission,? extends Submission>'",
            "                          int : sizeSubmissions             : ''",
            "       Collection<Submission> : valuesSubmissions           : ''",


    }, delimiter = ':')
    public void testDecorateMap(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        CDDefinitionSymbol symbol = getCDDefinitionSymbol("vartas.monticore.cd4code.decorator.Map");
        ASTCDDefinition ast = DecoratorCreator.create(symbol.getAstNode(), glex);

        ASTCDType cdClass = getCDType(ast, "Subreddit");
        ASTCDMethod cdMethod = getCDMethod(cdClass, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();
        Assertions.assertThat(printer.prettyprint(mcReturnType)).isEqualTo(returnName);
    }

    @ParameterizedTest
    @CsvSource(value = {
            " boolean : equalsNickname    : 'Object'",
            //TODO "Class<?> : getClassNickname  : ''",
            "     int : hashCodeNickname  : ''",
            "    void : notifyNickname    : ''",
            "    void : notifyAllNickname : ''",
            "  String : toStringNickname  : ''",
            "    void : waitNickname      : ''",
            "    void : waitNickname      : 'long'",
            "    void : waitNickname      : 'long : int'"


    }, delimiter = ':')
    public void testDecorateObject(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        CDDefinitionSymbol symbol = getCDDefinitionSymbol("vartas.monticore.cd4code.decorator.Optional");
        ASTCDDefinition ast = DecoratorCreator.create(symbol.getAstNode(), glex);

        ASTCDType cdClass = getCDType(ast, "User");
        ASTCDMethod cdMethod = getCDMethod(cdClass, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();
        Assertions.assertThat(printer.prettyprint(mcReturnType)).isEqualTo(returnName);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Optional<String> : filterNickname          : 'Predicate<? super String>'",
            "Optional<String> : getNickname             : ''",
            "            void : ifPresentNickname       : 'Consumer<? super String>'",
            "            void : ifPresentOrElseNickname : 'Consumer<? super String> : Runnable'",
            "         boolean : isEmptyNickname         : ''",
            "         boolean : isPresentNickname       : ''",
            "Optional<String> : orNickname              : 'Supplier<? extends Optional<? extends String>>'",
            "          String : orElseNickname          : 'String'",
            "          String : orElseGetNickname       : 'Supplier<? extends String>'",
            "          String : orElseThrowNickname     : ''",
            "  Stream<String> : streamNickname          : ''",
            "            void : setNickname             : 'String'",
            "            void : setId                   : 'int'",
            "             int : getId                   : ''"


    }, delimiter = ':')
    public void testDecorateOptional(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        CDDefinitionSymbol symbol = getCDDefinitionSymbol("vartas.monticore.cd4code.decorator.Optional");
        ASTCDDefinition ast = DecoratorCreator.create(symbol.getAstNode(), glex);

        ASTCDType cdClass = getCDType(ast, "User");
        ASTCDMethod cdMethod = getCDMethod(cdClass, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();
        Assertions.assertThat(printer.prettyprint(mcReturnType)).isEqualTo(returnName);
    }
}
