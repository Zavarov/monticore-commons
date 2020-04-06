<#assign cdPrinter = getGlobalVar("cdPrinter")>

${ast.printModifier()} ${ast.getName()}<#t>
    (<#t>
        <#-- Parameters -->
        <#list ast.getCDParameterList() as cdParameter>
            ${tc.include("core.Parameter", cdParameter)}<#if cdParameter?has_next>, </#if><#t>
        </#list>
    )<#t>
<#-- Exceptions -->
${ast.printThrowsDecl()}<#t>
    {<#t>
        ${include("hook.Constructor")}<#lt>
    }