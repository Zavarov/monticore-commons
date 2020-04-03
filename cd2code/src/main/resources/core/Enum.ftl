${include("hook.Package")}
<#t>
${include("hook.Import")}

${include("hook.Annotation")}
${ast.printModifier()} enum ${ast.getName()}{
    ${ast.printEnumConstants()}
}