${signature("cdAttribute", "cdMethod")}
<#if cdMethod.sizeCDParameters() == 1>
        throw new UnsupportedOperationException("Please overwrite this method and provide a loader.");
<#else>
        return this.${cdAttribute.getName()}.get(<#rt>
    <#list cdMethod.getCDParameterList() as cdParameter>
            ${cdParameter.getName()}<#t>
            <#if cdParameter?has_next>,</#if><#t>
    </#list>
        );<#lt>
</#if>