${signature("command")}
<#assign Helper = getGlobalVar("Helper")>
<#assign Symbol = command.getSymbol()>
<#assign Name = Symbol.getFullName()>
<#assign ClassName = Helper.getClassName(Symbol)>
<#assign Parameters = Helper.getParameters(Symbol)>
<#assign Size = Parameters?size>
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#assign List = "java.util.List">
<#assign Collectors = "java.util.stream.Collectors">
<#assign Shard = "vartas.discord.bot.entities.Shard">
<#assign Argument = "vartas.discord.argument._ast.ASTArgument">
<#assign Message = "net.dv8tion.jda.api.entities.Message">

    public static ${ClassName} create${ClassName} (${Message} context, ${List}<${Argument}> arguments, ${Shard} shard){
        ${includeArgs("creator.CheckArgument", Parameters)}
        ${ClassName} command = new ${ClassName}();
        //Set parameters
<#list Parameters as Parameter>
    <#assign Index = Parameter?index>
    <#assign Class = Parameter.getAstNode().getParameter().name()?lower_case?replace("_"," ")?capitalize?replace(" ","")>
    <#assign Name = Parameter.getName()>
    <#if Helper.isMany(Parameter)>
        command.set${Parameter.getName()?capitalize}(context, arguments.subList(${Index}, arguments.size()));
    <#else>
        command.set${Parameter.getName()?capitalize}(context, arguments.get(${Index}));
    </#if>
</#list>
        return command;
    }