

### sql判断一个字段的值是否为null，如果为null则将0替换成0

```sql
ma8030.order_unit_qty,

 case when ma8030.order_unit_qty is null then 0 
	else ma8030.order_unit_qty end ,
```

### sql判断一个字段的值是否为0，如果为0则将0替换成空

```sql
decode(ZWGDL,0,'',ZWGDL) as ZWGDL,

case when ZBYCDL1 = 0 then null else ZBYCDL1 end ZBYCDL1,
```



![](D:\学习笔记\数据库\简化case when.png)

如果字段类型是numeric就可以用这个（**如图**）

```
COALESCE ( expression,value1,value2……,valuen) 
COALESCE()函数的第一个参数expression为待检测的表达式，而其后的参数个数不定。
COALESCE()函数将会返回包括expression在内的所有参数中的第一个非空表达式。（0也属于非空，""也属于非空.）

如果expression不为空值则返回expression；否则判断value1是否是空值，

如果value1不为空值则返回value1；否则判断value2是否是空值，

如果value2不为空值则返回value2；……以此类推，
如果所有的表达式都为空值，则返回NULL。
```

case具有两种格式。简单case函数和case搜索函数。

```sql
--简单case函数

     case sex
     when '1' then '男'
     when '2' then '女'
     else '其他' end
```

```sql
--case搜索函数

     case when sex = '1' then '男'
     when sex = '2' then '女'
     else '其他' end
```

这两种方式，可以实现相同的功能。简单case函数的写法相对比较简洁，但是和case搜索函数相比，功能方面会有些限制，比如写判定式。 
还有一个需要注重的问题，case函数只返回第一个符合条件的值，剩下的case部分将会被自动忽略。

```sql
--比如说，下面这段sql，你永远无法得到“第二类”这个结果

     case when col_1 in ( 'a', 'b') then'第一类'
      when col_1 in ('a')    then '第二类'
      else '其他' end
```

