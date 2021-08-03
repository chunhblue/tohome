require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");

var _myAjax=require("myAjax");
define('bomSalePrint', function () {
    var self = {};
    var url_left = "",
		reThousands = null,
		toThousands = null,
		getThousands = null;
    var m = {
		toKen : null,
		use : null,
		main_box : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		// 栏位
		regionCd : null,
		cityCd : null,
		districtCd : null,
		storeCd : null,
		saleStartDate : null,
		saleEndDate : null,
		itemId : null,
    }

	// 创建js对象
    var  createJqueryObj = function(){
    	for (x in m) {
    		m[x] = $("#"+x);
		}
    }

    function init(_common) {
		createJqueryObj();
		url_left = _common.config.surl + "/bomSale";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		// 加载数据
		getList();
    }

	// 验证检索项是否合法
	var verifySearch = function(){
		var _startDate = m.saleStartDate.val();
		if(_startDate == null || $.trim(_startDate) == ''){
			return false;
		}
		var _endDate = m.saleEndDate.val();
		if(_endDate == null || $.trim(_endDate) == ''){
			return false;
		}
		if(_endDate < _startDate){
			return false;
		}
		// var temp = m.itemId.val();
		// if(temp!=null && $.trim(temp)!=''){
		// 	var reg = /^[0-9]*$/;
		// 	if(!reg.test(temp)){
		// 		return false;
		// 	}
		// }
		return true;
	}

	// 拼接检索参数
	var setParamJson = function(){
		// 创建请求字符串
		var searchJsonStr ={
			regionCd:m.regionCd.val(),
			cityCd:m.cityCd.val(),
			districtCd:m.districtCd.val(),
			storeCd:m.storeCd.val(),
			saleStartDate: m.saleStartDate.val(),
			saleEndDate: m.saleEndDate.val(),
			articleId:m.itemId.val(),
		};
		m.searchJson.val(JSON.stringify(searchJsonStr));
	}

	// 查询数据
    var getList = function(){
    	if(!verifySearch()){
			_common.prompt("Print data exception！",5,"info");/*打印数据异常*/
		}
		setParamJson();
		$.myAjaxs({
			url:url_left + "/getData",
			async:true,
			cache:false,
			type :"post",
			data :"searchJson="+m.searchJson.val(),
			dataType:"json",
			success:function(result){
				if(result.success){
					getTrVal(result.o);
					window.print();
				}else{
					_common.prompt("Failed to load report data!",5,"info");/*加载报表数据失败*/
				}
			},
			error : function(e){
				_common.prompt("Failed to load report data!",5,"info");/*加载报表数据失败*/
			}
		});
	}

	// 生成表格
	var getTrVal = function(data){
		if(data==null||data.length==0){return false;}
		// 拼接tr
		var _str = "<tr>" +
			"<th>Date</th>" +
			"<th>Store No.</th>" +
			"<th>Store Name</th>" +
			"<th>Barcode</th>" +
			"<th>Item Code</th>" +
			"<th>Item Name</th>" +
			"<th>UOM</th>" +
			"<th>Sales Quantity</th>" +
			"<th>Sales Amount(+VAT)</th>" +
			// "<th>Sales VAT Amount</th>" +
			// "<th>Returning Quantity</th>" +
			// "<th>Returning Amount(+VAT)</th>" +
			// "<th>Returning VAT Amount</th>" +
			/*"<th>GP</th>" +
			"<th>GP%</th>" +
			"<th>Daily Cost</th>" +*/
			"</tr>";
		// 计算求和
		var saleQtyTal = 0, saleCostTal = 0, saleAmtTal = 0, saleTaxTal = 0;
		var returnQtyTal = 0, returnAmtTal = 0, returnTaxTal = 0;
		for(var i=0; i<data.length; i++){
			var re = data[i];
			_str = _str + "<tr>" +
				"<td>" + fmtIntDate(re.accDate) + "</td>" +
				"<td>" + isEmpty(re.storeCd) + "</td>" +
				"<td>" + isEmpty(re.storeName) + "</td>" +
				"<td>" + isEmpty(re.barcode) + "</td>" +
				"<td>" + isEmpty(re.articleId) + "</td>" +
				"<td>" + isEmpty(re.articleName) + "</td>" +
				"<td>" + isEmpty(re.salesUnit) + "</td>" +
				"<td>"+ toThousands(re.saleQtyT) +"</td>" +
				"<td>" + toThousands(re.saleAmtT) + "</td>" +
				// "<td>" + toThousands(re.saleTaxT) + "</td>" +
				// "<td>" + toThousands(re.returnQtyT) + "</td>" +
				// "<td>" + toThousands(re.returnAmtT) + "</td>" +
				// "<td>" + toThousands(re.returnTaxT) + "</td>" +
				// "<td>" + toThousands(re.grossMargin) + "</td>" +
				// "<td>" + fmtRate(re.grossMarginRate) + "</td>" +
				// "<td>" + toThousands(re.avgCostNoTax) + "</td>" +
				"</tr>";
			saleCostTal = accAdd(saleCostTal, re.saleCostT);
			saleQtyTal = accAdd(saleQtyTal, re.saleQtyT);
			saleAmtTal = accAdd(saleAmtTal, re.saleAmtT);
			saleTaxTal = accAdd(saleTaxTal, re.saleTaxT);
			returnQtyTal = accAdd(returnQtyTal, re.returnQtyT);
			returnAmtTal = accAdd(returnAmtTal, re.returnAmtT);
			returnTaxTal = accAdd(returnTaxTal, re.returnTaxT);
		}
		// 合计行
		var grossMarginTal = accSub(saleAmtTal, saleCostTal);
		var grossMarginTalRate = accDiv(grossMarginTal, saleCostTal);
		var totalTr = "<tr>" +
			"<td></td>" +
			"<td>Total:</td>" +
			"<td></td>" +
			"<td></td>" +
			"<td></td>" +
			"<td></td>" +
			"<td></td>" +
			"<td>" + toThousands(saleQtyTal) + "</td>" +
			"<td>" + toThousands(saleAmtTal) + "</td>" +
			// "<td>" + toThousands(saleTaxTal) + "</td>" +
			// "<td>" + toThousands(returnQtyTal) + "</td>" +
			// "<td>" + toThousands(returnAmtTal) + "</td>" +
			// "<td>" + toThousands(returnTaxTal) + "</td>" +
			/*"<td>" + toThousands(grossMarginTal) + "</td>" +
			"<td>" + fmtRate(grossMarginTalRate) + "</td>" +*/
			// "<td></td>" +
			"</tr>";
		_str = _str + totalTr;
		// 打印信息设置
		$("#_printDate").text(getDate());
		$("#grid_table_p").empty();
		$("#grid_table_p").append(_str);
	}

	// 格式化百分比
	function fmtRate(rate){
		if(rate==null||rate==0){return "0%";}
		rate = Number(accMul(rate,100)).toFixed(2);
		return rate+"%";
	}

	// 格式化数字类型的日期：yyyymmdd → dd/mm/yyyy
	function fmtIntDate(date){
		if(date==null||date.length!=8){return "";}
		var res = "";
		res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		return res;
	}

	// 运算
	function accAdd(arg1,arg2){
		var r1,r2,m;
		try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
		try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
		m=Math.pow(10,Math.max(r1,r2));
		return (accMul(arg1,m)+accMul(arg2,m))/m;
	}
	function accSub(arg1,arg2){
		var r1,r2,m,n;
		try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
		try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
		m=Math.pow(10,Math.max(r1,r2));
		return accMul(arg1,m)-accMul(arg2,m);
	}
	function accMul(arg1,arg2){
		var m=0,s1=arg1.toString(),s2=arg2.toString();
		try{m+=s1.split(".")[1].length}catch(e){}
		try{m+=s2.split(".")[1].length}catch(e){}
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
	}
	function accDiv(arg1,arg2){
		var t1=0,t2=0,r1,r2;
		try{t1=arg1.toString().split(".")[1].length}catch(e){}
		try{t2=arg2.toString().split(".")[1].length}catch(e){}
		with(Math){
			r1=Number(arg1.toString().replace(".",""))
			r2=Number(arg2.toString().replace(".",""))
			return (r1/r2)*pow(10,t2-t1);
		}
	}
	var isEmpty = function (str) {
		if (str == null || str == undefined || str == '') {
			return '';
		}
		return str;
	};
	// 获取当前日期
	function getDate(){
		var mydate = new Date();
		var str = "" + check(mydate.getDate()) + "/";
		str += check((mydate.getMonth()+1)) + "/";
		str += mydate.getFullYear();
		return str;
	}

	// 补0
	function check(num){
		num = num < 10 ? '0'+num : num;
		return num;
	}

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('bomSalePrint');
_start.load(function (_common) {
	_index.init(_common);
});
