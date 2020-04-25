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

package vartas.monticore.cd4java.decorator;

import de.monticore.cd.cd4code.CD4CodePrettyPrinterDelegator;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import org.junit.jupiter.api.BeforeEach;
import vartas.monticore.cd4analysis.decorator.CDDefinitionDecorator;
import vartas.monticore.cd4java.BasicCDTest;

public class AbstractCDDecoratorTest extends BasicCDTest {
    protected CDDefinitionDecorator cdDefinitionDecorator;
    protected CD4CodePrettyPrinterDelegator printer;
    @BeforeEach
    public void setUp(){
        super.setUp();
        cdDefinitionDecorator = new CDDefinitionDecorator(new GlobalExtensionManagement());
        printer = new CD4CodePrettyPrinterDelegator();
    }
}
