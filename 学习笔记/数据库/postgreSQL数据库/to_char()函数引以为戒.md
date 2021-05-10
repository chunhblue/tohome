```
SELECT to_char(now(),'yyyyMMddhh24miss') as dbDate;
```

 正确：to_char(GetOnOffDateTime,'yyyymmddhh24miss')
错误：to_char(GetOnOffDateTime,'yyyymmddhh24mmss') 

错误的写法会导致时间越来越小（

例如：第一次是 20210508152723

​			第二次就会是 20210508152712

）