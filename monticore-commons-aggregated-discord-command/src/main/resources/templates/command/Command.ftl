${signature("package", "existsHandwrittenClass", "className", "parentName")}
<#assign symbol = ast.getCommandSymbol()>
<#assign parameters = symbol.getParameters()>
<#assign helper = getGlobalVar("helper")>
<#assign Message = "net.dv8tion.jda.api.entities.Message">
<#assign Communicator = "vartas.discord.bot.CommunicatorInterface">
<#assign Argument = "vartas.discord.argument._ast.ASTArgument">
<#assign List = "java.util.List">
package ${package};

public <#if existsHandwrittenClass>abstract </#if>class ${className} extends ${parentName}{
    ${includeArgs("command.VariableDeclaration", parameters)}

    public ${className}(
        ${Message} source,
        ${Communicator} communicator,
        ${List}<${Argument}> arguments
    )
    throws
        IllegalArgumentException,
        IllegalStateException
    {
        super(source, communicator, arguments);
        ${includeArgs("command.VariableInitialization", parameters, "source")}
    }
}