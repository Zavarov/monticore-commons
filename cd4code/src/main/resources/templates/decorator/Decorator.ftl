${signature("cdAttribute", "cdMethod")}<#rt>
<#assign suffix = cdAttribute.getName()?cap_first>
<#assign method = cdMethod.getName()?keep_before_last(suffix)>
<#assign hasReturnType = !cdMethod.getMCReturnType().isPresentMCVoidType()>
        <#if hasReturnType>return </#if>this.${cdAttribute.getName()}.${method}(<#rt>
<#list cdMethod.getCDParameterList() as cdParameter>
            ${cdParameter.getName()}<#t>
            <#if cdParameter?has_next>,</#if><#t>
</#list>
        );<#lt>