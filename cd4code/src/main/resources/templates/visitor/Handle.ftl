${signature("cdMethod", "cdSuperTypes")}
<#assign cdParameter = cdMethod.getCDParameter(0)>
    <#list cdSuperTypes?reverse as cdSuperType>
        this.visit((${cdSuperType})${cdParameter.getName()});
    </#list>
        this.visit(${cdParameter.getName()});
        this.traverse(${cdParameter.getName()});
        this.endVisit(${cdParameter.getName()});
    <#list cdSuperTypes as cdSuperType>
        this.endVisit((${cdSuperType})${cdParameter.getName()});
    </#list>