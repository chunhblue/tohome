Cannot read property 'replace' of undefined

 在JS代码中使用replace方法时遇到该问题 

```javascript
function fmtStringDate(date) {    
	var res = "";    
	date = date.replace(/\//g, "");   
	res = date.substring(4, 8) + date.substring(2, 4) + date.substring(0, 2);    
	return res;
}
```

原因：date值为null

解决办法：先判断date是否为空再进行replace操作