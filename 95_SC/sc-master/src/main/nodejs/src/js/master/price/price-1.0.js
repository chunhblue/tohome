require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('priceByDay', function () {
    var self = {};
    var url_left = "",
		url_root = "",
	    paramGrid = null,
	    selectTrTemp = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		aStore = null,
	    tempTrObjValue = {},//临时行数据存储
	    bmCodeOrItem = 0,//0 bmCode 1：item
    	common=null;
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		effective_date : null,
		item_code : null,
		item_name : null,
		item_barcode : null,
		vendor_id : null,
		// top_depCd : null,
		// depCd : null,
		category : null,
		sub_category : null,
		search: null,
		dep:null,
		pma:null,
		subCategory:null,
		aStore : null,
		reset: null
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
		url_left = url_root + "/priceByDay";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		// 首先验证当前操作人的权限是否混乱，
		if (m.use.val() == "0") {
			but_event();
		} else {
			m.error_pcode.show();
			m.main_box.hide();
		}
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// 初始化店铺运营组织检索
		// _common.initOrganization();
		// 初始化店铺名称下拉
		_common.initStoreform();
		// 初始化分类查询
		// initAutoMatic();
		_common.initCategoryAutoMatic();
		// 表格内按钮事件
		table_event();
		// 初始化下拉
		initSelectValue();
		//权限验证
		isButPermission();
		// 初始化查询
		// m.search.click();
	}

	// 表格内按钮点击事件
	var table_event = function(){
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
	var initAutoMatic=function(){
		$("#pma").attr("disabled", true);
		$("#category").attr("disabled", true);
		$("#subCategory").attr("disabled", true);
		$("#pmaRefresh").attr("disabled",true).css("pointer-events","none");
		$("#categoryRefresh").attr("disabled",true).css("pointer-events","none");
		$("#subCategoryRefresh").attr("disabled",true).css("pointer-events","none");
		a_dep = $("#dep").myAutomatic({
			url: url_root + "/deps",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_pma);
				$.myAutomatic.cleanSelectObj(a_category);
				$.myAutomatic.cleanSelectObj(a_subCategory);
				$("#pma").attr("disabled", true);
				$("#category").attr("disabled", true);
				$("#subCategory").attr("disabled", true);
				$("#pmaRefresh").attr("disabled",true).css("pointer-events","none");
				$("#categoryRefresh").attr("disabled",true).css("pointer-events","none");
				$("#subCategoryRefresh").attr("disabled",true).css("pointer-events","none");
				if (thisObj.attr("k") != null && thisObj.attr("k") != "" && thisObj.attr("k") != "999") {
					$("#pma").attr("disabled", false);
					$("#pmaRefresh").attr("disabled",false).css("pointer-events","auto");
				}
				var dinput = thisObj.attr("k");
				var str = "&depId=" + dinput;
				$.myAutomatic.replaceParam(a_pma, str);//赋值
			}
		});
		a_pma = $("#pma").myAutomatic({
			url: url_root + "/pmas",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_category);
				$.myAutomatic.cleanSelectObj(a_subCategory);
				$("#category").attr("disabled", true);
				$("#subCategory").attr("disabled", true);
				$("#categoryRefresh").attr("disabled",true).css("pointer-events","none");
				$("#subCategoryRefresh").attr("disabled",true).css("pointer-events","none");
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					$("#category").attr("disabled", false);
					$("#categoryRefresh").attr("disabled",false).css("pointer-events","auto");
				}
				var dinput = $("#dep").attr("k");
				var pinput = thisObj.attr("k");
				var str = "&depId=" + dinput+"&pmaId=" + pinput;
				$.myAutomatic.replaceParam(a_category, str);//赋值
			}
		});
		a_category = $("#category").myAutomatic({
			url: url_root + "/categorys",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_subCategory);
				$("#subCategory").attr("disabled", true);
				$("#subCategoryRefresh").attr("disabled",true).css("pointer-events","none");
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					$("#subCategory").attr("disabled", false);
					$("#subCategoryRefresh").attr("disabled",false).css("pointer-events","auto");
				}
				var dinput = $("#dep").attr("k");
				var pinput = $("#pma").attr("k");
				var cinput = thisObj.attr("k");
				var str = "&depId=" + dinput+"&pmaId=" + pinput+"&categoryId=" + cinput;
				$.myAutomatic.replaceParam(a_subCategory, str);//赋值
			}
		});
		a_subCategory = $("#subCategory").myAutomatic({
			url: url_root + "/subCategorys",
			ePageSize: 10,
			startCount: 0,
		});
		$.myAutomatic.cleanSelectObj(a_dep);
		$.myAutomatic.cleanSelectObj(a_pma);
		$.myAutomatic.cleanSelectObj(a_category);
		$.myAutomatic.cleanSelectObj(a_subCategory);
	}
	// 画面按钮点击事件
	var but_event = function(){
		// 分类信息监听事件
		// m.top_depCd.on("change",function(){
		// 	m.depCd.val("");
		// 	clearOptions("depCd");
		// 	m.category.val("");
		// 	clearOptions("category");
		// 	m.sub_category.val("");
		// 	clearOptions("sub_category");
		// 	// 不为空则加载
		// 	var _topDepCd = m.top_depCd.val();
		// 	if(isNotNull(_topDepCd)){
		// 		var param = "depCd="+_topDepCd;
		// 		initDep(param);
		// 	}
		// });
		// m.depCd.on("change",function(){
		// 	m.category.val("");
		// 	clearOptions("category");
		// 	m.sub_category.val("");
		// 	clearOptions("sub_category");
		// 	// 不为空则加载
		// 	var _topDepCd = m.top_depCd.val();
		// 	var _depCd = m.depCd.val();
		// 	if(isNotNull(_topDepCd)&&isNotNull(_depCd)){
		// 		var param = "depCd="+_topDepCd+"&pmaCd="+_depCd;
		// 		initCategory(param);
		// 	}
		// });
		// m.category.on("change",function(){
		// 	m.sub_category.val("");
		// 	clearOptions("sub_category");
		// 	// 不为空则加载
		// 	var _topDepCd = m.top_depCd.val();
		// 	var _depCd = m.depCd.val();
		// 	var _category = m.category.val();
		// 	if(isNotNull(_topDepCd)&&isNotNull(_depCd)&&isNotNull(_category)){
		// 		var param = "depCd="+_topDepCd+"&pmaCd="+_depCd+"&categoryCd="+_category;
		// 		initSubCategory(param);
		// 	}
		// });
		// 适用日期
		m.effective_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		// 清空按钮
		m.reset.on("click",function(){
			$("#storeRemove").click();
			$("#depRemove").click();
			getBusinessDate();
			m.item_code.val("");
			m.item_name.val("");
			m.item_barcode.val("");
			m.vendor_id.val("");
			selectTrTemp = null;
			_common.clearTable();
			$("#aStore").css("border-color","#CCC");
			m.effective_date.css("border-color","#CCC");
			// m.top_depCd.val("");
			// m.depCd.val("");
			// m.category.val("");
			// m.sub_category.val("");
			// clearOptions("depCd");
			// clearOptions("category");
			// clearOptions("sub_category");
		});
		// 检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()){
				//拼接检索参数
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
		let temp = $("#aStore").attr("k");
		if (!temp) {
			m.aStore.focus();
			$("#aStore").css("border-color","red");
			_common.prompt("Please select a store first!",3,"info");/*请选择 店铺*/
			return false;
		}else {
			$("#aStore").css("border-color","#CCC");
		}
		temp = m.effective_date.val();
		if (!temp) {
			m.effective_date.focus();
			m.effective_date.css("border-color","red");
			_common.prompt("Please select query date!",3,"info");/*请选择 查询日期*/
			return false;
		}else {
			m.effective_date.css("border-color","#CCC");
		}
		return true;
	}

	// 拼接检索参数
	var setParamJson = function(){
		// 日期格式转换
		let _effectiveDate = fmtStringDate(m.effective_date.val())||null;
		// 创建请求字符串
		let searchJsonStr ={
			storeCd:$("#aStore").attr("k"),
			effectiveDate:_effectiveDate,
			articleId:m.item_code.val().trim(),
			articleName:m.item_name.val().trim(),
			barcode:m.item_barcode.val().trim(),
			vendorId:m.vendor_id.val().trim(),
			depCd:m.dep.attr("k"),
			pmaCd:m.pma.attr("k"),
			categoryCd:m.category.attr("k"),
			subCategoryCd: m.subCategory.attr("k")
			// depCd:m.top_depCd.val(),
			// pmaCd:m.depCd.val(),
			// categoryCd:m.category.val(),
			// subCategoryCd:m.sub_category.val()
		};
		m.searchJson.val(JSON.stringify(searchJsonStr));
	}

	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				initTable();//列表初始化
				break;
			case "2":
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}
	// 表格初始化-当日价格一览样式
	var initTable = function(){
		tableGrid = $("#zgGridTable").zgGrid({
			title:"Query Result",//查询结果
			param:paramGrid,
			localSort: true,
			colNames:["Item Code","Item Name","Item English Name","Barcode","Top Department","Department","Category",
				"Sub-Category","Store Cluster","Store No.","Store Name","Vendor Code","Vendor Name",
				"Purchasing Price","Selling Price","Original Selling Price"],
			colModel:[
				{name:"articleId",type:"text",text:"right",width:"120",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"articleNameEn",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"barcode",type:"text",text:"right",width:"120",ishide:false,css:""},
				{name:"depName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"pmaName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"categoryName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"subCategoryName",type:"text",text:"left",width:"140",ishide:false,css:""},
				{name:"storeTypeName",type:"text",text:"left",width:"140",ishide:false,css:""},
				{name:"storeCd",type:"text",text:"right",width:"120",ishide:false,css:""},
				{name:"storeName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"vendorId",type:"text",text:"right",width:"120",ishide:false,css:""},
				{name:"vendorName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"orderPrice",type:"text",text:"right",width:"170",ishide:true,css:"",getCustomValue:getThousands},
				{name:"sellingPrice",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"baseSalePrice",type:"text",text:"right",width:"170",ishide:false,css:"",getCustomValue:getThousands},
			],//列内容
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
			},
			buttonGroup:[
				{butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"}
			]
		});
	}

	// 按钮权限验证
	var isButPermission = function () {
		var exportBut = $("#exportBut").val();
		if (Number(exportBut) != 1) {
			$("#export").remove();
		}
	}

	// 为0返回'0'
	function getString0(tdObj, value){
		var _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}

	// 获取商品状态名称
	function getLifecycleStatus(tdObj, value){
		var _value = "";
		switch (value) {
			case "00":
				_value = "未生效";
				break;
			case "10":
				_value = "正常上架";
				break;
			case "20":
				_value = "暂停销售";
				break;
			case "30":
				_value = "下架处理";
				break;
			case "90":
				_value = "淘汰";
				break;
		}
		return $(tdObj).text(_value);
	}

	// 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
	function fmtStringDate(date){
		var res = "";
		date = date.replace(/\//g,"");
		res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
		return res;
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
			async:true,
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

	// 加载部门信息
	function initTopDep(){
		getSelectOptions("Top Department","/ma0070/getDpt","",function(res){
			var selectObj = $("#top_depCd");
			selectObj.find("option:not(:first)").remove();
			for (var i = 0; i < res.length; i++) {
				var optionValue = res[i].depCd;
				var optionText = res[i].depCd+' '+res[i].depName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
	}

	// 加载大分类信息
	function initDep(param){
		getSelectOptions("Department","/ma0070/getPma", param,function(res){
			var selectObj = $("#depCd");
			selectObj.find("option:not(:first)").remove();
			for (var i = 0; i < res.length; i++) {
				var optionValue = res[i].pmaCd;
				var optionText = res[i].pmaCd+' '+res[i].pmName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
	}

	// 加载中分类信息
	function initCategory(param){
		getSelectOptions("Category","/ma0070/getCategory", param,function(res){
			var selectObj = $("#category");
			selectObj.find("option:not(:first)").remove();
			for (var i = 0; i < res.length; i++) {
				var optionValue = res[i].categoryCd;
				var optionText = res[i].categoryCd+' '+res[i].categoryName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
	}

	// 加载小分类信息
	function initSubCategory(param){
		getSelectOptions("Sub-Category","/ma0070/getSubCategory", param,function(res){
			var selectObj = $("#sub_category");
			selectObj.find("option:not(:first)").remove();
			for (var i = 0; i < res.length; i++) {
				var optionValue = res[i].subCategoryCd;
				var optionText = res[i].subCategoryCd+' '+res[i].subCategoryName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
	}

	// 获取业务日期
	function getBusinessDate(){
		getSelectOptions("Effective Date","/cm9060/getDate", null,function(res){
			var date = "";
			if(!!res && res.toString().length==8){
				res = res.toString();
				date = res.substring(6,8)+"/"+res.substring(4,6)+"/"+res.substring(0,4);
			}
			$('#effective_date').val(date);
		});
	}

	// 初始化下拉列表
	function initSelectValue(){
		// 初始化部门下拉
		initTopDep();
		// 获取业务日期
		getBusinessDate();
	}

	self.init = init;
	return self;
});

var _start = require('start');
var _index = require('priceByDay');
_start.load(function (_common) {
	_index.init(_common);
});
