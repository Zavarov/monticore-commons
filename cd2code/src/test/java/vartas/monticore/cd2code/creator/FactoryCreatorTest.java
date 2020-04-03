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

package vartas.monticore.cd2code.creator;

import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vartas.monticore.cd2code.BasicCDTest;

import static org.assertj.core.api.Assertions.assertThat;

public class FactoryCreatorTest extends BasicCDTest {
    private ASTCDClass cdFactoryClass;
    @BeforeEach
    public void setUp(){
        super.setUp();
        parseCDClass("V","vartas","monticore", "cd2code","creator","FactoryCreator");
        cdFactoryClass = FactoryCreator.create(cdClass, GLEX);
    }

    @Test
    public void testGenerate(){
        cdGenerator.generateFactory(cdClass);
        assertThat(QUALIFIED_PATH.resolve("factory").resolve(cdFactoryClass.getName()+".java")).exists();
    }

    @Test
    public void testCreate(){
        ASTCDMethod cdMethod = getMethod(cdFactoryClass, "create", "V", "int", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("V");
    }

    @Test
    public void testCreateSupplier(){
        ASTCDMethod cdMethod = getMethod(cdFactoryClass, "create", "Supplier<V>","V", "int", "String");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("V");
    }
}
