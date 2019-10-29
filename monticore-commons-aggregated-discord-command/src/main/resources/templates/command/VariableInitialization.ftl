${signature("parameters", "message")}
<#assign helper = getGlobalVar("helper")>
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#list parameters as parameter>
        <#assign class = parameter.getClass().getSimpleName()?remove_ending("Parameter")?remove_beginning("AST")>
        <#assign name = parameter.getName()>
        <#assign index = parameter?index>
        this.${name} = ${GeneratorHelper}.resolve${class}("${name}", arguments.get(${index}), ${message})
            .orElseThrow(() -> new IllegalArgumentException("The ${helper.formatAsOrdinal(index+1)} argument ${name} couldn't be resolved."));
</#list>