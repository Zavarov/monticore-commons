${signature("cdParameter", "cdField", "cdArguments")}
<#assign cdArgument = cdArguments[0]>
<#if cdArgument.getValue()>
        ${cdParameter.getName()}.ifPresent${cdField.getName()?cap_first}(_element -> _element.accept(this));
</#if>