<#assign mcPrinter = getGlobalVar("mcPrinter")>
${include("hook.Annotation")} ${mcPrinter.prettyprint(ast.getMCType())} ${ast.getName()}