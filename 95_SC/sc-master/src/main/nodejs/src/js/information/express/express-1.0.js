require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('operationExpress', function () {
    var self = {};
    var url_left = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
	    bmCodeOrItem = 0,//0 bmCode 1：item
    	common=null;

	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		main_box: null,
		identity : null,
		searchJson: null,
		businessDate : null,
		barcode : null,
		itemName : null,
		topDepCd : null,
		depCd : null,
		category : null,
		subCategory : null,
		search: null,
		reset: null,
		print: null,
		export: null
	}

 	// 创建js对象
    var createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }

    function init(_common) {
		createJqueryObj();
		url_left = _common.config.surl + "/operationExpress";
		//首先验证当前操作人的权限是否混乱，
		if (m.use.val() == "0") {
			but_event();
		} else {
			m.error_pcode.show();
			m.main_box.hide();
		}
		// console.log(m.use.val());
		//根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// initPageBytype("1");

		// $("#update").attr("disabled", "disabled");
		// $("#view").attr("disabled", "disabled");
	}

	//画面按钮点击事件
	var but_event = function(){
		//适用日期
		m.businessDate.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		//清空按钮
		m.reset.on("click",function(){
			m.businessDate.val("");
			m.barcode.val("");
			m.itemName.val("");
			m.topDepCd.val("");
			m.depCd.val("");
			m.category.val("");
			m.subCategory.val("");
		});
		//检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()){
				//拼接检索参数
				setParamJson();
				paramGrid = "searchJson="+m.searchJson.val();
				var _level = $("input[name='level']:checked").val();
				var data = null, tableGrid= null;
				switch(_level) {
					case "0":
						tableGrid = tableGrid1;
						$("#div_table1").show();
						$("#div_table2").hide();
						$("#div_table3").hide();
						break;
					case "1":
						tableGrid = tableGrid2;
						$("#div_table1").hide();
						$("#div_table2").show();
						$("#div_table3").hide();
						break;
					case "2":
						tableGrid = tableGrid3;
						$("#div_table1").hide();
						$("#div_table2").hide();
						$("#div_table3").show();
						break;
					default:
						m.error_pcode.show();
						m.main_box.hide();
				}
				tableGrid.setting("url",url_left+"/getdata");
				tableGrid.setting("param", paramGrid);
				// tableGrid.setting("page", 1);

				tableGrid.loadData(null);

			}else{
				$("#search").prop("disabled",true);
			}
			//判断当前操作人是否为事业部长或系统部，如果是，曾增加待审核数量等内容的刷新机制，其他身份不参与此操作
			if(m.identity.val()=="2"||m.identity.val()=="3"){
				refreshWaitingReview();
			}
		});
		// Barcode输入口 焦点离开事件
		m.barcode.on("blur",function(){
			var inputVal = $(this).val();
			if($.trim(inputVal)==""){
				m.itemName.text("");
				m.itemName.text('');
			}else{
				var reg = /^[0-9]*$/;
				if(!reg.test(inputVal)){
					m.itemName.text('');
					_common.prompt("Item Barcode must be a number!",5,"error");
					return false;
				}
				getItemInfoByItem1(inputVal,function(res){
					if(res.success){
						m.itemName.text($.trim(res.data.itemName));
						return true;
					}else{
						m.itemName.text("Item Name is not obtained!");// 未取得Item Name
						_common.prompt(res.message,5,"error");
						return false;
					}
				});
			}
		});
	}
	//验证检索项是否合法
	var verifySearch = function(){
		return true;
	}
	//拼接检索参数
	var setParamJson = function(){
		// 创建请求字符串
		var searchJsonStr ={

		};
		m.searchJson.val(JSON.stringify(searchJsonStr));
	}
	//根据Item Barcode取得该商品的详细对象，
	var getItemInfoByItem1 = function(item1,fun){
		$.myAjaxs({
			url:url_left+"/getiteminfo",
			async:true,
			cache:false,
			type :"get",
			data :"itemCode="+item1,
			dataType:"json",
			success:fun,
			complete:_common.myAjaxComplete
		});
	}
	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				initTable1();//列表初始化-门店级
				initTable2();//列表初始化-分类级
				$("#div_table2").hide();
				initTable3();//列表初始化-商品级
				$("#div_table3").hide();
				break;
			case "2":
				initTable2();//列表初始化
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}

	//表格初始化-店铺级
	var initTable1 = function(){
		var data = '[{"sc1":"1.00","sc2":"1.00","sc3":"1.00","sc4":"1.00","sc5":"1.00","sc6":"1.00","sc7":"1.00","sc8":"1.00","sc9":"1.00","sc10":"1.00"}]';
		tableGrid1 = $("#zgGridTable1").zgGrid({
			title:"Query Result",//查询结果
			param:paramGrid,
			localSort: true,
			colNames:["Sales Amount","Promotion Amount","Promotion Ratio(%)","GP Amount","GP Ratio(%)","Purchasing Amount",
				"Adjustment Amount","Inventory Amount","Customer Quantity","Customer Deal"],
			colModel:[
				{name:"sc1",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc2",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc3",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc4",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc5",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc6",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc7",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc8",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc9",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc10",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null}
			],//列内容
			traverseData:data,
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			// sidx:"bm.bm_type,order.bm_code",//排序字段
			// sord:"asc",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent:function(self){
				selectTrTemp = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			}
		});
	}

	//表格初始化-分类级
	var initTable2 = function(){
		var data = null;
		tableGrid2 = $("#zgGridTable2").zgGrid({
			title:"Query Result",//查询结果
			param:paramGrid,
			localSort: true,
			colNames:["Top Department","Department","Category","Sub-Category","Name","Sales Amount","Promotion Amount","Promotion Ratio(%)",
				"Store Ratio(%)","GP Amount","GP Ratio(%)","Purchasing Amount","Adjustment Amount","Inventory Amount"],
			colModel:[
				{name:"sc1",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc2",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc3",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc4",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc5",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc6",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc7",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc8",type:"text",text:"center",width:"140",ishide:false,css:"",getCustomValue:null},
				{name:"sc9",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc10",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc11",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc12",type:"text",text:"center",width:"140",ishide:false,css:"",getCustomValue:null},
				{name:"sc13",type:"text",text:"center",width:"140",ishide:false,css:"",getCustomValue:null},
				{name:"sc14",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null}
			],//列内容
			traverseData:data,
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			// sidx:"bm.bm_type,order.bm_code",//排序字段
			// sord:"asc",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent:function(self){
				selectTrTemp = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			}
		});
	}

	//表格初始化-商品级
	var initTable3 = function(){
		var data = null;
		tableGrid3 = $("#zgGridTable3").zgGrid({
			title:"Query Result",//查询结果
			param:paramGrid,
			localSort: true,
			colNames:["Item Code","Item Barcode","Item Name","Specification","Sales UOM","Sales Amount","Promotion Amount","Promotion Ratio(%)",
				"GP Amount","GP Ratio(%)","Purchasing Amount","Adjustment Amount","Inventory Amount"],
			colModel:[
				{name:"sc1",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc2",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc3",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc4",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"sc5",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc6",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc7",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc8",type:"text",text:"center",width:"140",ishide:false,css:"",getCustomValue:null},
				{name:"sc9",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc10",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null},
				{name:"sc11",type:"text",text:"center",width:"140",ishide:false,css:"",getCustomValue:null},
				{name:"sc12",type:"text",text:"center",width:"140",ishide:false,css:"",getCustomValue:null},
				{name:"sc13",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:null}
			],//列内容
			traverseData:data,
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			// sidx:"bm.bm_type,order.bm_code",//排序字段
			// sord:"asc",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent:function(self){
				selectTrTemp = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			}
		});
	}

	self.init = init;
	return self;
});

var _start = require('start');
var _index = require('operationExpress');
_start.load(function (_common) {
	_index.init(_common);
});
