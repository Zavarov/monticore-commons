${signature("command", "artifact")}
<#assign Helper = getGlobalVar("Helper")>
<#assign package = Helper.getPackageName(artifact)>
<#assign symbol = command.getSymbol()>
<#assign name = symbol.getFullName()>
<#assign className = Helper.getClassName(symbol)>
<#assign parameters = Helper.getParameters(symbol)>
<#assign size = parameters?size>
        commands.put("${name}", (context, arguments) -> ${package}.MontiCoreCommandCreator.create${className}(context, arguments, shard));