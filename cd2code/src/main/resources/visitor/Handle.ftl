${signature("cdMethod")}
<#assign cdParameter = cdMethod.getCDParameter(0)>

        this.visit(${cdParameter.getName()});

        this.traverse(${cdParameter.getName()});

        this.endVisit(${cdParameter.getName()});