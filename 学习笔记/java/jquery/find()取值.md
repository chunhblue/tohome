点击选中行中其中一个input框，获取本行中另外一个input字段值：

```
<input name='' οnclick='check(this)'>

 function check( o){

//获取上上级tr 的demo

var tr = o.parentNode.parentNode;
```

通过find()方法获取tr行中字段值

1、根据name取值：

```
var ss=$(tr).find("select[name='measures']").val();//下拉框

var ss=$(tr).find("input[name='measures']").val();//input
```

2、根据id取值

```
var ss=$(tr).find("#measures']").val();
alert(ss)

}
```

```javascript
// 取得列表中的字段的值
$(selectTrTemp).find('td[tag=barcode]').text(m.barcode.val());
```

————————————————
版权声明：本文为CSDN博主「chp891202」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/chp891202/java/article/details/80701398