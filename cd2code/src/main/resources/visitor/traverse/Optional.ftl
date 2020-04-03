${signature("cdClass", "cdAttribute")}
<#assign cdGenHelper = getGlobalVar("cdGenHelper")>
<#assign cdClassName = cdClass.getName()>
<#assign cdVarName = cdClassName?uncap_first>
<#assign cdAttributeName = cdAttribute.getName()?cap_first>
        ${cdVarName}.${cdGenHelper.getDefaultGetter(cdAttribute)}().ifPresent(_element -> _element.accept(this));