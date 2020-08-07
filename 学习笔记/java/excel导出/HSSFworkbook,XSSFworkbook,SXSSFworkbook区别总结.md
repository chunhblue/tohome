## HSSFworkbook,XSSFworkbook,SXSSFworkbook区别总结

1. HSSFWorkbook:是操作Excel2003以前（包括2003）的版本，扩展名是.xls；
2. XSSFWorkbook:是操作Excel2007后的版本，扩展名是.xlsx；
3. SXSSFWorkbook:是操作Excel2007后的版本，扩展名是.xlsx；



第一种：HSSFWorkbook

poi导出excel最常用的方式；但是此种方式的局限就是导出的行数至多为65535行，超出65536条后系统就会报错。此方式因为行数不足七万行所以一般不会发生内存不足的情况（OOM）。

 

第二种：XSSFWorkbook

这种形式的出现是为了突破HSSFWorkbook的65535行局限。其对应的是excel2007(1048576行，16384列)扩展名为“.xlsx”，最多可以导出104万行，不过这样就伴随着一个问题---OOM内存溢出，原因是你所创建的book sheet row cell等此时是存在内存的并没有持久化。

 

第三种：SXSSFWorkbook

从POI 3.8版本开始，提供了一种基于XSSF的低内存占用的SXSSF方式。对于大型excel文件的创建，一个关键问题就是，要确保不会内存溢出。其实，就算生成很小的excel（比如几Mb），它用掉的内存是远大于excel文件实际的size的。如果单元格还有各种格式（比如，加粗，背景标红之类的），那它占用的内存就更多了。对于大型excel的创建且不会内存溢出的，就只有SXSSFWorkbook了。它的原理很简单，用硬盘空间换内存（就像hash map用空间换时间一样）。

SXSSFWorkbook是streaming版本的XSSFWorkbook,它只会保存最新的excel rows在内存里供查看，在此之前的excel rows都会被写入到硬盘里（Windows电脑的话，是写入到C盘根目录下的temp文件夹）。被写入到硬盘里的rows是不可见的/不可访问的。只有还保存在内存里的才可以被访问到。

SXSSF与XSSF的对比：

a. 在一个时间点上，只可以访问一定数量的数据

b. 不再支持Sheet.clone()

c. 不再支持公式的求值

d. 在使用Excel模板下载数据时将不能动态改变表头，因为这种方式已经提前把excel写到硬盘的了就不能再改了

 

当数据量超出65536条后，在使用HSSFWorkbook或XSSFWorkbook，程序会报OutOfMemoryError：Javaheap space;内存溢出错误。这时应该用SXSSFworkbook。