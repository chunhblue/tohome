## postgresql批量插入copy_from()的使用

pg官网：   http://initd.org/psycopg/docs/cursor.html 



COPY_FROM说明：

```python
copy_from（file，table，sep ='\ t'，null ='\\ N'，size = 8192，columns = None ）
```

从类似文件的目标文件中读取数据，将它们附加到名为table的表中。

- file - 从中读取数据的类文件对象。它必须具有 read()和readline()方法。
- table - 要将数据复制到的表的名称。
- sep - 文件中预期的列分隔符。默认为选项卡。
- null - NULL文件中的文本表示。默认为两个字符串\N。
- size - 用于从文件中读取的缓冲区的大小。
- columns - 可以使用要导入的列的名称进行迭代。长度和类型应与要读取的文件的内容相匹配。如果未指定，则假定整个表与文件结构匹配。

————————————————
版权声明：本文为CSDN博主「儒雅的啷当」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_40659982/java/article/details/90704079