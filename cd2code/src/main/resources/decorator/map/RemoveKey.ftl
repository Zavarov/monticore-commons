${signature("cdAttribute", "cdMethod")}
<#assign cdParameter = cdMethod.getCDParameter(0)>
        return this.${cdAttribute.getName()}.remove(${cdParameter.getName()});