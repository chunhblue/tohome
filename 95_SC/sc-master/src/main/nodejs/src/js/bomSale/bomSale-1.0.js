require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('bomSale', function () {
    var self = {};
    var url_left = "",
		url_root = "",
	    paramGrid = null,
		item = null,
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
		aStore : null,
		saleStartDate : null,
		saleEndDate : null,
		item : null,
		// 按钮
		reset : null,
		search : null,
		print : null,
		export : null,
    }
	// 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
		}
    }
    function init(_common) {
    	createJqueryObj();
		url_root = _common.config.surl;
    	url_left = url_root + "/bomSale";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
    	// 首先验证当前操作人的权限是否混乱，
		if(m.use.val()=="0"){
			but_event();
		}else{
			m.error_pcode.show();
			m.main_box.hide();
		}
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// // 初始化店铺运营组织检索
		// _common.initOrganization();
		// 初始化店铺名称下拉
		_common.initStoreform();
		initAutoMatic();
		//权限验证
		isButPermission();
		// 初始化检索日期
		_common.initDate(m.saleStartDate,m.saleEndDate);
		freeTable();
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
    	switch(flgType) {
			case "1":
				break;
			case "2":
			   break;
			case "3":
				break;
			case "4":
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
    }

	//初始化下拉
	var initAutoMatic = function () {
		// 获取 Bom 商品数据
		// item=$("#item").myAutomatic({
		// 	url: url_left + "/getBomItemList",
		// 	ePageSize:5,
		// 	startCount:0,
		// })
	}
    
    // 画面按钮点击事件
    var but_event = function(){
    	// 重置按钮点击事件
    	m.reset.on("click",function(){
			$("#aStore").css("border-color","#CCC");
			$("#saleStartDate").css("border-color","#CCC");
			$("#saleEndDate").css("border-color","#CCC");
			$("#storeRemove").click();
			$("#itemRemove").click();
			m.saleStartDate.val("");
			m.saleEndDate.val("");
			$("#grid_table").empty();
    	});
    	// 检索按钮点击事件
    	m.search.on("click",function(){

    		if(verifySearch()){
				_common.loading();
				// 拼接检索参数
				setParamJson();
				// 查询数据
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
						}else{
							$("#grid_table").empty();
							_common.prompt(result.msg,5,"info");
						}
						_common.loading_close();
					},

				error : function(e){
					_common.loading_close();
						_common.prompt("Query failed!",5,"info");/*查询销售数据失败*/
					}
				});
    		}
    	});


    	// 打印按钮事件
		m.print.on("click",function () {
			if( $("table tr:visible").length < 1){
				_common.prompt("There is no printable data!",5,"info");/*没有可打印的数据*/
				return false;
			}
			if(!verifySearch()){return false;}
			var regionCd = $("#aRegion").attr("k");
			var cityCd = $("#aCity").attr("k");
			var districtCd = $("#aDistrict").attr("k");
			var storeCd = $("#aStore").attr("k");
			var regionName = $("#aRegion").attr("v");
			var cityName = $("#aCity").attr("v");
			var districtName = $("#aDistrict").attr("v");
			var storeName = $("#aStore").attr("v");
			var saleStartDate = fmtStringDate(m.saleStartDate.val());
			var saleEndDate = fmtStringDate(m.saleEndDate.val());
			var articleId = m.item.val();
			var _param = "regionCd=" + regionCd + "&cityCd=" + cityCd + "&districtCd=" + districtCd +
				"&storeCd=" + storeCd + "&regionName=" + regionName + "&cityName=" + cityName + "&districtName=" +
				districtName +"&storeName=" + storeName +
				"&saleStartDate=" + saleStartDate +
				"&saleEndDate=" + saleEndDate + "&articleId=" + articleId;
			var url = url_left +"/print?" + _param;
			window.open(encodeURI(url),"BOM Sales Report",'top=10,height=600,width=2000px,toolbar=no,menubar=yes,scrollbars=yes, resizable=yes,location=no, status=no,center:yes');
		});
    	// 导出按钮事件
		m.export.on("click",function () {
			if(verifySearch()){
				// 拼接检索参数
				setParamJson();
				paramGrid = "searchJson=" + m.searchJson.val();
				var url = url_left + "/export?" + paramGrid;
				window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
			}
		});


    };

    // 生成表格
    var getTrVal = function(data){
		if(data==null||data.length==0){return false;}
		// let trList = $("#grid_table  tr:not(:first)");
		// trList.remove();
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
			// "<th>GP</th>" +
			// "<th>GP%</th>" +
			// "<th>Daily Cost%</th>" +
			"</tr>";
		// 计算求和
		var saleQtyTal = 0, saleCostTal = 0, saleAmtTal = 0, saleTaxTal = 0;
		var returnQtyTal = 0, returnAmtTal = 0, returnTaxTal = 0;
		for(var i=0; i<data.length; i++){
			var re = data[i];
			  _str = _str + "<tr>" +
				"<td>" + fmtIntDate(re.accDate) + "</td>" +
				"<td>" + isEmpty(re.storeCd) + "</td>" +
				"<td  style='text-align:left'>" + isEmpty(re.storeName) + "</td>" +
				"<td>" + isEmpty(re.barcode) + "</td>" +
				"<td>" + isEmpty(re.articleId) + "</td>" +
				"<td  style='text-align:left'>" + isEmpty(re.articleName) + "</td>" +
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
			// $("#grid_table").append(_str);
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
			// "<td>" + toThousands(grossMarginTal) + "</td>" +
			// "<td>" + fmtRate(grossMarginTalRate) + "</td>" +
			// "<td></td>" +
			"</tr>";
		_str = _str + totalTr;
		$("#grid_table").empty();
		$("#grid_table").append(_str);
		freeTable();
	};


	/**
	 * 冻结列的绑定方法
	 */
	function freeTable(){
		var table = $("#grid_table");                                //获取当前table
		var tableId = table.attr('id');                        //table的ID之后作为参数传递
		var freezeRowNum = table.attr('freezeRowNum');        //获取页面table定义的冻结行和列
		var freezeColumnNum = table.attr('freezeColumnNum');

			freezeTable(table, freezeRowNum,  freezeColumnNum, pageWidth(), pageHeight()-200);

			var flag = false;
			$(window).resize(function() {
				if (flag)
					return ;

				setTimeout(function() {
					adjustTableSize(tableId, pageWidth(), pageHeight()-200);
					flag = false;
				}, 100);

				flag = true;
			});
	}

	/*
	 * 锁定表头和列
	 *
	 * 参数定义
	 *   table - 要锁定的表格元素或者表格ID
	 *   freezeRowNum - 要锁定的前几行行数，如果行不锁定，则设置为0
	 *   freezeColumnNum - 要锁定的前几列列数，如果列不锁定，则设置为0
	 *   width - 表格的滚动区域宽度
	 *   height - 表格的滚动区域高度
	 */
	function freezeTable(table, freezeRowNum, freezeColumnNum, width, height) {

		//获取冻结行数或者列数
		if (typeof(freezeRowNum) == 'string')
			freezeRowNum = parseInt(freezeRowNum)

		//获取table
		var tableId;
		if (typeof(table) == 'string') {
			tableId = table;
			table = $('#' + tableId);
		} else
			tableId = table.attr('id');

		/**
		 * 生成最外层的DIV用来承载内部的四个DIV
		 */
		var divTableLayout = $("#" + tableId + "_tableLayout");

		if (divTableLayout.length != 0) {
			divTableLayout.before(table);
			divTableLayout.empty();
		} else {
			table.after("<div id='" + tableId + "_tableLayout' style='overflow:hidden;height:" + height + "px; width:" + width + "px;'></div>");

			divTableLayout = $("#" + tableId + "_tableLayout");
		}

		/**
		 * 根据需要页面table定义的属性 需要冻结的行或者列 ，对应的拼接字符串用于生成不同的DIV  【如果行列同时冻结：生成总共四个DIV】【单独冻结行或列，仅需要生成两个DIV】
		 * 这个四个DIV都是包括数据在内，完全克隆了原本的table，
		 */
		var html = '';
		if (freezeRowNum > 0 && freezeColumnNum > 0)
			html += '<div id="' + tableId + '_tableFix" style="padding: 0px;"></div>';

		if (freezeRowNum > 0)
			html += '<div id="' + tableId + '_tableHead" style="padding: 0px;"></div>';

		html += '<div id="' + tableId + '_tableData" style="padding: 0px;"></div>';

		//将div追加到页面
		$(html).appendTo("#" + tableId + "_tableLayout");

		var divTableFix = freezeRowNum > 0 && freezeColumnNum > 0 ? $("#" + tableId + "_tableFix") : null;
		var divTableHead = freezeRowNum > 0 ? $("#" + tableId + "_tableHead") : null;
		var divTableData = $("#" + tableId + "_tableData"); //【数据DIV】【第一个DIV，也就是原本的那个真身】

		divTableData.append(table);
		//行列同时冻结生成的【行列DIV】，一般位于左上角重叠部分
		if (divTableFix != null) {
			var tableFixClone = table.clone(true);    //克隆1
			tableFixClone.attr("id", tableId + "_tableFixClone");
			divTableFix.append(tableFixClone);
		}
		//行冻结生成的【冻结行DIV】
		if (divTableHead != null) {
			var tableHeadClone = table.clone(true);//克隆2
			tableHeadClone.attr("id", tableId + "_tableHeadClone");
			divTableHead.append(tableHeadClone);
		}


		$("#" + tableId + "_tableLayout table").css("margin", "0");

		/**
		 * 根据冻结行数，设置行冻结的div的高度【如果行列同时冻结，则行列div也设置对应高度】
		 */
		if (freezeRowNum > 0) {
			var HeadHeight = 0;
			var ignoreRowNum = 0;
			$("#" + tableId + "_tableHead tr:lt(" + freezeRowNum + ")").each(function () {
				if (ignoreRowNum > 0)
					ignoreRowNum--;
				else {
					var td = $(this).find('td:first, th:first');
					HeadHeight += td.outerHeight(true);

					ignoreRowNum = td.attr('rowSpan');
					if (typeof(ignoreRowNum) == 'undefined')
						ignoreRowNum = 0;
					else
						ignoreRowNum = parseInt(ignoreRowNum) - 1;
				}
			});
			HeadHeight += 2;

			divTableHead.css("height", HeadHeight);
			divTableFix != null && divTableFix.css("height", HeadHeight);
		}



		//滚动
		divTableData.scroll(function () {
			divTableHead != null && divTableHead.scrollLeft(divTableData.scrollLeft());
		});

		divTableFix != null && divTableFix.css({ "overflow": "hidden", "position": "absolute", "z-index": "50" });
		divTableHead != null && divTableHead.css({ "overflow": "hidden",  "position": "absolute", "z-index": "45" });
		divTableData.css({ "overflow": "scroll", "width": width, "height": height, "position": "absolute" });

		divTableFix != null && divTableFix.offset(divTableLayout.offset());
		divTableHead != null && divTableHead.offset(divTableLayout.offset());
		divTableData.offset(divTableLayout.offset());
	}

	/*
	 * 调整锁定表的宽度和高度，这个函数在resize事件中调用
	 *
	 * 参数定义
	 *   table - 要锁定的表格元素或者表格ID
	 *   width - 表格的滚动区域宽度
	 *   height - 表格的滚动区域高度
	 */
	function adjustTableSize(table, width, height) {
		var tableId;
		if (typeof(table) == 'string')
			tableId = table;
		else
			tableId = table.attr('id');

		$("#" + tableId + "_tableLayout").width(width).height(height);
		$("#" + tableId + "_tableHead").width(width);
		$("#" + tableId + "_tableData").width(width).height(height);
		$("#grid_table  th").css({"padding":"8px","font-size":"13px"})
		$("#grid_table  td").css({"padding":"8px","font-size":"12px"})
	}

	//返回页面的高度
	function pageHeight() {
		if ( /msie/.test(navigator.userAgent.toLowerCase())) {
			return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight : document.body.clientHeight;
		} else {
			return innerHeight;
		}
	};

	//返回当前页面宽度
	function pageWidth() {
		if ( /msie/.test(navigator.userAgent.toLowerCase())) {
			return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth : document.body.clientWidth;
		} else {
			return innerWidth;
		}
	};


	function judgeNaN (value) {
		return value !== value;
	}

	var isEmpty = function (str) {
		if (str == null || str == undefined || str == '') {
			return '';
		}
		return str;
	};
    
    // 验证检索项是否合法
    var verifySearch = function(){
		let _storeCd = m.aStore.attr('k');
		if (!_storeCd) {
			$("#aStore").focus();
			$("#aStore").css("border-color","red");
			_common.prompt("Please select a store first!",3,"info");/*请选择商店！*/;
			return false;
		}else {
			$("#aStore").css("border-color","#CCC");
		}
		let _StartDate = null;
		if(!$("#saleStartDate").val()){
			_common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
			$("#saleStartDate").css("border-color","red");
			$("#saleStartDate").focus();
			return false;
		}else{
			_StartDate = new Date(fmtDate($("#saleStartDate").val())).getTime();
			if(judgeNaN(_StartDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#saleStartDate").focus();
				$("#saleStartDate").css("border-color","red");
				return false;
			}else {
				$("#saleStartDate").css("border-color","#CCC");
			}
			$("#saleStartDate").css("border-color","#CCC");
		}
		let _EndDate = null;
		if(!$("#saleEndDate").val()){
			_common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
			$("#saleEndDate").css("border-color","red");
			$("#saleEndDate").focus();
			return false;
		}else{
			_EndDate = new Date(fmtDate($("#saleEndDate").val())).getTime();
			if(judgeNaN(_EndDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#saleEndDate").css("border-color","red");
				$("#saleEndDate").focus();
				return false;
			}else {
				$("#saleEndDate").css("border-color","#CCC");
			}
			$("#saleEndDate").css("border-color","#CCC");
		}
		if(_StartDate>_EndDate){
			$("#saleEndDate").focus();
			_common.prompt("The start date cannot be greater than the end date!",5,"error");/*开始时间不能大于结束时间*/
			return false;
		}
		let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
		if(difValue>62){
			_common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
			$("#saleEndDate").focus();
			return false;
		}
    	return true;
    }
    
    // 拼接检索参数
    var setParamJson = function(){
    	// 日期格式转换
		var _startDate = fmtStringDate(m.saleStartDate.val())||null;
		var _endDate = fmtStringDate(m.saleEndDate.val())||null;
 		// 创建请求字符串
 		var searchJsonStr ={
			regionCd:$("#aRegion").attr("k"),
			cityCd:$("#aCity").attr("k"),
			districtCd:$("#aDistrict").attr("k"),
			storeCd:$("#aStore").attr("k"),
			saleStartDate: _startDate,
			saleEndDate: _endDate,
			articleId:m.item.val(),
 		};
 		m.searchJson.val(JSON.stringify(searchJsonStr));
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

	// 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
	function fmtStringDate(date){
		var res = "";
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
		return res;
	}
	// DD/MM/YYYY to YYYY-MM-DD  格式转换
	function fmtDate(date) {
		var res = '';
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
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
		let result = Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
		if (!isFinite(result)) {
			result = 0;
		}
		return result;
	}
	function accDiv(arg1,arg2){
		var t1=0,t2=0,r1,r2;
		try{t1=arg1.toString().split(".")[1].length}catch(e){}
		try{t2=arg2.toString().split(".")[1].length}catch(e){}
		with(Math){
			r1=Number(arg1.toString().replace(".",""))
			r2=Number(arg2.toString().replace(".",""))
			let r3 = !isFinite(r1/r2)? 0 : r1/r2;
			return (r3)*pow(10,t2-t1);
		}
	}
	// 按钮权限验证
	var isButPermission = function () {
		var printBut = $("#printBut").val();
		if (Number(printBut) != 1) {
			$("#print").remove();
		}
	};

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('bomSale');
_start.load(function (_common) {
	_index.init(_common);
});
