## poi实现word、excel、ppt转html

 \###简介
java实现在线预览功能是一个大家在工作中也许会遇到的需求，如果公司有钱，直接使用付费的第三方软件或者云在线预览服务就可以了，例如永中office、office web 365(http://www.officeweb365.com/)他们都有云在线预览服务，就是要钱0.0
如果想要免费的，可以用openoffice，还需要借助其他的工具(例如swfTools、FlexPaper等)才行，可参考这篇文章http://blog.csdn.net/z69183787/article/details/17468039，写的挺细的，实现原理就是：
1.通过第三方工具openoffice，将word、excel、ppt、txt等文件转换为pdf文件；
2.通过swfTools将pdf文件转换成swf格式的文件；
3.通过FlexPaper文档组件在页面上进行展示。
当然如果装了Adobe Reader XI，那把pdf直接拖到浏览器页面就可以直接打开预览，这样就不需要步骤2、3了，前提就是客户装了Adobe Reader XI这个pdf阅读器。
我这里介绍通过poi实现word、excel、ppt转html，这样就可以放在页面上了。 

 https://blog.csdn.net/yjclsx/article/details/51441632 