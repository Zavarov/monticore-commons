${signature("parameter")}
<#assign Helper = getGlobalVar("Helper")>
<#assign List = "java.util.List">
        ${List}<${Helper.getType(parameter.getAstNode().get())}> ${parameter.getName()}