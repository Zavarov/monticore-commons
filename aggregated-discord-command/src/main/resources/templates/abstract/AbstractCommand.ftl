${signature("package")}
<#assign ClassName = ast.getSpannedScope().getLocalClassAttributeSymbols()?first.getName()>
<#assign Shard = "vartas.discord.bot.entities.Shard">
<#assign symbol = ast.getSymbol()>
<#assign Message = "net.dv8tion.jda.api.entities.Message">
<#assign Jda = "net.dv8tion.jda.api.JDA">
<#assign Guild = "net.dv8tion.jda.api.entities.Guild">
<#assign Member = "net.dv8tion.jda.api.entities.Member">
<#assign TextChannel = "net.dv8tion.jda.api.entities.TextChannel">
<#assign MessageChannel = "net.dv8tion.jda.api.entities.MessageChannel">
<#assign User = "net.dv8tion.jda.api.entities.User">
<#assign Command = "vartas.discord.bot.Command">
<#assign Config = "vartas.discord.bot.entities.Configuration">
<#assign Logger = "org.slf4j.Logger">
<#assign JDALogger = "net.dv8tion.jda.internal.utils.JDALogger">
package ${package};

public abstract class Abstract${ClassName} implements ${Command}{
    private ${Logger} log = ${JDALogger}.getLog(this.getClass().getSimpleName());
    private ${Message} source;
    private ${Shard} shard;
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
    ${includeArgs("abstract.InitVariable",User, "selfuser")}
    ${includeArgs("abstract.InitVariable",Jda, "jda")}
    ${includeArgs("abstract.GetVariable",Jda, "jda")}
    ${includeArgs("abstract.GetVariable",User, "selfuser")}
    ${includeArgs("abstract.GetVariable",User, "author")}
    ${includeArgs("abstract.GetVariable",Message, "source")}
    ${includeArgs("abstract.GetVariable",Shard, "shard")}

    ${includeArgs("abstract.GetVariable",Logger, "log")}

    //Should be initialized last
    protected void initShard(${Shard} shard){
        this.shard = shard;
<#if Helper.requiresGuild(symbol)>
    ${includeArgs("abstract.CheckGuild")}
    ${includeArgs("abstract.CheckPermission", symbol)}
</#if>
    ${includeArgs("abstract.CheckRank", symbol)}
    }
}