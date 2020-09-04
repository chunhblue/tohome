## java拆分excel的多个sheet为多个独立的excel文件

 https://blog.csdn.net/liuhl0910/article/details/94779224 

```java
package com.liuhl.myexcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Hello world!
 *
 */
public class App {
    private static void copyFile(String srcPathStr, String desPathStr) {
        try {
            FileInputStream fis = new FileInputStream(srcPathStr);
            FileOutputStream fos = new FileOutputStream(desPathStr);
            byte datas[] = new byte[1024 * 8];
            int len = 0;
            while ((len = fis.read(datas)) != -1) {
                fos.write(datas, 0, len);
            }
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String filePath = "G:\\Work\\excel\\46.xlsx";
        // 第一步，创建一个webbook，对应一个Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
        int total = workbook.getNumberOfSheets();//3
        workbook.close();
        System.out.println(total);
        for (int i = 0; i < total; i++) {// 获取每个Sheet表
            String filePath2 = "G:\\Work\\excel\\46"+i+".xlsx";
            copyFile(filePath, filePath2);
            File file = new File(filePath2);
            XSSFWorkbook workbook2 = new XSSFWorkbook(new FileInputStream(file));
            int total2 = workbook2.getNumberOfSheets();
            for (int j = total2-1; j >= 0 ; j--) {
                if (i == j) {
                    continue;
                }
                workbook2.removeSheetAt(j);
            }
            String filePath3 = "G:\\Work\\excel\\46_"+i+".xlsx";
            FileOutputStream fout = new FileOutputStream(filePath3);
            workbook2.write(fout);
            workbook2.close();
            fout.close();
            file.delete();//删除文件
        }
        System.out.println("ok");
    }
}
```

