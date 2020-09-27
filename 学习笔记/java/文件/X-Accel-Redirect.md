## Nginx 利用 X-Accel-Redirect response.setHeader 控制文件下载

 https://www.cnblogs.com/kgdxpr/p/3587878.html （配置方式）

对于非公开的图片和视频资源，web服务器应当把这些资源保护起来，避免外部直接访问。Nginx通过internal可以设置资源内部访问。例：

location /files {
root /var/www;
internal;
}

这样/var/www/files/下的所有文件就无法访问。

到时候所有用户的文件都挂载到网站的指定目录；

所有图片等静态资源都通过访问php来获取。Php接收请求，解析真实路径，X-Accel-Redirect把真实路径以静态资源的方式输出。权限的判断可以再php接收请求后进行判断。

header("X-Accel-Redirect: /files/" . $path);

```java
// fileDir 配置的target
response.setHeader("X-Accel-Redirect", fileDir +File.separator+filePath);
```

