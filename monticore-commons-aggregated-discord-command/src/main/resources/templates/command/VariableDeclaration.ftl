${signature("parameters")}
<#assign Helper = getGlobalVar("Helper")>
<#list parameters as parameter>
    protected ${Helper.getType(parameter)} ${parameter.getName()};
</#list>