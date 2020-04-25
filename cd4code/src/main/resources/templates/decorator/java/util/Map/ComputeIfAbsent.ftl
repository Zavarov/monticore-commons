${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
<#assign cdFunction = cdMethod.getCDParameter(1)>
        return this.${cdAttribute.getName()}.computeIfAbsent(${cdKey.getName()}, ${cdFunction.getName()});