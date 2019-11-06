${signature("parameter")}
<#assign Helper = getGlobalVar("Helper")>
    protected ${Helper.getType(parameter.getAstNode().get())} ${parameter.getName()};