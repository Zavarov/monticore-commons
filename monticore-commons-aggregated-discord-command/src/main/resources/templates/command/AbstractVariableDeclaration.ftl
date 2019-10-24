${signature("symbol")}
<#if symbol.requiresGuild()>
    protected GuildConfiguration config;
    protected Guild guild;
    protected Member member;
    protected TextChannel channel;
<#else>
    protected MessageChannel channel;
</#if>