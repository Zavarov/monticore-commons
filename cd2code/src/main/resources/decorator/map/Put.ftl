${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
<#assign cdValue = cdMethod.getCDParameter(1)>
        return this.${cdAttribute.getName()}.put(${cdKey.getName()}, ${cdValue.getName()});