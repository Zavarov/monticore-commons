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

package vartas.reddit.comment;

import org.junit.Before;
import org.junit.Test;
import vartas.reddit.CommentInterface;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {
    List<CommentInterface> comments;
    @Before
    public void setUp(){
        comments = CommentHelper.parse("src/test/resources/comment.com");
    }

    @Test
    public void testGetAuthor(){
        assertThat(comments.get(0).getAuthor()).isEqualTo("author");
    }

    @Test
    public void testgetId(){
        assertThat(comments.get(0).getId()).isEqualTo("id");
    }

    @Test
    public void testGetSubreddit(){
        assertThat(comments.get(0).getSubreddit()).isEqualTo("subreddit");
    }

    @Test
    public void testGetScore(){
        assertThat(comments.get(0).getScore()).isEqualTo(-1);
    }

    @Test
    public void testGetSubmission(){
        assertThat(comments.get(0).getSubmission()).isEqualTo("submission");
    }

    @Test
    public void testGetSubmissionTitle(){
        assertThat(comments.get(0).getSubmissionTitle()).isEqualTo("submissionTitle");
        assertThat(comments.get(1).getSubmissionTitle()).isEqualTo("junk");
    }

    @Test
    public void testGetCreated(){
        assertThat(comments.get(0).getCreated().toEpochMilli()).isEqualTo(2L);
    }

    @Test
    public void testEquals(){
        assertThat(comments.get(0).equals(comments.get(1))).isTrue();
        assertThat(comments.get(0).equals("junk")).isFalse();
    }

    @Test
    public void testHashCode(){
        assertThat(comments.get(0).hashCode()).isEqualTo(comments.get(1).hashCode());
    }

    @Test
    public void testGetPermalink(){
        assertThat(comments.get(0).getPermalink()).isEqualTo("https://www.reddit.com/r/subreddit/comments/submission/-/id");
    }
}
