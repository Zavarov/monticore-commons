${signature("symbol")}
<#list symbol.getValidRanks() as rank>
        if(!communicator.environment().rank().checkRank(source.getAuthor(), RankType.${rank}))
            throw new IllegalStateException("You need to have the ${rank.getMontiCoreName()} rank to execute this command.");
</#list>