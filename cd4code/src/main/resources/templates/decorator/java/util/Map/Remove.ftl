${signature("cdAttribute", "cdMethod")}
        return this.${cdAttribute.getName()}.remove(<#rt>
<#list cdMethod.getCDParameterList() as cdParameter>
            ${cdParameter.getName()}<#t>
            <#if cdParameter?has_next>,</#if><#t>
</#list>
        );<#lt>