require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('vendorDetails', function () {
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
		_vendorId : null,
		_effectiveStartDate : null,
		back: null,
		emailSetting: null,
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
		// 查询加载数据
		getVendorMaster();
	}

	// 画面按钮点击事件
	var but_event = function(){
		// 返回按钮
		m.back.on("click",function(){
			_common.updateBackFlg(KEY);
			top.location = url_left;
		});
		// vendor email setting 按钮
		m.emailSetting.on("click",function () {
			$("#email_setting_dialog").modal("show");
		});
	}

	// 获取主档基本信息
	var getVendorMaster = function(){
		var vendorId = m._vendorId.val();
		var startDate = m._effectiveStartDate.val();
		if(vendorId == null ||$.trim(vendorId) == ''
			|| startDate == null ||$.trim(startDate) == ''){
			_common.prompt("Parameter is empty!",5,"error");
			return false;
		}
		var _data = {
			vendorId : vendorId,
			effectiveStartDate : startDate
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
					// 获取配送范围
					getDetails(result.o.regionCd);
					// 获取最低订户数量/金额
					getMinOtyAmt(param);
					// 获取大分类信息
					getDepartment(param);
					// 获取银行信息
					getBankInfo(param);
					// 获取商品分类信息
					getProductGroup(param);
					// 获取vendor email
					getVendorEmail(param);
					// 获取trading term数据
					getTradingTerm(param);
					// 获取account信息
					getAccountInfo(param);
					// 加载基础数据
					$('#masterVendor').val(result.o.behalfVendorId);
					$('#masterPayment').val(result.o.masterPayment);
					$('#regionCd').val(result.o.regionName);
					$('#vendorId').val(result.o.vendorId);
					$('#vendorCodeReceivable').val(result.o.vendorCodeReceivable);
					$('#vendorType').val(result.o.vendorType);
					$('#effectiveStartDate').val(fmtIntDate(result.o.effectiveStartDate));
					$('#effectiveEndDate').val(fmtIntDate(result.o.effectiveEndDate));
					$('#vendorName').val(result.o.vendorName);
					$('#vendorNameShort').val(result.o.vendorNameShort);
					// $('#depCd').val(result.o.topDepName);
					$('#orderFlg').val(result.o.orderFlg);
					$('#businessType').val(result.o.businessType);
					$('#tradeStartDate').val(fmtIntDate(result.o.tradeStartDate));
					$('#tradeEndDate').val(fmtIntDate(result.o.tradeEndDate));
					$('#vendorZipCode').val(result.o.vendorZipCode);
					$('#adminName').val(result.o.adminName);
					$('#vendorEmail').val(result.o.vendorEmail);
					$('#vendorAddress1').val(result.o.vendorAddress1);
					$('#vendorAddress2').val(result.o.vendorAddress2);
					$('#vendorTelNo').val(result.o.vendorTelNo);
					$('#vendorFaxNo').val(result.o.vendorFaxNo);
					$('#orderSendMethod').val(result.o.orderSendMethod);
					$('#shipmentFlg').val(result.o.shipmentFlg);
					$('#cutoffTime').val(result.o.cutoffTime);
					$('#orderAddress1').val(result.o.orderAddress1);
					$('#orderAddress2').val(result.o.orderAddress2);
					$('#orderTelNo').val(result.o.orderTelNo);
					$('#orderFaxNo').val(result.o.orderFaxNo);
					$('#orderEmail').val(result.o.orderEmail);
					$('#orderAdminName').val(result.o.orderAdminName);
					$('#orderAdminName2').val(result.o.orderAdminName2);
					$('#orderAdminName3').val(result.o.orderAdminName3);
					$('#supplyPurchaseRate').val(result.o.supplyPurchaseRate);
					$('#badDiscountRate').val(result.o.badDiscountRate);
					$('#payTypeCd').val(result.o.payTypeCd);
					$('#payPeriodDay').val(result.o.payPeriodDay);
					$('#payPeriodCd').val(result.o.payPeriodCd);
					$('#payTaxNo').val(result.o.payTaxNo);
					$('#businessRegNo').val(result.o.businessRegNo);
					$('#bankCountry').val(result.o.bankCountry);
					$('#payCurrencyType').val(result.o.payCurrencyType);
					$('#creditLimit').val(result.o.creditLimit);
					$('#keepCreditLimit').val(result.o.keepCreditLimit);
					$('#accountPayable').val(result.o.accountPayable);
					$('#accountReceivable').val(result.o.accountReceivable);
					$('#remarks').val(result.o.remarks);
					// 判断多选是否选中
					var _temp = result.o.contractSigned;
					if(_temp=='1'){$('#contractSigned').attr('checked',true);}
					_temp = result.o.liquidationAgreement;
					if(_temp=='1'){$('#liquidationAgreement').attr('checked',true);}
					_temp = result.o.license;
					if(_temp=='1'){$('#license').attr('checked',true);}
					_temp = result.o.accountsReceivable;
					if(_temp=='1'){$('#accountsReceivable').attr('checked',true);}
					_temp = result.o.bankInformation;
					if(_temp=='1'){$('#bankInformation').attr('checked',true);}
					_temp = result.o.addendumSigned;
					if(_temp=='1'){$('#addendumSigned').attr('checked',true);}
					_temp = result.o.trading;
					if(_temp=='1'){$('#trading').attr('checked',true);}
					_temp = result.o.authorizationLetter;
					if(_temp=='1'){$('#authorizationLetter').attr('checked',true);}
					_temp = result.o.listingProposal;
					if(_temp=='1'){$('#listingProposal').attr('checked',true);}
					_temp = result.o.othersCheck;
					if(_temp=='1'){
						$('#othersCheck').attr('checked',true);
						$('#others').val(result.o.others);
					}
				}else{
					_common.prompt(result.msg,5,"error");
				}
			},
			error : function(e){
				_common.prompt("Request Failed!",5,"error");
			}
		});
	}

	// 获取配送范围详情
	var getDetails = function(regionCd){
		var vendorId = m._vendorId.val();
		var startDate = m._effectiveStartDate.val();
		if(vendorId == null ||$.trim(vendorId) === ''
			|| startDate == null ||$.trim(startDate) === ''){
			_common.prompt("Parameter is empty!",5,"error");
			return false;
		}
		var _data = {
			vendorId : vendorId,
			effectiveStartDate : startDate,
			regionCd : regionCd
		};
		var param = JSON.stringify(_data);
		paramGrid = "searchJson=" + param;
		tableGrid.setting("url", url_left + "/getDeliveryType");
		tableGrid.setting("param", paramGrid);
		tableGrid.loadData();
	}

	// 获取最低订货数量/金额
	var getMinOtyAmt = function(param){
		paramGrid = "searchJson=" + param;
		minOrderAmtGridTable.setting("url", url_left + "/getMinOtyAmt");
		minOrderAmtGridTable.setting("param", paramGrid);
		minOrderAmtGridTable.loadData();
	}

	// 获取大分类信息
	var getDepartment = function(param){
		paramGrid = "searchJson=" + param;
		departmentGridTable.setting("url", url_left + "/getDepartment");
		departmentGridTable.setting("param", paramGrid);
		departmentGridTable.loadData();
	}

	// 获取银行信息
	var getBankInfo = function(param){
		paramGrid = "searchJson=" + param;
		bankInfoGridTable.setting("url", url_left + "/getBankInfo");
		bankInfoGridTable.setting("param", paramGrid);
		bankInfoGridTable.loadData();
	}

	// 获取银行信息
	var getAccountInfo = function(param){
		paramGrid = "searchJson=" + param;
		accountGridTable.setting("url", url_left + "/getAccountInfo");
		accountGridTable.setting("param", paramGrid);
		accountGridTable.loadData();
	}

	// 获取商品分类信息
	var getProductGroup = function(param){
		paramGrid = "searchJson=" + param;
		productGroupGridTable.setting("url", url_left + "/getProductGroup");
		productGroupGridTable.setting("param", paramGrid);
		productGroupGridTable.loadData();
	}

	// 获取vendor email信息
	var getVendorEmail = function(param){
		paramGrid = "searchJson=" + param;
		emailGridTtable.setting("url", url_left + "/getVendorEmail");
		emailGridTtable.setting("param", paramGrid);
		emailGridTtable.loadData();
	}

	// 获取trading term 数据
	var getTradingTerm = function (param) {
		paramGrid = "searchJson=" + param;
		tradingGridTtable.setting("url", url_left + "/getTradingTerm");
		tradingGridTtable.setting("param", paramGrid);
		tradingGridTtable.loadData();
	}

	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				initTable(); // 列表初始化
				initMinOrderAmtTable(); // 最小订货数量和金额初始化
				initProductGroupTable(); // 商品分类列表初始化
				initDepartmentTable(); // 大分类列表初始化
				initBankInfoTable(); // 银行信息列表初始化
				initEmailGridTtable(); // 初始化email setting
				initTradingGridTtable(); // 初始化Trading term
				initAccountGridTable(); // account信息列表初始化
				break;
			case "2":
				initTable2();//列表初始化
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}

	// 表格初始化-最小订货数量和金额
	var initEmailGridTtable = function(){
		emailGridTtable = $("#emailGridTtable").zgGrid({
			title:"Order Email Setting",
			param:paramGrid,
			localSort: true,
			height: "200",
			colNames:["Order Type","Region","City","PO Category Name","Mail Title","Email(Use';'separate)","CC Email(Use';'separate)",
			          "Attn","PO Email","Order Address","Order Tel","Order Fax","Order Contact Name"],
			colModel:[
				{name:"orderTypeName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"regionName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"structureName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"productGroupName",type:"text",text:"center",width:"130",ishide:false,css:"",},
				{name:"mailTitle",edittype:"textarea",text:"center",width:"130",ishide:false,css:"",},
				{name:"email",edittype:"textarea",text:"center",width:"200",ishide:false,css:"",},
				{name:"ccEmail",edittype:"textarea",text:"center",width:"200",ishide:false,css:""},
				{name:"attn",edittype:"textarea",text:"center",width:"130",ishide:false,css:""},
				{name:"poEmail",edittype:"textarea",text:"center",width:"200",ishide:false,css:""},
				{name:"orderAddress",edittype:"textarea",text:"center",width:"200",ishide:false,css:""},
				{name:"orderTelNo",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"orderFaxNo",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"orderAdminName",type:"text",text:"center",width:"150",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 表格初始化 trading term
	var initTradingGridTtable = function () {
		tradingGridTtable = $("#tradingGridTtable").zgGrid({
			title:"Trading Term",
			param:paramGrid,
			localSort: true,
			height: "200",
			colNames:["Trading Term","Default Trading Term"],
			colModel:[
				{name:"tradingTermNo",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"isDefault",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:isDefault},
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 表格初始化-最小订货数量和金额
	var initMinOrderAmtTable = function(){
		minOrderAmtGridTable = $("#minOrderAmtGridTable").zgGrid({
			title:"Min. Order Quantity/Amount",
			param:paramGrid,
			localSort: true,
			colNames:["City","Min. Order Quantity","Min. Order Amount"],
			colModel:[
				{name:"structureName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"minOrderQty",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:getString0},
				{name:"minOrderAmtTax",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:getString0}
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 表格初始化-商品分类列表
	var initProductGroupTable = function(){
		productGroupGridTable = $("#productGroupGridTable").zgGrid({
			title:"Product Group",
			param:paramGrid,
			localSort: true,
			colNames:["Product Group Name","Min. Order Amount"],
			colModel:[
				{name:"productGroupName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"productMOA",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:getString0}
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 表格初始化-大分类列表初始化
	var initDepartmentTable = function(){
		departmentGridTable = $("#departmentGridTable").zgGrid({
			title:"Department",
			param:paramGrid,
			localSort: true,
			colNames:["Top Department Code","Top Department","Department Code","Department Name"],
			colModel:[
				{name:"depCd",type:"text",text:"center",width:"100",ishide:true,css:""},
				{name:"depName",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"pmaCd",type:"text",text:"center",width:"100",ishide:true,css:""},
				{name:"pmaName",type:"text",text:"center",width:"130",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 表格初始化-银行信息列表初始化
	var initBankInfoTable = function(){
		bankInfoGridTable = $("#bankInfoGridTable").zgGrid({
			title:"Bank Information By Region",
			param:paramGrid,
			localSort: true,
			colNames:["Region","Bank Name","Bank Code","Bank Account","Bank Account Name","Branch","Bank Country"],
			colModel:[
				{name:"regionName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"payBank",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"bankCode",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"payBankAccount",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"payAccountName",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"branch",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"bankCountry",type:"text",text:"center",width:"130",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	//
	var initAccountGridTable = function(){
		accountGridTable = $("#accountGridTable").zgGrid({
			title:"",
			param:paramGrid,
			localSort: true,
			colNames:["Region","Account Payable","Account Receivable"],
			colModel:[
				{name:"regionName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"payable",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"receivable",type:"text",text:"center",width:"130",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 表格初始化
	var initTable = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Vendor Delivery Range",
			param:paramGrid,
			localSort: true,
			isPage: false, // 不需要分页
			colNames:["Order Type","Region","City","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"],
			colModel:[
				{name:"orderTypeName",type:"text",text:"center",width:"150",ishide:false,css:""},
				{name:"regionName",type:"text",text:"center",width:"150",ishide:false,css:""},
				{name:"structureName",type:"text",text:"center",width:"150",ishide:false,css:""},
				{name:"deliveryDay1",type:"text",text:"center",width:"150",ishide:false,css:""},
				{name:"deliveryDay2",type:"text",text:"center",width:"150",ishide:false,css:""},
				{name:"deliveryDay3",type:"text",text:"center",width:"150",ishide:false,css:""},
				{name:"deliveryDay4",type:"text",text:"center",width:"150",ishide:false,css:""},
				{name:"deliveryDay5",type:"text",text:"center",width:"150",ishide:false,css:""},
				{name:"deliveryDay6",type:"text",text:"center",width:"150",ishide:false,css:""},
				{name:"deliveryDay7",type:"text",text:"center",width:"150",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
		});
	}

	// 为0返回'0'
	function getString0(tdObj, value){
		var _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}

	function isDefault(tdObj, value) {
		var _value = '';
		if (value=='1') {
			_value = 'Yes';
		} else if (value=='0') {
			_value = 'No';
		}
		return $(tdObj).text(_value);
	}

	// 判断是否标识
	function isYesOrNo(tdObj, value){
		var _value = "";
		switch (value) {
			case "0":
				_value = "No";
				break;
			case "1":
				_value = "Yes";
				break;
		}
		return $(tdObj).text(_value);
	}

	// 格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		return res;
	}

	// 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
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
var _index = require('vendorDetails');
_start.load(function (_common) {
	_index.init(_common);
});
