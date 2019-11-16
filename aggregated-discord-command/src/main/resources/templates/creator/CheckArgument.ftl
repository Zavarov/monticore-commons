${signature("parameters")}
<#assign Helper = getGlobalVar("Helper")>
<#assign English = getGlobalVar("English")>
<#assign Size = Helper.getMinSize(parameters)>
        if(arguments.size() < ${Size})
            throw new IllegalArgumentException("This command requires at least ${Size} ${English.plural("argument",Size)}.");