${signature("Package", "ExistsHandwrittenClass")}
<#assign ClassName = ast.getSpannedScope().getLocalClassAttributeSymbols()?first.getName()>
<#assign ParentName = "Abstract"+ClassName>
<#assign Symbol = ast.getSymbol()>
<#assign Parameters = Symbol.getSpannedScope().getLocalParameterVariableSymbols()>
package ${Package};

public <#if ExistsHandwrittenClass>abstract </#if>class ${ClassName}<#if ExistsHandwrittenClass>TOP</#if> extends ${ParentName}{
<#list Parameters as Parameter>
    ${includeArgs("command.VariableDeclaration", Parameter)}
</#list>
<#list Parameters as Parameter>
    ${includeArgs("command.SetMethod", Parameter)}
</#list>
<#if !ExistsHandwrittenClass>
    @Override
    public void run(){}
</#if>
}