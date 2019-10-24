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

package vartas.discord.bot.rank;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.Files;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;
import vartas.discord.bot.rank._ast.ASTRankArtifact;
import vartas.discord.bot.rank.prettyprint.RankPrettyPrinter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.Semaphore;

/**
 * This class grants access to the internal rank file for Discord users.
 */
public class RankConfiguration {
    /**
     * The logger for any error message.
     */
    protected Logger log = JDALogger.getLog(this.getClass().getSimpleName());
    /**
     * The mutex that ensures that only a single thread is allowed to modify this rank file.
     */
    protected Semaphore mutex = new Semaphore(1);
    /**
     * The respective file containing the ranks.
     */
    protected File reference;
    /**
     * The ranks for each user.
     */
    protected Multimap<Long, RankType> ranks;

    /**
     * Creats a new instance and extracts the data from the AST node.
     * The reason why we don't stick to the AST tree is because we are allowed to modify the entries of the
     * rank file.
     * @param ast the AST instance of the rank file.
     * @param reference the target file where any update will be written into.
     */
    public RankConfiguration(ASTRankArtifact ast, File reference){
        this.reference = reference;
        this.ranks = HashMultimap.create(ast.getRanks());

        //In case there is no file,
        update();
    }

    /**
     * @param user the user instace.
     * @return true if the user has the Root rank.
     */
    public boolean hasRootRank(User user){
        return ranks.get(user.getIdLong()).contains(RankType.ROOT);
    }

    /**
     * @param user the user instace.
     * @return true if the user has the Developer rank.
     */
    public boolean hasDeveloperRank(User user){
        return ranks.get(user.getIdLong()).contains(RankType.DEVELOPER);
    }

    /**
     * @param user the user instace.
     * @return true if the user has the Reddit rank.
     */
    public boolean hasRedditRank(User user){
        return ranks.get(user.getIdLong()).contains(RankType.REDDIT);
    }

    /**
     * Adds the Root rank to a user.
     * @param user the user instace.
     */
    public void addRootRank(User user){
        mutex.acquireUninterruptibly();
        ranks.put(user.getIdLong(), RankType.ROOT);
        mutex.release();

        update();
    }


    /**
     * Adds the Developer to from a user.
     * @param user the user instace.
     */
    public void addDeveloperRank(User user){
        mutex.acquireUninterruptibly();
        ranks.put(user.getIdLong(), RankType.DEVELOPER);
        mutex.release();

        update();
    }


    /**
     * Adds the Reddit rank to a user.
     * @param user the user instace.
     */
    public void addRedditRank(User user){
        mutex.acquireUninterruptibly();
        ranks.put(user.getIdLong(), RankType.REDDIT);
        mutex.release();

        update();
    }

    /**
     * Removes the Root rank from a user.
     * @param user the user instace.
     */
    public void removeRootRank(User user){
        mutex.acquireUninterruptibly();
        ranks.remove(user.getIdLong(), RankType.ROOT);
        mutex.release();

        update();
    }

    /**
     * Removes the Developer rank from a user.
     * @param user the user instace.
     */
    public void removeDeveloperRank(User user){
        mutex.acquireUninterruptibly();
        ranks.remove(user.getIdLong(), RankType.DEVELOPER);
        mutex.release();

        update();
    }

    /**
     * Removes the Reddit rank from a user.
     * @param user the user instace.
     */
    public void removeRedditRank(User user){
        mutex.acquireUninterruptibly();
        ranks.remove(user.getIdLong(), RankType.REDDIT);
        mutex.release();

        update();
    }

    /**
     * Checks if the member has the specified rank and also deals with the root rank overwriting any other rank.
     * @param member the member in question.
     * @param rank the rank that is being checked.
     * @return true, if the member has the given rank.
     */
    public boolean checkRank(Member member, RankType rank){
        return checkRank(member.getUser(), rank);
    }

    /**
     * Checks if the user has the specified rank and also deals with the root rank overwriting any other rank.
     * @param user the user in question.
     * @param rank the rank that is being checked.
     * @return true, if the user has the given rank.
     */
    public boolean checkRank(User user, RankType rank){
        return checkRank(user.getIdLong(), rank);
    }

    /**
     * Checks if the id of an user has the specified rank and also deals with the root rank overwriting any other rank.
     * @param id the id in question.
     * @param rank the rank that is being checked.
     * @return true, if the id has the given rank.
     */
    public boolean checkRank(long id, RankType rank){
        return ranks.containsEntry(id, RankType.ROOT) || ranks.containsEntry(id, rank);
    }

    /**
     * @return the raw  map containing all ranks for each user id.
     */
    public Multimap<Long, RankType> getRanks(){
        return Multimaps.unmodifiableMultimap(ranks);
    }
    /**
     * Stores the current ranks on the disc and overwrites any previous file.
     */
    private void update(){
        try {
            mutex.acquireUninterruptibly();

            if(!reference.getParentFile().exists())
                java.nio.file.Files.createDirectories(reference.getParentFile().toPath());
            if(!reference.exists())
                java.nio.file.Files.createFile(reference.toPath());
            String content = new RankPrettyPrinter(new IndentPrinter()).prettyprint(this);
            Files.writeToTextFile(new StringReader(content), reference);
        }catch(IOException e){
            log.error(e.getMessage());
        }finally {
            mutex.release();
        }
    }
}
