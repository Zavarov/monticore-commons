${signature("cdAttribute", "cdMethod")}
<#assign cdParameterIndex = cdMethod.getCDParameter(0)>
<#assign cdParameterCollection = cdMethod.getCDParameter(1)>
        return this.${cdAttribute.getName()}.addAll(${cdParameterIndex.getName()}, ${cdParameterCollection.getName()});