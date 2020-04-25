${signature("cdAttribute", "cdMethod")}
        return this.${cdAttribute.getName()}.toArray(<#rt>
<#list cdMethod.getCDParameterList() as cdParameter>
            ${cdParameter.getName()}<#t>
            <#if cdParameter?has_next>,</#if><#t>
</#list>
        );<#lt>