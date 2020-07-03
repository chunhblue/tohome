## .iml文件

idea 对module 配置信息之意， infomation of module
iml是 intellij idea的工程配置文件，里面是当前project的一些配置信息。

- iml 文件是IntelliJIDEA自动创建的模块文件，用于Java应用开发，存储一些模块开发相关的信息，比如一个Java组件，插件组件，Maven组件等等，还可能会存储一些模块路径信息，依赖信息以及别的一些设置。

## .idea文件夹

.idea存放项目的配置信息，包括历史记录，版本控制信息等。

------

可以点击file->Settings->File types,在右下角的Ignore files and folders中可以输入.idea;和*.iml;将其隐藏。

![](D:\学习笔记\idea\ignore files and folders.png)



不建议隐藏iml，因为项目名出现中括号是因为iml文件名和项目文件名不一样，需要更改iml文件名 

![](D:\学习笔记\idea\iml文件名.png)