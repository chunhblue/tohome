

| Name                             | Description      |
| -------------------------------- | ---------------- |
| character varying(n), varchar(n) | 变长，有长度限制 |
| character(n), char(n)            | 定长，不足补空白 |
| text                             | 变长，无长度限制 |

SQL 定义了两种基本的字符类型：character varying(n) 和 character(n) ，这里的 n 是一个正整数。两种类型都可以存储最多 n 个字符的字符串。试图存储更长的字符串到这些类型的字段里会产生一个错误，除非超出长度的字符都是空白，这种情况下该字符串将被截断为最大长度。这个看上去有点怪异的例外是 SQL 标准要求的。

**如果要存储的字符串比声明的长度短，类型为 character 的数值将会用空白填满；而类型为 character varying 的数值将只是存储短些的字符串。**

**varchar(n) 和 char(n) 分别是 character varying(n) 和 character(n)的别名**，没有声明长度的 character 等于 character(1) ；如果不带长度说明词使用 character varying，那么该类型接受任何长度的字符串。后者是 PostgreSQL 的扩展。

————————————————
版权声明：本文为CSDN博主「HypocrisyC」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/amoscn/java/article/details/80901999

