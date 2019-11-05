${signature("symbol")}
<#assign Helper = getGlobalVar("Helper")>
<#assign Rank = "vartas.discord.bot.rank._ast.ASTRank">
<#assign Joiner = "de.se_rwth.commons.Joiners.DOT">
<#assign GeneratorHelper = "vartas.discord.aggregated.generator.CommandGeneratorHelper">
<#list Helper.getRanks(symbol) as rank>
        if(!${GeneratorHelper}.checkRank(environment, author, ${Rank}.${rank}))
            throw new IllegalStateException("You need to have the ${rank} rank to execute this command.");
</#list>