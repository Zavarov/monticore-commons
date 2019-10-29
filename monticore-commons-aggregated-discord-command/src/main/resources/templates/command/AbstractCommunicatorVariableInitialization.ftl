${signature("symbol")}
<#if symbol.requiresGuild()>
        this.config = communicator.config(source.getGuild());
</#if>