${signature("cdAttribute", "cdMethod")}
<#assign cdParameter = cdMethod.getCDParameter(0)>
        this.${cdAttribute.getName()} = ${cdParameter.getName()}.orElse(null);