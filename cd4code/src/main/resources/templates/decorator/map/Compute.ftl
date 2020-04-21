${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
<#assign cdBiFunction = cdMethod.getCDParameter(1)>
        return this.${cdAttribute.getName()}.compute(${cdKey.getName()}, ${cdBiFunction.getName()});