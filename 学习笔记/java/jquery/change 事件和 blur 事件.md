## [input 输入框 change 事件和 blur 事件]

输入框的 change 和 blur 事件绝大多数情况下表现是一致的，输入结束后离开输入框会先后触发 change 和 blur。那么这两个事件的区别在哪呢？

当文本框获得焦点后，没有输入任何内容，或者最终文本框的值没有改变时，是不会触发 change 事件的，而 blur 事件始终会触发。如果希望文本框的值一发生改变就立马执行某些操作，而不是等到离开再执行，那么可以使用 keyup 事件。

```javascript
$("#userId").on("change blur",function(){
// 这里可以执行某些操作
}
```

