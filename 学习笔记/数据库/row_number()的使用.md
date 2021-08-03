

 **(1),ROW_NUMBER() OVER (ORDER BY xlh DESC)**  

 简单的说row_number()从1开始，为每一条分组记录返回一个数字，这里的ROW_NUMBER() OVER (ORDER BY xlh DESC) 是先把xlh列降序，再为降序以后的没条xlh记录返回一个序号。  

![](D:\heads ref\tohome\学习笔记\数据库\row_number()1.png)

 **(2),row_number() OVER (PARTITION BY COL1 ORDER BY COL2)**  

 表示根据COL1分组，在分组内部根据 COL2排序，而此函数计算的值就表示每组内部排序后的顺序编号（组内连续的唯一的)

 需求：根据部门分组，显示每个部门的工资等级

![](D:\heads ref\tohome\学习笔记\数据库\row_number()2.png)