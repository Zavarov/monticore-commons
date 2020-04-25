${signature("cdAttribute", "cdMethod")}
        this.${cdAttribute.getName()}.ifPresentOrElse(<#rt>
<#list cdMethod.getCDParameterList() as cdParameter>
            ${cdParameter.getName()}<#t>
            <#if cdParameter?has_next>,</#if><#t>
</#list>
        );<#lt>