${signature("symbol")}
<#assign Permission = "net.dv8tion.jda.api.Permission">
<#assign PermissionUtil = "net.dv8tion.jda.internal.utils.PermissionUtil">
<#if symbol.requiresGuild()>
    <#list symbol.getRequiredPermissions() as permission>
        if(!${PermissionUtil}.checkPermission(source.getMember(), ${Permission}.${permission}))
            throw new IllegalStateException("You need to have the ${permission.getName()} permission to execute this command.");
    </#list>
</#if>