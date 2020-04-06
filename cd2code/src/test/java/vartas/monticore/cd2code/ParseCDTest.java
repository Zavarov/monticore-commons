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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ParseCDTest extends BasicCDTest {
    @ParameterizedTest(name = "[{index}] {0} <= {1}")
    @CsvSource({
            "Browser, vartas.monticore.cd2code.BrowserCD",
            "Person, vartas.monticore.cd2code.PersonCD",
            "Website, vartas.monticore.cd2code.InternetCD",
            "Database, vartas.monticore.cd2code.DatabaseCD",
            "Nested, vartas.monticore.cd2code.LanguageCD"
    })
    public void testParse(String className, String classDiagram){
        parseCDClass(className, classDiagram);
    }
}