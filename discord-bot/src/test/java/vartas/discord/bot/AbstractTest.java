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

package vartas.discord.bot;

import de.se_rwth.commons.logging.Log;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.RoleImpl;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import net.dv8tion.jda.internal.entities.UserImpl;
import net.dv8tion.jda.internal.utils.config.AuthorizationConfig;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTest {
    protected JDAImpl jda;
    protected GuildImpl guild;
    private Map<Long, TextChannelImpl> channels = new HashMap<>();
    private Map<Long, RoleImpl> roles = new HashMap<>();
    private Map<Long, User> users = new HashMap<>();

    protected UserImpl user1;
    protected UserImpl user2;

    @BeforeClass
    public static void initLog(){
        Log.initWARN();
        Log.enableFailQuick(false);
    }

    @Before
    public void initJda(){
        AuthorizationConfig config = new AuthorizationConfig(AccountType.BOT, "");
        jda = new JDAImpl(config){
            @Override
            public User getUserById(long id){
                return users.getOrDefault(id, null);
            }
            @Override
            public User getUserById(@NotNull String id){
                return(getUserById(Long.parseUnsignedLong(id)));
            }
        };

        guild = new GuildImpl(jda, 12345){
            @Override
            public TextChannel getTextChannelById(long id){
                return channels.getOrDefault(id, null);
            }
            @Override
            public TextChannel getTextChannelById(@NotNull String id){
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

        user1 = new UserImpl(1L, jda);
        user2 = new UserImpl(2L, jda);
        users.put(user1.getIdLong(), user1);
        users.put(user2.getIdLong(), user2);
    }
}