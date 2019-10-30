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

package vartas.reddit.submission;

import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class SubmissionHelperTest {
    File target;
    @Before
    public void setUp() throws IOException {
        Log.enableFailQuick(false);
        target = new File("target/test/resources/directory/junk.sub");
        FileUtils.deleteDirectory(target.getParentFile());
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(target.getParentFile());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseArtifactFileNotFound(){
        SubmissionHelper.parse("#");
    }
    @Test(expected=IllegalArgumentException.class)
    public void testParseArtifactInvalidFile(){
        SubmissionHelper.parse("src/test/resources/comment.com");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testStoreArtifactInvalidFile() throws IOException {
        Files.createDirectories(target.getParentFile().toPath());
        Files.createFile(target.toPath());
        assertThat(target.setWritable(false)).isTrue();
        SubmissionHelper.store(Collections.emptyList(), target);
    }

    @Test
    public void testStoreArtifactCreatedFileAndDirectories(){
        SubmissionHelper.store(Collections.emptyList(), target);
        assertThat(target.exists());
    }
}
