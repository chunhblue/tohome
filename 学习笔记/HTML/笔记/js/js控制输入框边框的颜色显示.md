输入框变红色

```javascript
$("#id").css("border-color","red");
```

## **当输入框内容为空的时候，让内容为空的输入框，边框设为红色，同时再次获得焦点是，恢复原来颜色**

```css
css:
<style type="text/css">
       .redBorder{
           border:#ff0000 1px solid
       }#000000
       .blackBorder{
        border:#000000 1px solid
       }
    </style>

```

```html
html:
<table id="tabID" >
  <tbody id="specialBudgetInfoList">
<tr>
<td>
<input type='text' id='testID' name='test' style='text-align: right' οnfοcus='blackBorder(this)'/>
</td>
</tr>
</tbody>
</table>

```

```javascript
js: /**
  * 检查输入值是否填完整
  */
  function checkInputsValue(id){
  var tbodyNode = document.getElementById(id);
  var flag = true;
  var inputNodes = tbodyNode.getElementsByTagName("input");
  for(var i=0;i<inputNodes.length;i++){
 
  if(inputNodes[i].type == 'text'){
  //将input标签的type为text，并且值为空的，设置边框为红色
  if(inputNodes[i].value == " " || inputNodes[i].value == ""){
  inputNodes[i].className = "redBorder";  //方框设为红色，默认是为黑色的
  flag = false;
  }
  }
  }
  return flag;
  }
 
  //黑色边框
  function blackBorder(obj){
  obj.className = "blackBorder";
  }
//提交方法
function submitInfo(){
if(checkInputsValue("specialBudgetInfoList") == false){
alert("请输入内容");
return;
}
}

```

————————————————
版权声明：本文为CSDN博主「Jackson-Huang」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/demon_zero/java/article/details/41648941