### Js动态添加复选框Checkbox的实例方法!!! 

 https://www.cnblogs.com/cfinder010/p/3502979.html 

 https://blog.csdn.net/yelin042/article/details/76864574 

### [在js中怎样获得checkbox里选中的多个值？(jQuery)](https://www.cnblogs.com/sunxirui00/p/7498014.html)

思路：利用name属性值获取checkbox对象，然后循环判断checked属性（true表示被选中，false表示未选中）。下面进行实例演示：

1、HTML结构

```
<input type="checkbox" name="test" value="1"/><span>1</span>
<input type="checkbox" name="test" value="2"/><span>2</span>
<input type="checkbox" name="test" value="3"/><span>3</span>
<input type="checkbox" name="test" value="4"/><span>4</span>
<input type="checkbox" name="test" value="5"/><span>5</span>
<input type='button' value='提交' onclick="show()"/>
```

2、javascript代码(jQuery)

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
function show(){
    obj = document.getElementsByName("test");
    check_val = [];
    for(k in obj){
        if(obj[k].checked)
            check_val.push(obj[k].value);
    }
    alert(check_val);
}
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

3、演示效果

![img](D:\学习笔记\HTML基础\笔记\js\checkbox循环复选框.jpg)