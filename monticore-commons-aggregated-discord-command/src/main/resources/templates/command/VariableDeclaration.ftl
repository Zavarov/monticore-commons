${signature("parameters")}
<#assign helper = getGlobalVar("helper")>
<#list parameters as parameter>
    protected ${helper.getType(parameter)} ${parameter.getName()};
</#list>