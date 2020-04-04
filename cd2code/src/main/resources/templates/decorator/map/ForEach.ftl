${signature("cdAttribute", "cdMethod")}
<#assign cdParameter = cdMethod.getCDParameter(0)>
        this.${cdAttribute.getName()}.forEach(${cdParameter.getName()});