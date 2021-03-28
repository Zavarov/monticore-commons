${signature("cdParameter", "cdField", "cdArguments")}
<#assign cdArgument = cdArguments[0]>
<#if cdArgument.getValue()>
        ${cdParameter.getName()}.get${cdField.getName()?cap_first}().accept(getRealThis());
</#if>