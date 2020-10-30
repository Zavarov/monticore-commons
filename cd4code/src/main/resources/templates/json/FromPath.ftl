${signature("cdMethod")}
<#assign target = cdMethod.getCDParameter(0).getName()>
<#assign source = cdMethod.getCDParameter(1).getName()>
        return ${cdMethod.getName()}(${target}, java.nio.file.Files.readString(${source}));