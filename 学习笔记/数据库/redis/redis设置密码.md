# [Redis设置密码](https://www.cnblogs.com/tenny-peng/p/11543440.html)

设置密码有两种方式。

#### 1. 命令行设置密码。

运行cmd切换到redis根目录，先启动服务端
`>redis-server.exe`
另开一个cmd切换到redis根目录，启动客户端
`>redis-cli.exe -h 127.0.0.1 -p 6379`
客户端使用config get requirepass命令查看密码

```
>config get requirepass
1)"requirepass"
2)""    //默认空
```

客户端使用config set requirepass yourpassword命令设置密码

```
>config set requirepass 123456
>OK
```

一旦设置密码，必须先验证通过密码，否则所有操作不可用

```
>config get requirepass
(error)NOAUTH Authentication required
```

使用auth password验证密码

```
>auth 123456
>OK
>config get requirepass
1)"requirepass"
2)"123456"
```

也可以退出重新登录
`redis-cli.exe -h 127.0.0.1 -p 6379 -a 123456`
命令行设置的密码在服务重启后失效，所以一般不使用这种方式。

#### 2. 配置文件设置密码

在redis根目录下找到redis.windows.conf配置文件，搜索requirepass，找到注释密码行，添加密码如下：

```
# requirepass foobared
requirepass tenny     //注意，行前不能有空格
```

重启服务后，客户端重新登录后发现

```
>config get requirepass
1)"requirepass"
2)""
```

密码还是空？

网上查询后的办法：创建redis-server.exe 的快捷方式， 右键快捷方式属性，在目标后面增加redis.windows.conf， 这里就是关键，你虽然修改了.conf文件，但是exe却没有使用这个conf，所以我们需要**手动指定**一下exe按照**修改后的conf**运行，就OK了。

所以，这里我再一次重启redis服务(指定配置文件)
`>redis-server.exe redis.windows.conf`
客户端再重新登录，OK了。

```
>redis-cli.exe -h 127.0.0.1 -p 6379 -a 123456
>config get requirepass
1)"requirepass"
2)"123456"
```

疑问: redis目录下有两个配置文件redis.windows.conf和redis.windows-server.conf，看到网上有的人用前者有的人用后者，不清楚到底该用哪一个。看了下两个文件又没啥区别，个人就用前者了。