${signature("cdAttribute", "cdMethod")}
<#assign cdParameterObject = cdMethod.getCDParameter(0)>
        return this.${cdAttribute.getName()}.lastIndexOf(${cdParameterObject.getName()});