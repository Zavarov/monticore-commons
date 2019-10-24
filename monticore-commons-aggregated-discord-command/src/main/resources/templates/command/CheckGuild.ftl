${signature("symbol")}
<#if symbol.requiresGuild()>
        if(source.getGuild() == null)
            throw new IllegalStateException("The message needs to be sent inside a guild");
</#if>