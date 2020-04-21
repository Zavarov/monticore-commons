${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
<#assign cdLoader = cdMethod.getCDParameter(1)>
        return this.${cdAttribute.getName()}.get(${cdKey.getName()}, ${cdLoader.getName()});