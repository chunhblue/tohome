 https://blog.csdn.net/weixin_37704921/article/details/84324606 （含代码）

### multipart/form-data与application/octet-stream的区别、application/x-www-form-urlencoded

### multipart/form-data:

- 1、既可以提交普通键值对，也可以提交(多个)文件键值对。
- 2、HTTP规范中的Content-Type不包含此类型，只能用在POST提交方式下，属于http客户端(浏览器、java httpclient)的扩展
- 3、通常在浏览器表单中，或者http客户端(java httpclient)中使用。

页面中，form的enctype是multipart/form-data,提交时，content-type也是multipart/form-data。

multipart/form-data格式

--------

```
---------代码
```

### application/octet-stream

1、只能提交二进制，而且只能提交一个二进制，如果提交文件的话，只能提交一个文件,后台接收参数只能有一个，而且只能是流（或者字节数组）

2、属于HTTP规范中Content-Type的一种

3、很少使用

```
-------------代码
```

### application/x-www-form-urlencoded

- 1、不属于http content-type规范，通常用于浏览器表单提交，数据组织格式:name1=value1&name2=value2,post时会放入http body，get时，显示在在地址栏。
- 2、所有键与值，都会被urlencoded，请查看urlencoder

数据组织格式

```
------------代码
```

