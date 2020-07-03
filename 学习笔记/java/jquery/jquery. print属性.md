 https://www.cnblogs.com/sloveling/p/jquery_print.html 

## [jquery print属性设置](https://www.cnblogs.com/sloveling/p/jquery_print.html)

**一，需求背景**

 项目中遇到打印功能，想实现完美的打印功能，对元素进行操作等，可以使用jquery print 插件。

**二，依赖文件** 

 [git地址请戳这里](https://github.com/DoersGuild/jQuery.print)

1，jquery

2，jquery.print-preview.js

3，jQuery.print.js

**三，使用和配置**

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```javascript
        $("#myElementId").print({

            globalStyles:true,//是否包含父文档的样式，默认为true

            mediaPrint:false,//是否包含media='print'的链接标签。会被globalStyles选项覆盖，默认为false

            stylesheet:null,//外部样式表的URL地址，默认为null

            noPrintSelector:".no-print",//不想打印的元素的jQuery选择器，默认为".no-print"

            iframe:true,//是否使用一个iframe来替代打印表单的弹出窗口，true为在本页面进行打印，false就是说新开一个页面打印，默认为true

            append:null,//将内容添加到打印内容的后面

            prepend:null,//将内容添加到打印内容的前面，可以用来作为要打印内容

            deferred: $.Deferred()//回调函数

            });
```