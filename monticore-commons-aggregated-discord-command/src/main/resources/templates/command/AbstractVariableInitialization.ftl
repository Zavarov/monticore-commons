${signature("symbol")}
<#assign Helper = getGlobalVar("Helper")>
<#if Helper.requiresGuild(symbol)>
        this.guild = source.getGuild();
        this.member = source.getMember();
        this.channel = source.getTextChannel();
<#else>
        this.channel = source.getChannel();
</#if>