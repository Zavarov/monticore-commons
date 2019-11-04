/*
 * Copyright (c) 2019 Zavarov
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

package vartas.discord.command.cocos;

import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import de.monticore.literals.mccommonliterals._cocos.MCCommonLiteralsASTStringLiteralCoCo;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.lang3.StringUtils;

public class NamesHaveNoWhitespacesCoCo implements MCCommonLiteralsASTStringLiteralCoCo {
    public static final String ERROR_MESSAGE = "The String '%s' contains spaces.";
    @Override
    public void check(ASTStringLiteral node) {
        if(StringUtils.containsWhitespace(node.getValue()))
            Log.error(String.format(ERROR_MESSAGE, node.getValue()));
    }
}
