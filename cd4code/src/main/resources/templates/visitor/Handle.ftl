${signature("cdMethod")}
<#assign cdParameter = cdMethod.getCDParameter(0)>
        this.walkUpFrom(${cdParameter.getName()});
        this.traverse(${cdParameter.getName()});
        this.endWalkUpFrom(${cdParameter.getName()});