autocomplete 属性 清除input框输入存留历史值,防止下拉历史值显示

廿二又 2017-04-01 17:12:07  25554  收藏 4
分类专栏： web前端
版权
autocomplete 属性规定输入字段是否应该启用自动完成功能。

自动完成允许浏览器预测对字段的输入。当用户在字段开始键入时，浏览器基于之前键入过的值，应该显示出在字段中填写的选项。

注释：autocomplete 属性适用于 <form>，以及下面的 <input> 类型：text, search, url, telephone, email, password, datepickers, range 以及 color。

```html
<input type="text" name = "name" autocomplete="off" />
```

如果打开 on 
效果如下

![](D:\heads ref\tohome\学习笔记\HTML\笔记\html\自动下拉历史记录.jpg)

————————————————
版权声明：本文为CSDN博主「廿二又」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/u014596302/java/article/details/68946327