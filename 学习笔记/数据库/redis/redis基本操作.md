 在 Redis 下，默认有16个数据库，数据库是由一个整数索引标识（**就是说数据库名是 0-15**），而不是由一个数据库名称。默认情况下，一个客户端连接到数据库 0。 

开启redis数据库：打开一个cmd,输入redis-server，或者直接从文件打开。然后再开启一个命令提示符，输入redis-cli.

首先是使用终端(127.0.0.1:6379>表示成功)

![](D:\学习笔记\redis\redis登录端口密码.png)

```java
redis-cli
1
```

（2）先给大家普及一条命令（这个是清空所有数据库数据，没事不要随便玩，哈哈）：

```java
flushall
1
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190401093211427.png)

（2）切换数据库（默认有 0-15）：

```java
select 0
```

 

（3）清空当前数据库数据示例：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190401094824961.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9pY29kZS5ibG9nLmNzZG4ubmV0,size_16,color_FFFFFF,t_70)



（4）数据重复 key 测试示例：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190401095523499.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9pY29kZS5ibG9nLmNzZG4ubmV0,size_16,color_FFFFFF,t_70) 