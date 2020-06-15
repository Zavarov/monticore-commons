${signature("argument")}<#t>
= com.google.common.cache.CacheBuilder<#t>
    .newBuilder()<#t>
<#if argument.isPresent()>
    .expireAfterAccess(java.time.Duration.parse("${argument.get()}"))<#t>
</#if>
    .build()<#lt>