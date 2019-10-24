${signature("parameters")}
<#list parameters as parameter>
        <#assign var = parameter.getVar()>
        <#assign index = parameter?index>
        <#assign symbol = parameter.getSymbol().getClass().getSimpleName()>
        this.${var} = parameters
                    .get(${index})
                    .getEnclosingScope()
                    .<${symbol}>resolve("${var}", ${symbol}.KIND)
                    .flatMap(symbol -> symbol.resolve(source))
                    //index + 1 because we start from 0
                    .orElseThrow(() -> new IllegalArgumentException("The ${helper.formatAsOrdinal(index+1)} argument couldn't be resolved."));
</#list>