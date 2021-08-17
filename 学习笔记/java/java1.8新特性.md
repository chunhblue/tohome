https://blog.csdn.net/qq_29411737/article/details/80835658

 https://www.bilibili.com/video/BV1ut411g7E9?from=search&seid=9725022821599982645  (听课)

Lambda 表达式

函数式接口

方法引用

Stream API

并行流和串行流

ForkJoin框架

Optional 容器

接口中可以定义默认实现方法和静态方法

```java
// 在接口中可以使用default和static关键字来修饰接口中定义的普通方法
public interface Interface {
    default  String getName(){
        return "zhangsan";
    }
}
```

新的日期API  LocalDate | LocalTime | LocalDateTime

-  新的日期API都是不可变的，更使用于多线程的使用环境中 

