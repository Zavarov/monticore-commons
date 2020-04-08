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

package vartas.monticore.cd2java.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import org.junit.jupiter.api.BeforeEach;
import vartas.monticore.cd2code.BasicCDTest;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2code.prettyprint.CD2CodePrettyPrinter;
import vartas.monticore.cd2java.CD2JavaGeneratorHelper;
import vartas.monticore.cd2java.template.CDDecoratorTemplate;

public abstract class AbstractDecoratorTest extends BasicCDTest {
    public CDGeneratorHelper generatorHelper;
    public CDDecoratorTemplate cdTemplate;
    public ASTCDType cdDecoratedType;

    protected CD2CodePrettyPrinter mcPrinter;

    @BeforeEach
    public void setUp(){
        generatorHelper = new CD2JavaGeneratorHelper(cdCompilationUnit, new GlobalExtensionManagement(), SOURCES_PATH);
        cdTemplate = new CDDecoratorTemplate(generatorHelper);
        cdDecoratedType = cdClass.deepClone();

        cdDecoratedType.accept(cdTemplate);
    }
}
