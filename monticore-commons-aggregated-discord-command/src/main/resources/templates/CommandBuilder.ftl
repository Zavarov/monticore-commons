${signature("asts", "package")}
package ${package};
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
<#assign Communicator = "vartas.discord.bot.CommunicatorInterface">
<#assign CallParser = "vartas.discord.call._parser.CallParser">

public class CommandBuilder{
    protected ${Logger} log = ${JDALogger}.getLog("CommandBuilder");
    protected ${Map}<String, ${BiFunction}<${Message}, ${List}<${Argument}>, ${Command}>> commands = new ${HashMap}<>();
    protected ${CallParser} parser = new ${CallParser}();

    public CommandBuilder(${Communicator} communicator){
<#list asts as ast>
    <#assign commandPackage = helper.getPackage(ast)>
    <#list ast.getCommandList() as command>
        <#assign symbol = command.getCommandSymbol()>
        <#assign name = symbol.getFullName()>
        <#assign className = symbol.getClassName()>
        commands.put("${name}", (context, parameter) -> new ${commandPackage}.${className}(context, communicator, parameter));
    </#list>
</#list>
    }

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