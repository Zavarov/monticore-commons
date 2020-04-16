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

package vartas.monticore.cd4java;

import de.monticore.cd.cd4code._symboltable.CD4CodeGlobalScope;
import de.monticore.io.paths.ModelPath;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vartas.monticore.cd4analysis._symboltable.CD4CodeLanguage;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelTest {
    ModelPath modelPath = new ModelPath(Paths.get("src","main","models","cd4java"));
    CD4CodeLanguage language;
    CD4CodeGlobalScope globalScope;

    @BeforeAll
    public static void setUpAll(){
        Log.initDEBUG();
    }

    @BeforeEach
    public void setUp(){
        language = new CD4CodeLanguage();
        globalScope = new CD4CodeGlobalScope(modelPath, language);
    }

    @Test
    public void testLoadIterable(){
        assertThat(globalScope.resolveCDDefinition("java.lang.Iterable")).isPresent();
        assertThat(globalScope.resolveCDType("java.lang.Iterable.Iterable")).isPresent();
    }

    @Test
    public void testLoadCollection(){
        assertThat(globalScope.resolveCDDefinition("java.util.Collection")).isPresent();
        assertThat(globalScope.resolveCDType("java.util.Collection.Collection")).isPresent();
    }

    @Test
    public void testLoadList(){
        assertThat(globalScope.resolveCDDefinition("java.util.List")).isPresent();
        assertThat(globalScope.resolveCDType("java.util.List.List")).isPresent();
    }

    @Test
    public void testLoadSet(){
        assertThat(globalScope.resolveCDDefinition("java.util.Set")).isPresent();
        assertThat(globalScope.resolveCDType("java.util.Set.Set")).isPresent();
    }
}
