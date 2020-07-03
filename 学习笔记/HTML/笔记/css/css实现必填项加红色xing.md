```html
<label for="i_reason_name"  class="col-sm-3 control-label not-null" style="white-space:nowrap">Reasons Name</label>
```

**class="col-sm-3 control-label not-null"  直接实现在label前面加*，表示必填项。**



## 二、CSS 实现必填项前/后添加红色星号

1 . 常规写法

```html
<label><span style="color:red;">* </span>用户名 : </label>
<input type="text" value=""/>
```

2 . CSS写法(更简洁方便 , 而且便于统一调整样式)

```html
<style>
    label.xrequired:before {
        content: '* ';
        color: red;
    }
</style>
<label class="xrequired">用户名 : </label>
<input type="text" value=""/>
```

————————————————
版权声明：本文为CSDN博主「BlueKitty1210」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/xingbaozhen1210/java/article/details/90415635