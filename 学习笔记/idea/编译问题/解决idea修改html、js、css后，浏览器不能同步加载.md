## [解决idea修改html、js、css后，浏览器不能同步加载](https://www.cnblogs.com/xphhh/p/11355336.html)

重装了IDEA后，忽略了一些设置，导致在开发springboot项目时，启动了项目，修改了前端文件，但是浏览器中并不能实时加载修改的内容。
主要是IDEA的2个地方需要设置：

一、修改file-settings

 ![img](https://img2018.cnblogs.com/blog/1628476/201908/1628476-20190814231032696-1165881871.png) 

二、在IDEA中，按快捷键：Ctrl+Shift+Alt+/

弹出提示，选择第一个：

 ![img](https://img2018.cnblogs.com/blog/1628476/201908/1628476-20190814231154858-942015534.png) 

 点击后，勾选，重启IDEA生效。

 ![img](https://img2018.cnblogs.com/blog/1628476/201908/1628476-20190814231240723-821066412.jpg) 





## 如果上面的都不生效的话：就是idea的原因

把项目目录里面的.idea文件夹删除之后，重新打开，重新生成index之后，ok，这次生效了。给未来的自己提个醒~~

（删除.idea文件之后，需要 重新在idea中配置jdk版本，并且在右侧重新显示出maven按钮，才可以重新启动项目。）

显示出maven按钮：鼠标右键点击项目的pom.xml，选择下方的 Add as maven build file ，即可重新购置编译器。