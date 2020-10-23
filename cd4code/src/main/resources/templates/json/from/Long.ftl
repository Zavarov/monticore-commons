${signature("cdMethod","cdAttribute")}
<#assign source = cdMethod.getCDParameter(0).getName()>
<#assign target = cdMethod.getCDParameter(1).getName()>
<#assign attribute = cdAttribute.getName()>
        ${target}.set${attribute?cap_first}(${source}.getLong("${attribute}"));