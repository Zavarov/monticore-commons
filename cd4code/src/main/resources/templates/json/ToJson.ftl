${signature("cdType", "cdMethod", "cdAttributes")}
<#assign source = cdMethod.getCDParameter(0).getName()>
<#assign target = cdMethod.getCDParameter(1).getName()>
<#assign json = "JSON" + cdType.getName()>
        //Create new instance for serializer
        ${json} $json = new ${json}();
        //Serialize attributes
<#list cdAttributes as cdAttribute>
        $json.$to${cdAttribute.getName()?cap_first}(${source}, ${target});
</#list>
        //Return serialized  object
        return ${target};