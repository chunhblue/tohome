### 激活URL地址：

http://139.199.89.239:1008/b8fdf475-b9f7-4146-b426-6e1bb5a17a16

###  在线生成UUID

http://www.ofmonkey.com/transfer/guid 

###  idea的jrebel插件离线激活的方式 

 https://www.jb51.net/softs/629017.html 



点击Change license进行激活
激活后一定要手动切换到离线模式进行使用，过程如图 如下步骤进行操作：File ——> Setting... ——> JRebel ——> Work offline l ——> OK



## 2.4 热部署配置（轻量级热部署配置）

l pom ---- 添加spring-boot-devtools依赖；

**（ 可以支持修改java文件会自动重启程序，一些资源无需触发重启，例如thymeleaf模板文件就可以实时编辑 ）**                               

l application.properties ---- # dev tools

n # dev tools

n #热部署生效

n spring.devtools.restart.enabled=true

n #设置重启的目录，添加哪个目录的文件需要重启

n spring.devtools.restart.additional-paths=src/main/java

l 测试方法：启动应用，修改TestController中logTest方法返回内容，查看应用是否自动重启，调用http://127.0.0.1:8086/test/logTest，查看返回结果是否更新；