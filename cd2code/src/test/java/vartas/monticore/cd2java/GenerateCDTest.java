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

package vartas.monticore.cd2java;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.monticore.cd2code.BasicCDTest;

public class GenerateCDTest extends BasicCDTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "vartas.monticore.cd2code.BrowserCD",
            "vartas.monticore.cd2code.DatabaseCD",
            "vartas.monticore.cd2code.InternetCD",
            "vartas.monticore.cd2code.PersonCD",
            "vartas.monticore.cd2java.FactoryCD",
            "vartas.monticore.cd2java.VisitorCD",
            "vartas.monticore.cd2java.CacheCD",
            "vartas.monticore.cd2java.ListCD",
            "vartas.monticore.cd2java.MapCD",
            "vartas.monticore.cd2java.OptionalCD",
            "vartas.monticore.cd2java.SetCD"
    })
    public void testGenerate(String classDiagram){
        Main.main(new String[]{
                MODEL_PATH.toString(),
                TEMPLATE_PATH.toString(),
                OUTPUT_PATH.toString(),
                classDiagram
        });
    }
}
