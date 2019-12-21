${signature("symbol")}
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#assign Helper = getGlobalVar("Helper")>
<#assign Permission = "net.dv8tion.jda.api.Permission">
<#assign Rank = "vartas.discord.bot.entities.Rank.Ranks">
<#list Helper.getPermissions(symbol) as permission>
        if(!(${GeneratorHelper}.checkPermission(message.getMember(), ${Permission}.${permission}) || ${GeneratorHelper}.checkRank(shard.getCluster(), message.getAuthor(), ${Rank}.ROOT)))
            throw new IllegalStateException("You need to have the ${permission} permission to execute this command.");
</#list>