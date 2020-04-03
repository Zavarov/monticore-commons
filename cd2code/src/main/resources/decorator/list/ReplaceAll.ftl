${signature("cdAttribute", "cdMethod")}
<#assign cdParameterUnaryOperator = cdMethod.getCDParameter(0)>
        this.${cdAttribute.getName()}.replaceAll(${cdParameterUnaryOperator.getName()});