 https://blog.csdn.net/eieiei438/article/details/83824375 

### 注意事项

1. 这里没有控制文件的名称为中文【自行编解码】
2. response中设置返回的内容格式为【二进制格式】
3. 当不写response.setHeader("Content-Disposition", "attachment;filename=" + name);的时候，浏览器读到有后缀的文件时会尝试打开；即使下载也会命名为file。
4. response.setHeader("Content-Disposition", "attachment;filename=" + name);
   - attachment强制性下载
   - filename下载文件的名称
5. 其他。。。

```java
@RequestMapping("/file")
@ResponseBody
public void file(HttpServletRequest request, HttpServletResponse response) {
    String name = request.getParameter("file");
    String path = "/file" + File.separator + name;
 
    File imageFile = new File(path);
    if (!imageFile.exists()) {
        return;
    }
 
    //下载的文件携带这个名称
    response.setHeader("Content-Disposition", "attachment;filename=" + name);
    //文件下载类型--二进制文件
    response.setContentType("application/octet-stream");
 
    try {
        FileInputStream fis = new FileInputStream(path);
        byte[] content = new byte[fis.available()];
        fis.read(content);
        fis.close();
 
        ServletOutputStream sos = response.getOutputStream();
        sos.write(content);
 
        sos.flush();
        sos.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

