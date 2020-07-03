###  一、前言

#### BootstrapValidator是基于bootstrap3的jquery表单验证插件，是最适合bootstrap框架的表单验证插件 

### 二、问题描述

当按钮的类型为submit时，使用bootstrapValidator的isValid()能够使验证表单正常工作，但当button的type类型为button时，只调用bootstrapValidator的isValid()方法无法正常工作。这时候就需要使用bootstrapValidator的validate()方法进行激活。

```javascript
// yourform指的是你的模态框
var bootstrapValidator = $("#yourform").data('bootstrapValidator');
bootstrapValidator.validate();  // 激活
if(bootstrapValidator.isValid()){ // 表单验证

}
```



**bootstrapValidator不触发校验**

————————————————
版权声明：本文为CSDN博主「弓长步又」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/biedazhangshu/java/article/details/50729553

