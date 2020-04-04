${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
        return this.${cdAttribute.getName()}.getIfPresent(${cdKey.getName()});