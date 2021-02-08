require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('promotionDetails', function () {
    var self = {};
    var url_left = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
	    bmCodeOrItem = 0,//0 bmCode 1：item
    	common=null;
	const KEY = 'MM_PROMOTION_QUERY';
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		_promotionCd : null,
		back: null,
		dialog_cancel:null
	}

 	// 创建js对象
    var createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  	}
    }

    function init(_common) {
		createJqueryObj();
		url_left = _common.config.surl + "/mmPromotion";
		// 首先验证当前操作人的权限是否混乱，
		if (m.use.val() == "0") {
			but_event();
		} else {
			m.error_pcode.show();
			m.main_box.hide();
		}
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// 表格内按钮绑定
		table_event();
		// 查询加载数据
		getPromotion();
	}

	// 表格内按钮点击事件
	var table_event = function(){
		// 类别设定例外查看按钮
		$("#category_view").on("click",function(){
			// 显示表格
			$('#brandExDiv').hide();
			$('#categoryExDiv').show();
			// 查询数据
			getCategoryException();
			$('#details_dialog').modal("show");
		});
		// 品牌设定例外查看按钮
		$("#brand_view").on("click",function(){
			// 显示表格
			$('#brandExDiv').show();
			$('#categoryExDiv').hide();
			// 查询数据
			getBrandException();
			$('#details_dialog').modal("show");
		});
	}

	// 画面按钮点击事件
	var but_event = function(){
		// 返回按钮
		m.back.on("click",function(){
			_common.updateBackFlg(KEY);
			top.location = url_left;
		});
		// 弹窗返回按钮
		m.dialog_cancel.on("click",function(){
			$('#details_dialog').modal("hide");
		});
	}

	// 获取详细信息
	var getPromotion = function(){
		var promotionCd = m._promotionCd.val();
		if(promotionCd == null ||$.trim(promotionCd) == ''){
			_common.prompt("Parameter is empty!",5,"error");
			return false;
		}
		var _data = {
			promotionCd : promotionCd
		}
		var param = JSON.stringify(_data);
		$.myAjaxs({
			url:url_left+"/getData",
			async:true,
			cache:false,
			type :"post",
			data :"searchJson="+param,
			dataType:"json",
			success: function(result){
				if(result.success){
					// 获取例外店铺信息
					getException(param);
					// 获取促销区域信息
					getArea(param);
					// 获取品牌信息
					getBrand(param);
					// 获取类别信息
					getCategory(param);
					// 获取商品信息
					getItem(param);
					// 获取条件信息
					getCondition(param);
					// 加载基本信息
					$('#department').val(result.o.depName);
					$('#regionCd').val(result.o.regionName);
					$('#promotionCd').val(result.o.promotionCd);
					$('#promotionName').val(result.o.promotionName);
					$('#promotionStartDate').val(fmtIntDate(result.o.promotionStartDate));
					$('#promotionEndDate').val(fmtIntDate(result.o.promotionEndDate));
					$('#promotionStartTime').val(fmtIntTime(result.o.promotionStartTime));
					$('#promotionEndTime').val(fmtIntTime(result.o.promotionEndTime));
					$('#promotionPromptFlgC').val(result.o.promotionPromptFlg);
					$('#promotionPromptName').val(result.o.promotionPromptName);
					$("input[name='promotionPattern'][value="+result.o.promotionDiff+"]").attr("checked",true);
					$("input[name='promotionType'][value="+result.o.promotionType+"]").attr("checked",true);
					$("input[name='promotionRatioType'][value="+result.o.promotionAllotType+"]").attr("checked",true);
					// 判断多选是否选中
					if(result.o.promotionWeekCycle != null && result.o.promotionWeekCycle != ''){
						var str = result.o.promotionWeekCycle;
						var name = null;
						for(var i = 0; i < str.length; i++){
							switch(i){
								case 0: name = 'monday'; 	break;
								case 1: name = 'tuesday'; 	break;
								case 2: name = 'wednesday'; break;
								case 3: name = 'thursday'; 	break;
								case 4: name = 'friday'; 	break;
								case 5: name = 'saturday'; 	break;
								case 6: name = 'sunday'; 	break;
								default: continue;
							}
							if(str.charAt(i)=='1'){$('#'+name).attr('checked',true);}
						}
					}
				}else{
					_common.prompt(result.msg,5,"error");
				}
			},
			error : function(e){
				_common.prompt("Query failed!",5,"error");
			}
		});
	}

	// 获取例外店铺设定
	var getException = function(param){
		paramGrid = "searchJson=" + param;
		exceptionGridTable.setting("url", url_left + "/getException");
		exceptionGridTable.setting("param", paramGrid);
		exceptionGridTable.loadData();
	}

	// 获取促销区域设定
	var getArea = function(param){
		paramGrid = "searchJson=" + param;
		areaGridTable.setting("url", url_left + "/getArea");
		areaGridTable.setting("param", paramGrid);
		areaGridTable.loadData();
	}

	// 获取品牌设定例外商品
	var getBrandException = function(){
		var _param = {
			promotionCd:$('#promotionCd').val()
		};
		paramGrid = "searchJson=" + JSON.stringify(_param);
		brandExGridTable.setting("url", url_left + "/getBrandEx");
		brandExGridTable.setting("param", paramGrid);
		brandExGridTable.loadData();
	}

	// 获取品牌设定
	var getBrand = function(param){
		paramGrid = "searchJson=" + param;
		brandGridTable.setting("url", url_left + "/getBrand");
		brandGridTable.setting("param", paramGrid);
		brandGridTable.loadData();
	}

	// 获取类别设定例外商品
	var getCategoryException = function(){
		var _param = {
			promotionCd:$('#promotionCd').val()
		};
		paramGrid = "searchJson=" + JSON.stringify(_param);
		categoryExGridTable.setting("url", url_left + "/getCategoryEx");
		categoryExGridTable.setting("param", paramGrid);
		categoryExGridTable.loadData();
	}

	// 获取类别设定
	var getCategory = function(param){
		paramGrid = "searchJson=" + param;
		categoryGridTable.setting("url", url_left + "/getCategory");
		categoryGridTable.setting("param", paramGrid);
		categoryGridTable.loadData();
	}

	// 获取商品设定
	var getItem = function(param){
		paramGrid = "searchJson=" + param;
		itemGridTable.setting("url", url_left + "/getItem");
		itemGridTable.setting("param", paramGrid);
		itemGridTable.loadData();
	}

	// 获取条件设定
	var getCondition = function(param){
		paramGrid = "searchJson=" + param;
		conditionTableGrid.setting("url", url_left + "/getCondition");
		conditionTableGrid.setting("param", paramGrid);
		conditionTableGrid.loadData();
	}

	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				initConditionTable();// 条件设定列表初始化
				initItemTable();// 商品设定列表初始化
				initAreaTable();// 促销区域列表初始化
				initExceptionTable();// 例外店铺列表初始化
				initBrandTable();// 品牌设定列表初始化
				initCategoryTable();// 类别设定列表初始化
				initBrandExceptionTable();// 品牌设定例外商品列表初始化
				initCategoryExceptionTable();// 类别设定例外商品列表初始化
				break;
			case "2":
				initTable2();//列表初始化
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}

	// 表格初始化-例外店铺
	var initExceptionTable = function(){
		exceptionGridTable = $("#exceptionGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			localSort: true,
			colNames:["Serial","Except Type","Store No.","Store Name","City","District"],
			colModel:[
				{name:"serial",type:"text",text:"left",width:"60",ishide:false,css:""},
				{name:"exceptType",type:"text",text:"left",width:"60",ishide:false,css:""},
				{name:"storeCd",type:"text",text:"right",width:"60",ishide:false,css:""},
				{name:"storeName",type:"text",text:"left",width:"60",ishide:false,css:""},
				{name:"cityName",type:"text",text:"left",width:"60",ishide:false,css:""},
				{name:"districtName",type:"text",text:"left",width:"60",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			isPage:false,//是否需要分页
			showRowNumber: true//显示序号列
		});
	}

	// 表格初始化-促销区域
	var initAreaTable = function(){
		areaGridTable = $("#areaGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			localSort: true,
			colNames:["Serial","City","Price Group"],
			colModel:[
				{name:"serial",type:"text",text:"left",width:"60",ishide:false,css:""},
				{name:"storeTypeName",type:"text",text:"left",width:"60",ishide:false,css:""},
				{name:"maName",type:"text",text:"left",width:"60",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			isPage:false,//是否需要分页
			showRowNumber: true//显示序号列
		});
	}

	// 表格初始化-品牌设定例外商品
	var initBrandExceptionTable = function(){
		brandExGridTable = $("#brandExDetailsTable").zgGrid({
			title:"MM Promotion Brand Exception Setting",
			param:paramGrid,
			localSort: true,
			colNames:["Promotion Pattern","Brand","Top Department","Department","Category","Sub-Category",
				"Item Code","Item Name"],
			colModel:[
				{name:"promotionTermGroup",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getPatternName},
				{name:"brandName",type:"text",text:"left",width:"100",ishide:false,css:""},
				{name:"depName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"pmaName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"categoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"subCategoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"articleId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"150",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 表格初始化-品牌设定
	var initBrandTable = function(){
		brandGridTable = $("#brandGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			localSort: true,
			colNames:["Promotion Pattern","Brand Code","Brand Name"],
			colModel:[
				{name:"promotionTermGroup",type:"text",text:"left",width:"100",ishide:false,css:"",getCustomValue:getPatternName},
				{name:"brandCd",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"brandName",type:"text",text:"left",width:"130",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			isPage:false,//是否需要分页
			buttonGroup:[
				{butType:"custom",butHtml:"<button id='brand_view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-check'></span>View Exception</button>"}//查看 默认："",lg:最大，sm小，xs超小
			]
		});
	}

	// 表格初始化-类别设定例外商品
	var initCategoryExceptionTable = function(){
		categoryExGridTable = $("#categoryExDetailsTable").zgGrid({
			title:"MM Promotion Category Exception Setting",
			param:paramGrid,
			localSort: true,
			colNames:["Promotion Pattern","Top Department","Department","Category","Sub-Category",
				"Item Code","Item Name"],
			colModel:[
				{name:"promotionTermGroup",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getPatternName},
				{name:"depName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"pmaName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"categoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"subCategoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"articleId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"150",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 表格初始化-类别设定
	var initCategoryTable = function(){
		categoryGridTable = $("#categoryGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			localSort: true,
			colNames:["Promotion Pattern","Top Department","Department","Category","Sub-Category"],
			colModel:[
				{name:"promotionTermGroup",type:"text",text:"left",width:"100",ishide:false,css:"",getCustomValue:getPatternName},
				{name:"depName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"pmaName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"categoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"subCategoryName",type:"text",text:"left",width:"130",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			isPage:false,//是否需要分页,
			buttonGroup:[
				{butType:"custom",butHtml:"<button id='category_view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-check'></span>View Exception</button>"}//查看 默认："",lg:最大，sm小，xs超小
			]
		});
	}

	// 表格初始化-商品设定
	var initItemTable = function(){
		itemGridTable = $("#itemGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			localSort: true,
			colNames:["Promotion Pattern","Item Code","Item Name","Sub-Category","Selling Price"],
			colModel:[
				{name:"promotionTermGroup",type:"text",text:"left",width:"100",ishide:false,css:""},
				{name:"articleId",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"subCategoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"articleSalePrice",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getString0}
			],//列内容
			width:"max",// 宽度自动
			isPage:false// 是否需要分页
		});
	}

	// 表格初始化-条件设定
	var initConditionTable = function(){
		conditionTableGrid = $("#conditionGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			localSort: true,
			colNames:["Promotion Pattern","Required Purchase Quantity","Required Purchase Amount",
				"Value Of Promotion Type","Manually Set Ratio"],
			colModel:[
				{name:"promotionTermGroup",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getPatternName},
				{name:"promotionTermQty",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"promotionTermAmt",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getString0},
				{name:"promotionValue",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getString0},
				{name:"promotionAllotValue",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getString0}
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 获取分组标识名称
	function getPatternName(tdObj, value){
		var _value = "";
		switch (value) {
			case "01":
				_value = "A";
				break;
			case "02":
				_value = "B";
				break;
			case "03":
				_value = "C";
				break;
		}
		return $(tdObj).text(_value);
	}

	// 为0返回'0'
	function getString0(tdObj, value){
		var _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}

	// 格式化数字类型的日期
	function fmtIntDate(date){
		if(date == null||$.trim(date) == ''||date.length!=8){
			return date;
		}
		var res = "";
		res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		return res;
	}

	// 格式化数字类型的时间
	function fmtIntTime(time){
		if(time == null||$.trim(time) == ''||time.length<4){
			return time;
		}
		var res = "";
		res = time.substring(0,2)+":"+time.substring(2,4);
		return res;
	}

	//字符串日期格式转换：dd/mm/yyyy → yyyymmdd
	function fmtStringDate(date){
		var res = "";
		date = date.replace(/\//g,"");
		res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
		return res;
	}

	self.init = init;
	return self;
});

var _start = require('start');
var _index = require('promotionDetails');
_start.load(function (_common) {
	_index.init(_common);
});
