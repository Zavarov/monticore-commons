${signature("cdMethod", "cdSupertypes")}
<#assign cdParameter = cdMethod.getCDParameter(0)>
        this.visit(${cdParameter.getName()});
<#list cdSupertypes as cdSupertype>
        this.walkUpFrom((${cdSupertype})${cdParameter.getName()});
</#list>