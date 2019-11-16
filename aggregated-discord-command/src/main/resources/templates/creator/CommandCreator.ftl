${signature("artifact", "package")}
package ${package};

public final class MontiCoreCommandCreator{
<#list artifact.getCommandList() as command>
    ${includeArgs("creator.CreateMethod", command)}
</#list>
}