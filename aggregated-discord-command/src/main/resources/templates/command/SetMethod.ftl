${signature("Parameter")}
<#assign Argument = "vartas.discord.argument._ast.ASTArgument">
<#assign List = "java.util.List">
<#assign Lists = "com.google.common.collect.Lists">
<#assign VariableName = Parameter.getName()>
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#assign Class = Parameter.getAstNode().getParameter().name()?lower_case?replace("_"," ")?capitalize?replace(" ","")>
<#assign Name = Parameter.getName()>
<#assign Message = "net.dv8tion.jda.api.entities.Message">
<#if Helper.isMany(Parameter)>
    protected void set${VariableName?capitalize}(${Message} context, ${List}<${Argument}> arguments){
        this.${VariableName} = ${Lists}.transform(arguments, argument -> ${GeneratorHelper}.resolve${Class}("${Name}", argument, context));
    }
<#else>
    protected void set${VariableName?capitalize}(${Message} context, ${Argument} argument){
        this.${VariableName} = ${GeneratorHelper}.resolve${Class}("${Name}", argument, context);
    }
</#if>