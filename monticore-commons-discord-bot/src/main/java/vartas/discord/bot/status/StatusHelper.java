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

package vartas.discord.bot.status;

import vartas.discord.bot.status._ast.ASTStatusArtifact;
import vartas.discord.bot.status._parser.StatusParser;

import java.io.IOException;
import java.util.Optional;

public abstract class StatusHelper {

    public static ASTStatusArtifact parse(String filePath){
        try{
            StatusParser parser = new StatusParser();
            Optional<ASTStatusArtifact> status = parser.parse(filePath);
            if(parser.hasErrors())
                throw new IllegalArgumentException("The parser encountered errors while parsing "+filePath);
            if(!status.isPresent())
                throw new IllegalArgumentException("The status file couldn't be parsed");

            return status.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }
}
