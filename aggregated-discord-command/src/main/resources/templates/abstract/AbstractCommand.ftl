${signature("package", "existsHandwrittenClass")}
<#assign ClassName = ast.getSpannedScope().getLocalClassAttributeSymbols()?first.getName()>
<#assign Communicator = "vartas.discord.bot.entities.DiscordCommunicator">
<#assign Environment = "vartas.discord.bot.entities.DiscordEnvironment">
<#assign symbol = ast.getCommandSymbol()>
<#assign Message = "net.dv8tion.jda.api.entities.Message">
<#assign Jda = "net.dv8tion.jda.api.JDA">
<#assign Guild = "net.dv8tion.jda.api.entities.Guild">
<#assign Member = "net.dv8tion.jda.api.entities.Member">
<#assign TextChannel = "net.dv8tion.jda.api.entities.TextChannel">
<#assign MessageChannel = "net.dv8tion.jda.api.entities.MessageChannel">
<#assign User = "net.dv8tion.jda.api.entities.User">
<#assign Command = "vartas.discord.bot.Command">
<#assign Config = "vartas.discord.bot.entities.BotGuild">
<#assign Logger = "org.slf4j.Logger">
<#assign JDALogger = "net.dv8tion.jda.internal.utils.JDALogger">
<#assign Rank = "vartas.discord.bot.entities.BotRank">
package ${package};

public abstract class Abstract${ClassName} extends ${Command}{
    private ${Logger} log = ${JDALogger}.getLog(this.getClass().getSimpleName());
    private ${Rank} rank;
    private ${Message} source;
    private ${Communicator} communicator;
    private ${Environment} environment;
    private ${User} author;
    private ${User} selfuser;
    private ${Jda} jda;

<#if Helper.requiresGuild(symbol)>
    private ${Guild} guild;
    private ${Member} member;
    private ${TextChannel} channel;
    private ${Config} config;
    private ${Member} selfmember;
<#else>
    private ${MessageChannel} channel;
</#if>

<#if Helper.requiresGuild(symbol)>
    ${includeArgs("abstract.InitVariable",Guild, "guild")}
    ${includeArgs("abstract.InitVariable",Member, "member")}
    ${includeArgs("abstract.InitVariable",TextChannel, "channel")}
    ${includeArgs("abstract.InitVariable",Config, "config")}
    ${includeArgs("abstract.InitVariable",Member, "selfmember")}

    ${includeArgs("abstract.GetVariable",Guild, "guild")}
    ${includeArgs("abstract.GetVariable",Member, "member")}
    ${includeArgs("abstract.GetVariable",TextChannel, "channel")}
    ${includeArgs("abstract.GetVariable",Config, "config")}
    ${includeArgs("abstract.GetVariable",Member, "selfmember")}
<#else>
    ${includeArgs("abstract.InitVariable",MessageChannel, "channel")}
    ${includeArgs("abstract.GetVariable",MessageChannel, "channel")}
</#if>
    ${includeArgs("abstract.InitVariable",User, "author")}
    ${includeArgs("abstract.InitVariable",Message, "source")}
    ${includeArgs("abstract.InitVariable",Communicator, "communicator")}
    ${includeArgs("abstract.InitVariable",Rank, "rank")}
    ${includeArgs("abstract.InitVariable",User, "selfuser")}
    ${includeArgs("abstract.InitVariable",Jda, "jda")}
    ${includeArgs("abstract.GetVariable",Jda, "jda")}
    ${includeArgs("abstract.GetVariable",User, "selfuser")}
    ${includeArgs("abstract.GetVariable",Rank, "rank")}
    ${includeArgs("abstract.GetVariable",Communicator, "communicator")}
    ${includeArgs("abstract.GetVariable",User, "author")}
    ${includeArgs("abstract.GetVariable",Message, "source")}
    ${includeArgs("abstract.GetVariable",Environment, "environment")}

    ${includeArgs("abstract.GetVariable",Logger, "log")}

    //Should be initialized last
    protected void initEnvironment(${Environment} environment){
        this.environment = environment;
<#if Helper.requiresGuild(symbol)>
    ${includeArgs("abstract.CheckGuild")}
    ${includeArgs("abstract.CheckPermission", symbol)}
    ${includeArgs("abstract.CheckRank", symbol)}
</#if>
    }

<#if !existsHandwrittenClass>
    @Override
    public void run(){}
</#if>
}