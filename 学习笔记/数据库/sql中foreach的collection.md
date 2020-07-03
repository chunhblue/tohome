# [foreach中collection的三种用法](https://www.cnblogs.com/xiemingjun/p/9800999.html)

转载：http://blog.sina.com.cn/s/blog_b0d90e8c0102v1q1.html

传参参考：http://www.cnblogs.com/ruiati/p/6410339.html

foreach的主要用在构建in条件中，它可以在SQL语句中进行迭代一个集合。

foreach元素的属性主要有 item，index，collection，open，separator，close。

  item表示集合中每一个元素进行迭代时的别名，
  index指 定一个名字，用于表示在迭代过程中，每次迭代到的位置，
  open表示该语句以什么开始，
  separator表示在每次进行迭代之间以什么符号作为分隔 符，
  close表示以什么结束。


在使用foreach的时候最关键的也是最容易出错的就是collection属性，该属性是必须指定的，但是在不同情况 下，该属性的值是不一样的，主要有一下3种情况：

  \1. 如果传入的是单参数且参数类型是一个List的时候，collection属性值为list
  \2. 如果传入的是单参数且参数类型是一个array数组的时候，collection的属性值为array
  \3. 如果传入的参数是多个的时候，我们就需要把它们封装成一个Map了，当然单参数也可

以封装成map，实际上如果你在传入参数的时候，在breast里面也是会把它封装成一个Map的，map的key就是参数名，所以这个时候collection属性值就是传入的List或array对象在自己封装的map里面的key 下面分别来看看上述三种情况的示例代码：
1.单参数List的类型：
 

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
1 <select id="dynamicForeachTest" parameterType="java.util.List" resultType="Blog">
2           select * from t_blog where id in
3        <foreach collection="list" index="index" item="item" open="(" separator="," close=")">
4                #{item}       
5        </foreach>    
6    </select>
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 


上述collection的值为list，对应的Mapper是这样的
public List dynamicForeachTest(List ids);
测试代码：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

![复制代码](https://common.cnblogs.com/images/copycode.gif)

```
 1 @Test
 2     public void dynamicForeachTest() {
 3         SqlSession session = Util.getSqlSessionFactory().openSession();      
 4         BlogMapper blogMapper = session.getMapper(BlogMapper.class);
 5          List ids = new ArrayList();
 6          ids.add(1);
 7          ids.add(3);
 8           ids.add(6);
 9         List blogs = blogMapper.dynamicForeachTest(ids);
10          for (Blog blog : blogs)
11              System.out.println(blog);
12          session.close();
13      }
```

![复制代码](https://common.cnblogs.com/images/copycode.gif)

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 


2.单参数array数组的类型：

```
1 <select id="dynamicForeach2Test" parameterType="java.util.ArrayList" resultType="Blog">
2     select * from t_blog where id in
3     <foreach collection="array" index="index" item="item" open="(" separator="," close=")">
4          #{item}
5     </foreach>
6 </select>    
```

 


上述collection为array，对应的Mapper代码：
public List dynamicForeach2Test(int[] ids);
对应的测试代码：
   

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

![复制代码](https://common.cnblogs.com/images/copycode.gif)

```
 1 @Test
 2 public void dynamicForeach2Test() {
 3         SqlSession session = Util.getSqlSessionFactory().openSession();
 4         BlogMapper blogMapper = session.getMapper(BlogMapper.class);
 5         int[] ids = new int[] {1,3,6,9};
 6         List blogs = blogMapper.dynamicForeach2Test(ids);
 7         for (Blog blog : blogs)
 8         System.out.println(blog);    
 9         session.close();
10 }
```

![复制代码](https://common.cnblogs.com/images/copycode.gif)

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 


3.自己把参数封装成Map的类型

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
1 <select id="dynamicForeach3Test" parameterType="java.util.HashMap" resultType="Blog">
2         select * from t_blog where title like "%"#{title}"%" and id in
3          <foreach collection="ids" index="index" item="item" open="(" separator="," close=")">
4               #{item}
5          </foreach>
6 </select>
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 


上述collection的值为ids，是传入的参数Map的key，对应的Mapper代码：
public List dynamicForeach3Test(Map params);
对应测试代码：
  

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

![复制代码](https://common.cnblogs.com/images/copycode.gif)

```
@Test
    public void dynamicForeach3Test() {
        SqlSession session = Util.getSqlSessionFactory().openSession();
         BlogMapper blogMapper = session.getMapper(BlogMapper.class);
          final List ids = new ArrayList();
          ids.add(1);
          ids.add(2);
          ids.add(3);
          ids.add(6);
         ids.add(7);
         ids.add(9);
        Map params = new HashMap();
         params.put("ids", ids);
         params.put("title", "中国");
        List blogs = blogMapper.dynamicForeach3Test(params);
         for (Blog blog : blogs)
             System.out.println(blog);
         session.close();
     }
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)