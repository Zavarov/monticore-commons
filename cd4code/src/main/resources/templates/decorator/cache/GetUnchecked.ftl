${signature("cdAttribute", "cdMethod")}
<#assign cdKey = cdMethod.getCDParameter(0)>
        try{
            return get${cdAttribute.getName()?cap_first}(${cdKey.getName()});
        }catch(ExecutionException e){
            throw new UncheckedExecutionException(e.getCause());
        }