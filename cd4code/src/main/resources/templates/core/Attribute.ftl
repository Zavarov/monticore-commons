<#assign mcPrinter = getGlobalVar("mcPrinter")>
${include("hook.Annotation")} ${ast.printModifier()} ${mcPrinter.prettyprint(ast.getMCType())} ${ast.getName()} ${include("hook.Value")};