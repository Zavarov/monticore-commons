<#assign hasModifier = ast.printModifier()?has_content>
<#assign cdPrinter = getGlobalVar("cdPrinter")>
${include("hook.Annotation")}<#t>
<#-- Method signature -->
<#if hasModifier>${ast.printModifier()} </#if>${cdPrinter.prettyprint(ast.getMCReturnType())} ${ast.getName()}<#t>
    (<#t>
        <#-- Parameters -->
        <#list ast.getCDParameterList() as cdParameter>
            ${tc.include("core.Parameter", cdParameter)}<#if cdParameter?has_next>, </#if><#t>
        </#list>
    )<#t>
<#-- Exceptions -->
${ast.printThrowsDecl()}<#t>
<#-- Method body -->
<#if ast.getModifier().isAbstract()>
    ;<#lt>
<#else>
    {<#lt>
        ${include("hook.Method")}<#lt>
    }
</#if>