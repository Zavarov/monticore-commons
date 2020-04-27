${signature("cdClass")}
<#list cdClass.getCDAttributeList() as cdAttribute>
    ${tc.include("hook.Attribute", cdAttribute)}
</#list>