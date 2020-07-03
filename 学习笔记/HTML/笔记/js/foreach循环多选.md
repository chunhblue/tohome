```javascript
// 多选状态
trs.forEach(function (item) {   
let cols = pmaTableGrid.getSelectColValue(item,'pmaCd');   
$("#zgGridTtable>.zgGrid-tbody tr").each(function () {     
	let _pmaCd = $(this).find('td[tag=pmaCd]').text();     
	if (_pmaCd == cols['pmaCd']) {         
	_addFlg = true;        
	return false; // 结束遍历      
	}   
});
})
```

Stocking --> Stocktake Plan Setting画面

