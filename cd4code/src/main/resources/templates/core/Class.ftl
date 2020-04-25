${include("hook.Package")}
<#t>
${include("hook.Import")}

${include("hook.Annotation")}
${ast.printModifier()} class ${ast.getName()} <#t>
<#if ast.isPresentSuperclass()> extends ${ast.printSuperClass()}</#if> <#t>
<#if ast.isPresentTImplements()> implements ${ast.printInterfaces()}</#if> {
<#t>
<#list ast.getCDAttributeList() as cdAttribute>
    ${tc.include("core.Attribute", cdAttribute)}
</#list>
<#t>
<#list ast.getCDConstructorList() as cdConstructor>
    ${tc.include("core.Constructor", cdConstructor)}
</#list>
<#list ast.getCDMethodList() as cdMethod>
    ${tc.include("core.Method", cdMethod)}
</#list>
}