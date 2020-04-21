${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
        this.${cdAttribute.getName()}.invalidate(${cdKey.getName()});