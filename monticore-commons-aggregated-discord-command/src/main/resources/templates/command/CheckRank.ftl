${signature("symbol")}
<#assign RankType = "vartas.discord.bot.rank.RankType">
<#list symbol.getValidRanks() as rank>
        if(!environment.rank().checkRank(source.getAuthor(), ${RankType}.${rank}))
            throw new IllegalStateException("You need to have the ${rank.getMontiCoreName()} rank to execute this command.");
</#list>