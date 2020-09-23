${signature("cdAttribute", "cdMethod")}<#rt>
<#assign cdParameter = cdMethod.getCDParameter(0)>
        this.${cdAttribute.getName()} = ${cdParameter.getName()};