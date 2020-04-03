${signature("cdAttribute", "cdMethod")}
<#assign cdParameter = cdMethod.getCDParameter(0)>
        com.google.common.base.Preconditions.checkNotNull(${cdParameter.getName()});
        this.${cdAttribute.getName()} = ${cdParameter.getName()};