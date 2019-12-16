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

package vartas.discord.bot.status.visitor;

import org.junit.Before;
import org.junit.Test;
import vartas.discord.bot.AbstractTest;
import vartas.discord.bot.entities.Status;
import vartas.discord.bot.status.StatusHelper;
import vartas.discord.bot.status._ast.ASTStatusArtifact;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class StatusVisitorTest extends AbstractTest {
    private Status status;
    private ASTStatusArtifact artifact;
    private List<String> valid = Arrays.asList("Status0","Status1","Status2","Status3","Status4");

    @Before
    public void setUp(){
        status = new Status();
        artifact = StatusHelper.parse("src/test/resources/status.stt");
        new StatusVisitor().accept(artifact, status);
    }

    @Test
    public void getTest(){
        for(int i = 0 ; i < 20 ; ++i){
            Optional<String> stringOpt = status.get();
            assertThat(stringOpt).isPresent();
            assertThat(stringOpt.get()).isIn(valid);
        }
    }
}
