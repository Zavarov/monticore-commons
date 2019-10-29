${signature("commands", "package")}
package ${package};
<#assign Communicator = getGlobalVar("Communicator")>
<#assign Helper = getGlobalVar("Helper")>
<#assign Ordinal = getGlobalVar("Ordinal")>
<#assign Message = "net.dv8tion.jda.api.entities.Message">
<#assign Command = "vartas.discord.bot.AbstractCommand">
<#assign Call = "vartas.discord.call._ast.ASTCallArtifact">
<#assign Argument = "vartas.discord.argument._ast.ASTArgument">
<#assign IOException = "java.io.IOException">
<#assign List = "java.util.List">
<#assign Map = "java.util.Map">
<#assign Optional = "java.util.Optional">
<#assign HashMap = "java.util.HashMap">
<#assign BiFunction = "java.util.function.BiFunction">
<#assign Preconditions = "com.google.common.base.Preconditions">
<#assign Logger = "org.slf4j.Logger">
<#assign JDALogger = "net.dv8tion.jda.internal.utils.JDALogger">
<#assign CallParser = "vartas.discord.call._parser.CallParser">
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#assign AbstractCommandBuilder = "vartas.discord.bot.AbstractCommandBuilder">

public class CommandBuilder extends ${AbstractCommandBuilder}{
    protected ${Logger} log = ${JDALogger}.getLog("CommandBuilder");
    protected ${Map}<String, ${BiFunction}<${Message}, ${List}<${Argument}>, ${Command}>> commands = new ${HashMap}<>();
    protected ${CallParser} parser = new ${CallParser}();

    public CommandBuilder(${Communicator} communicator){
<#list commands as ast>
    <#assign commandPackage = Helper.getPackage(ast)>
    <#list ast.getCommandList() as command>
        <#assign symbol = command.getCommandSymbol()>
        <#assign name = symbol.getFullName()>
        <#assign className = symbol.getClassName()>
        <#assign parameters = symbol.getParameters()>
        commands.put("${name}", (context, arguments) -> {
            ${includeArgs("CheckArgument", parameters)}
            ${commandPackage}.${className} command = new ${commandPackage}.${className}(
        <#list parameters as parameter>
            <#assign class = parameter.getClass().getSimpleName()?remove_ending("Parameter")?remove_beginning("AST")>
            <#assign name = parameter.getName()>
            <#assign index = parameter?index>
                ${GeneratorHelper}.resolve${class}("${name}", arguments.get(${index}), context)
                    .orElseThrow(() -> new IllegalArgumentException("The ${Ordinal.format(index+1)} argument ${name} couldn't be resolved."))<#if parameter?has_next>,</#if>
        </#list>
            );
            command.setSource(context);
            command.setCommunicator(communicator);
            return command;
        });
    </#list>
</#list>
    }

    @Override
    public ${Command} build(String content, ${Message} source) {
        ${Preconditions}.checkNotNull(content);
        ${Preconditions}.checkNotNull(source);

        ${Optional}<${Call}> artifactOpt;

        try {
            artifactOpt = parser.parse_String(content);
        }catch(${IOException} e){
            log.error(e.getMessage());
            artifactOpt = ${Optional}.empty();
        }

        ${Preconditions}.checkArgument(artifactOpt.isPresent());
        ${Preconditions}.checkArgument(!parser.hasErrors());

        ${Call} artifact = artifactOpt.get();
        String name = artifact.getQualifiedName();

        ${Preconditions}.checkArgument(commands.containsKey(name));

        return commands.get(name).apply(source, artifact.getArgumentList());
    }
}