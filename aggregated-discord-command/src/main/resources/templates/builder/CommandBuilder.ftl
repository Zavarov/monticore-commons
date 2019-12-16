${signature("commands", "package")}
package ${package};
<#assign Shard = "vartas.discord.bot.entities.Shard">
<#assign Message = "net.dv8tion.jda.api.entities.Message">
<#assign Command = "vartas.discord.bot.Command">
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
<#assign CommandBuilder = "vartas.discord.bot.CommandBuilder">

public class MontiCoreCommandBuilder extends ${CommandBuilder}{
    protected ${Logger} log = ${JDALogger}.getLog("CommandBuilder");
    protected ${Map}<String, ${BiFunction}<${Message}, ${List}<${Argument}>, ${Command}>> commands = new ${HashMap}<>();
    protected ${CallParser} parser = new ${CallParser}();

    public MontiCoreCommandBuilder(${Shard} shard){
<#list commands as artifact>
    <#list artifact.getCommandList() as command>
        ${includeArgs("builder.Command", command, artifact)}
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

        ${Preconditions}.checkArgument(artifactOpt.isPresent(), "Failed parsing '"+content+"'");

        ${Call} artifact = artifactOpt.get();
        String name = artifact.getQualifiedName();

        ${Preconditions}.checkArgument(commands.containsKey(name), "No command corresponds to '"+name+"'");

        return commands.get(name).apply(source, artifact.getArgumentList());
    }
}