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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelTest extends BasicCDTest{
    @Test
    public void testLoadIterable(){
        assertThat(globalScope.resolveCDDefinition("java.lang.Iterable")).isPresent();
        assertThat(globalScope.resolveCDType("java.lang.Iterable.Iterable")).isPresent();
    }

    @Test
    public void testLoadObject(){
        assertThat(globalScope.resolveCDDefinition("java.lang.Object")).isPresent();
        assertThat(globalScope.resolveCDType("java.lang.Object.Object")).isPresent();
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

    @Test
    public void testLoadOptional(){
        assertThat(globalScope.resolveCDDefinition("java.util.Optional")).isPresent();
        assertThat(globalScope.resolveCDType("java.util.Optional.Optional")).isPresent();
    }

    @Test
    public void testLoadMap(){
        assertThat(globalScope.resolveCDDefinition("java.util.Map")).isPresent();
        assertThat(globalScope.resolveCDType("java.util.Map.Map")).isPresent();
    }
}
