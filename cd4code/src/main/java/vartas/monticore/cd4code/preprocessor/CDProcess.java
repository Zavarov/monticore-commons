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

package vartas.monticore.cd4code.preprocessor;

import de.monticore.cd.cd4analysis._ast.ASTCD4AnalysisNode;
import de.monticore.cd.cd4code._visitor.CD4CodeInheritanceVisitor;
import de.monticore.generating.templateengine.GlobalExtensionManagement;

import java.util.function.Consumer;

public abstract class CDProcess implements CD4CodeInheritanceVisitor, Consumer<ASTCD4AnalysisNode> {
    protected final GlobalExtensionManagement glex;
    public CDProcess(GlobalExtensionManagement glex){
        this.glex = glex;
    }

    @Override
    public void accept(ASTCD4AnalysisNode ast) {
        ast.accept(this);
    }
}
