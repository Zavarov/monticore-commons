${signature("cdClass", "cdMethod")}
<#assign genHelper = getGlobalVar("cdGenHelper")>
<#assign cdClassName = cdClass.getName()>
<#assign cdVarName = "_factory" + cdClassName>
<#assign cdSupplier = cdMethod.getCDParameter(0)>
        ${cdClassName} ${cdVarName} = ${cdSupplier.getName()}.get();
<#list cdMethod.getCDParameterList() as cdParameter>
    <#-- Skip supplier -->
    <#if !cdParameter?is_first>
        ${cdVarName}.${genHelper.getDefaultSetter(cdParameter)}(${cdParameter.getName()});
    </#if>
</#list>
        return ${cdVarName};