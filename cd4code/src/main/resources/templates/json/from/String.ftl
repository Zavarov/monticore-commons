${signature("jsonKey","cdAttribute")}
<#assign source = ast.getCDParameter(0).getName()>
<#assign target = ast.getCDParameter(1).getName()>
<#assign attribute = cdAttribute.getName()>
        ${target}.set${attribute?cap_first}(${source}.getString("${jsonKey}"));