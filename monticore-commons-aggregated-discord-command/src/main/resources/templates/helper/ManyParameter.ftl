${signature("parameter", "start")}
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#assign class = parameter.getAstNode().get().getParameter().name()?lower_case?replace("_"," ")?capitalize?replace(" ","")>
<#assign name = parameter.getName()>
<#assign Collectors = "java.util.stream.Collectors">
                arguments.subList(${start}, arguments.size())
                         .stream()
                         .map(argument -> ${GeneratorHelper}.resolve${class}("${name}", argument, context))
                         .collect(${Collectors}.toList())