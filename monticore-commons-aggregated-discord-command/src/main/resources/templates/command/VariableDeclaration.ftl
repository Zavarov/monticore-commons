${signature("parameters")}
<#assign Helper = getGlobalVar("Helper")>
<#list parameters as parameter>
    protected ${Helper.getType(parameter.getAstNode().get())} ${parameter.getName()};
</#list>