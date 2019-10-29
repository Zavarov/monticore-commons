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

package vartas.discord.bot;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;
import vartas.discord.bot.config._ast.ASTConfigArtifact;
import vartas.discord.bot.rank.RankConfiguration;
import vartas.reddit.CommentInterface;
import vartas.reddit.SubmissionInterface;
import vartas.reddit.SubredditInterface;
import vartas.reddit.comment.CommentHelper;
import vartas.reddit.submission.SubmissionHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public interface EnvironmentInterface {
    /**
     * The logger for the environment.
     */
    Logger log = JDALogger.getLog(EnvironmentInterface.class.getSimpleName());
    /**
     * The formatter for dates.
     */
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * @return the configuration file.
     */
    ASTConfigArtifact config();
    /**
     * @return rank file.
     */
    RankConfiguration rank();
    /**
     * @param id the id of the guild.
     * @return the bot that this guild belongs to.
     */
    CommunicatorInterface communicator(long id);
    /**
     * Returns all JDAs that are registered in this environment.
     */
    List<JDA> jdas();
    /**
     * Attempts to shutdown all communicators.
     * @return the task that will await the shutdown of all communicators.
     */
    Runnable shutdown();
    /**
     * @param subreddit the name of the subreddit.
     * @return the subreddit instance with that name.
     */
    Optional<SubredditInterface> subreddit(String subreddit);
    /**
     * Makes the program post submissions from the subreddit in the specified channel.
     * In addition, it will also update the guild configuration file to memorize the change.
     * @param subreddit the name of the subreddit.
     * @param channel the textchannel where new submissions are posted.
     */
    void add(String subreddit, TextChannel channel);
    /**
     * Removes a channel from the set of all channels where new submissions from this subreddit are posted.
     * In addition, it will also update the guild configuration file to memorize the change.
     * @param subreddit the name of the subreddit.
     * @param channel the channel that is removed from the set.
     */
    void remove(String subreddit, TextChannel channel);
    /**
     * Uses the pushshift API to retrieve more than the past 1000 submissions.
     * @param subreddit the subreddit the submissions are from.
     * @param start the inclusively oldest submission in the interval.
     * @param end the inclusively newest submission in the interval.
     * @return the submissions in the subreddit within the given interval.
     */
    Optional<TreeSet<SubmissionInterface>> pushshift(String subreddit, Instant start, Instant end);
    /**
     * @param subreddit the subreddit the submissions are from.
     * @param start the inclusively oldest submission in the interval.
     * @param end the inclusively newest submission in the interval.
     * @return the submissions in the subreddit within the given interval.
     */
    Optional<TreeSet<SubmissionInterface>> submission(String subreddit, Instant start, Instant end);
    /**
     * @param submission the submission the comments are requested from.
     * @return the comments of the submission.
     */
    Optional<List<CommentInterface>> comment(SubmissionInterface submission);
    /**
     * @param submissions a collection of submissions the comments are requested from.
     * @return the comments of the submissions.
     */
    default Optional<List<CommentInterface>> comment(Collection<SubmissionInterface> submissions){
        List<CommentInterface> comments = new ArrayList<>();
        Optional<List<CommentInterface>> comment;
        for(SubmissionInterface submission : submissions){
            comment = comment(submission);
            if(comment.isPresent())
                comments.addAll(comment.get());
            else
                return Optional.empty();
        }
        return Optional.of(comments);
    }
    /**
     * Returns all guilds that are registered in the environment.
     * Since a guild only exists in a single shard, this means that the result will be an aggregation over all shards.
     */
    default List<Guild> guilds(){
        return jdas().stream().map(JDA::getGuilds).flatMap(Collection::stream).collect(Collectors.toList());
    }
    /**
     * @param guild a guild instance.
     * @return the bot that this guild belongs to.
     */
    default CommunicatorInterface communicator(Guild guild){
        return communicator(guild.getId());
    }
    /**
     * @param channel a text channel inside a guild.
     * @return the bot that this guild belongs to.
     */
    default CommunicatorInterface communicator(TextChannel channel){
        return communicator(channel.getGuild());
    }
    /**
     * @param role a role inside a guild.
     * @return the bot that this guild belongs to.
     */
    default CommunicatorInterface communicator(Role role){
        return communicator(role.getGuild());
    }
    /**
     * @param id the id of the guild.
     * @return the bot that this guild belongs to.
     * @throws NumberFormatException if the string can't be parsed as a long.
     */
    default CommunicatorInterface communicator(String id) throws NumberFormatException{
        return communicator(Long.parseLong(id));
    }
    /**
     * @param date the date the submissions were submitted.
     * @param subreddit the subreddit the submissions are from.
     * @return a list of all submissions from that subreddit on that specific date.
     */
    static List<SubmissionInterface> loadSubmission(Instant date, String subreddit){
        String source = String.format("pushshift/%s/%s.sub",subreddit, dateFormat.format(Date.from(date)));

        if(Files.exists(Paths.get(source)))
            return SubmissionHelper.parse(source);
        else
            return Collections.emptyList();
    }
    /**
     * @param from the start of the interval.
     * @param until the inclusive end of the interval
     * @param subreddit the subreddit the submissions are from.
     * @return a list of all submissions from that subreddit within the given interval.
     */
    static ListMultimap<Instant, SubmissionInterface> loadSubmission(Instant from, Instant until, String subreddit){
        ListMultimap<Instant, SubmissionInterface> submissions = ArrayListMultimap.create();
        Instant current = from;

        //Visit all days including the last day
        while(!current.isAfter(until)){
            submissions.putAll(current, loadSubmission(current, subreddit));
            current = current.plus(1, DAYS);
        }

        return submissions;
    }
    /**
     * @param date the date the submissions were submitted.
     * @param subreddit the subreddit the submissions are from
     * @return a list of all comments in that subreddit during the specified date.
     */
    static List<CommentInterface> loadComment(Instant date, String subreddit){
        String source = String.format("pushshift/%s/%s.com",subreddit, dateFormat.format(Date.from(date)));

        if(Files.exists(Paths.get(source)))
            return CommentHelper.parse(source);
        else
            return Collections.emptyList();
    }
    /**
     * @param from the start of the interval.
     * @param until the inclusive end of the interval
     * @param subreddit the subreddit the submissions are from.
     * @return a list of all comments from that subreddit within the given interval.
     */
    static ListMultimap<Instant, CommentInterface> loadComment(Instant from, Instant until, String subreddit){
        ListMultimap<Instant, CommentInterface> comments = ArrayListMultimap.create();
        Instant current = from;

        //Visit all days including the last day
        while(!current.isAfter(until)){
            comments.putAll(current, loadComment(current, subreddit));
            current = current.plus(1, DAYS);
        }

        return comments;
    }

    /**
     * Counts the number of days in the interval.
     * @param from the start of the interval.
     * @param until the inclusive end of the interval
     * @return the number of days it takes to get from the start to the end of the interval.
     */
    static long countDays(Instant from, Instant until){
        //In Duration.between the end is exclusive
        return Duration.between(from, until).plusDays(1).toDays();
    }

    /**
     * Stores the comments on the disk.
     * @param date the date the submissions were submitted.
     * @param subreddit the subreddit the submissions are from
     * @param comments the comments that are stored.
     * @throws IllegalArgumentException if the comment couldn't be stored in the internal file.
     */
    static void storeComment(Instant date, String subreddit, Collection<CommentInterface> comments) throws IllegalArgumentException{
        File target = new File(String.format("pushshift/%s/%s.com",subreddit, dateFormat.format(Date.from(date))));
        CommentHelper.store(comments, target);
    }

    /**
     * Stores the comments on the disk.
     * @param date the date the submissions were submitted.
     * @param subreddit the subreddit the submissions are from
     * @param submissions the submissions that are stored.
     * @throws IllegalArgumentException if the comment couldn't be stored in the internal file.
     */
    static void storeSubmission(Instant date, String subreddit, Collection<SubmissionInterface> submissions) throws IllegalArgumentException{
        File target = new File(String.format("pushshift/%s/%s.sub",subreddit, dateFormat.format(Date.from(date))));
        SubmissionHelper.store(submissions, target);
    }

    /**
     * @param subreddit the subreddit in question.
     * @param from the start of the interval.
     * @param until the inclusive end of the interval
     * @return all dates for which data is present
     */
    static List<Instant> listRequestedDates(String subreddit, Instant from, Instant until){
        List<Instant> timestamps = new ArrayList<>((int)countDays(from, until));
        File file;

        //Visit all days including the last day
        while(!from.isAfter(until)){
            file = new File(String.format("pushshift/%s/%s.sub",subreddit, dateFormat.format(Date.from(from))));
            if(file.exists())
                timestamps.add(from);
            from = from.plus(1, DAYS);
        }
        return timestamps;
    }
}
