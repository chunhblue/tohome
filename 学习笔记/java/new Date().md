## js中 new Date().getTime()得到的是毫秒数

0、时间戳转日期字符串

```javascript
function getLocalTime(nS) {     		
var d = new Date(parseInt(nS)* 1000);    //根据时间戳生成的时间对象		
var date = (d.getFullYear()) + "-" + 					
(d.getMonth() + 1) + "-" +					
(d.getDate()) + " " + 					
(d.getHours()) + ":" + 					
(d.getMinutes()) + ":" + 					
(d.getSeconds()); 		
return date;   
}
document.write(getLocalTime(1552889937));
```

1、当前时间换时间戳

```javascript
var timestamp = parseInt(new Date().getTime()/1000);    // 当前时间戳document.write(timestamp);
```

2、当前时间换日期字符串

```javascript
var now = new Date();
var yy = now.getFullYear();      //年
var mm = now.getMonth() + 1;     //月
var dd = now.getDate();          //日
var hh = now.getHours();         //时
var ii = now.getMinutes();       //分
var ss = now.getSeconds();       //秒
var clock = yy + "-";
if(mm < 10) clock += "0";
clock += mm + "-";
if(dd < 10) clock += "0";
clock += dd + " ";
if(hh < 10) clock += "0";
clock += hh + ":";
if (ii < 10) clock += '0';
clock += ii + ":";
if (ss < 10) clock += '0';
clock += ss;
document.write(clock);     //获取当前日期
```

3、日期字符串转时间戳

```javascript
var date = '2015-03-05 17:59:00.0';
date = date.substring(0,19);    
date = date.replace(/-/g,'/'); 
var timestamp = new Date(date).getTime();document.write(timestamp);
```

