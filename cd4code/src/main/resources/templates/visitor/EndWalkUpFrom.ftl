${signature("cdMethod", "cdSupertypes")}
<#assign cdParameter = cdMethod.getCDParameter(0)>
<#list cdSupertypes as cdSupertype>
        this.endWalkUpFrom((${cdSupertype})${cdParameter.getName()});
</#list>
        this.endVisit(${cdParameter.getName()});