 https://blog.csdn.net/qq_37464248/article/details/82769868?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.edu_weight&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.edu_weight 

### 创建自动增长序列

```sql
CREATE SEQUENCE book_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;
```

| 参数         | 描述                                                         |
| :----------- | :----------------------------------------------------------- |
| START WITH   | 设置起始值，允许序列从任何地方开始                           |
| INCREMENT BY | 设置增量，指定在哪个值得基础上创建新值，正值将产生递增序列，负值将产生递减序列；默认值为1。 |
| NO MINVALUE  | 设置序列可以生成的最小值，如果未指定NO MINVALUE，对于升序和降序序列，默认值分别为1和![-2^{63}-1](https://private.codecogs.com/gif.latex?-2%5E%7B63%7D-1)。 |
| NO MAXVALUE  | 设置序列可以生成的最大值，如果未指定此子句，将使用默认值，对于升序和降序序列，默认值为![2^{63}-1](https://private.codecogs.com/gif.latex?2%5E%7B63%7D-1)和-1。 |
| CACHE        | 设置高速缓存，要分配多少序列号并将其存贮在内存中方便更快的访问，最小值为1，默认值也是1。 |

### 为book表添加自动增长序列

```
ALTER TABLE book ALTER COLUMN id SET DEFAULT nextval('book_id_seq');
```

