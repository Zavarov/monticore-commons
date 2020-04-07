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

package vartas.monticore.cd2code;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import org.junit.jupiter.api.Test;
import vartas.monticore.cd2code._parser.CD2CodeParser;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CDGeneratorHelperTest {
    private ASTCDAttribute parseAttribute(String content){
        CD2CodeParser parser = new CD2CodeParser();
        try {
            return parser.parse_StringCDAttribute(content).orElseThrow();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetMCTypeArgumentName(){
        ASTCDAttribute ast = parseAttribute("Map<Integer, String> map;");

        assertThat(CDGeneratorHelper.getMCTypeArgumentName(ast, 0)).isEqualTo("Integer");
        assertThat(CDGeneratorHelper.getMCTypeArgumentName(ast, 1)).isEqualTo("String");
    }

    @Test
    public void testGetMCTypeArgumentNameNested(){
        ASTCDAttribute ast = parseAttribute("Map<Integer, Map<Float, String>> map;");

        assertThat(CDGeneratorHelper.getMCTypeArgumentName(ast, 0)).isEqualTo("Integer");
        assertThat(CDGeneratorHelper.getMCTypeArgumentName(ast, 1)).isEqualTo("Map<Float,String>");
    }
}
