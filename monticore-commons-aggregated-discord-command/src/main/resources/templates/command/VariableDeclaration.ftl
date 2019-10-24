${signature("parameters")}
<#list parameters as parameter>
    protected ${parameter.getSymbol().getQualifiedResolvedName()} ${parameter.getVar()};
</#list>