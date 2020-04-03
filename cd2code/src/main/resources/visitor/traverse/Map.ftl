${signature("cdClass", "cdAttribute", "iterateKeys", "iterateValues")}
<#assign cdClassName = cdClass.getName()>
<#assign cdVarName = cdClassName?uncap_first>
<#assign cdAttributeName = cdAttribute.getName()?cap_first>
<#if iterateKeys>
        ${cdVarName}.keySet${cdAttributeName}().forEach(_key -> _key.accept(this));
</#if>

<#if iterateValues>
        ${cdVarName}.values${cdAttributeName}().forEach(_value -> _value.accept(this));
</#if>