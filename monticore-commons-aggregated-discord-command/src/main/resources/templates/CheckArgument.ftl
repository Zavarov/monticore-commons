${signature("parameters")}
<#assign English = getGlobalVar("English")>
            if(arguments.size() != ${parameters?size})
                throw new IllegalArgumentException("The command requires exactly ${parameters?size} ${English.plural("argument", parameters?size)}.");