## SimpleDateFormat转换时间，12,24时间格式

 在使用SimpleDateFormat时格式化时间的 yyyy.MM.dd 为年月日

- 而如果希望格式化时间为12小时制的，则使用***\*hh:mm:ss\**** 
- 如果希望格式化时间为24小时制的，则使用***\*HH:mm:ss\**** 

```java
 Date d = new Date();  

        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制  

        System.out.println(ss.format(d));  

        Date date = new Date();  

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制  

        String LgTime = sdformat.format(date);  

        System.out.println(LgTime);
        
结果为  :

2008-05-28 01:32:54 

2008-05-28 13:32:54 
————————————————
版权声明：本文为CSDN博主「yangshuanbao」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/yangshuanbao/article/details/6864054
```

