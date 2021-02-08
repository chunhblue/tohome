java 取交集方法retainAll

有两个集合newCoures和oldCourses，判断这两个集合是否包含相同的对象或元素，可以使用retainAll方法：

```
oldCourses.retainAll(newCoures)。
```

1. 如果存在相同元素，oldCourses中仅保留相同的元素。
2. 如果不存在相同元素，oldCourse会变为空。

如果有多个集合oldCourses1、oldCourses2、oldCourses3等，分别与newCourses比较，应该将newCourses统一放在后面，像这样：

```
oldCourses1.retainAll(newCoures);

oldCourses2.retainAll(newCoures);

oldCourses3.retainAll(newCoures);
```

