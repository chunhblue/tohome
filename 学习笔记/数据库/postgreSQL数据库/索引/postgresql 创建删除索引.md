```sql
--查询索引
select * from pg_indexes where tablename='tab1';   

```

```sql
--创建索引(查询用到哪几列，就对哪几个字段创建索引)

CREATE INDEX index_moni_gk_city_day ON moni_gk_city_day USING btree (datatime, citycode);
CREATE INDEX index_moni_gk_city_hour ON moni_gk_city_hour USING btree (datatime, citycode);
CREATE INDEX index_moni_gk_site_day ON moni_gk_site_day USING btree (datatime, stationcode);
CREATE INDEX index_moni_gk_site_hour ON moni_gk_site_hour USING btree (datatime, stationcode);
```

```sql
--删除索引
drop index tab1_bill_code_index  ;
```

```
注意：
```

虽然索引的目的在于提高数据库的性能，但这里有几个情况需要避免使用索引。

使用索引时，需要考虑下列准则：

- 索引不应该使用在较小的表上。
- 索引不应该使用在有频繁的大批量的更新或插入操作的表上。
- 索引不应该使用在含有大量的 NULL 值的列上。
- 索引不应该使用在频繁操作的列上。