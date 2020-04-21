<#assign cdPrinter = getGlobalVar("cdPrinter")>
${include("hook.Annotation")} ${ast.printModifier()} ${cdPrinter.printType(ast)} ${ast.getName()} ${include("hook.Value")};