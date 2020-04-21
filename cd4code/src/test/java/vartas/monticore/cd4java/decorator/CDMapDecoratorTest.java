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

package vartas.monticore.cd4java.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDDefinition;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._symboltable.CDDefinitionSymbol;
import de.monticore.types.mcbasictypes._ast.ASTMCReturnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import vartas.monticore.cd4java.CSV2StringArray;

import static org.assertj.core.api.Assertions.assertThat;

public class CDMapDecoratorTest extends AbstractCDDecoratorTest {
    ASTCDDefinition cdDefinition;
    CDDefinitionSymbol cdDefinitionSymbol;

    @BeforeEach
    public void setUp(){
        super.setUp();
        cdDefinitionSymbol = globalScope.resolveCDDefinition("vartas.monticore.cd4code.Map").orElseThrow();
        cdDefinition = cdDefinitionSymbol.getAstNode();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "                         void : clearSubmissions            : ''",
            "                   Submission : computeSubmissions          : 'ID, BiFunction<? super ID,? super Submission,? extends Submission>'",
            "                   Submission : computeIfAbsentSubmissions  : 'ID, Function<? super ID,? extends Submission>'",
            "                   Submission : computeIfPresentSubmissions : 'ID, BiFunction<? super ID,? super Submission,? extends Submission>'",
            "                      boolean : containsKeySubmissions      : 'Object'",
            "                      boolean : containsValueSubmissions    : 'Object'",
            "Set<Map.Entry<ID,Submission>> : entrySetSubmissions         : ''",
            "                      boolean : equalsSubmissions           : 'Object'",
            "                         void : forEachSubmissions          : 'BiConsumer<? super ID,? super Submission>'",
            "                   Submission : getSubmissions              : 'Object'",
            "                   Submission : getOrDefaultSubmissions     : 'Object, Submission'",
            "                      boolean : isEmptySubmissions          : ''",
            "                      Set<ID> : keySetSubmissions           : ''",
            "                   Submission : mergeSubmissions            : 'ID, Submission, BiFunction<? super Submission,? super Submission,? extends Submission>'",
            "                   Submission : putSubmissions              : 'ID, Submission'",
            "                         void : putAllSubmissions           : 'Map<? extends ID,? extends Submission>'",
            "                   Submission : putIfAbsentSubmissions      : 'ID, Submission'",
            "                   Submission : removeSubmissions           : 'Object'",
            "                      boolean : removeSubmissions           : 'Object, Object'",
            "                   Submission : replaceSubmissions          : 'ID, Submission'",
            "                      boolean : replaceSubmissions          : 'ID, Submission, Submission'",
            "                         void : replaceAllSubmissions       : 'BiFunction<? super ID,? super Submission,? extends Submission>'",
            "                          int : sizeSubmissions             : ''",
            "       Collection<Submission> : valuesSubmissions           : ''",


    }, delimiter = ':')
    public void testParse(String returnName, String methodName, @ConvertWith(CSV2StringArray.class) String[] parameters){
        ASTCDDefinition ast = cdDefinitionDecorator.decorate(cdDefinition);
        ASTCDType cdClass = getCDType(ast, "Subreddit");
        ASTCDMethod cdMethod = getMethod(cdClass, methodName, parameters);

        ASTMCReturnType mcReturnType = cdMethod.getMCReturnType();

        assertThat(printer.prettyprint(mcReturnType)).isEqualTo(returnName);
    }
}
