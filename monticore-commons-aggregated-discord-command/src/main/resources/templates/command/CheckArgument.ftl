${signature("parameters")}
<#assign helper = getGlobalVar("helper")>
        if(arguments.size() != ${parameters?size})
            throw new IllegalArgumentException("The command requires exactly ${parameters?size} ${helper.pluralOf("argument", parameters?size)}.");