${signature("cdParameter", "cdField", "cdArguments")}
<#assign key = cdArguments[0]>
<#assign value = cdArguments[1]>
<#if key.getValue()>
        ${cdParameter.getName()}.keys${cdField.getName()?cap_first}().forEach(_key -> _key.accept(getRealThis()));
</#if>
<#if value.getValue()>
        ${cdParameter.getName()}.values${cdField.getName()?cap_first}().forEach(_value -> _value.accept(getRealThis()));
</#if>