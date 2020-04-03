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

package vartas.monticore.cd2code.decorator;

import org.junit.jupiter.api.Test;
import vartas.monticore.cd2code.BasicCDTest;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractDecoratorTest extends BasicCDTest {
    @Test
    public void testGenerate(){
        cdGenerator.generateClass(cdClass);
        assertThat(QUALIFIED_PATH.resolve("decorator").resolve(cdClass.getName()+".java")).exists();
    }
}
