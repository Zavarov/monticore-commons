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

package vartas.discord.command;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import vartas.discord.command._symboltable.CommandGlobalScope;

import java.io.File;
import java.io.IOException;

public class CommandHelperTest extends AbstractTest{
    CommandGlobalScope scope;
    File target;
    @Before
    public void setUp() throws IOException {
        target = new File("target/test/resources/directory/junk.com");
        FileUtils.deleteDirectory(target.getParentFile());
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(target.getParentFile());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseArtifactFileNotFound(){
        CommandHelper.parse(scope,"#");
    }
    @Test(expected=IllegalArgumentException.class)
    public void testParseArtifactInvalidFile(){
        CommandHelper.parse(scope,"src/test/resources/junk.txt");
    }

}
