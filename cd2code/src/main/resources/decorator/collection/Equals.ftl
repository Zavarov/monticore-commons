${signature("cdAttribute", "cdMethod")}
<#assign cdParameter = cdMethod.getCDParameter(0)>
        return this.${cdAttribute.getName()}.equals(${cdParameter.getName()});