${signature("symbol")}
<#assign Rank = "vartas.discord.bot.entities.BotRank.Type">
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#assign Helper = getGlobalVar("Helper")>
<#list Helper.getRanks(symbol) as rank>
        if(!(${GeneratorHelper}.checkRank(environment, author, ${Rank}.${rank}) || ${GeneratorHelper}.checkRank(environment, author, ${Rank}.ROOT)))
            throw new IllegalStateException("You need to have the ${rank} rank to execute this command.");
</#list>