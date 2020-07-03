# Git 提交代码步骤

第一步：

提交代码第一步：git status  查看当前状态

当你忘记修改了哪些文件的时候可以使用 git status  来查看当前状态，

红色的字体显示的就是你修改的文件。

 ![img](https://img-blog.csdnimg.cn/20190415144400185.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RjMjgyNjE0OTY2,size_16,color_FFFFFF,t_70) 

第二步:

提交代码第二步：git add .  或者 git add xxx

如图1、如果你git status 查看了当前状态发现都是你修改过的文件，都要提交，那么你可以直接使用 git add .  就可以把你的内容全部添加到本地git缓存区中

如图2、如果你git status 查看了当前状态发现有部分文件你不想提交，那么就使用git add xxx(上图中的红色文字的文件链接)  就可以提交部分文件到本地git缓存区。

   ![img](https://img-blog.csdnimg.cn/20190415144525378.png) 

   ![img](https://img-blog.csdnimg.cn/20190415144632487.png) 

第三步：

提交代码第三步：git commit -m "提交代码"   推送修改到本地git库中

 ![img](https://img-blog.csdnimg.cn/20190415144803894.png) 

第四步：

提交代码第四步：git pull <远程主机名> <远程分支名>  取回远程主机某个分支的更新，再与本地的指定分支合并。

 ![img](https://img-blog.csdnimg.cn/20190415144918660.png)  

第五步：

提交代码第五步：git push <远程主机名> <远程分支名>  把当前提交到git本地仓库的代码推送到远程主机的某个远程分之上

 ![img](https://img-blog.csdnimg.cn/20190415145007850.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RjMjgyNjE0OTY2,size_16,color_FFFFFF,t_70) 

————————————————
版权声明：本文为CSDN博主「青春年少不知疼」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/dc282614966/java/article/details/89311683