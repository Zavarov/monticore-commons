${include("hook.Package")}
<#t>
${include("hook.Import")}

${include("hook.Annotation")}
<#-- Interface signature -->
${ast.printModifier()} interface ${ast.getName()}<#t>
<#-- Associated interfaces -->
<#if ast.getInterfaceList()?has_content> extends ${ast.printInterfaces()}</#if> {
<#t>
<#list ast.getCDAttributeList() as cdAttribute>
    ${tc.include("core.Attribute", cdAttribute)}<#t>
</#list>
<#t>
<#list ast.getCDMethodList() as cdMethod>
    <#if !cdMethod.getModifier().isAbstract()>default </#if><#rt>
    ${tc.include("core.Method", cdMethod)}<#t>
</#list>
}