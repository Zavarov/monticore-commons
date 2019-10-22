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

import org.junit.Before;
import org.junit.Test;
import vartas.reddit.SubmissionInterface;

import java.io.File;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class SubmissionPrettyPrinterTest {
    SubmissionInterface submission;
    @Before
    public void setUp(){
        submission = SubmissionHelper.parse("src/test/resources/submission.sub").get(0);

        File target = new File("target/test/resources/submission.sub");
        SubmissionHelper.store(Collections.singleton(submission), target);

        submission = SubmissionHelper.parse("target/test/resources/submission.sub").get(0);
    }

    @Test
    public void testGetAuthor(){
        assertThat(submission.getAuthor()).isEqualTo("author");
    }

    @Test
    public void testgetId(){
        assertThat(submission.getId()).isEqualTo("id");
    }

    @Test
    public void testGetLinkFlairText(){
        assertThat(submission.getLinkFlairText()).contains("linkFlairText");
    }

    @Test
    public void testGetSubreddit(){
        assertThat(submission.getSubreddit()).isEqualTo("subreddit");
    }

    @Test
    public void testIsNsfw(){
        assertThat(submission.isNsfw()).isTrue();
    }

    @Test
    public void testIsSpoiler(){
        assertThat(submission.isSpoiler()).isTrue();
    }

    @Test
    public void testGetScore(){
        assertThat(submission.getScore()).isEqualTo(1);
    }

    @Test
    public void testGetTitle(){
        assertThat(submission.getTitle()).isEqualTo("title");
    }

    @Test
    public void testGetCreated(){
        assertThat(submission.getCreated().getTime()).isEqualTo(1L);
    }

    @Test
    public void testGetSelfText(){
        assertThat(submission.getSelfText()).contains("selfText");
    }

    @Test
    public void testGetThumbnail(){
        assertThat(submission.getThumbnail()).contains("thumbnail");
    }

    @Test
    public void testGetUrl(){
        assertThat(submission.getUrl()).isEqualTo("url");
    }

    @Test
    public void testGetPermalink(){
        assertThat(submission.getPermalink()).isEqualTo("permalink");
    }
}
