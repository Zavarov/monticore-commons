${signature("cdAttribute", "cdMethod")}
<#assign cdKeys = cdMethod.getCDParameter(0)>
        this.${cdAttribute.getName()}.invalidateAll(${cdKeys.getName()});