Optional int parameter 'page' is present but cannot be translated into a null value due to being declared as a primitive type. Consider declaring it as object wrapper for the corresponding primitive type.

 存在可选的int参数'page'，但由于声明为基本类型，所以不能转换为空值。考虑将其声明为对应原语类型的对象包装器。 

：js里面格式转换有问题



 **ajax请求中还要一个重要的参数： contentType: "application/json",表示传入参数的格式** 

JSON.stringify({})

```javascript
var saveAlbumJson = function () {
          
            $.ajax(
            {
                url: "/Home/PostAlbum",
                type: "POST",
                contentType: "application/json",
                dataType:"json",
                data: JSON.stringify({ "AlbumName": "shanghai", "Entered": "5/9/2013" }),
                success: function (result) {
                    alert(result);
                },
                error: function (xhr, status, p3, p4) {
                    var err = "Error " + " " + status + " " + p3;
                    if (xhr.responseText && xhr.responseText[0] == "{")
                        err = JSON.parse(xhr.responseText).message;
                    alert(err);
                }
            });
        }
```

