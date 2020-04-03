<#assign cdPrinter = getGlobalVar("cdPrinter")>
<#assign mcType = ast.getMCType()>
${include("hook.Annotation")}
    ${ast.printModifier()} ${cdPrinter.prettyprint(mcType)} ${ast.getName()} ${include("hook.Value")};