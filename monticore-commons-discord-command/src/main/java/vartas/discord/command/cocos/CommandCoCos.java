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

import vartas.discord.command._cocos.CommandCoCoChecker;

public abstract class CommandCoCos {

    public static CommandCoCoChecker getCheckerForAllCoCos(){
        CommandCoCoChecker checker = new CommandCoCoChecker();

        checker.addCoCo(new AtMostOneGuildCoCo());
        checker.addCoCo(new AtMostOneAttachmentCoCo());
        checker.addCoCo(new AtMostOneParameterCoCo());
        checker.addCoCo(new AtMostOnePermissionCoCo());
        checker.addCoCo(new AtMostOneRankCoCo());
        checker.addCoCo(new ClassNameStartsWithCapitalLetterCoCo());
        checker.addCoCo(new ExactlyOneClassNameCoCo());
        checker.addCoCo(new PermissionOnlyInGuildCoCo());
        checker.addCoCo(new RoleParameterRequiresGuildCoCo());
        checker.addCoCo(new TextChannelParameterRequiresGuildCoCo());
        checker.addCoCo(new MemberParameterRequiresGuildCoCo());
        checker.addCoCo(new MessageParameterRequiresGuildCoCo());
        checker.addCoCo(new CommandNameIsUniqueCoCo());
        checker.addCoCo(new ClassNameIsUniqueCoCo());
        checker.addCoCo(new NamesHaveNoWhitespacesCoCo());
        checker.addCoCo(new ManyOnlyAtLastParameterCoCo());

        return checker;
    }
}
