${signature("symbol")}
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#assign Helper = getGlobalVar("Helper")>
<#assign Permission = "net.dv8tion.jda.api.Permission">
<#assign Rank = "vartas.discord.bot.entities.BotRank.Type">
<#list Helper.getPermissions(symbol) as permission>
        if(!(${GeneratorHelper}.checkPermission(member, ${Permission}.${permission}) || ${GeneratorHelper}.checkRank(environment, author, ${Rank}.ROOT)))
            throw new IllegalStateException("You need to have the ${permission} permission to execute this command.");
</#list>