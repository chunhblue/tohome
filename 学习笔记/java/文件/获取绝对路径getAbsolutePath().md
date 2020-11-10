## getPath()与getAbsolutePath()的区别

```java
public class TextPath {
	public static void main(String[] args) {
		File file = new File("workspace\\test\\test1.txt"); 
		//取得相对路径
		System.out.println("Path: " + file.getPath()); 
		//取得绝对路径
		System.out.println("getAbsolutePath: " + file.getAbsolutePath()); 
 
	}
}
```

测试结果：

![](D:\heads ref\tohome\学习笔记\java\文件\测试结果.png)

