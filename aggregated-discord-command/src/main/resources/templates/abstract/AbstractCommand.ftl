${signature("package")}
<#assign ClassName = ast.getSpannedScope().getLocalClassAttributeSymbols()?first.getName()>
<#assign Cluster = "vartas.discord.bot.entities.Cluster">
<#assign Shard = "vartas.discord.bot.entities.Shard">
<#assign symbol = ast.getSymbol()>
<#assign Message = "net.dv8tion.jda.api.entities.Message">
<#assign Jda = "net.dv8tion.jda.api.JDA">
<#assign Guild = "net.dv8tion.jda.api.entities.Guild">
<#assign Member = "net.dv8tion.jda.api.entities.Member">
<#assign TextChannel = "net.dv8tion.jda.api.entities.TextChannel">
<#assign MessageChannel = "net.dv8tion.jda.api.entities.MessageChannel">
<#assign User = "net.dv8tion.jda.api.entities.User">
<#assign SelfUser = "net.dv8tion.jda.api.entities.SelfUser">
<#assign Command = "vartas.discord.bot.Command">
<#assign Configuration = "vartas.discord.bot.entities.Configuration">
<#assign Logger = "org.slf4j.Logger">
<#assign JDALogger = "net.dv8tion.jda.internal.utils.JDALogger">
<#assign ClusterVisitor = "vartas.discord.bot.entities.Cluster.VisitorDelegator">
<#if Helper.requiresGuild(symbol)>
    <#assign CommandVisitor = "vartas.discord.aggregated.GuildCommandVisitor">
<#else>
    <#assign CommandVisitor = "vartas.discord.aggregated.CommandVisitor">
</#if>
package ${package};

public abstract class Abstract${ClassName} extends ${ClusterVisitor} implements ${Command}, ${CommandVisitor}{
<#if Helper.requiresGuild(symbol)>
    private ${Guild} guild;
</#if>
    private ${Shard} shard;

    public Abstract${ClassName}(){
        setClusterVisitor(new ${Cluster}.ClusterVisitor());
        setShardVisitor(new ${Cluster}.ShardVisitor());
        setConfigurationVisitor(new ${Shard}.ConfigurationVisitor());
        setShardVisitor(new ${Shard}.ShardVisitor());
    }

    @Override
    public void accept(${Message} message, ${Shard} shard){
        this.shard = shard;
    <#if Helper.requiresGuild(symbol)>
        this.guild = message.getGuild();
    </#if>
        //Check permissions
    <#if Helper.requiresGuild(symbol)>
        ${includeArgs("abstract.CheckGuild")}
        ${includeArgs("abstract.CheckPermission", symbol)}
    </#if>
        ${includeArgs("abstract.CheckRank", symbol)}

        //Apply visitor
        handle(message);
        handle(shard.getCluster());
        uponCompletion();
    }

    public abstract void uponCompletion();

    //Configuration
    @Override
    public void visit(${Configuration} configuration){
        ${CommandVisitor}.super.visit(configuration);
        super.visit(configuration);
    }
    @Override
    public void traverse(${Configuration} configuration){
        ${CommandVisitor}.super.traverse(configuration);
        super.traverse(configuration);
    }
    @Override
    public void endVisit(${Configuration} configuration){
        ${CommandVisitor}.super.endVisit(configuration);
        super.endVisit(configuration);
    }
    //For Guild commands, only handle the guild-specific configuration
    @Override
    public void handle(${Configuration} configuration){
    <#if Helper.requiresGuild(symbol)>
        if(guild.getIdLong() == configuration.getGuildId()){
            ${CommandVisitor}.super.handle(configuration);
            super.handle(configuration);
        }
    </#if>
    }
    //JDA
    @Override
    public void visit(${Jda} jda){
        ${CommandVisitor}.super.visit(jda);
        super.visit(jda);
    }
    @Override
    public void traverse(${Jda} jda){
        ${CommandVisitor}.super.traverse(jda);
        super.traverse(jda);
    }
    @Override
    public void endVisit(${Jda} jda){
        ${CommandVisitor}.super.endVisit(jda);
        super.endVisit(jda);
    }
    //The uniqueness of the JDA is guaranteed by the shard
    @Override
    public void handle(${Jda} jda){
        ${CommandVisitor}.super.handle(jda);
        super.handle(jda);
    }
    //Shard
    @Override
    public void visit(${Shard} shard){
        ${CommandVisitor}.super.visit(shard);
        super.visit(shard);
    }
    @Override
    public void traverse(${Shard} shard){
        ${CommandVisitor}.super.traverse(shard);
        super.traverse(shard);
    }
    @Override
    public void endVisit(${Shard} shard){
        ${CommandVisitor}.super.endVisit(shard);
        super.endVisit(shard);
    }
    //Only handle the shard the command is in
    @Override
    public void handle(${Shard} shard){
        if(this.shard == shard){
            ${CommandVisitor}.super.handle(shard);
            super.handle(shard);
        }
    }
}