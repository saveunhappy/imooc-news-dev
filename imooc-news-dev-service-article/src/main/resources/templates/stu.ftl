<html>
<head>
    <title>Hello Freemarker</title>
</head>
<body>
<div>
        hello ${there}

    <div>

        用户名：${stu.uid}
        用户名：${stu.age}
        用户名：${stu.amount}
        用户名：${stu.username}
<#--        用户名：${stu.articleList}-->
        用户名：${stu.birthday?string('yyyy-MM-dd HH:mm:ss')}
        用户名：${stu.spouse.username}
        用户名：${stu.spouse.age}
        用户名：${stu.haveChild?string('yes', 'no')}
<#--        用户名：${stu.parents}-->
        <br>
        <#list stu.articleList as article>
            <div>${article.id}</div>
            <div>${article.title}</div>
        </#list>


        <br>
        <#list stu.parents?keys as key>
            <div>${stu.parents[key]}</div>
        </#list>

        <br>
        <#if stu.uid == '333'>
            <div>${stu.age}</div>
        </#if>
        <#if stu.age == 11>
            <div>${stu.uid}</div>
        </#if>
        <#if stu.spouse??>
            <div>有对象</div>
        </#if>
        <#if !stu.spouse??>
            <div>单身狗</div>
        </#if>
    </div>
</div>
</body>

</html>