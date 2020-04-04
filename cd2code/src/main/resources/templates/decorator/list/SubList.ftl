${signature("cdAttribute", "cdMethod")}
<#assign cdParameterFromIndex = cdMethod.getCDParameter(0)>
<#assign cdParameterToIndex = cdMethod.getCDParameter(1)>
        return this.${cdAttribute.getName()}.subList(${cdParameterFromIndex.getName()}, ${cdParameterToIndex.getName()});