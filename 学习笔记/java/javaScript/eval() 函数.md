## 定义和用法

eval() 函数可计算某个字符串，并执行其中的的 JavaScript 代码。

### 语法

```
eval(string)
```

| 参数   | 描述                                                         |
| :----- | :----------------------------------------------------------- |
| string | 必需。要计算的字符串，其中含有要计算的 JavaScript 表达式或要执行的语句。 |

### 返回值

通过计算 string 得到的值（如果有的话）。

### 例子 1

在本例中，我们将在几个字符串上运用 eval()，并看看返回的结果：

```javascript
<script type="text/javascript">

eval("x=10;y=20;document.write(x*y)")

document.write(eval("2+2"))

var x=10
document.write(eval(x+17))

</script>
```

输出：

```javascript
200
4
27
```

*****

对于服务器返回的JSON字符串，如果 jQuery 异步请求没做类型说明，或者以字符串方式接受，那么需要做一次对象化处理，方式不是太麻烦，就是将该字符串放于 eval()中执行一次。这种方式也适合以普通 javascipt 方式获取 json 对象，以下举例说明：

```javascript
var u = eval('('+user+')');
```

为什么要 eval 这里要添加 **('('+user+')')** 呢？

原因在于：eval 本身的问题。 由于 json 是以 **{}** 的方式来开始以及结束的，在 js 中，它会被当成一个语句块来处理，所以必须强制性的将它转换成一种表达式。

加上圆括号的目的是迫使 eval 函数在处理 JavaScript 代码的时候强制将括号内的表达式（expression）转化为对象，而不是作为语句（statement）来执行。举一个例子，例如对象字面量 {}，如若不加外层的括号，那么 eval 会将大括号识别为j avascript 代码块的开始和结束标记，那么{}将会被认为是执行了一句空语句。所以下面两个执行结果是不同的：

```javascript
alert(eval("{}"); // return undefined
alert(eval("({})");// return object[Object]
```

### 测试实例

```javascript
var user = '{name:"张三",age:23,'+   
    'address:{city:"青岛",zip:"266071"},'+    'email:"iteacher@haiersoft.com.cn",'+  
    'showInfo:function(){'+  
    'document.write("姓名："+this.name+"<br/>");'+  
    'document.write("年龄："+this.age+"<br/>");'+  
    'document.write("地址："+this.address.city+"<br/>");'+  
    'document.write("邮编："+this.address.zip+"<br/>");'+  
    'document.write("E-mail："+this.email+"<br/>");} }';   
var u = eval('('+user+')');  
u.showInfo();
```