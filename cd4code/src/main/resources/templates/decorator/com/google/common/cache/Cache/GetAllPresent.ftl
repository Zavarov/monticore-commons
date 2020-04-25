${signature("cdAttribute", "cdMethod")}
<#assign cdKeys = cdMethod.getCDParameter(0)>
        return this.${cdAttribute.getName()}.getAllPresent(${cdKeys.getName()});