### [js正则表达式校验正数、负数、和小数：^(\-|\+)?\d+(\.\d+)?$](https://www.cnblogs.com/lengzhan/p/6042309.html)



```html
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <script type="text/javascript">
        function validation() {
            var val = document.getElementById("txtNumber").value;
            var regu = /^(\-|\+)?\d+(\.\d+)?$/;
            if (val != "") {
                if (!regu.test(val)) {
                    document.getElementById("labResult").style.color = "red";
                    document.getElementById("labResult").innerHTML = "验证失败！";
                } else {
                    document.getElementById("labResult").style.color = "green";
                    document.getElementById("labResult").innerHTML = "验证成功！";
                }
            }
        }
    </script>
</head>
<body>
    <input id="txtNumber" name="txtNumber" type="text" />
    <input id="btnValidation" name="btnValidation" type="button" value="校验" onclick="validation()" />
    验证结果：<label id="labResult" ></label>
</body>
</html>
    
```

![img](https://images2015.cnblogs.com/blog/525760/201611/525760-20161108111945608-1213807272.png)

![img](https://images2015.cnblogs.com/blog/525760/201611/525760-20161108112041717-198740231.png)

![img](https://images2015.cnblogs.com/blog/525760/201611/525760-20161108112105624-1480287933.png)

![img](https://images2015.cnblogs.com/blog/525760/201611/525760-20161108112126170-1737252035.png)

