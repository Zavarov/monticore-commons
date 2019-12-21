${signature("package")}
<#assign ClassName = ast.getSpannedScope().getLocalClassAttributeSymbols()?first.getName()>
<#assign Shard = "vartas.discord.bot.entities.Shard">
<#assign symbol = ast.getSymbol()>
<#assign Message = "net.dv8tion.jda.api.entities.Message">
<#assign Jda = "net.dv8tion.jda.api.JDA">
<#assign Guild = "net.dv8tion.jda.api.entities.Guild">
<#assign Member = "net.dv8tion.jda.api.entities.Member">
<#assign TextChannel = "net.dv8tion.jda.api.entities.TextChannel">
<#assign MessageChannel = "net.dv8tion.jda.api.entities.MessageChannel">
<#assign User = "net.dv8tion.jda.api.entities.User">
<#assign SelfUser = "net.dv8tion.jda.api.entities.SelfUser">
<#assign Command = "vartas.discord.bot.Command">
<#assign Config = "vartas.discord.bot.entities.Configuration">
<#assign Logger = "org.slf4j.Logger">
<#assign JDALogger = "net.dv8tion.jda.internal.utils.JDALogger">
<#assign CommandVisitor = "vartas.discord.aggregated.CommandVisitor">
<#assign GuildCommandVisitor = "vartas.discord.aggregated.GuildCommandVisitor">
package ${package};

public abstract class Abstract${ClassName} implements ${Command}, <#if Helper.requiresGuild(symbol)>${GuildCommandVisitor}<#else>${CommandVisitor}</#if>{
    @Override
    public void accept(${Message} message, ${Shard} shard){
        //Check permissions
    <#if Helper.requiresGuild(symbol)>
        ${includeArgs("abstract.CheckGuild")}
        ${includeArgs("abstract.CheckPermission", symbol)}
    </#if>
        ${includeArgs("abstract.CheckRank", symbol)}
        //Apply visitor
    <#if Helper.requiresGuild(symbol)>
        handle(shard.guild(message.getGuild()));
    </#if>
        handle(message);
        handle(shard);
        finalize();
    }

    public abstract void finalize();
}