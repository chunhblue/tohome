```csharp
SELECT tablename from pg_tables
WHERE tablename not like 'pg%'
and tablename not like 'sql_%'
order by tablename ;
```

列出表名

