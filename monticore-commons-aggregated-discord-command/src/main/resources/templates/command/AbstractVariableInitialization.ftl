${signature("symbol")}
<#if symbol.requiresGuild()>
        this.config = communicator.config(source.getGuild());
        this.guild = source.getGuild();
        this.member = source.getMember();
        this.channel = source.getTextChannel();
<#else>
        this.channel = source.getChannel();
</#if>