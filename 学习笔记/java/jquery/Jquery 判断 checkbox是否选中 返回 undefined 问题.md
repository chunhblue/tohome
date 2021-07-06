 jquery 语法有些改变 

 

```
$('#input').attr('checked'); 
```

 **attr** 返回的是checked或者是undefined，不是原来的true和false了 

######  高版本使用方式 

```

//获取是否选中 
var isChecked = $('#input').prop('checked'); 
//或 
var isChecked = $('#input').is(":checked"); 
//设置选中 
$('#input').prop('checked',true); 

```

https://blog.csdn.net/xuyw10000/article/details/21248051