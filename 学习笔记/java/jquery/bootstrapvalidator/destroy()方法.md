 bootstrapValidator.destroy()：

 问题：经常开发中用到modal对话框弹出验证以后第二次弹框时上次的验证效果依然有效，那就要想办法第二次弹框之前去掉上次的验证； 

解决办法：

引入bootstrap的validator的校验js和css；

解决二次校验问题方法

1.定义bootstrap validator校验方法(校验部门名称是否重复)

2.在js默认启动时调用启动校验
$(function(){formValidator();});

3.在modal隐藏时移除校验重新添加校验

```javascript
$("#addDepModal").on('hidden.bs.modal',function(e){

//移除上次的校验配置

$("#addDepForm").data('bootstrapValidator').destroy();

$('#addDepForm').data('bootstrapValidator',null);

//重新添加校验

formValidator();

});
```

————————————————
版权声明：本文为CSDN博主「mayundoyouknow」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/ahou2468/java/article/details/78839861