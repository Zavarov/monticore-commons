${signature("cdMethod", "cdAttribute", "cdType")}
<#assign source = cdMethod.getCDParameter(0).getName()>
<#assign target = cdMethod.getCDParameter(1).getName()>
<#assign attribute = cdAttribute.getName()>
<#assign type = cdType.getName()>
        ${target}.put("${attribute}", JSON${type}.toJson(${source}.get${attribute?cap_first}(), new JSONObject()));