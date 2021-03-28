require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('maItemSaleEdit', function () {
	var self = {};
	var url_left = "",
		systemPath = "",
		paramGrid = null,
		selectTrTemp = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		tempTrObjValue = {},//临时行数据存储
		common=null;
	const KEY = 'MATERIAL_ITEM_SALES_QUERY';
	var m = {
		reset: null,
		voucherCd:null,//单据编号 隐藏
		voucher_cd:null,//单据编号
		store_name:null,//店铺名称
		sale_date:null,//销售日期
		barcode:null,//barcode
		formulaArticleId:null,//商品id
		formulaArticleName:null,//商品名称
		tranSerialNo:null,//单号
		posId:null,//pos Id
		saleQty:null,//销售数量
		saleAmt:null,//销售金额
		returnsViewBut : null,//返回
	}
	// 创建js对象
	var  createJqueryObj = function(){
		for (x in m)  {
			m[x] = $("#"+x);
		}
	}
	function init(_common) {
		createJqueryObj();
		url_left =_common.config.surl+"/maItemSale";
		systemPath = _common.config.surl;
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		//事件绑定
		but_event();
		//列表初始化
		initTable();
		//加载头档数据
		dataInitByVoucherCd();
	}

	//画面按钮点击事件
	var but_event = function(){
		//返回一览
		m.returnsViewBut.on("click",function(){
			_common.updateBackFlg(KEY);
			top.location = url_left;
		});
	}

	//页面数据赋值
	var dataInitByVoucherCd = function () {
		$.myAjaxs({
			url:url_left+"/getMa4350",
			async:true,
			cache:false,
			type :"post",
			data :"voucherCd="+m.voucherCd.val(),
			dataType:"json",
			success:function(result){
				if(result.success){
					var ma4350Dto = result.data;
					//赋值配方销售单头档基本信息
					m.voucher_cd.val(ma4350Dto.voucherCd);
					m.store_name.val(ma4350Dto.storeName);
					m.sale_date.val(fmtIntDate(ma4350Dto.saleDate));
					m.barcode.val(ma4350Dto.barcode);
					m.formulaArticleId.val(ma4350Dto.formulaArticleId);
					m.formulaArticleName.val(ma4350Dto.articleName);
					m.tranSerialNo.val(ma4350Dto.tranSerialNo);
					m.posId.val(ma4350Dto.posId);
					m.saleQty.val(toThousands(ma4350Dto.saleQty));
					m.saleAmt.val(toThousands(ma4350Dto.saleAmt));
					//查询配方销售单商品明细信息
					paramGrid = "voucherCd="+m.voucherCd.val();
					tableGrid.setting("url", url_left + "/getDetailList");
					tableGrid.setting("param", paramGrid);
					tableGrid.loadData(null);
				}else{
					_common.prompt(result.message,5,"error");
				}
			},
			error : function(e){
				_common.prompt("request failed!",5,"error");
			},
			complete:_common.myAjaxComplete
		});
	}

	//表格初始化-配方销售单详细列表样式
	var initTable = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Item Information",
			param:paramGrid,
			colNames:["Item Barcode","Item Code","Item Name","Specification","UOM","Unit Qty","Total Qty","Sales Price","Amount"],
			colModel:[
				{name:"barcode",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"articleId",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"spec",type:"text",text:"left",width:"90",ishide:false,css:""},
				{name:"uom",type:"text",text:"left",width:"80",ishide:false,css:""},
				{name:"unitQty",type:"text",text:"right",width:"110",ishide:false,css:""},
				{name:"articleQty",type:"text",text:"right",width:"110",ishide:false,css:""},
				{name:"salePrice",type:"text",text:"right",width:"110",ishide:false,css:"",getCustomValue:_common.getThousands},
				{name:"salePriceAmount",type:"text",text:"right",width:"110",ishide:true,css:"",getCustomValue:_common.getThousands}
			],//列内容
			width:"max",//宽度自动
			isPage:false,//是否需要分页
			lineNumber:true,//序号
			isCheckbox:false,
			freezeHeader:false,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent:function(self){
				selectTrTemp = null;//清空选择的行
				// if($("#zgGridTtable>.zgGrid-tbody tr").length>0){
				// 	var salePriceAmount = 0;
				// 	$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
				// 		var _salePriceAmount = parseFloat($(this).find('td[tag=salePriceAmount]').text().replace(/,/g, ""));
				// 		if(!isNaN(_salePriceAmount))
				// 			salePriceAmount +=  parseFloat(_salePriceAmount);
				// 	});
				// 	//合计  售价金额
				// 	var total = "<tr style='text-align: right' id='total'>" +
				// 		"<td></td><td></td><td></td><td></td>" +
				// 		"<td></td><td></td><td></td><td></td>" +
				// 		"<td></td><td>"+reThousands(salePriceAmount)+"</td>" +
				// 		"</tr>"
				// 	$("#zgGridTtable_tbody").append(total);
				// }
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			},
		});
	}

	//日期字段格式化格式
	var dateFmt = function(tdObj, value){
		return $(tdObj).text(fmtIntDate(value));
	}

	//金额格式化
	var moneyFmt = function (tdObj, value) {
		return $(tdObj).text(_common.fmoney(value,2));
	}

	//格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
		return res;
	}

	// 格式化数字类型的日期
	function fmtDate(date){
		var res = "";
		res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
		return res;
	}

	function subfmtDate(date){
		var res = "";
		if(date!=null&&date!=""){
			res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
		}
		return res;
	}

	//格式化数字类型的日期
	function fmtStrToInt(strDate){
		var res = "";
		res = strDate.replace(/-/g,"");
		return res;
	}
	self.init = init;
	return self;
});
var _start = require('start');
var _index = require('maItemSaleEdit');
_start.load(function (_common) {
	_index.init(_common);
});
