### 如何将yyyymmdd形式的字符串转换成timestamp?

```sql
timestamp(substr('20071208',1,4)||'-'||substr('20071208',5,2)||'-'||substr('20071208',7,2)||' 00:00:00')
```

