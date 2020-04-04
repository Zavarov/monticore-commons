${include("hook.Package")}
<#t>
${include("hook.Import")}
<#t>
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;
import com.google.common.cache.*;
import com.google.common.collect.*;

${include("hook.Annotation")}
${ast.printModifier()?trim} class ${ast.getName()} <#t>
<#if ast.isPresentSuperclass()> extends ${ast.printSuperClass()}</#if> <#t>
<#if ast.isPresentTImplements()> implements ${ast.printInterfaces()}</#if> {
<#t>
<#list ast.getCDAttributeList() as cdAttribute>
    ${tc.include("core.Attribute", cdAttribute)}
</#list>
<#t>
<#list ast.getCDMethodList() as cdMethod>
    ${tc.include("core.Method", cdMethod)}
</#list>
}