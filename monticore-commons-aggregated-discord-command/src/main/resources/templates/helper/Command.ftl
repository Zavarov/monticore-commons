${signature("command", "artifact")}
<#assign Helper = getGlobalVar("Helper")>
<#assign package = Helper.getPackage(artifact)>
<#assign symbol = command.getCommandSymbol()>
<#assign name = symbol.getFullName()>
<#assign className = Helper.getClassName(symbol)>
<#assign parameters = Helper.getParameters(symbol)>
<#assign size = parameters?size>
        commands.put("${name}", (context, arguments) -> {
            ${includeArgs("helper.CheckArgument", parameters)}
            ${package}.${className} command = new ${package}.${className}(
<#list parameters as parameter>
<#assign index = parameter?index>
    <#if Helper.isMany(parameter)>
        ${includeArgs("helper.ManyParameter", parameter, index)}
    <#else>
        ${includeArgs("helper.SingleParameter", parameter, index, size)}
    </#if>
</#list>
            );
            command.setSource(context);
            command.setCommunicator(communicator);
            return command;
        });