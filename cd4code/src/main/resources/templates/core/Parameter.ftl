<#assign cdPrinter = getGlobalVar("cdPrinter")>
${include("hook.Annotation")} ${cdPrinter.printType(ast)} ${ast.getName()}