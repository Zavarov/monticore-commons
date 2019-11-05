${signature("package", "existsHandwrittenClass", "className")}
<#assign Communicator = getGlobalVar("Communicator")>
<#assign Environment = getGlobalVar("Environment")>
<#assign symbol = ast.getCommandSymbol()>
<#assign parameters = symbol.getSpannedScope().getLocalParameterVariableSymbols()>
<#assign Message = "net.dv8tion.jda.api.entities.Message">
<#assign User = "net.dv8tion.jda.api.entities.User">
<#assign Argument = "vartas.discord.argument._ast.ASTArgument">
<#assign List = "java.util.List">
<#assign AbstractCommand = "vartas.discord.bot.AbstractCommand">
<#assign Logger = "org.slf4j.Logger">
<#assign JDALogger = "net.dv8tion.jda.internal.utils.JDALogger">
package ${package};

public <#if existsHandwrittenClass>abstract </#if>class ${className} extends ${AbstractCommand}{
    protected ${Logger} log = ${JDALogger}.getLog("${className?remove_beginning("Abstract")}");
    protected ${Message} source;
    protected ${Communicator} communicator;
    protected ${Environment} environment;
    protected ${User} author;
    ${includeArgs("command.AbstractVariableDeclaration", symbol)}

    public void setSource(${Message} source){
        this.source = source;
        this.author = source.getAuthor();
        ${includeArgs("command.CheckGuild", symbol)}
        ${includeArgs("command.CheckPermission", symbol)}
        ${includeArgs("command.AbstractVariableInitialization", symbol)}
    }

    public void setCommunicator(${Communicator} communicator){
        this.communicator = communicator;
        this.environment = communicator.environment();
        ${includeArgs("command.CheckRank", symbol)}
    }

<#if !existsHandwrittenClass>
    @Override
    public void run(){}
</#if>
}