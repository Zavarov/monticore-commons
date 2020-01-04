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

package vartas.discord.aggregated;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nonnull;

public interface CommandVisitor extends CommandVisitorCore{
    default void visit(@Nonnull User author){}
    default void traverse(@Nonnull User author){}
    default void endVisit(@Nonnull User author){}
    default void handle(@Nonnull User author){
        visit(author);
        traverse(author);
        endVisit(author);
    }

    default void visit(@Nonnull MessageChannel messageChannel){}
    default void traverse(@Nonnull MessageChannel messageChannel){}
    default void endVisit(@Nonnull MessageChannel messageChannel){}
    default void handle(@Nonnull MessageChannel messageChannel){
        visit(messageChannel);
        traverse(messageChannel);
        endVisit(messageChannel);
    }

    @Override
    default void traverse(@Nonnull Message message){
        handle(message.getJDA());
        handle(message.getJDA().getSelfUser());

        handle(message.getAuthor());
        handle(message.getChannel());
    }
}
