${signature("parameters")}
<#assign helper = getGlobalVar("helper")>
        if(parameters.size() != ${parameters?size})
            throw new IllegalArgumentException("The command requires exactly ${parameters?size} ${helper.pluralOf("argument", parameters?size)}.");