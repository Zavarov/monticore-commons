${signature("parameter")}
<#assign Helper = getGlobalVar("Helper")>
        ${Helper.getType(parameter.getAstNode().get())} ${parameter.getName()}