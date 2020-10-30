${signature("cdType", "cdMethod", "cdAttributes")}
<#assign target = cdMethod.getCDParameter(0).getName()>
<#assign source = cdMethod.getCDParameter(1).getName()>
<#assign json = "JSON" + cdType.getName()>
        //Create new instance for deserializer
        ${json} $json = new ${json}();
        //Deserialize attributes
<#list cdAttributes as cdAttribute>
        $json.$from${cdAttribute.getName()?cap_first}(${source}, ${target});
</#list>
        //Return deserialized  object
        return ${target};