${signature("cdClass", "cdAttribute")}
<#assign cdClassName = cdClass.getName()>
<#assign cdVarName = cdClassName?uncap_first>
<#assign cdAttributeName = cdAttribute.getName()?cap_first>
        ${cdVarName}.iterator${cdAttributeName}().forEachRemaining(_element -> _element.accept(this));