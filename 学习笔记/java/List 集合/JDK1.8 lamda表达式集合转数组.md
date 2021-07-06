```java
List<String> emails = new ArrayList<>();
// 获取数组sendEmails[]
String[] sendEmails = emails.stream().filter(email -> StringUtils.isNotBlank(email)).toArray(String[]::new);
```

5.List排序

要对List中的对象进行排序以前非常麻烦，什么对象实现Comparable接口啊，写一个StudentComparator实现Comparator接口呀，非常麻烦，现在非常简单一行代码搞定(两种方式)：

list.sort(Comparator.comparing(Student::getName)); //按名字排序

list.sort((p1,p2) -> { return p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase()); });//lambda表达
————————————————
版权声明：本文为CSDN博主「丰语者」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/weixin_30145703/article/details/114133043