**当js里面和controller的参数名不相同时，使用此注解使之对应**

@RequestParam

```java
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default true;

    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
```

与js里的参数名一一对应 

例子：

```java
@ResponseBody
@RequestMapping(value = "/getDOCList")
public AjaxResultDto getDOCList(HttpServletRequest request, HttpSession session,                                @RequestParam("tstore")String storeCd, @RequestParam("vstore")String storeCd1) {

}
```

```javascript
var getSelectDOC1 = function(vstore,tstore,val){

}
```

