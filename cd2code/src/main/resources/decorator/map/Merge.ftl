${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
<#assign cdValue = cdMethod.getCDParameter(1)>
<#assign cdBiFunction = cdMethod.getCDParameter(2)>
        return this.${cdAttribute.getName()}.merge(${cdKey.getName()}, ${cdValue.getName()}, ${cdBiFunction.getName()});