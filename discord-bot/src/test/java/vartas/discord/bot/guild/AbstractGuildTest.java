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

package vartas.discord.bot.guild;

import de.monticore.io.paths.ModelPath;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.RoleImpl;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.guild._ast.ASTGuildArtifact;
import vartas.discord.bot.guild._symboltable.GuildGlobalScope;
import vartas.discord.bot.guild._symboltable.GuildLanguage;
import vartas.discord.bot.guild._symboltable.IGuildScope;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGuildTest extends AbstractTest {
    protected GuildGlobalScope globalScope;
    protected IGuildScope guildScope;
    protected ASTGuildArtifact ast;

    protected GuildImpl guild;
    protected Map<Long, TextChannelImpl> channels;
    protected Map<Long, RoleImpl> roles;

    @Before
    public void initJda(){
        channels = new HashMap<>();
        roles = new HashMap<>();
        guild = new GuildImpl(null, 12345){
            @Override
            public TextChannel getTextChannelById(long id){
                return channels.getOrDefault(id, null);
            }
            @Override
            public TextChannel getTextChannelById(@NotNull  String id){
                return(getTextChannelById(Long.parseUnsignedLong(id)));
            }
            @Override
            public Role getRoleById(long id){
                return roles.getOrDefault(id, null);
            }
            @Override
            public Role getRoleById(@NotNull String id){
                return(getRoleById(Long.parseUnsignedLong(id)));
            }
        };

        for(long id = 0 ; id < 5 ; ++id)
            channels.put(id, new TextChannelImpl(id, guild));

        for(long id = 5 ; id < 10 ; ++id)
            roles.put(id, new RoleImpl(id, guild));
    }

    @Before
    public void initScope(){
        GuildLanguage language = new GuildLanguage();
        ModelPath modelPath = new ModelPath(Paths.get("src/test/resources"));
        globalScope = new GuildGlobalScope(modelPath, language);

        ast = GuildHelper.parse(globalScope, "src/test/resources/guild.gld", null);
        guildScope = ast.getSpannedScope();
    }
}
