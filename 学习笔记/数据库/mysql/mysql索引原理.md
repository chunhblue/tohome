https://blog.csdn.net/ysl19910806/article/details/98950553

## MySQL索引原理及B-Tree / B+Tree结构详解

**目录**

[摘要](https://blog.csdn.net/ysl19910806/article/details/98950553#t1)

[数据结构及算法基础](https://blog.csdn.net/ysl19910806/article/details/98950553#t2)

[索引的本质](https://blog.csdn.net/ysl19910806/article/details/98950553#t3)

[B-Tree和B+Tree](https://blog.csdn.net/ysl19910806/article/details/98950553#t4)

[B-Tree](https://blog.csdn.net/ysl19910806/article/details/98950553#t5)

[B+Tree](https://blog.csdn.net/ysl19910806/article/details/98950553#t6)

[带有顺序访问指针的B+Tree](https://blog.csdn.net/ysl19910806/article/details/98950553#t7)

[为什么使用B-Tree（B+Tree）](https://blog.csdn.net/ysl19910806/article/details/98950553#t8)

[主存存取原理](https://blog.csdn.net/ysl19910806/article/details/98950553#t9)

[磁盘存取原理](https://blog.csdn.net/ysl19910806/article/details/98950553#t10)

[局部性原理与磁盘预读](https://blog.csdn.net/ysl19910806/article/details/98950553#t11)

[B-/+Tree索引的性能分析](https://blog.csdn.net/ysl19910806/article/details/98950553#t12)

[MySQL索引实现](https://blog.csdn.net/ysl19910806/article/details/98950553#t13)

[MyISAM索引实现](https://blog.csdn.net/ysl19910806/article/details/98950553#t14)

[InnoDB索引实现](https://blog.csdn.net/ysl19910806/article/details/98950553#t15)

[索引使用策略及优化](https://blog.csdn.net/ysl19910806/article/details/98950553#t16)

[示例数据库](https://blog.csdn.net/ysl19910806/article/details/98950553#t17)

[最左前缀原理与相关优化](https://blog.csdn.net/ysl19910806/article/details/98950553#t18)

[情况一：全列匹配。](https://blog.csdn.net/ysl19910806/article/details/98950553#t19)

[情况二：最左前缀匹配。](https://blog.csdn.net/ysl19910806/article/details/98950553#t20)

[情况三：查询条件用到了索引中列的精确匹配，但是中间某个条件未提供。](https://blog.csdn.net/ysl19910806/article/details/98950553#t21)

[情况四：查询条件没有指定索引第一列。](https://blog.csdn.net/ysl19910806/article/details/98950553#t22)

[情况五：匹配某列的前缀字符串。](https://blog.csdn.net/ysl19910806/article/details/98950553#t23)

[情况六：范围查询。](https://blog.csdn.net/ysl19910806/article/details/98950553#t24)

[情况七：查询条件中含有函数或表达式。](https://blog.csdn.net/ysl19910806/article/details/98950553#t25)

[索引选择性与前缀索引](https://blog.csdn.net/ysl19910806/article/details/98950553#t26)

[InnoDB的主键选择与插入优化](https://blog.csdn.net/ysl19910806/article/details/98950553#t27)

[后记](https://blog.csdn.net/ysl19910806/article/details/98950553#t28)