 splice() 方法向/从数组中添加/删除项目，然后返回被删除的项目。 

```javascript
// 语法
arrayObject.splice(index,howmany,item1,.....,itemX)
```

- splice() 方法可删除从 index 处开始的零个或多个元素，并且用参数列表中声明的一个或多个值来替换那些被删除的元素。

- 如果从 arrayObject 中删除了元素，则返回的是含有被删除的元素的数组。

  ![](D:\heads ref\tohome\学习笔记\java\javaScript\spilce().jpg)

```javascript
var arr = new Array(6)
arr[0] = "George"
arr[1] = "John"
arr[2] = "Thomas"
arr[3] = "James"
arr[4] = "Adrew"
arr[5] = "Martin"

document.write(arr + "<br />")
arr.splice(2,1,"baby")
document.write(arr)
// ---------------------------------------------
George,John,Thomas,James,Adrew,Martin
George,John,baby,James,Adrew,Martin
```

**第一个数是删除元素的索引，第二个数代表被删除的元素的个数，第三个代表要添加进数组的元素**

```javascript
var arr = new Array(6)
arr[0] = "George"
arr[1] = "John"
arr[2] = "Thomas"
arr[3] = "James"
arr[4] = "Adrew"
arr[5] = "Martin"

document.write(arr + "<br />")
arr.splice(2,1)
document.write(arr)
// ---------------------------------------------
George,John,Thomas,James,Adrew,Martin
George,John,James,Adrew,Martin
```

