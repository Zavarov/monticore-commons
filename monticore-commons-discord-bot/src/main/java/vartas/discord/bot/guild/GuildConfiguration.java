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

package vartas.discord.bot.guild;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.Files;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;
import vartas.discord.bot.guild._ast.ASTGuildArtifact;
import vartas.discord.bot.guild.prettyprinter.GuildPrettyPrinter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This class grants access to the configuration file for Discord guilds.
 */
public class GuildConfiguration {
    /**
     * The logger for any error message.
     */
    protected Logger log = JDALogger.getLog(this.getClass().getSimpleName());
    /**
     * The mutex that ensures that only a single thread is allowed to modify this configuration file.
     */
    protected Semaphore mutex = new Semaphore(1);
    /**
     * All subreddits and the channels any update will be posted in.
     */
    protected HashMultimap<String, Long> redditFeeds = HashMultimap.create();
    /**
     * All self-assignable roles that have been grouped together.
     * No two roles can be in multiple groups.
     */
    protected HashMultimap<String, Long> roleGroups = HashMultimap.create();
    /**
     * All expressions.
     */
    protected Set<String> filteredExpressions = new HashSet<>();
    /**
     * The combined pattern for all expressions.
     */
    protected Optional<Pattern> pattern = Optional.empty();
    /**
     * The custom prefix in the guild.
     */
    protected Optional<String> prefix = Optional.empty();
    /**
     * The respective file containing this configuration.
     */
    protected File reference;

    /**
     * Creates a new instance and extracts the data from the AST node.
     * The reason why we don't stick to the AST tree is because we are allowed to modify the entries of the
     * configuration file.
     * @param guild the AST instance of the configuration file.
     * @param reference the target file where any update will be written into.
     */
    public GuildConfiguration(ASTGuildArtifact guild, File reference){
        redditFeeds.putAll(guild.getSubredditFeeds());
        roleGroups.putAll(guild.getRoleGroups());
        filteredExpressions.addAll(guild.getFilter());
        prefix = guild.getPrefix();

        pattern = filteredExpressions.stream().reduce((u,v) -> u + "|" + v).map(Pattern::compile);

        this.reference = reference;
        this.update();
    }

    /**
     * Creates an empty instance of the configuration file.
     * @param reference the target file where any update will be written into.
     */
    public GuildConfiguration(File reference){
        this.reference = reference;
        this.update();
    }

    /**
     * Stores the current configuration on the disc and overwrites any previous file.
     */
    private void update(){
        try {
            mutex.acquireUninterruptibly();

            if(!reference.getParentFile().exists())
                java.nio.file.Files.createDirectories(reference.getParentFile().toPath());
            if(!reference.exists())
                java.nio.file.Files.createFile(reference.toPath());
            String content = new GuildPrettyPrinter(new IndentPrinter()).prettyprint(this);
            Files.writeToTextFile(new StringReader(content), reference);
        }catch(IOException e){
            log.error(e.getMessage());
        }finally {
            mutex.release();
        }
    }
    /**
     * A helper function that transforms the elements of a set.
     * If the input function maps an entry to null, it will be removed from the configuration file.
     * @param source the source set
     * @param function a function that transforms objects of type U into type V
     * @param <U> the type of the elements in the source set
     * @param <V> the type of the elements in the target set
     * @return a set with values of type V
     */
    private <U,V> Set<V> resolve(Collection<U> source, Function<U,V> function){
        Iterator<U>  iterator = source.iterator();
        Set<V> target = new HashSet<>();
        boolean modified = false;

        while(iterator.hasNext()){
            V next = function.apply(iterator.next());
            if(next == null) {
                modified = true;
                iterator.remove();
            }
            target.add(next);
        }
        if(modified)
            update();
        return target;
    }

    /**
     * A helper function that transforms the values of a multimap.
     * If the input function maps a value to null, it will be removed from the configuration file.
     * @param source the source multimap
     * @param function a function that transforms objects of type V into type W.
     * @param <U> the key type of both multimaps
     * @param <V> the value type of the source multimap.
     * @param <W> the value type of the target multimap.
     * @return a multimap with values of type W
     */
    private <U,V,W> Multimap<U,W> resolve(Multimap<U,V> source, Function<V,W> function){
        Multimap<U,W> target = HashMultimap.create();

        for(Map.Entry<U, Collection<V>> entry : source.asMap().entrySet())
            target.putAll(entry.getKey(), resolve(entry.getValue(), function));

        return target;
    }
    /**
     * @param subreddit a subreddit.
     * @param channel the id of a textchannel.
     * @return true if submissions from the subreddit are posted in that channel.
     */
    public boolean containsRedditFeed(String subreddit, TextChannel channel){
        return redditFeeds.containsEntry(subreddit,channel.getIdLong());
    }
    /**
     * Links a subreddit to a channel. New submissions will then be posted in there.
     * @param subreddit a subreddit.
     * @param channel a textchannel.
     */
    public void addRedditFeed(String subreddit, TextChannel channel){
        mutex.acquireUninterruptibly();
        redditFeeds.put(subreddit, channel.getIdLong());
        mutex.release();

        update();
    }
    /**
     * Removes the link between a subreddit and a textchannel.
     * @param subreddit the subreddit.
     * @param channel the channel.
     */
    public void removeRedditFeed(String subreddit, TextChannel channel) {
        mutex.acquireUninterruptibly();
        redditFeeds.remove(subreddit, channel.getIdLong());
        mutex.release();

        update();
    }
    /**
     * Returns all feeds that are active in this server.
     * @param guild the guild this file is associated with.
     * @return a map of all subreddit-textchannel pairs.
     */
    public Multimap<String, TextChannel> getRedditFeeds(Guild guild){
        return resolve(redditFeeds,guild::getTextChannelById);
    }

    /**
     * @return a raw map of all subreddit-textchannel pairs.
     */
    public Multimap<String, Long> getRedditFeeds(){
        return Multimaps.unmodifiableMultimap(redditFeeds);
    }
    /**
     * @param guild the guild this file is associated with.
     * @param subreddit the subreddit.
     * @return all channel that are linked to the specified subreddit..
     */
    public Set<TextChannel> getRedditFeed(Guild guild, String subreddit){
        return resolve(redditFeeds.get(subreddit),guild::getTextChannelById);
    }

    /**
     * @param subreddit the subreddit.
     * @return all raw channels that are linked to the specified subreddit.
     */
    public Set<Long> getRedditFeed(String subreddit){
        return Collections.unmodifiableSet(redditFeeds.get(subreddit));
    }
    /**
     * @param expression an expression.
     * @return true if the expression is in the list of filtered word.
     */
    public boolean isFiltered(String expression){
        return filteredExpressions.contains(expression);
    }
    /**
     * Adds an expression to the list of filtered words.
     * @param expression an expression.
     * @throws PatternSyntaxException if the new expression wasn't valid.
     */
    public void addToFilter(String expression) throws PatternSyntaxException {
        mutex.acquireUninterruptibly();
        filteredExpressions.add(expression);
        mutex.release();

        pattern = filteredExpressions.stream().reduce((u,v) -> u + "|" + v).map(Pattern::compile);
        update();
    }
    /**
     * Removes an expression from the list of filtered words.
     * @param expression an expression.
     */
    public void removeFromFilter(String expression){
        mutex.acquireUninterruptibly();
        filteredExpressions.remove(expression);
        mutex.release();

        pattern = filteredExpressions.stream().reduce((u,v) -> u + "|" + v).map(Pattern::compile);
        update();
    }

    /**
     * @param text an input text that is checked for filtered words.
     * @return true if at least one expression is matched in the text.
     */
    public boolean anyMatch(String text){
        return pattern.map(p -> p.matcher(text).find()).orElse(false);
    }
    /**
     * @return all filtered words.
     */
    public Set<String> getFilter(){
        return Collections.unmodifiableSet(filteredExpressions);
    }
    /**
     * @param role a role.
     * @return true if the role is in a group.
     */
    public boolean isTagged(Role role){
        return roleGroups.containsValue(role.getIdLong());
    }
    /**
     * Adds a role to a group of roles.
     * @param tag the tag of the group.
     * @param role the role.
     */
    public void tag(String tag, Role role){
        mutex.acquireUninterruptibly();
        roleGroups.put(tag, role.getIdLong());
        mutex.release();

        update();
    }
    /**
     * Removes a role from a group of roles.
     * @param role the role.
     */
    public void untag(Role role){
        mutex.acquireUninterruptibly();
        roleGroups.values().removeIf(id -> id == role.getIdLong());
        mutex.release();

        update();
    }
    /**
     * If roles are found that are not in the guild.
     * @param role a role.
     * @return the group tag of this role.
     */
    public Optional<String> getTag(Role role){
        return roleGroups
                .entries()
                .stream()
                .filter(e -> e.getValue() == role.getIdLong())
                .map(Map.Entry::getKey)
                .findAny();
    }
    /**
     * @param guild the guild this file is associated with.
     * @return a map of all role groups.
     */
    public Multimap<String, Role> getTags(Guild guild){
        return resolve(roleGroups, guild::getRoleById);
    }

    /**
     * @return the raw map of all groups.
     */
    public Multimap<String, Long> getTags(){
        return Multimaps.unmodifiableMultimap(roleGroups);
    }
    /**
     * @return the custom prefix for this server.
     */
    public Optional<String> getPrefix(){
        return prefix;
    }
    /**
     * Sets a custom prefix for the server and overwrites any older ones.
     * The bot will react to any command that starts with the default prefix and this custom prefix.
     * Using null will remove the any custom prefix.
     * @param prefix the new prefix
     */
    public void setPrefix(String prefix){
        mutex.acquireUninterruptibly();
        this.prefix = Optional.ofNullable(prefix);
        mutex.release();

        update();
    }
}