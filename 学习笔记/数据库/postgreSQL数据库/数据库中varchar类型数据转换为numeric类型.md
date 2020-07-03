numeric有好几种选择，有整形、小数型等等。都是用c[as](http://www.baidu.com/s?wd=as&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1YYnAnsPWb3n1RknynYnyPW0ZwV5Hcvrjm3rH6sPfKWUMw85HfYnjn4nH6sgvPsT6KdThsqpZwYTjCEQLGCpyw9Uz4Bmy-bIi4WUvYETgN-TLwGUv3EnWDdPjRLrHTs)t来实现


前提：A表的[ID](http://www.baidu.com/s?wd=ID&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1YYnAnsPWb3n1RknynYnyPW0ZwV5Hcvrjm3rH6sPfKWUMw85HfYnjn4nH6sgvPsT6KdThsqpZwYTjCEQLGCpyw9Uz4Bmy-bIi4WUvYETgN-TLwGUv3EnWDdPjRLrHTs)字段是[VARCHAR](http://www.baidu.com/s?wd=VARCHAR&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1YYnAnsPWb3n1RknynYnyPW0ZwV5Hcvrjm3rH6sPfKWUMw85HfYnjn4nH6sgvPsT6KdThsqpZwYTjCEQLGCpyw9Uz4Bmy-bIi4WUvYETgN-TLwGUv3EnWDdPjRLrHTs)类型

```
1.SELECT CAST(ID AS INTEGER) FROM A
2.SELECT CAST(ID AS INT) FROM A
2.SELECT CAST(ID AS NUMERIC(18,X)) FROM A  --X指小数位，如果想保留2位小数则是2，如果不保留小数位则是0 
```

**注意：数据库表的字段是什么类型的，as后面就需要接其jdbcType对应的类型。在这里吃了亏，记录下来以提醒自己。**



也可以使用CONVERT来搞定此问题：

```mysql
`select` `server_id ``from` `cardserver ``where` `game_id = 1 ``order` `by` `CONVERT``(server_id,SIGNED) ``desc` `limit 10`
```

 https://www.cnblogs.com/shuilangyizu/p/6677187.html 