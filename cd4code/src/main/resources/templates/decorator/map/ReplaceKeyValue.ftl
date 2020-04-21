${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
<#assign cdOldValue = cdMethod.getCDParameter(1)>
<#assign cdNewValue = cdMethod.getCDParameter(2)>
        return this.${cdAttribute.getName()}.replace(${cdKey.getName()}, ${cdOldValue.getName()}, ${cdNewValue.getName()});