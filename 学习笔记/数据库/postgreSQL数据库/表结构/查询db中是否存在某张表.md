两个方法：

1

```
SELECT count(*) FROM pg_class
WHERE relname = 'public.table_name';
```

2

```
SELECT
	count(*)
FROM
	information_schema.tables 
WHERE
	table_schema = 'public' 
	AND table_type = 'BASE TABLE' 
	AND TABLE_NAME = 'table_name';
```

