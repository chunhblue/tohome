### 一. 游标是系统为用户开设的一个数据缓冲区，存放SQL语句的执行结果。

用户可以用SQL语句逐一从游标中获取记录，并赋值给主变量，交由python进一步处理，一组主变量一次只能存放一条记录。

仅使用主变量并不能完全满足SQL语句向应用程序输出数据的要求

#### 1.游标和游标的优点

在数据库中，游标是一个十分重要的概念。游标提供了一种从表中检索出的数据进行操作的灵活手段，就本质而言，游标实际上是一种能从包括多条数据记录的结果集中每次提取一条记录的机制。游标总是与一条SQL选择语句相关联因为游标由结果集（可以是零条，一条或由相关的选择语句检索出的多条记录）和结果集中指向特定记录的游标位置组成。当决定对结果进行处理时，必须声明一个指向该结果的游标。

**常用 方法：**

- cursor(): 创建游标对象
- close(): 关闭游标对象
- fetchone(): 得到结果集的下一行
- fetchmany([size = cursor.arraysize]):得到结果集的下几行
- fetchall():得到结果集中剩下的所有行
- excute(sql[,args]): 执行一个数据库查询或命令
- executemany(sql,args):执行多个数据库查询或命令



```python
import pymysql

# 创建连接

conn = pymysql.connect(host="10.10.1.3", port=3306, user="root", passwd="passw0rd", db="nova")

# 创建游标

cus = conn.cursor()

# 定义sql

sql = "select * from instances;"

# 执行

# cus.execute(sql)

# 取所有的结果,取结果之前，一定要先执行sql

# cus.fetchall()

# 取一个结果

# cus.fetchone()

# 取10行数据

# cus.fetchmany(size=10)

# 关闭游标

# cus.close()

# cus.executemany()

try:
    cus.execute(sql)
    result = cus.fetchone()
    print(result)
except Exception as e:
    raise e
finally:
    cus.close()
    conn.close()
```


**mysql 操作**

```python
create table 表名(
    列名 数据类型 not null
    ………………
);

create table Stdunet(
stdId int not null,
stdname varchar(100),
age int,
sex enum('M', 'F'),
score int);
```



**varchar 和 char 的区别**

```
'123'              varchar(10)
'123       '       char(10)
```



**授权一个超级用户**

```
grant all privileges on *.* to 'user1'@'%' identified by '123456';
你这个user1用户，只能对所有的库，所有的表进行增删改查等，没有对其他用户进行授权   user2就没法授权

### + with grant option 才有给别人授权的权限，为超级用户。

grant all privileges on *.* to 'user1'@'%' identified by '123456' with grant option;
```


**mysql 增删改查**

```python
select 列名 from 表名 where 条件判断

select * from sutdent where group by stdname

               a, c where a.id = c.组id

select a.id a.name, c.id from a join c on c.组id = a.id and ^^^^
select * from table_name limiet 10;
show create table_name
desc table_name

insert into table_name (id, name, age) values(1, 'ling', 18), (), (), ();

delete from table where 条件判断

truncate  只清楚数据，不删除表结构
drop    表结果都给你干掉了


update table_name set 列名=xxx, where 条件

create index 库名_表名_列名1_列名2 （列名1， 列名2）
```



————————————————
版权声明：本文为CSDN博主「夏季恋风的味道」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/u012286287/java/article/details/80261498