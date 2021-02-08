 判断是否为txt格式(根据扩展名来判断不太准确，这里根据文件头来判断)

```java
File toFile = new File("E:\pandian\Stocktake ice cream.txt");
boolean isText = true;
FileInputStream fin = new FileInputStream(toFile);
long len = toFile.length();
for (int j = 0; j < (int) len; j++) {
    int t = fin.read();
    if (t < 32 && t != 9 && t != 10 && t != 13) {
         isText = false;
         break;
       }
   }                
if (!isText) {
   System.out.println("Upload file format error!");
   }                
```