${signature("cdType", "jsonKey", "cdAttribute")}
<#assign source = ast.getCDParameter(0).getName()>
<#assign target = ast.getCDParameter(1).getName()>
<#assign attribute = cdAttribute.getName()>
<#assign type = cdType.getName()>
        ${target}.put("${jsonKey}", JSON${type}.toJson(${source}.get${attribute?cap_first}(), new JSONObject()));