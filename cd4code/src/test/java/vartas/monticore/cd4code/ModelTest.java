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

package vartas.monticore.cd4code;

import de.se_rwth.commons.Joiners;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import vartas.monticore.BasicCDTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelTest extends BasicCDTest {
    @ParameterizedTest
    @CsvSource(value = {
            "java.io : Serializable",

            "java.lang : CharSequence",
            "java.lang : Comparable",
            "java.lang : Exception",
            "java.lang : Iterable",
            "java.lang : Object",
            "java.lang : Runnable",
            "java.lang : RuntimeException",
            "java.lang : String",
            "java.lang : Throwable",

            "java.nio.file : Path",

            "java.time : Instant",

            "java.util.stream : BaseStream",
            "java.util.stream : IntStream",
            "java.util.stream : Stream",

            "java.util : Collection",
            "java.util : List",
            "java.util : Locale",
            "java.util : Map",
            "java.util : Optional",
            "java.util : Set",

            "com.google.common.cache : Cache",
            "com.google.common.collect : Multimap"
    }, delimiter = ':')
    public void testModel(String packageName, String className){
        String qualifiedName;

        qualifiedName = Joiners.DOT.join(packageName, className);
        assertThat(globalScope.resolveCDDefinition(qualifiedName)).isPresent();

        qualifiedName = Joiners.DOT.join(packageName, className, className);
        assertThat(globalScope.resolveCDType(qualifiedName)).isPresent();
    }
}
