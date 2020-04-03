${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
        return Optional.ofNullable(this.${cdAttribute.getName()}.getIfPresent(${cdKey.getName()})).orElseThrow();
