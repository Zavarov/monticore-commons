${signature("parameters")}
<#assign Helper = getGlobalVar("Helper")>
<#list parameters as parameter>
    <#if Helper.isMany(parameter)>
        ${includeArgs("command.ConstructorVariableMany", parameter)} <#if parameter?has_next>,</#if>
    <#else>
        ${includeArgs("command.ConstructorVariableSingle", parameter)} <#if parameter?has_next>,</#if>
    </#if>
</#list>