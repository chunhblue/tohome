```java
Enumeration em = request.getSession().getAttributeNames();  //得到session中所有的属性名
while (em.hasMoreElements()) {      request.getSession().removeAttribute(em.nextElement().toString()); //遍历删除session中的值
}
```

session.removeAttribute("sessionname")是清除SESSION里的某个属性.
session.invalidate()是让SESSION失效.

或许你可以用getAttributeNames来得到所有属性名,然后再removeAttribute 

