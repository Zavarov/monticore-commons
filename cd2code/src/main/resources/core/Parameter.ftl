<#assign cdPrinter = getGlobalVar("cdPrinter")>
<#assign mcType = ast.getMCType()>
${include("hook.Annotation")} ${cdPrinter.prettyprint(mcType)} ${ast.getName()}