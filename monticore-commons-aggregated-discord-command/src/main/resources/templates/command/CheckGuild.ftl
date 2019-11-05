${signature("symbol")}
<#assign Helper = getGlobalVar("Helper")>
<#if Helper.requiresGuild(symbol)>
        if(source.getGuild() == null)
            throw new IllegalStateException("The message needs to be sent inside a guild");
</#if>