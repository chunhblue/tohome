 **去掉所有字符串里面所有的逗号,eg:123,111,222.00——123111222.00** 

```js
function clear(str) { 
str = str.replace(/,/g, ""); //取消字符串中出现的所有逗号 
return str; 
}
```

**//字符串日期格式转换：dd/mm/yyyy → yyyymmdd**

```js
function fmtStringDate(date) {
	var res = "";
	date = date.replace(/\//g, "");
	res = date.substring(4, 8) + date.substring(2, 4) + date.substring(0, 2);
	return res;
}
```

