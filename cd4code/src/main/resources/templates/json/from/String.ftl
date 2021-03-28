${signature("jsonKey","cdAttribute","isOptional")}
<#assign source = ast.getCDParameter(0).getName()>
<#assign target = ast.getCDParameter(1).getName()>
<#assign attribute = cdAttribute.getName()>
<#if isOptional>
        ${target}.set${attribute?cap_first}(${source}.optString("${jsonKey}", "").isBlank() ? Optional.empty() : Optional.of(${source}.getString("${jsonKey}")));
<#else>
        ${target}.set${attribute?cap_first}(${source}.getString("${jsonKey}"));
</#if>