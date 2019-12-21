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

package vartas.discord.aggregated;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import vartas.discord.bot.entities.Configuration;
import vartas.discord.bot.entities.Shard;

import javax.annotation.Nonnull;

public interface CommandVisitorCore {
    default void visit(@Nonnull SelfUser selfUser){}
    default void traverse(@Nonnull SelfUser selfUser){}
    default void endVisit(@Nonnull SelfUser selfUser){}
    default void handle(@Nonnull SelfUser selfUser){
        visit(selfUser);
        traverse(selfUser);
        endVisit(selfUser);
    }
    default void visit(@Nonnull Configuration configuration){}
    default void traverse(@Nonnull Configuration configuration){}
    default void endVisit(@Nonnull Configuration configuration){}
    default void handle(@Nonnull Configuration configuration){
        visit(configuration);
        traverse(configuration);
        endVisit(configuration);
    }
    default void visit(@Nonnull JDA jda){}
    default void traverse(@Nonnull JDA jda){}
    default void endVisit(@Nonnull JDA jda){}
    default void handle(@Nonnull JDA jda){
        visit(jda);
        traverse(jda);
        endVisit(jda);
    }
    default void visit(@Nonnull Shard shard){}
    default void traverse(@Nonnull Shard shard){}
    default void endVisit(@Nonnull Shard shard){}
    default void handle(@Nonnull Shard shard){
        visit(shard);
        traverse(shard);
        endVisit(shard);
    }
    default void visit(@Nonnull Message message){}
    void traverse(@Nonnull Message message);
    default void endVisit(@Nonnull Message message){}
    default void handle(@Nonnull Message message){
        visit(message);
        traverse(message);
        endVisit(message);
    }
}
