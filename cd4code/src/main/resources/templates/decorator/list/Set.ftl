${signature("cdAttribute", "cdMethod")}
<#assign cdParameterIndex = cdMethod.getCDParameter(0)>
<#assign cdParameterValue = cdMethod.getCDParameter(1)>
        return this.${cdAttribute.getName()}.set(${cdParameterIndex.getName()}, ${cdParameterValue.getName()});