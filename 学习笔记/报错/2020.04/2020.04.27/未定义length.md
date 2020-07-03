### Uncaught TypeError: Cannot read property ‘length’ of undefined

 1，问题原因：是$.each()函数执行报的错，在执行each时，传入的参数为undefined
或为null时也会有此错误’length’ of null
2，解决方案：在each前先判断是否为undefined或者是否为空，如是则不遍历就不会js报错了。 

在 stocktake_process / stocktakeProcessPrint-1.0.js 里面遇到的，



可以先console.log一下传入的参数。

