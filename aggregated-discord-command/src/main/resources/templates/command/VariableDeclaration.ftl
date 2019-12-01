${signature("Parameter")}
<#assign List = "java.util.List">
<#assign Helper = getGlobalVar("Helper")>
<#assign ClassName = Helper.getType(Parameter.getAstNode())>
<#assign VariableName = Parameter.getName()>
<#if Helper.isMany(Parameter)>
    protected ${List}<${ClassName}> ${VariableName};
<#else>
    protected ${ClassName} ${VariableName};
</#if>