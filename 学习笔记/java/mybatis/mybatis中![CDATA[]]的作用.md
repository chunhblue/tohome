## mybatis中 <![CDATA[  ]]>  的作用

在使用mybatis 时我们sql是写在xml 映射文件中，如果写的sql中有一些特殊的字符的话，在解析xml文件的时候会被转义，但我们不希望他被转义，所以我们要使用<![CDATA[ ]]>来解决。

<![CDATA[  ]]> 是什么，这是XML语法。在CDATA内部的所有内容都会被解析器忽略。

如果文本包含了很多的"<"字符 <=和"&"字符——就象程序代码一样，那么最好把他们都放到CDATA部件中。

但是有个问题那就是 <if test="">  </if>  <where>  </where>  <choose>  </choose>  <trim>  </trim> 等这些标签都不会被解析，所以我们只把有特殊字符的语句放在 <![CDATA[  ]]>  尽量缩小 <![CDATA[  ]]> 的范围。

实例如下：

```xml
<select id="allUserInfo" parameterType="java.util.HashMap" resultMap="userInfo1">
<![CDATA[
SELECT newsEdit,newsId, newstitle FROM shoppingGuide WHERE 1=1 AND newsday > #{startTime} AND newsday <= #{endTime}
]]>
<if test="etidName!=''">
AND newsEdit=#{etidName}
</if>
</select>
```


因为这里有 ">"  "<=" 特殊字符所以要使用 <![CDATA[  ]]> 来注释，但是有<if> 标签，所以把<if>等 放外面

\---------------------
作者：QH_JAVA
来源：CSDN
原文：https://blog.csdn.net/qh_java/article/details/50755655?utm_source=copy