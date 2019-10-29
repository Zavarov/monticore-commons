${signature("package", "existsHandwrittenClass", "className", "parentName")}
<#assign symbol = ast.getCommandSymbol()>
<#assign parameters = symbol.getParameters()>
<#assign Communicator = getGlobalVar("Communicator")>
<#assign Helper = getGlobalVar("Helper")>
<#assign Message = "net.dv8tion.jda.api.entities.Message">
<#assign Argument = "vartas.discord.argument._ast.ASTArgument">
<#assign List = "java.util.List">
package ${package};

public <#if existsHandwrittenClass>abstract </#if>class ${className} extends ${parentName}{
    ${includeArgs("command.VariableDeclaration", parameters)}

    public ${className}(
<#list parameters as parameter>
        ${Helper.getType(parameter)} ${parameter.getName()}<#if parameter?has_next>,</#if>
</#list>
    )
    throws
        IllegalArgumentException,
        IllegalStateException
    {
        ${includeArgs("command.VariableInitialization", parameters)}
    }
}