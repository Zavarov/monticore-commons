${signature("cdAttribute", "cdMethod")}
<#assign cdParameter = cdMethod.getCDParameter(0)>
        this.${cdAttribute.getName()}.sort(${cdParameter.getName()});