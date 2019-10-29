${signature("symbol")}
<#assign Configuration = "vartas.discord.bot.guild.GuildConfiguration">
<#assign Guild = "net.dv8tion.jda.api.entities.Guild">
<#assign Member = "net.dv8tion.jda.api.entities.Member">
<#assign TextChannel = "net.dv8tion.jda.api.entities.TextChannel">
<#assign MessageChannel = "net.dv8tion.jda.api.entities.MessageChannel">
<#if symbol.requiresGuild()>
    protected ${Configuration} config;
    protected ${Guild} guild;
    protected ${Member} member;
    protected ${TextChannel} channel;
<#else>
    protected ${MessageChannel} channel;
</#if>