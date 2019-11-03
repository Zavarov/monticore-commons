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

package vartas.discord.bot.guild.visitor;

import de.se_rwth.commons.Joiners;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.guild.AbstractGuildTest;
import vartas.discord.bot.guild._ast.ASTIdentifier;
import vartas.discord.bot.guild._symboltable.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SymbolRemoverTest extends AbstractGuildTest {
    Path target = Paths.get("src/test/resources/directory/junk.gld");

    @Before
    public void setUp() throws IOException {
        Files.createDirectory(target.getParent());
        Files.createFile(target);
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(target);
        Files.deleteIfExists(target.getParent());
    }

    @Test
    public void testRemoveGuildArtifactSymbol() {
        assertThat(target).exists();

        String name = guild.getId();

        Optional<GuildArtifactSymbol> symbolOpt = globalScope.resolveGuildArtifactDown(name);

        assertThat(symbolOpt).isPresent();

        GuildArtifactSymbol symbol = symbolOpt.get();
        symbol.setReference(target);

        SymbolRemover.remove(symbol);

        assertThat(globalScope.resolveGuildArtifactDown(name)).isNotPresent();
        assertThat(target).doesNotExist();
    }

    @Test(expected=IllegalStateException.class)
    public void testRemoveGuildDeleteFileFailure(){
        String name = guild.getId();

        Optional<GuildArtifactSymbol> symbolOpt = globalScope.resolveGuildArtifactDown(name);

        assertThat(symbolOpt).isPresent();

        GuildArtifactSymbol symbol = symbolOpt.get();
        symbol.setReference(target.getParent());

        SymbolRemover.remove(symbol);
    }

    @Test
    public void removeStringEntrySymbol(){
        String name = Joiners.DOT.join(guild.getId(), ASTIdentifier.BLACKLIST.name());

        Optional<StringEntrySymbol> symbolOpt = globalScope.resolveStringEntryDown(name);

        assertThat(symbolOpt).isPresent();

        SymbolRemover.remove(symbolOpt.get());

        assertThat(globalScope.resolveStringEntryDown(name)).isNotPresent();
    }

    @Test
    public void removeStringValueSymbol(){
        String name = Joiners.DOT.join(guild.getId(), ASTIdentifier.BLACKLIST.name(), "expression");

        Optional<StringValueSymbol> symbolOpt = globalScope.resolveStringValueDown(name);

        assertThat(symbolOpt).isPresent();

        SymbolRemover.remove(symbolOpt.get());

        assertThat(globalScope.resolveStringValueDown(name)).isNotPresent();
    }

    @Test
    public void removeLongGroupValueSymbol(){
        String name = Joiners.DOT.join(guild.getId(), ASTIdentifier.ROLEGROUP.name(), "a","5");

        Optional<LongGroupValueSymbol> symbolOpt = globalScope.resolveLongGroupValueDown(name);

        assertThat(symbolOpt).isPresent();

        SymbolRemover.remove(symbolOpt.get());

        assertThat(globalScope.resolveLongGroupValueDown(name)).isNotPresent();
    }

    @Test
    public void removeLongGroupArtifactSymbol(){
        String name = Joiners.DOT.join(guild.getId(), ASTIdentifier.ROLEGROUP.name(), "a");

        Optional<LongGroupArtifactSymbol> symbolOpt = globalScope.resolveLongGroupArtifactDown(name);

        assertThat(symbolOpt).isPresent();

        SymbolRemover.remove(symbolOpt.get());

        assertThat(globalScope.resolveLongGroupArtifactDown(name)).isNotPresent();
    }

    @Test
    public void removeLongGroupEntrySymbol(){
        String name = Joiners.DOT.join(guild.getId(), ASTIdentifier.ROLEGROUP.name());

        Collection<LongGroupEntrySymbol> symbols = globalScope.resolveLongGroupEntryDownMany(name);
        int expected = symbols.size();

        do{
            symbols = globalScope.resolveLongGroupEntryDownMany(name);

            SymbolRemover.remove(symbols.iterator().next());
            --expected;

            assertThat(globalScope.resolveLongGroupEntryDownMany(name)).hasSize(expected);
        //The last symbol was removed in this operation
        }while(symbols.size() == 1);

        assertThat(globalScope.resolveLongGroupArtifactDown(name)).isNotPresent();
    }

    @Test
    public void testSetRealThis(){
        SymbolRemover visitor = new SymbolRemover();

        assertThat(visitor.getRealThis()).isEqualTo(visitor);
        visitor.setRealThis(null);
        assertThat(visitor.getRealThis()).isNull();
    }
}
