${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
<#assign cdDefaultValue = cdMethod.getCDParameter(0)>
        return this.${cdAttribute.getName()}.getOrDefault(${cdKey.getName()}, ${cdDefaultValue.getName()});