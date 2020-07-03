## Mybatis框架中jdbcType="DATE" 和 jdbcType="TIMESTAMP" 两种类型的区别

 也算不上是Mybatis的bug，只能说是特性，本来就是这么设置的，在连接oracle数据库的时候，当jdbcType="**DATE**"类型时，返回的时间只有年月日（yyyy-MM-dd）的,当jdbcType=“**TIMESTAMP**”的时候，返回的时间是年月日和时分秒（yyyy-MM-dd HH:mm:ss），

————————————————
版权声明：本文为CSDN博主「zhangfx5」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/u010526028/article/details/74315775

mapper.xml

-  CONCAT:合并多个字符串 

