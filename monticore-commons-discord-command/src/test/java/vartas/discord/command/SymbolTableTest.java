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

package vartas.discord.command;

import com.google.common.base.Joiner;
import de.monticore.symboltable.ISymbol;
import de.se_rwth.commons.Joiners;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.rank._ast.ASTRank;
import vartas.discord.command._ast.ASTRestriction;
import vartas.discord.permission._ast.ASTPermission;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SymbolTableTest extends AbstractTest{
    String prefix = "example";
    String command = "test";
    Joiner joiner = Joiners.DOT;

    @Before
    public void setUp(){
        CommandHelper.parse(scope, "src/test/resources/Command.cmd");
    }

    @Test
    public void testResolveCommand(){
        Optional<? extends ISymbol> symbolOpt;
        ISymbol symbol;

        symbolOpt = scope.resolveCommandDown(joiner.join(prefix, command));
        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();
        assertThat(symbol.getName()).isEqualTo(command);
    }

    @Test
    public void testResolveGuildRestriction(){
        Optional<? extends ISymbol> symbolOpt;
        ISymbol symbol;

        symbolOpt = scope.resolveRestrictionNameDown(joiner.join(prefix, command, ASTRestriction.GUILD.name()));
        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();
        assertThat(symbol.getName()).isEqualTo(ASTRestriction.GUILD.name());
    }

    @Test
    public void testResolveAttachmentRestriction(){
        Optional<? extends ISymbol> symbolOpt;
        ISymbol symbol;

        symbolOpt = scope.resolveRestrictionNameDown(joiner.join(prefix, command, ASTRestriction.ATTACHMENT.name()));
        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();
        assertThat(symbol.getName()).isEqualTo(ASTRestriction.ATTACHMENT.name());
    }

    @Test
    public void testResolveClassAttribute(){
        Optional<? extends ISymbol> symbolOpt;
        ISymbol symbol;

        symbolOpt = scope.resolveClassAttributeDown(joiner.join(prefix, command, "TestCommand"));
        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();
        assertThat(symbol.getName()).isEqualTo("TestCommand");
    }

    @Test
    public void testResolveAdministratorPermission(){
        Optional<? extends ISymbol> symbolOpt;
        ISymbol symbol;

        symbolOpt = scope.resolvePermissionName(joiner.join(prefix, command, ASTPermission.ADMINISTRATOR.name()));
        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();
        assertThat(symbol.getName()).isEqualTo(ASTPermission.ADMINISTRATOR.name());
    }

    @Test
    public void testResolveManageMessagesPermission(){
        Optional<? extends ISymbol> symbolOpt;
        ISymbol symbol;

        symbolOpt = scope.resolvePermissionName(joiner.join(prefix, command, ASTPermission.MESSAGE_MANAGE.name()));
        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();
        assertThat(symbol.getName()).isEqualTo(ASTPermission.MESSAGE_MANAGE.name());
    }

    @Test
    public void testResolveGuildParameterAttribute(){
        Optional<? extends ISymbol> symbolOpt;
        ISymbol symbol;

        symbolOpt = scope.resolveParameterVariableDown(joiner.join(prefix, command, "g"));
        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();
        assertThat(symbol.getName()).isEqualTo("g");
    }

    @Test
    public void testResolveDateParameterAttribute(){
        Optional<? extends ISymbol> symbolOpt;
        ISymbol symbol;

        symbolOpt = scope.resolveParameterVariableDown(joiner.join(prefix, command, "d"));
        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();
        assertThat(symbol.getName()).isEqualTo("d");
    }

    @Test
    public void testResolveRootRankAttribute(){
        Optional<? extends ISymbol> symbolOpt;
        ISymbol symbol;

        symbolOpt = scope.resolveRankName(joiner.join(prefix, command, ASTRank.ROOT.name()));
        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();
        assertThat(symbol.getName()).isEqualTo(ASTRank.ROOT.name());
    }

    @Test
    public void testResolveDevRankAttribute(){
        Optional<? extends ISymbol> symbolOpt;
        ISymbol symbol;

        symbolOpt = scope.resolveRankName(joiner.join(prefix, command, ASTRank.DEVELOPER.name()));
        assertThat(symbolOpt).isPresent();
        symbol = symbolOpt.get();
        assertThat(symbol.getName()).isEqualTo(ASTRank.DEVELOPER.name());
    }
}
