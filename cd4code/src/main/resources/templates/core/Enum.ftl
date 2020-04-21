${include("hook.Package")}
<#t>
${include("hook.Import")}
<#t>
${include("core.Import")}

${include("hook.Annotation")}
${ast.printModifier()} enum ${ast.getName()}{
    ${ast.printEnumConstants()};
<#list ast.getCDMethodList() as cdMethod>
    ${tc.include("core.Method", cdMethod)}
</#list>
}