## rank() over()用法记录，---说明：该函数只适用于oracle

并列排序

### 一.rank over(order by 列名)

 **1、 按age升序给运动员排名** 

```
 select pid,name,age,rank() over(order by age) as rank_num from players;
```

![](D:\heads ref\tohome\学习笔记\数据库\rank() over()1.png)

### 二：rank over(partition by 列名,order by 列名)

 **1、按年龄分组，组内按分数降序排名** 

```
select name,age,score,rank() over(partition by age order by score desc) as rank_num from players;
```

![](D:\heads ref\tohome\学习笔记\数据库\rank() over()2.png)