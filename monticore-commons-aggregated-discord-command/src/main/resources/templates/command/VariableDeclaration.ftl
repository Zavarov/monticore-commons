${signature("parameters")}
<#assign Helper = getGlobalVar("Helper")>
<#list parameters as parameter>
    <#if Helper.isMany(parameter)>
        ${includeArgs("command.VariableDeclarationMany", parameter)}
    <#else>
        ${includeArgs("command.VariableDeclarationSingle", parameter)}
    </#if>
</#list>