${signature("cdClass")}
<#assign cdGenHelper = getGlobalVar("cdGenHelper")>
<#list cdClass.getCDAttributeList() as cdAttribute>
    ${tc.include("hook.Attribute", cdAttribute)}
</#list>