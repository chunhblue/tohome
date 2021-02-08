require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('vouchersList', function () {
    var self = {};
    var url_left = "",
		url_root = "",
		reason = null,
	    paramGrid = null,
	    selectTrTemp = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
	    tempTrObjValue = {},//临时行数据存储
		approvalRecordsParamGrid = null,
    	common=null;
	const KEY = 'INVENTORY_DOCUMENTS_QUERY';
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		voucherStartDate : null,
		voucherEndDate : null,
		voucherType: null,
		isUpload: null,
		voucherNo: null,
		voucherStatus: null,
		aRegion : null,
		aCity : null,
		aDistrict : null,
		aStore : null,
		search: null,
		itemInfo : null,
		reason : null,
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
		url_left = url_root + "/inventoryVoucher";
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
		// 加载下拉列表
		getSelectValue();
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// 初始化店铺运营组织检索
		_common.initOrganization();
		// 表格内按钮事件
		table_event();
		//权限验证
		isButPermission();
		// 执行查询
		// m.search.click();
		// 初始化检索日期
		_common.initDate(m.voucherStartDate,m.voucherEndDate);
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
		if (obj.reviewSts=='-1') {
			obj.reviewSts='';
		}
		m.voucherStartDate.val(obj.voucherStartDate);
		m.voucherEndDate.val(obj.voucherEndDate);
		m.isUpload.val(obj.upload);
		m.voucherNo.val(obj.voucherNo);
		m.voucherStatus.val(obj.reviewSts);
		m.voucherType.val(obj.voucherType);
		m.itemInfo.val(obj.itemInfo);
		$.myAutomatic.setValueTemp(reason,obj.reason,obj.reasonName);
		// 设置组织架构回显
		_common.setAutomaticVal(obj);

		//拼接检索参数
		setParamJson();
		paramGrid = "searchJson="+m.searchJson.val();
		tableGrid.setting("url",url_left+"/getdata");
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
		obj.regionName=$('#aRegion').attr('v');
		obj.cityName=$('#aCity').attr('v');
		obj.districtName=$('#aDistrict').attr('v');
		obj.storeName=$('#aStore').attr('v');
		obj.voucherNo=m.voucherNo.val();
		obj.voucherStartDate=m.voucherStartDate.val();
		obj.voucherEndDate=m.voucherEndDate.val();
		obj.upload=m.isUpload.val();
		obj.reviewSts=m.voucherStatus.val();
		obj.voucherType=m.voucherType.val();
		obj.itemInfo=m.itemInfo.val();
		obj.reason=$('#reason').attr('k');
		obj.page=tableGrid.getting("page");
		obj.flg='0';
		sessionStorage.setItem(KEY,JSON.stringify(obj));
	}

	// 表格内按钮点击事件
	var table_event = function(){
		// modify按钮
		$("#updateStoreDetails").on("click", function (e) {
			//验证当前batch处理是否禁用
			let batchFlg = false;
			_common.batchCheck(function (result) {
				if(!result.success){//禁用
					_common.prompt(result.message,5,"info");
					batchFlg = false;
				}else{
					batchFlg = true;
				}
			});
			if(!batchFlg){return batchFlg};
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}else{
				var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,voucherNo,reviewId,voucherDate,voucherType,storeCd1");
				var _type = cols["voucherType"], _url = '';
				if(_type == '603'){
					_url = '/stockScrap';
				}else if(_type == '604'){
					_url = '/stockAdjustment';
				}else if(_type == '500'||_type == '501'||_type == '502'||_type == '506'){
					_url = '/storeTransfers';
				}else if(_type == '504'||_type == '505'){
					saveParamToSession();
					_url = '/transferCorrection';
					// _common.prompt("The Store Transfer Correction cannot be modified",5,"info");
					// return false;
				} else if (_type == '601'||_type == '602') {
					_url = '/itemTransfer';
				} else {
					_common.prompt("Unknown Type",5,"error");
					return false;
				}
				let reviewId = cols["reviewId"];
				if(reviewId==null||reviewId==""){
					reviewId = 0;
				}
				//获取数据审核状态
				_common.getRecordStatus(cols["voucherNo"],0,function (result) {
					if(result.success){
						var voucherDate = '';
						if(cols["voucherDate"]!=null&&cols["voucherDate"]!=''){
							voucherDate = subfmtDate(cols["voucherDate"]);
						}
						var param = "storeCd=" + cols["storeCd"] + "&voucherNo=" + cols["voucherNo"]+
							"&voucherDate=" + voucherDate + "&voucherType=" + _type +
							"&storeCd1=" + cols["storeCd1"] + "&flag=edit";
						top.location = url_root+ _url +"/edit?"+param;
					}else{
						_common.prompt(result.message,5,"error");
					}
				},reviewId);
			}
		});

		// view按钮
		$("#viewStoreDetails").on("click", function (e) {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}else{
				var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,voucherNo,voucherDate,voucherType,storeCd1");
				var _type = cols["voucherType"], _url = '';
				if(_type == '603'){
					_url = '/stockScrap';
				} else if (_type == '604'){
					_url = '/stockAdjustment';
				} else if (_type == '500'||_type == '501'||_type == '506'){
					_url = '/storeTransferIn';
				} else if (_type == '502') {
					_url = '/storeTransfers';
				} else if(_type == '504'||_type == '505'){
					saveParamToSession();
					_url = '/transferCorrection';
				} else if (_type == '601'||_type == '602') {
					_url = '/itemTransfer';
				} else {
					_common.prompt("Unknown Type",5,"error");
					return false;
				}
				var voucherDate = '';
				if(cols["voucherDate"]!=null&&cols["voucherDate"]!=''){
					voucherDate = subfmtDate(cols["voucherDate"]);
				}
				var param = "storeCd=" + cols["storeCd"] + "&voucherNo=" + cols["voucherNo"]+
					"&voucherDate=" + voucherDate + "&voucherType=" + _type+
					"&storeCd1=" + cols["storeCd1"];
				top.location = url_root+ _url +"/view?"+param;
			}
		});

		//审核记录
		$("#approvalRecords").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"voucherNo,reviewId");
			let reviewId = cols['reviewId'];
			if(reviewId==null||reviewId==""){
				reviewId = 0;
			}
			approvalRecordsParamGrid = "id="+cols['voucherNo']+
				"&reviewId="+reviewId;
			approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
			approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
			approvalRecordsTableGrid.setting("page", 1);
			approvalRecordsTableGrid.loadData(null);
			$('#approvalRecords_dialog').modal("show");
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

	//画面按钮点击事件
	var but_event = function(){
		// 清空按钮
		m.reset.on("click",function(){
			m.voucherStartDate.val("");
			m.voucherEndDate.val("");
			m.voucherType.val("");
			m.isUpload.val("");
			m.voucherNo.val("");
			m.voucherStatus.val("");
			m.itemInfo.val("");
			$("#regionRemove").click();
			$("#reasonRemove").click();
			$("#amRemove").click();
			$("#omRemove").click();
			$("#ocRemove").click();
			selectTrTemp = null;
			_common.clearTable();
		});
		// 检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()){
				//拼接检索参数
				setParamJson();
				paramGrid = "searchJson="+m.searchJson.val();
				tableGrid.setting("url",url_left+"/getdata");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);
				tableGrid.loadData();
			}
		});
	}

	function judgeNaN (value) {
		return value !== value;
	}

	// 验证检索项是否合法
	var verifySearch = function(){
		let _StartDate = null;
		if(!$("#voucherStartDate").val()){
			_common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
			$("#voucherStartDate").focus();
			return false;
		}else{
			_StartDate = new Date(fmtDate($("#voucherStartDate").val())).getTime();
			if(judgeNaN(_StartDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#voucherStartDate").focus();
				return false;
			}
		}
		let _EndDate = null;
		if(!$("#voucherEndDate").val()){
			_common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
			$("#voucherEndDate").focus();
			return false;
		}else{
			_EndDate = new Date(fmtDate($("#voucherEndDate").val())).getTime();
			if(judgeNaN(_EndDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#voucherEndDate").focus();
				return false;
			}
		}
		if(_StartDate>_EndDate){
			$("#voucherEndDate").focus();
			_common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
			return false;
		}
		let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
		if(difValue>62){
			_common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
			$("#voucherEndDate").focus();
			return false;
		}
		return true;
	}
	// 拼接检索参数
	var setParamJson = function(){
		// 传票日期
		var _startDate = fmtStringDate(m.voucherStartDate.val())||null;
		var _endDate = fmtStringDate(m.voucherEndDate.val())||null;

		// 创建请求字符串
		var searchJsonStr ={
			regionCd:$("#aRegion").attr("k"),
			cityCd:$("#aCity").attr("k"),
			districtCd:$("#aDistrict").attr("k"),
			storeCd:$("#aStore").attr("k"),
			voucherNo:m.voucherNo.val().trim(),
			voucherStartDate:_startDate,
			voucherEndDate:_endDate,
			upload:m.isUpload.val().trim(),
			reviewSts:m.voucherStatus.val().trim()||'-1',
			voucherType:m.voucherType.val().trim(),
			itemInfo:m.itemInfo.val().trim(),
			reason:$('#reason').attr('k'),
			omCode:$("#om").attr("k"),
			ocCode:$("#oc").attr("k"),
			amCode:$("#am").attr("k"),
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
			colNames:["Document Date","Document No.","Store No.","Store Name","Area Manager Code","Area Manager Name","Operation Manager","Operation Manager Name","Operation Controller Code ","Operation Controller Name","Store No.","Document Type","Document Type","Review Id","Document Status",
				"Remarks","Amount","Upload","Date Created","Created By"],
			colModel:[
				{name:"voucherDate",type:"text",text:"center",width:"130",ishide:false,getCustomValue:dateFmt},
				{name:"voucherNo",type:"text",text:"right",width:"110",ishide:false,css:""},
				{name:"storeCd",type:"text",text:"right",width:"100",ishide:true,css:""},
				{name:"storeName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"ofc",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"ofcName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"om",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"omName",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"oc",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"ocName",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"storeCd1",type:"text",text:"right",ishide:true},
				{name:"voucherType",type:"text",text:"left",ishide:true},
				{name:"voucherType",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getTypeName},
				{name:"reviewId",type:"text",text:"left",width:"130",ishide:true,css:""},
				{name:"stsName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"remark",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"voucherAmtNoTax",type:"text",text:"right",width:"160",ishide:true,getCustomValue:getThousands},
				{name:"uploadName",type:"text",text:"left",width:"80",ishide:false,css:""},
				{name:"createYmd",type:"text",text:"center",width:"150",ishide:false,getCustomValue:_common.formatDateAndTime},
				{name:"createUser",type:"text",text:"left",width:"130",ishide:false}
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
					butType: "update",
					butId: "updateStoreDetails",
					butText: "Modify",
					butSize: ""
				},
				{
					butType: "view",
					butId: "viewStoreDetails",
					butText: "View",
					butSize: ""
				},
				{
					butType:"custom",
					butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
				},
				{
					butType:"custom",
					butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
				}
			],
		});

		//审核记录
		approvalRecordsTableGrid = $("#approvalRecordsTable").zgGrid({
			title:"Approval Records",
			param:approvalRecordsParamGrid,
			colNames:["Approval Type","Result","User Code","User Name","Date Time","Comments"],
			colModel:[
				{name:"typeName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"result",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"userCode",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"userName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"dateTime",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"comments",type:"text",text:"center",width:"200",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			sidx:"",//排序字段
			sord:"",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			}
		});
	}

	// 按钮权限验证
	var isButPermission = function () {
		var editButPm = $("#editButPm").val();
		if (Number(editButPm) != 1) {
			$("#updateStoreDetails").remove();
		}
		var viewButPm = $("#viewButPm").val();
		if (Number(viewButPm) != 1) {
			$("#viewStoreDetails").remove();
		}
		var exportButPm = $("#exportButPm").val();
		if (Number(exportButPm) != 1) {
			$("#export").remove();
		}
	}

	// 日期字段格式化格式
	var dateFmt = function(tdObj, value){
		if(value!=null&&value.trim()!=''&&value.length==8) {
			value = fmtIntDate(value);
		}
		return $(tdObj).text(value);
	}

	// 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
	function fmtStringDate(date){
		var res = "";
		date = date.replace(/\//g,"");
		res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
		return res;
	}

	// 格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		return res;
	}

	// 日期处理
	function subfmtDate(date){
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

	// 获取传票类型名称
	function getTypeName(tdObj, value){
		var _value = "";

		switch (value) {
			case "601":
				_value = "In-store Transfer";
				break;
			case "602":
				_value = "In-store Transfer";
				break;
			case "603":
				_value = "Inventory Write-off";
				break;
			case "604":
				_value = "Inventory Adjustment";
				break;
			case "605":
				_value = "Take Stock";
				break;
			case "500":
				_value = "Transfer Instructions";
				break;
			case "501":
				_value = "Store Transfer In";
				break;
			case "502":
				_value = "Store Transfer Out";
				break;
			case "504":
				_value = "Transfer In Correction";
				break;
			case "505":
				_value = "Transfer Out Correction";
				break;
			case "506":
				_value = "Transfer";
				break;
			case "507":
				_value = "Inventory";
				break;
		}

		/*switch (value) {
			case "601":
				_value = "In-store Transfer In";
			break;
			case "602":
				_value = "In-store Transfer Out";
			break;
			case "603":
				_value = "Scrap";
			break;
			case "604":
				_value = "Stock Adjustment";
			break;
			case "605":
				_value = "Take Stock";
			break;
			case "500":
				_value = "Transfer Instructions";
			break;
			case "501":
				_value = "Store Transfer In";
			break;
			case "502":
				_value = "Store Transfer Out";
			break;
			case "506":
				_value = "Transfer";
			break;
		}*/
		return $(tdObj).text(_value);
	}

	// 为0返回'0'
	function getString0(tdObj, value){
		var _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}

	// 初始化下拉列表
	function initSelectOptions(title, code, fun) {
		// 共通请求
		$.myAjaxs({
			url:url_root+"/cm9010/getCode",
			async:false,
			cache:false,
			type :"post",
			data :"codeValue="+code,
			dataType:"json",
			success : fun,
			error : function(e){
				_common.prompt(title+" Failed to load data!",5,"error");
			}
		});
	}

	// 请求加载下拉列表
	function getSelectValue(){
		// 加载select
		initSelectOptions("Document Status","00430", function (result) {
			let selectObj = $("#voucherStatus");
			selectObj.find("option:not(:first)").remove();
			for (let i = 0; i < result.length; i++) {
				let optionValue = result[i].codeValue;
				if(optionValue == '20'){
					continue;
				}
				let optionText = result[i].codeName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
		initSelectOptions("Upload","00245",function (result) {
			let selectObj = $("#isUpload");
			selectObj.find("option:not(:first)").remove();
			for (let i = 0; i < result.length; i++) {
				let optionValue = result[i].codeValue;
				let optionText = result[i].codeName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
		reason=$("#reason").myAutomatic({
			url:url_root+"/cm9010/getResonCode",
			ePageSize:10,
			startCount:0,
		})
		am = $("#am").myAutomatic({
			url: url_root + "/ma1000/getAMByPM",
			ePageSize: 10,
			startCount: 0,
		});
		om = $("#om").myAutomatic({
			url: url_root + "/ma1000/getOM",
			ePageSize: 10,
			startCount: 0,
		});
		oc = $("#oc").myAutomatic({
			url: url_root + "/ma1000/getOC",
			ePageSize: 10,
			startCount: 0,
		});



	}

	self.init = init;
	return self;
});

var _start = require('start');
var _index = require('vouchersList');
_start.load(function (_common) {
	_index.init(_common);
});
