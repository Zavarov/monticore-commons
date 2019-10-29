${signature("parameters")}
<#list parameters as parameter>
        this.${parameter.getName()} = ${parameter.getName()};
</#list>