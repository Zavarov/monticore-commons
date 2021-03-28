${signature("cdType", "jsonKey", "cdAttribute","isOptional")}
<#assign source = ast.getCDParameter(0).getName()>
<#assign target = ast.getCDParameter(1).getName()>
<#assign attribute = cdAttribute.getName()>
<#assign type = cdType.getName()>
<#if isOptional>
        ${source}.ifPresent${attribute?cap_first}(
            $value -> JSON${type}.toJson($value, new JSONObject())
        );
<#else>
        ${target}.put("${jsonKey}", JSON${type}.toJson(${source}.get${attribute?cap_first}(), new JSONObject()));
</#if>