${signature("cdParameter", "cdField", "cdArguments")}<#t>
<#assign key = cdArguments[0]>
<#assign value = cdArguments[1]>
<#if key.getValue()>
        ${cdParameter.getName()}.keys${cdField.getName()?cap_first}().forEach(_key -> _key.accept(this));
</#if>
<#if value.getValue()>
        ${cdParameter.getName()}.values${cdField.getName()?cap_first}().forEach(_value -> _value.accept(this));
</#if>