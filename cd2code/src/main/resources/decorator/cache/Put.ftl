${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
<#assign cdValue = cdMethod.getCDParameter(1)>
        this.${cdAttribute.getName()}.put(${cdKey.getName()}, ${cdValue.getName()});