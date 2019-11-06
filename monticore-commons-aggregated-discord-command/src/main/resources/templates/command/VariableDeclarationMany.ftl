${signature("parameter")}
<#assign Helper = getGlobalVar("Helper")>
<#assign List = "java.util.List">
    protected ${List}<${Helper.getType(parameter.getAstNode().get())}> ${parameter.getName()};