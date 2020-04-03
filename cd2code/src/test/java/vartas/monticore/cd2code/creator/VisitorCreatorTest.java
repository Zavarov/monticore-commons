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

import de.monticore.cd.cd4analysis._ast.ASTCDInterface;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vartas.monticore.cd2code.decorator.AbstractDecoratorTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class VisitorCreatorTest extends AbstractDecoratorTest {
    private ASTCDInterface cdVisitorInterface;
    private Path outputPath;

    @BeforeEach
    public void setUp(){
        super.setUp();
        parseCDClass("V","vartas","monticore", "cd2code","creator","VisitorCreator");
        cdVisitorInterface = VisitorCreator.create(cdDefinition, GLEX);

        outputPath = QUALIFIED_PATH.resolve("creator").resolve("visitor").resolve(cdVisitorInterface.getName()+".java");
    }

    @AfterEach
    public void tearDown() throws IOException {
        if(Files.exists(outputPath))
            Files.delete(outputPath);
    }

    @Test
    public void testGenerate(){
        cdGenerator.generateVisitor();
        assertThat(outputPath).exists();
    }

    @Test
    public void testVisit(){
        ASTCDMethod cdMethod = getMethod(cdVisitorInterface, "visit", "V");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testEndVisit(){
        ASTCDMethod cdMethod = getMethod(cdVisitorInterface, "endVisit", "V");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testTraverse(){
        ASTCDMethod cdMethod = getMethod(cdVisitorInterface, "traverse", "V");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }

    @Test
    public void testHandle(){
        ASTCDMethod cdMethod = getMethod(cdVisitorInterface, "handle", "V");

        assertThat(mcPrinter.prettyprint(cdMethod.getMCReturnType())).isEqualTo("void");
    }
}
