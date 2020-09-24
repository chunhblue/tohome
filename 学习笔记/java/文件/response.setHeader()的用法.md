## [response.setHeader()的用法](https://www.cnblogs.com/mingforyou/p/3281945.html)

 https://www.cnblogs.com/mingforyou/p/3281945.html 

response.setHeader()下载中文文件名乱码问题 收藏 1. HTTP消息头

（1）通用信息头

即能用于请求消息中,也能用于响应信息中,但与被传输的实体内容没有关系的信息头,如Data,Pragma

主要: Cache-Control , Connection , Data , Pragma , Trailer , Transfer-Encoding , Upgrade

（2）请求头

用于在请求消息中向服务器传递附加信息,主要包括客户机可以接受的数据类型,压缩方法,语言,以及客户计算机上保留的信息和发出该请求的超链接源地址等.

主要: Accept , Accept-Encoding , Accept-Language , Host ,

（3）响应头

用于在响应消息中向客户端传递附加信息,包括服务程序的名称,要求客户端进行认证的方式,请求的资源已移动到新地址等.

主要: Location , Server , WWW-Authenticate(认证头)

（4）实体头

用做实体内容的元信息,描述了实体内容的属性,包括实体信息的类型,长度,压缩方法,最后一次修改的时间和数据的有效期等.

主要: Content-Encoding , Content-Language , Content-Length , Content-Location , Content-Type

（4）扩展头

主要：Refresh, Content-Disposition

\2. 几个主要头的作用

（1）Content-Type的作用

该实体头的作用是让服务器告诉浏览器它发送的数据属于什么文件类型。

例如：当Content-Type 的值设置为text/html和text/plain时,前者会让浏览器把接收到的实体内容以HTML格式解析,后者会让浏览器以普通文本解析.

（2）Content-Disposition 的作用

当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。

在讲解这个内容时,张老师同时讲出了解决中文文件名乱码的解决方法,平常想的是使用getBytes() , 实际上应使用email的附件名编码方法对文件名进行编码,但IE不支持这种作法(其它浏览器支持) , 使用javax.mail.internet.*包的MimeUtility.encodeWord("中文.txt")的方法进行编码。

Content-Disposition扩展头的例子：

```java
<%@ page pageEncoding="GBK" contentType="text/html;charset=utf-8" import="java.util.*,java.text.*" %>

<%=DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CHINA).format(new Date())

%>

<%

        response.setHeader("Content-Type","video/x-msvideo");

        response.setHeader("Content-Disposition", "attachment;filename=aaa.doc");

%>
```

Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，保存类型以Content中设置的为准。

注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。

（3）Authorization头的作用

Authorization的作用是当客户端访问受口令保护时，服务器端会发送401状态码和WWW-Authenticate响应头，要求客户机使用Authorization来应答。

例如：

```java
<%@ page pageEncoding="GBK" contentType="text/html;charset=utf-8" import="java.util.*,java.text.*" %>

<%=DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CHINA).format(new Date())

%>

<%

response.setStatus(401);

response.setHeader("WWW-Authenticate", "Basic realm=/"Tomcat Manager Application/"");

%>
```

3．如何实现文件下载

要实现文件下载，我们只需要设置两个特殊的相应头，它们是什么头？如果文件名带中文，该如何解决？

两个特殊的相应头：

```
----Content-Type:    application/octet-stream

----Content-Disposition: attachment;filename=aaa.zip
```

例如：

```
response.setContentType("image/jpeg");response.setHeader("Content- Disposition","attachment;filename=Bluehills.jpg");
```

如果文件中filename参数中有中文，则就会出现乱码。

解决办法：

（1）MimeUtility.encodeWord("中文.txt");//现在版本的IE还不行

（2）new String("中文".getBytes("GB2312"),"ISO8859- 1");//实际上这个是错误的

\4. 测试并分析文件名乱码问题

response.setHeader()下载中文文件名乱码问题

```
response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
```

下载的程序里有了上面一句，一般在IE6的下载提示框上将正确显示文件的名字，无论是简体中文，还是日文。不过当时确实没有仔细测试文件名为很长的中文文件名的情况。现如今经过仔细测试，发现文字只要超过17个字，就不能下载了。分析如下：

一. 通过原来的方式，也就是先用URLEncoder编码，当中文文字超过17个时，IE6 无法下载文件。这是IE的bug，参见微软的知识库文章 KB816868 。原因可能是IE在处理 Response Header 的时候，对header的长度限制在150字节左右。而一个汉字编码成UTF-8是9个字节，那么17个字便是153个字节，所以会报错。而且不跟后缀也不对.

二. 解决方案：将文件名编码成ISO8859-1是有效的解决方案，代码如下：

response.setHeader( "Content-Disposition", "attachment;filename=" + new String( fileName.getBytes("gb2312"), "ISO8859-1" ) );

在确保附件文件名都是简体中文字的情况下，那么这个办法确实是最有效的，不用让客户逐个的升级IE。如果台湾同胞用，把gb2312改成big5就行。但现在的系统通常都加入了 国际化的支持，普遍使用UTF-8。如果文件名中又有简体中文字，又有繁体中文，还有日文。那么乱码便产生了。另外，在上Firefox (v1.0-en)下载也是乱码。

三. 参看邮件中的中文附件名的形式，用outlook新建一个带有中文附件的邮件，然后看这个邮件的源代码，找到：

Content-Disposition: attachment;

filename="=?gb2312?B?0MK9qCDOxLG+zsS1tS50eHQ=?="

用这个filename原理上就可以显示中文名附件，但是现在IE并不支持，Firefox是支持的。尝试使用 javamail 的MimeUtility.encode()方法来编码文件名，也就是编码成 =?gb2312?B?xxxxxxxx?= 这样的形式，并从 RFC1522 中找到对应的标准支持。

折中考虑，结合了一、二的方式，代码片断如下：

```java
String fileName = URLEncoder.encode(atta.getFileName(), "UTF-8");

/*

\* see http://support.microsoft.com/default.aspx?kbid=816868

*/

if (fileName.length() > 150) {

  String guessCharset = xxxx

//根据request的locale 得出可能的编码，中文操作系统通常是gb2312

fileName = new String(atta.getFileName().getBytes(guessCharset), "ISO8859-1");

}

response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
```

编码转换的原理：

首先在源程序中将编码设置成GB2312字符编码,然后将源程序按Unicode编码转换成字节码加载到内存中（java加载到内存中的字节码都是Unicode编码），然后按GB2312编码获得中文字符串的字节数组，然后生成按ISO8859-1编码形式的Unicode字符串（这时的4个字节就变成了8个字节，高位字节补零）,

java培训  北京java培训 java培训班  java就业培训 java培训机构 软件培训 最好的java培训

当在网络中传输时，因为setHeader方法中的字符只能按ISO8859-1传输，所以这时候就又把Unicode字符转换成了ISO8859-1的编码传到浏览器（就是把刚才高位补的零全去掉），这时浏览器接收到的ISO8859-1码的字符因为符合GB2312编码，所以就可以显示中文了。

\5. jsp翻译成class时的编码问题

记事本中代码块1：

<%=

​    "a中文".length()

%>

代码块2：

<%@ page pageEncoding="gbk"%>

<%=

​    "a中文".length()

%>

为什么上面的输出值为5，改成下面的则输出3？

因为上面的代码没有添加该文件的编码说明 , WEB应用程序在将jsp翻译成class文件时 , 把该字符串的内容按默认的保存方式指定的编码ASCII码来算的，在UTF-8中，原ASCII字符占一个字节，汉字占两个字节，对应两个字符，长度就变成了5 , 而下面的是GBK编码, 一个汉字和一个英文都对应一个字符,得到结果就为3.

]

response.setHeader(...)文件名中有空格的时候

String fileName = StringUtils.trim(file.getName());

String formatFileName = encodingFileName(name);//在后面定义方法encodingFileName(String fileName); response.setHeader("Content-Disposition", "attachment; filename=" + formatFileName );

//处理文件名中出现的空格 

//其中%20是空格在UTF-8下的编码

```java
public static String encodingFileName(String fileName) {     
        String returnFileName = "";     
        try {       
            returnFileName = URLEncoder.encode(fileName, "UTF-8");       
            returnFileName = StringUtils.replace(returnFileName, "+", "%20");
            if (returnFileName.length() > 150) {         
                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");        
                returnFileName = StringUtils.replace(returnFileName, " ", "%20");      
            }     
        } catch (UnsupportedEncodingException e) {      
            e.printStackTrace();       
            if (log.isWarnEnabled()) {         
                log.info("Don't support this encoding ...");     
            }    
        }    
        return returnFileName;  
    }
```

