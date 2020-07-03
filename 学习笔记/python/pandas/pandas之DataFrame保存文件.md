DataFrame 数据的保存和读取

df.to_csv 写入到 csv 文件
pd.read_csv 读取 csv 文件
df.to_json 写入到 json 文件
pd.read_json 读取 json 文件
df.to_html 写入到 html 文件
pd.read_html 读取 html 文件
df.to_excel 写入到 excel 文件
pd.read_excel 读取 excel 文件
————————————————
版权声明：本文为CSDN博主「tz_zs」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/tz_zs/java/article/details/81137998

**pandas.DataFrame.to_csv**
将 DataFrame 写入到 csv 文件

https://pandas.pydata.org/pandas-docs/stable/generated/pandas.DataFrame.to_csv.html

```python
DataFrame.to_csv(path_or_buf=None, sep=', ', na_rep='', float_format=None, columns=None, header=True, 				 index=True,index_label=None, mode='w', encoding=None, compression=None, quoting=None, 				  quotechar='"',line_terminator='\n', chunksize=None, tupleize_cols=None, 	  date_format=None, doublequote=True,escapechar=None, decimal='.')
```


参数：

path_or_buf : 文件路径，如果没有指定则将会直接返回字符串的 json
sep : 输出文件的字段分隔符，默认为 “,”
na_rep : 用于替换空数据的字符串，默认为''
float_format : 设置浮点数的格式（几位小数点）
columns : 要写的列
header : 是否保存列名，默认为 True ，保存
index : 是否保存索引，默认为 True ，保存
index_label : 索引的列标签名
.

```python
# -*- coding:utf-8 -*-

"""
@author:    tz_zs
"""

import numpy as np
import pandas as pd

list_l = [[11, 12, 13, 14, 15], [21, 22, 23, 24, 25], [31, 32, 33, 34, 35]]
date_range = pd.date_range(start="20180701", periods=3)
df = pd.DataFrame(list_l, index=date_range,
                  columns=['a', 'b', 'c', 'd', 'e'])
print(df)
"""
             a   b   c   d   e
2018-07-01  11  12  13  14  15
2018-07-02  21  22  23  24  25
2018-07-03  31  32  33  34  35
"""

df.to_csv("tzzs_data.csv")
"""
csv 文件内容：
,a,b,c,d,e
2018-07-01,11,12,13,14,15
2018-07-02,21,22,23,24,25
2018-07-03,31,32,33,34,35
"""
read_csv = pd.read_csv("tzzs_data.csv")
print(read_csv)
"""
   Unnamed: 0   a   b   c   d   e
0  2018-07-01  11  12  13  14  15
1  2018-07-02  21  22  23  24  25
2  2018-07-03  31  32  33  34  35
"""

df.to_csv("tzzs_data2.csv", index_label="index_label")
"""
csv 文件内容：
index_label,a,b,c,d,e
2018-07-01,11,12,13,14,15
2018-07-02,21,22,23,24,25
2018-07-03,31,32,33,34,35
"""

read_csv2 = pd.read_csv("tzzs_data2.csv")
print(read_csv2)
"""
  index_label   a   b   c   d   e
0  2018-07-01  11  12  13  14  15
1  2018-07-02  21  22  23  24  25
2  2018-07-03  31  32  33  34  35
```

————————————————
版权声明：本文为CSDN博主「tz_zs」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/tz_zs/java/article/details/81137998