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

package vartas.discord.entity.prettyprinter;

import de.monticore.literals.mccommonliterals._ast.ASTNatLiteral;
import de.monticore.literals.mccommonliterals._ast.MCCommonLiteralsNodeFactory;
import de.monticore.prettyprint.IndentPrinter;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.entity._ast.ASTRole;
import vartas.discord.entity._ast.ASTTextChannel;
import vartas.discord.entity._ast.ASTUser;
import vartas.discord.entity._ast.EntityNodeFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityPrettyPrinterTest {
    ASTNatLiteral id;
    EntityPrettyPrinter prettyPrinter;
    @Before
    public void setUp(){
        id = MCCommonLiteralsNodeFactory.createASTNatLiteral();
        id.setDigits("12345");

        prettyPrinter = new EntityPrettyPrinter(new IndentPrinter());
    }
    @Test
    public void testPrettyPrintUser(){
        ASTUser user = EntityNodeFactory.createASTUser();
        user.setId(id);

        assertThat(prettyPrinter.prettyprint(user)).isEqualTo("<@12345>");
    }
    @Test
    public void testPrettyPrintMemberWithNickname(){
        ASTUser member = EntityNodeFactory.createASTUser();
        member.setId(id);
        member.setSemicolon(true);

        assertThat(prettyPrinter.prettyprint(member)).isEqualTo("<@!12345>");
    }
    @Test
    public void testPrettyPrintRole(){
        ASTRole role = EntityNodeFactory.createASTRole();
        role.setId(id);

        assertThat(prettyPrinter.prettyprint(role)).isEqualTo("<@&12345>");
    }
    @Test
    public void testPrettyPrintTextChannel(){
        ASTTextChannel channel = EntityNodeFactory.createASTTextChannel();
        channel.setId(id);

        assertThat(prettyPrinter.prettyprint(channel)).isEqualTo("<#12345>");
    }

    @Test
    public void testSetRealThis(){
        assertThat(prettyPrinter.getRealThis()).isEqualTo(prettyPrinter);
        prettyPrinter.setRealThis(null);
        assertThat(prettyPrinter.getRealThis()).isNull();
    }
}
