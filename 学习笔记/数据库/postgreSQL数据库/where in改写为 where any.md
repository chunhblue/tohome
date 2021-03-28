## postgresql where in改写为 where any

通常 where in sql

```
where column in (field-1,field-2,...,field-n);
```

稍高级 where in sql

```
 where column in (select column from table where condition)
```

更高级 where in sql

```
where column = any(array(select id from table where condition))
#或
where column = any(array[field-1,field-2,...,field-n]);
```

使用any 在数据量多的情况下，效果会比前两种好的多，因为利用了数组的特性，any函数代表了搜索数组中任意匹配的元素
————————————————
版权声明：本文为CSDN博主「majinbo111」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/u011944141/article/details/91450106