${signature("cdAttribute", "cdMethod")}
<#assign cdParameterIndex = cdMethod.getCDParameter(0)>
        return this.${cdAttribute.getName()}.get(${cdParameterIndex.getName()});