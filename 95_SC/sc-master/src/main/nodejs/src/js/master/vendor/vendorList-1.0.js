require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("select2.css");
require("zgGrid");
require("myAutomatic");
require("select2");

var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('vendorList', function () {
    var self = {};
    var url_left = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
	    bmCodeOrItem = 0,//0 bmCode 1：item
    	common=null;
	const KEY = 'VENDOR_MASTER_QUERY';
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		vendorId : null,
		vendorName : null,
		// businessType: null,
		businessTypeList: null,
		deliveryType: null,
		orderType: null,
		contactName: null,
		behalfVendorId: null,
		search: null,
		reset: null,
	}

 	// 创建js对象
    var createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  	}
    }

    function init(_common) {
		createJqueryObj();
		url_left = _common.config.surl + "/vendorMaster";
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

		m.vendorId.val(obj.vendorId);
		m.vendorName.val(obj.vendorName);
		m.businessTypeList.val(obj.businessTypeList).trigger("change");
		m.deliveryType.val(obj.deliveryType);
		m.orderType.val(obj.orderSendMethod);
		m.contactName.val(obj.adminName);
		m.behalfVendorId.val(obj.behalfVendorId);

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
		obj.businessTypeList=m.businessTypeList.val();
		obj.page=tableGrid.getting("page");
		obj.flg='0';
		sessionStorage.setItem(KEY,JSON.stringify(obj));
	}

	//表格内按钮点击事件
	var table_event = function(){
		$('#businessTypeList').select2({
			placeholder: '-- All/Please Select --',
			allowClear: true,
			maximumSelectionLength:3,
			minimumResultsForSearch: -1,
		});

		// 查看按钮
		$("#view").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select the data to view!",5,"error");
				return false;
			}

			var cols = tableGrid.getSelectColValue(selectTrTemp,"vendorId,effectiveStartDate");
			var param = "vendorId=" + cols["vendorId"] + "&effectiveStartDate=" + cols["effectiveStartDate"];
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
			m.vendorId.val("");
			m.vendorName.val("");
			// m.businessType.val("");
			$('#businessTypeList').select2('val','');
			m.deliveryType.val("");
			m.orderType.val("");
			m.contactName.val("");
			m.behalfVendorId.val("");
			m.searchJson.val("");
			selectTrTemp = null;
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

	//把数组转化为,号分割
	function changeArrayToDate(array){
		if (!array) {
			return "";
		}
		var changeDate=JSON.stringify(array);
		changeDate=changeDate.substring(1,changeDate.length-1);
		changeDate=changeDate.replace(/\"/g,"");
		return changeDate;
	}

	// 验证检索项是否合法
	var verifySearch = function(){
		var reg = /^[0-9]*$/;
		// 供应商编号不只是数字
		/*if(!reg.test(m.vendorId.val())){
			m.vendorId.focus();
			_common.prompt("Vendor Code must be a number!",5,"error"); // 供应商编号必须是纯数字
			return false;
		}*/
		return true;
	}
	// 拼接检索参数
	var setParamJson = function(){
		// 创建请求字符串
		var searchJsonStr ={
			vendorId : m.vendorId.val().trim(),
			vendorName : m.vendorName.val().trim(),
			businessType : changeArrayToDate(m.businessTypeList.val()),
			// businessTypeList : m.businessTypeList.val(),
			deliveryType : m.deliveryType.val().trim(),
			orderSendMethod : m.orderType.val().trim(),
			adminName : m.contactName.val().trim(),
			behalfVendorId : m.behalfVendorId.val().trim()
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
	//表格初始化-采购样式
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Query Result",//查询结果
			param:paramGrid,
			localSort: true,
			colNames:["Vendor Code","Effective Start Date","Master Vendor","Vendor Name","Business Type","Registered Address",
				"Order Contact Name","Tel","Order Type"],
			colModel:[
				{name:"vendorId",type:"text",text:"right",width:"80",ishide:false,css:"",title: 'asd'},
				{name:"effectiveStartDate",type:"text",text:"center",ishide:true},
				{name:"behalfVendorId",type:"text",text:"left",width:"130",ishide:true,css:""},
				{name:"vendorName",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"businessType",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getBusinessType},
				{name:"vendorAddress1",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"adminName",type:"text",text:"left",width:"100",ishide:false,css:""},
				{name:"vendorTelNo",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"orderSendMethod",type:"text",text:"left",width:"80",ishide:false,css:"",getCustomValue:getOrderType}
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

	// 获取业务区分名称
	function getBusinessType(tdObj, value){
		var _value = "";
		value = value.trim();
		if (!value) {
			return $(tdObj).text("");
		}
		let arr = value.split(",");
		for (let i = 0; i < arr.length; i++) {
			if (arr[i]=="0") {
				_value += "Distribution Center,"
			} else if (arr[i]=="1") {
				_value += "Logistics Vendor,"
			} else if (arr[i]=="2") {
				_value += "Self-delivering Vendor,";
			}
		}
		_value=_value.substring(0,_value.lastIndexOf(","))
		return $(tdObj).text(_value);
	}

	// 获取订单方式名称
	function getOrderType(tdObj, value){
		var _value = "";
		switch (value) {
			case "1":
				_value = "Email";
				break;
		}
		return $(tdObj).text(_value);
	}

	self.init = init;
	return self;
});

var _start = require('start');
var _index = require('vendorList');
_start.load(function (_common) {
	_index.init(_common);
});
