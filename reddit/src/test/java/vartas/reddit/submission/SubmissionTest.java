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

import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

public class SubmissionTest{
    List<SubmissionInterface> submissions;
    @Before
    public void setUp(){
        submissions = SubmissionHelper.parse("src/test/resources/submission.sub");
    }

    @Test
    public void testGetAuthor(){
        assertThat(submissions.get(0).getAuthor()).isEqualTo("author");
    }

    @Test
    public void testgetId(){
        assertThat(submissions.get(0).getId()).isEqualTo("id");
    }

    @Test
    public void testGetLinkFlairText(){
        assertThat(submissions.get(0).getLinkFlairText()).contains("linkFlairText");
        assertThat(submissions.get(1).getLinkFlairText()).isNotPresent();
    }

    @Test
    public void testGetSubreddit(){
        assertThat(submissions.get(0).getSubreddit()).isEqualTo("subreddit");
    }

    @Test
    public void testIsNsfw(){
        assertThat(submissions.get(0).isNsfw()).isTrue();
    }

    @Test
    public void testIsSpoiler(){
        assertThat(submissions.get(0).isSpoiler()).isTrue();
    }

    @Test
    public void testGetScore(){
        assertThat(submissions.get(0).getScore()).isEqualTo(1);
    }

    @Test
    public void testGetTitle(){
        assertThat(submissions.get(0).getTitle()).isEqualTo("title");
    }

    @Test
    public void testGetCreated(){
        assertThat(submissions.get(0).getCreated().toEpochSecond(ZoneOffset.UTC)).isEqualTo(1L);
    }

    @Test
    public void testGetSelfText(){
        assertThat(submissions.get(0).getSelfText()).contains("selfText");
        assertThat(submissions.get(1).getSelfText()).isNotPresent();
    }

    @Test
    public void testGetThumbnail(){
        assertThat(submissions.get(0).getThumbnail()).contains("thumbnail");
        assertThat(submissions.get(1).getThumbnail()).isNotPresent();
    }

    @Test
    public void testGetUrl(){
        assertThat(submissions.get(0).getUrl()).isEqualTo("url");
    }

    @Test
    public void testGetPermalink(){
        assertThat(submissions.get(0).getPermalink()).isEqualTo("permalink");
    }

    @Test
    public void testOrder(){
        List<SubmissionInterface> submissions = SubmissionHelper.parse("src/test/resources/ordered.sub");
        TreeSet<SubmissionInterface> ordered = new TreeSet<>(submissions);

        Iterator<SubmissionInterface> iterator = ordered.iterator();
        assertThat(iterator.next().getCreated().toEpochSecond(ZoneOffset.UTC)).isEqualTo(1L);
        assertThat(iterator.next().getCreated().toEpochSecond(ZoneOffset.UTC)).isEqualTo(2L);
        assertThat(iterator.next().getCreated().toEpochSecond(ZoneOffset.UTC)).isEqualTo(3L);
    }

    @Test
    public void testEquals(){
        assertThat(submissions.get(0).equals(submissions.get(1))).isTrue();
        assertThat(submissions.get(0).equals("junk")).isFalse();
    }

    @Test
    public void testHashCode(){
        assertThat(submissions.get(0).hashCode()).isEqualTo(submissions.get(1).hashCode());
    }

    @Test
    public void testGetShortLink(){
        assertThat(submissions.get(0).getShortLink()).isEqualTo("https://redd.it/id");
    }
}
