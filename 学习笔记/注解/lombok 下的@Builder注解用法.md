 https://blog.csdn.net/qq_35568099/article/details/80438538 

 https://www.cnblogs.com/lori/p/9024933.html 

 pom依赖  

```
<dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>0.10.2</version>
 </dependency>
```

@Builder声明实体，表示可以进行Builder方式初始化，@Value注解，表示只公开getter，对所有属性的setter都封闭，即private修饰，所以它不能和@Builder现起用

一般地，我们可以这样设计实体！

```
@Builder(toBuilder = true)
@Getter
public class UserInfo {
  private String name;
  private String email;
  @MinMoney(message = "金额不能小于0.")
  @MaxMoney(value = 10, message = "金额不能大于10.")
  private Money price;

}
```

 @Builder注解赋值新对象 

```
UserInfo userInfo = UserInfo.builder()
        .name("zzl")
        .email("bgood@sina.com")
        .build();
```

@Builder注解修改原对象的属性值

修改实体，要求实体上添加@Builder(toBuilder=true)

```
userInfo = userInfo.toBuilder()
        .name("OK")
        .email("zgood@sina.com")
        .build();
```

