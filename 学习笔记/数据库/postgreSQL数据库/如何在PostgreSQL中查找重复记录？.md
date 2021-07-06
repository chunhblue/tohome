 基本思想是使用一个嵌套查询和计数聚合： 

```javascript
select * from yourTable ou
where (select count(*) from yourTable inr
where inr.sid = ou.sid) > 1
```

优化后：

```javascript
select Column1, Column2, count(*)
from yourTable
group by Column1, Column2
HAVING count(*) > 1
```

或者更短

```javascript
SELECT (yourTable.*)::text, count(*)
FROM yourTable
GROUP BY yourTable.*
HAVING count(*) > 1
```

实际需求，部分字段重复

```sql
SELECT n_roleid,c_priority, c_url, store_cd, record_cd, n_typeid, sub_no,C_ENABLE,to_char(d_insert_time,'yyyyMMdd hh24mi'),count(*) 
from t_backlog where to_char(d_insert_time,'yyyyMMdd')>'20210702'
group by n_roleid,c_priority, c_url, store_cd, record_cd, n_typeid, sub_no,C_ENABLE,to_char(d_insert_time,'yyyyMMdd hh24mi') having count(*) > 1;
```

