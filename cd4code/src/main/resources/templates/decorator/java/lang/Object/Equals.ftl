${signature("cdAttribute", "cdMethod")}
<#assign cdObject = cdMethod.getCDParameter(0)>
        return this.${cdAttribute.getName()}.equals(${cdObject.getName()});