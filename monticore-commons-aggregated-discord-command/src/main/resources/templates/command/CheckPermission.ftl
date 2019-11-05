${signature("symbol")}
<#assign Helper = getGlobalVar("Helper")>
<#assign Permission = "net.dv8tion.jda.api.Permission">
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#if Helper.requiresGuild(symbol)>
    <#list Helper.getPermissions(symbol) as permission>
        if(!${GeneratorHelper}.checkPermission(source.getMember(), ${Permission}.${permission}))
            throw new IllegalStateException("You need to have the ${permission} permission to execute this command.");
    </#list>
</#if>