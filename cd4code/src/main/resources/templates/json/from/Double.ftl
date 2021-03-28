${signature("jsonKey","cdAttribute","isOptional")}
<#assign source = ast.getCDParameter(0).getName()>
<#assign target = ast.getCDParameter(1).getName()>
<#assign attribute = cdAttribute.getName()>
<#if isOptional>
        ${target}.set${attribute?cap_first}(<#rt>
            ${source}.isNull("${jsonKey}") ? Optional.empty() : Optional.of(${source}.getDouble("${jsonKey}"))<#t>
        );<#lt>
<#else>
        ${target}.set${attribute?cap_first}(${source}.getDouble("${jsonKey}"));
</#if>