

```
SELECT b.relname,a.* FROM pg_trigger a, pg_class b WHERE tgrelid=b.oid and tgenabled = 'O';
```

- pg_trigger 表示触发器
- pg_class  表
- tgenabled ：  'O'表示正在启用(onable)；'D'表示已禁止(disabled);

除了我们自己写的触发器，数据库本身也会生成很多关于权限的触发器，是系统自带的，注意不要误关。