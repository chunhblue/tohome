### [Postgresql中string转换成timestamp类型](https://www.cnblogs.com/haimishasha/p/6815251.html)

 #{beginTime} 的格式要为 

```sql
TO_DATE(#{startTime}, 'yyyyMMdd') 
  AND op_date <![CDATA[>= ]]> TO_TIMESTAMP(#{beginTime}, 'YYYY-MM-DD HH24:MI:SS')
  AND op_date <![CDATA[<= ]]> TO_TIMESTAMP(#{endTime}, 'YYYY-MM-DD HH24:MI:SS') 
```

