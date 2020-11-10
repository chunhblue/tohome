```sql
-- 若a为null,则a为b
coalesce(a,b)
```

```sql
-- on_hand_qty 必须是numeric类型
COALESCE(sk.on_hand_qty, 0::numeric) AS on_hand_qty,
```

