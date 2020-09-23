${signature("cdClass", "cdAttributes")}
<#list cdAttributes as cdAttribute>
    ${tc.include("hook.Attribute", cdAttribute)}<#rt>
</#list>