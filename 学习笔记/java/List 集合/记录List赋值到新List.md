## 记录List赋值到新List时，对象值改变，新List内值也改变处理方法

 List中值为对象，并非基础数据类型，这些对象对应的内存中地址是一致的，单据改变后，内存地址并没有改变，所以导致保存的原单据值也是同样改变了 

 解决方案，使用BeanUtils.clonebean(Object object) ，解决了该问题 

```
<dependency>
    <groupId>commons-beanutils</groupId>
    <artifactId>commons-beanutils</artifactId>
    <version>1.8.2</version>
</dependency>
```

```java
// 原list集合
List<StocktakeItemDTO> list = new ArrayList<>();
List<StocktakeItemDTO> allList = new ArrayList<>();
  for(StocktakeItemDTO dto:list){
       // 自定义类 可以先克隆出自定义类，再把它赋值给新的类，来覆盖原来类的引用
       // 调用了getPropertyUtils().copyProperties(newBean, bean);基础类实际上还是复制的引用
        allList.add((StocktakeItemDTO) BeanUtils.cloneBean(dto));
     }
//   之后是对原List的赋值操作...
```

https://www.iteye.com/blog/renxiangzyq-551430

BeanUtils.clonebean(Object object) ，该链接测试了该方法对基础类型的类无法进行深克隆.

BeanUtils是属性copy，深copy用对象串行化的方法吧