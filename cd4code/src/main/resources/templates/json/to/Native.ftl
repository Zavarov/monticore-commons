${signature("cdMethod", "cdAttribute")}
<#assign source = cdMethod.getCDParameter(0).getName()>
<#assign target = cdMethod.getCDParameter(1).getName()>
<#assign attribute = cdAttribute.getName()>
        ${target}.put("${attribute}", ${source}.get${attribute?cap_first}());