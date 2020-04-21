${signature("cdClass", "cdMethod")}
<#assign cdClassName = cdClass.getName()>
        return create(<#rt>
            () -> new ${cdClassName}()<#t>
<#if !cdMethod.isEmptyCDParameters()>,</#if><#t>
<#list cdMethod.getCDParameterList() as cdParameter>
            ${cdParameter.getName()}<#t>
    <#if cdParameter?has_next>,</#if><#t>
</#list>
        );<#lt>