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

import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nonnull;

public interface GuildCommandVisitor extends CommandVisitorCore{
    default void visit(@Nonnull Member author){}
    default void traverse(@Nonnull Member author){}
    default void endVisit(@Nonnull Member author){}
    default void handle(@Nonnull Member author){
        visit(author);
        traverse(author);
        endVisit(author);
    }
    default void visit(@Nonnull Guild guild){}
    default void traverse(@Nonnull Guild guild){}
    default void endVisit(@Nonnull Guild guild){}
    default void handle(@Nonnull Guild guild){
        visit(guild);
        traverse(guild);
        endVisit(guild);
    }
    default void visit(@Nonnull TextChannel textChannel){}
    default void traverse(@Nonnull TextChannel textChannel){}
    default void endVisit(@Nonnull TextChannel textChannel){}
    default void handle(@Nonnull TextChannel textChannel){
        visit(textChannel);
        traverse(textChannel);
        endVisit(textChannel);
    }

    @Override
    default void traverse(@Nonnull Message message){
        Preconditions.checkNotNull(message.getMember());
        handle(message.getJDA());
        handle(message.getJDA().getSelfUser());

        handle(message.getGuild());
        handle(message.getMember());
        handle(message.getTextChannel());
    }
}
