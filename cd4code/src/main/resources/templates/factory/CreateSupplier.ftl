${signature("cdClass", "cdMethod")}
<#assign cdClassName = cdClass.getName()>
<#assign cdVarName = "_" + cdClassName + "Instance">
<#assign cdSupplier = cdMethod.getCDParameter(0)>
        ${cdClassName} ${cdVarName} = ${cdSupplier.getName()}.get();
<#list cdMethod.getCDParameterList() as cdParameter>
    <#-- Skip supplier -->
    <#if !cdParameter?is_first>
        ${cdVarName}.set${cdParameter.getName()?cap_first}(${cdParameter.getName()});
    </#if>
</#list>
        return ${cdVarName};