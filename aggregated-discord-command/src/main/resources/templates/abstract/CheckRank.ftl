${signature("symbol")}
<#assign Rank = "vartas.discord.bot.entities.Rank.Ranks">
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#assign Helper = getGlobalVar("Helper")>
<#list Helper.getRanks(symbol) as rank>
        if(!(${GeneratorHelper}.checkRank(shard.getCluster(), author, ${Rank}.${rank}) || ${GeneratorHelper}.checkRank(shard.getCluster(), author, ${Rank}.ROOT)))
            throw new IllegalStateException("You need to have the ${rank} rank to execute this command.");
</#list>