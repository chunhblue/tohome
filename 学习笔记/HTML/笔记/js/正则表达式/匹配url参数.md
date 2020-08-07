```javascript
/([^?&]+)=([^?&]+)/g
```

 *中括号里的表示非的意思，所以[?&=]表示非&?的字符。接一个=号。后面再接非&?的字符。* 

 这个正则是匹配url地址的参数的。



 *var url = ‘[www.163.com?name=呼啦啦&function=哈哈](http://www.163.com/?name=呼啦啦&function=哈哈)’;
console.log(url.match(/([?&=]+)=([?&=]*)/g));
//[“name=呼啦啦”, “function=哈哈”]
//匹配的就是url中的’name=呼啦啦’和’&function=哈哈’ 

