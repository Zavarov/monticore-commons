${signature("cdAttribute", "cdMethod")}
<#assign cdMap = cdMethod.getCDParameter(0)>
        this.${cdAttribute.getName()}.putAll(${cdMap.getName()});