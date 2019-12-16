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

package vartas.discord.bot.credentials;

import vartas.discord.bot.credentials._ast.ASTCredentialsArtifact;
import vartas.discord.bot.credentials._parser.CredentialsParser;
import vartas.discord.bot.credentials.cocos.CredentialsCoCos;

import java.io.IOException;
import java.util.Optional;

public abstract class CredentialsHelper {
    public static ASTCredentialsArtifact parse(String filePath) throws IllegalArgumentException{
        ASTCredentialsArtifact ast = parseArtifact(filePath);
        checkCoCos(ast);
        return ast;
    }

    private static void checkCoCos(ASTCredentialsArtifact ast){
        CredentialsCoCos.getCheckerForAllCoCos().checkAll(ast);
    }


    private static ASTCredentialsArtifact parseArtifact(String filePath){
        try{
            CredentialsParser parser = new CredentialsParser();
            Optional<ASTCredentialsArtifact> config = parser.parse(filePath);
            if(config.isEmpty())
                throw new IllegalArgumentException("The guild configuration file couldn't be parsed");

            return config.get();
        }catch(IOException e){
            throw new IllegalArgumentException(e);
        }
    }
}
