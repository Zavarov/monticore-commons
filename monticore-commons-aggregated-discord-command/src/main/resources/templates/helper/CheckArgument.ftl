${signature("parameters")}
<#assign Helper = getGlobalVar("Helper")>
<#assign English = getGlobalVar("English")>
<#assign size = Helper.getMinSize(parameters)>
            if(arguments.size() < ${size})
                throw new IllegalArgumentException("This command requires at least ${size} ${English.plural("argument",size)}.");