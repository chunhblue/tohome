```
select pg_constraint.conname as pk_name,pg_attribute.attname as colname,pg_type.typname as typename from 
pg_constraint  inner join pg_class 
on pg_constraint.conrelid = pg_class.oid 
inner join pg_attribute on pg_attribute.attrelid = pg_class.oid 
and  pg_attribute.attnum = pg_constraint.conkey[1]
inner join pg_type on pg_type.oid = pg_attribute.atttypid
where pg_class.relname = 'teacher' 
and pg_constraint.contype='p';
```

查出表teacher的主键名



 https://www.cnblogs.com/songxingzhu/p/4689449.html 

