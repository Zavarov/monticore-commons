${signature("jsonKey","cdAttribute","isOptional")}
<#assign source = ast.getCDParameter(0).getName()>
<#assign target = ast.getCDParameter(1).getName()>
<#assign attribute = cdAttribute.getName()>
<#if isOptional>
        ${source}.ifPresent${attribute?cap_first}(
            $value -> ${target}.put("${jsonKey}", $value)
        );
<#else>
        ${target}.put("${jsonKey}", ${source}.get${attribute?cap_first}());
</#if>