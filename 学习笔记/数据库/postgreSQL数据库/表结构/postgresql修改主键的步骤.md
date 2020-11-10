

```
--删除主键
ALTER TABLE public.pi0130 DROP CONSTRAINT pi0130_pkey;
-- 添加联合主键
ALTER TABLE public.pi0130  ADD CONSTRAINT pi0130_pkey  PRIMARY KEY (pi_cd,article_id,barcode); 
```

