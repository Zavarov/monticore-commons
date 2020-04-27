${signature("cdClass", "cdAttribute", "iterateKeys", "iterateValues")}
<#assign cdClassName = cdClass.getName()>
<#assign cdVarName = cdClassName?uncap_first>
<#assign cdAttributeName = cdAttribute.getName()?cap_first>
<#if iterateKeys>
        ${cdVarName}.asMap${cdAttributeName}().keySet().forEach(_key -> _key.accept(this));
</#if>

<#if iterateValues>
        ${cdVarName}.asMap${cdAttributeName}().values().forEach(_value -> _value.accept(this));
</#if>