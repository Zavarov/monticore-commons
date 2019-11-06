${signature("parameter", "index", "size")}
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#assign class = parameter.getAstNode().get().getParameter().name()?lower_case?replace("_"," ")?capitalize?replace(" ","")>
<#assign name = parameter.getName()>
                ${GeneratorHelper}.resolve${class}("${name}", arguments.get(${index}), context)<#if index < (size-1)>,</#if>
