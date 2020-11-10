 https://www.cnblogs.com/smallleiit/articles/11686684.html 

###  一、索引的类型： 

 \1. B-Tree: 

 \2. Hash： 

  \3. GiST： 

 \4. GIN： 

###  二、复合索引： 

  \1. B-Tree类型的复合索引： 

  \2. GiST类型的复合索引： 

 \3. GIN类型的复合索引： 

###  三、组合多个索引： 

###  四、唯一索引： 

 目前，只有B-Tree索引可以被声明为唯一索引。
   CREATE UNIQUE INDEX name ON table (column [, ...]); 

###  五、表达式索引： 

###  六、部分索引： 

 \1. 索引字段和谓词条件字段一致： 

 \2. 索引字段和谓词条件字段不一致： 

 \3. 数据表子集的唯一性约束： 

###  七、检查索引的使用： 

