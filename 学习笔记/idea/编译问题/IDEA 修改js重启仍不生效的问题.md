# 记一次IDEA 修改js重启仍不生效的问题

20200424  最近在去其他项目帮忙之后，重新回以前产品部拉取代码后，发现更新js之后chrome加载的仍是旧文件，因为项目是springboot的，application的配置已经是update classes and resources，网上搜了一圈发现基本上都这样就可以了。后来我自己把项目删了，重新import maven，仍没有效果；最后把项目目录里面的.idea文件夹删除之后，重新打开，重新生成index之后，ok，这次生效了。给未来的自己提个醒~~
————————————————
版权声明：本文为CSDN博主「k_orey」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/a327792916/java/article/details/94599535



有是一个坑爹的bug，把web工程发布到tomcat上之后，一开始运行的好好的，后来修改里面的js传给服务端的数据，一点变化都没有，，
都说要把配置里面换成更新

修改on update Action 为 Redeploy
On frame deactivation 为Update classes and resources
————————————————
版权声明：本文为CSDN博主「清风丿自来」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_39098813/java/article/details/80723375

#### 如果还是不能同步，那就是浏览器的问题的，浏览器加载了缓存文件，需要禁用缓存

![](D:\学习笔记\idea\浏览器缓存.png)