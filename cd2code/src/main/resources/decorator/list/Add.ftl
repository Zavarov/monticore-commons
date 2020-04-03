${signature("cdAttribute", "cdMethod")}
<#assign cdParameterIndex = cdMethod.getCDParameter(0)>
<#assign cdParameterValue = cdMethod.getCDParameter(1)>
        this.${cdAttribute.getName()}.add(${cdParameterIndex.getName()}, ${cdParameterValue.getName()});