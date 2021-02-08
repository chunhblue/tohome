require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('itemList', function () {
    var self = {};
    var url_left = "",
		url_root = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
	    bmCodeOrItem = 0,//0 bmCode 1：item
    	common=null;
	const KEY = 'ITEM_MASTER_QUERY';
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		itemCode : null,
		itemName : null,
		barcode : null,
		itemType: null,
		brand: null,
		topDepCd : null,
		// depCd : null,
		category : null,
		subCategory : null,
		search: null,
		reset: null,
		dep:null,
		pma:null

	}

 	// 创建js对象
    var createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  	}
    }

    function init(_common) {
		createJqueryObj();
		url_root = _common.config.surl;
		url_left = url_root + "/itemMaster";
		// 首先验证当前操作人的权限是否混乱，
		if (m.use.val() == "0") {
			but_event();
		} else {
			m.error_pcode.show();
			m.main_box.hide();
		}
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// 表格内按钮事件
		table_event();
		// 初始化分类选择
		_common.initCategoryAutoMatic();
		// 初始化品牌选择
		initAutoMatic();
		// 初始化下拉列表
		initSelectValue();
		//权限验证
		isButPermission();
		// 初始化查询加载
		// m.search.click();
		initParam();
	}

	// 初始化参数, back 的时候 回显 已选择的检索项
	var initParam = function () {
		let searchJson=sessionStorage.getItem(KEY);
		if (!searchJson) {
			return;
		}
		let obj = eval("("+searchJson+")");
		// 取出后就清除
		sessionStorage.removeItem(KEY);
		// 不是通过 back 正常方式返回, 清除不加载数据
		if (obj.flg=='0') {
			return;
		}

		m.itemCode.val(obj.articleId);
		m.barcode.val(obj.barcode);
		m.itemName.val(obj.articleName);
		m.itemType.val(obj.materialType);
		$.myAutomatic.setValueTemp(brand,obj.brand,obj.brandName);
		// 设置 大中小分类回显
		_common.setCategoryAutomaticVal(obj);

		//拼接检索参数
		setParamJson();
		paramGrid = "searchJson="+m.searchJson.val();
		tableGrid.setting("url",url_left+"/getList");
		tableGrid.setting("param", paramGrid);
		tableGrid.setting("page", obj.page);
		tableGrid.loadData();
	}

	// 保存 参数信息
	var saveParamToSession = function () {
		let searchJson = m.searchJson.val();
		if (!searchJson) {
			return;
		}

		let obj = eval("("+searchJson+")");
		obj.depName=m.dep.attr('v');
		obj.pmaName=m.pma.attr('v');
		obj.categoryName=m.category.attr('v');
		obj.subCategoryName=m.subCategory.attr('v');
		obj.brandName=m.brand.attr('v');
		obj.page=tableGrid.getting("page");
		obj.flg='0';
		sessionStorage.setItem(KEY,JSON.stringify(obj));
	}

	// 表格内按钮点击事件
	var table_event = function(){
		// 查看按钮
		$("#view").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select the data to view!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"articleId,effectiveStartDate");
			var param = "articleId=" + cols["articleId"] + "&effectiveStartDate=" + cols["effectiveStartDate"];
			saveParamToSession();
			top.location = url_left+"/view?"+param;
		});
		// 导出按钮点击事件
		$("#export").on("click",function(){
			if(verifySearch()){
				// 拼接检索参数
				setParamJson();
				paramGrid = "searchJson=" + m.searchJson.val();
				var url = url_left + "/export?" + paramGrid;
				window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
			}
		});
	}

	// 画面按钮点击事件
	var but_event = function(){
		// 清空按钮
		m.reset.on("click",function(){
			m.itemCode.val("");
			m.barcode.val("");
			m.itemName.val("");
			m.itemType.val("");
			m.brand.val("");
			$("#depRemove").click();
			$("#brandRemove").click();
			selectTrTemp = null;
			m.searchJson.val("");
			_common.clearTable();
		});
		// 检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()){
				// 拼接检索参数
				setParamJson();
				paramGrid = "searchJson="+m.searchJson.val();
				tableGrid.setting("url",url_left+"/getList");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);
				tableGrid.loadData();
			}
		});
	}

	// 验证检索项是否合法
	var verifySearch = function(){
		return true;
	}

	// 拼接检索参数
	var setParamJson = function(){
		// 创建请求字符串
		var searchJsonStr ={
			articleId:m.itemCode.val().trim(),
			articleName:m.itemName.val().trim(),
			materialType:m.itemType.val().trim(),
			barcode:m.barcode.val().trim(),
			brand:m.brand.attr("k"),
			depCd:m.dep.attr("k"),
			pmaCd:m.pma.attr("k"),
			categoryCd:m.category.attr("k"),
			subCategoryCd:m.subCategory.attr("k")
		};
		m.searchJson.val(JSON.stringify(searchJsonStr));
	}

	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				initTable1();//列表初始化
				break;
			case "2":
				initTable2();//列表初始化
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}
	// 表格初始化-采购样式
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Query Result",//查询结果
			param:paramGrid,
			localSort: true,
			colNames:["Item Code","Start Date","Item Name","Item English Name","Item Specification",
				"Top Department","Department","Category","Sub-Category",
				"Material Type","Product Status","Brand(Full)"],
			colModel:[
				{name:"articleId",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"effectiveStartDate",type:"text",ishide:true},
				{name:"articleName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"articleNameEn",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"spec",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"depName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"pmaName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"categoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"subCategoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"materialTypeName",type:"text",text:"left",width:"130",ishide:false,css:"",},
				{name:"productStatus",type:"left",text:"center",width:"130",ishide:false,css:"",getCustomValue:getProductStatus},
				{name:"brandName",type:"text",text:"left",width:"130",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
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
			},
			buttonGroup:[
				{
					butType: "view",
					butId: "view",
					butText: "View",
					butSize: ""
				},
				{butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"}
			]
		});
	}
	// 按钮权限验证
	var isButPermission = function () {
		var viewBut = $("#viewBut").val();
		if (Number(viewBut) != 1) {
			$("#view").remove();
		}
		var exportBut = $("#exportBut").val();
		if (Number(exportBut) != 1) {
			$("#export").remove();
		}
	}

	// 获取商品状态名称
	function getProductStatus(tdObj, value){
		var _value = "";
		switch (value) {
			case "10":
				_value = "Core item";
				break;
			case "20":
				_value = "New Item";
				break;
			case "30":
				_value = "Terminated item";
				break;
			case "40":
				_value = "Normal";
				break;
		}
		return $(tdObj).text(_value);
	}

	// 判断是否为空
	function isNotNull(value) {
		if(value==null||$.trim(value)==""){
			return false;
		}
		return true;
	}

	// 清空下拉
	function clearOptions(selectId) {
		var selectObj = $("#" + selectId);
		selectObj.find("option:not(:first)").remove();

	}

	// 请求下拉数据
	function getSelectOptions(title, url, param, fun) {
		$.myAjaxs({
			url:url_root+url,
			async:false,
			cache:false,
			type :"post",
			data : param,
			dataType:"json",
			success : fun,
			error : function(e){
				_common.prompt(title+"Failed to load data!",5,"error");
			}
		});
	}

	// 加载商品形态下拉
	function initItemType(param){
		getSelectOptions("Material Type","/itemMaster/getMaterialType", null,function(res){
			var selectObj = $("#itemType");
			selectObj.find("option:not(:first)").remove();
			for (var i = 0; i < res.length; i++) {
				var optionValue = res[i].codeValue;
				var optionText = res[i].codeValue+' '+res[i].codeName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
	}

	// 初始化下拉列表
	function initSelectValue(){
		// 初始化部门下拉
		// initTopDep();
		// 初始化商品形态下拉
		initItemType();
	}

	var initAutoMatic = function(){
		brand = $("#brand").myAutomatic({
			url: url_left + "/brands",
			ePageSize: 10,
			startCount: 2,
			selectEleClick: function (thisObj) {}
		});
	}

	self.init = init;
	return self;
});

var _start = require('start');
var _index = require('itemList');
_start.load(function (_common) {
	_index.init(_common);
});
