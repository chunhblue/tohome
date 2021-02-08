require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");
var query_list = null;
var _myAjax=require("myAjax");
define('receiptReturnWarehouse', function () {
	var self = {};
	var url_left = "",
		paramGrid = null,
		approvalRecordsParamGrid = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		selectTrTemp = null,
		tempTrObjValue = {},//临时行数据存储
		tempTrOrderId = null,// 选中的单据编号
		tempTrOrderDate = null,//选择的退货时间
		dc = null,
		common=null;
	const KEY = 'ITEM_RETURN_ENTRY_TO_DC';
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		searchJson:null,
		// 查询部分
		rt_date:null,
		order_id:null,
		org_order_id:null,
		delivery_center_id:null,
		search:null,
		reset:null,
		status : null,
		aRegion : null,
		aCity : null,
		aDistrict : null,
		aStore : null,
		typeId:null,//审核类型id
		rt_start_date:null,
		rt_end_date:null,
		reviewStatus:null,
		typeId:null,//审核类型id
	}
	// 创建js对象
	var  createJqueryObj = function(){
		for (x in m)  {
			m[x] = $("#"+x);
		}
	}
	function init(_common) {
		createJqueryObj();
		url_left=_common.config.surl+"/returnWarehouse";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		but_event();
		initTable1();//列表初始化
		// 初始化店铺运营组织检索
		_common.initOrganization();
		//表格内按钮事件
		table_event();
		//权限验证
		isButPermission();
		// 初始化检索日期
		_common.initDate(m.rt_start_date,m.rt_end_date);
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

		m.rt_start_date.val(obj.orderStartDate);
		m.rt_end_date.val(obj.orderEndDate);
		m.order_id.val(obj.orderId);
		m.org_order_id.val(obj.orgOrderId);
		m.reviewStatus.val(obj.reviewStatus);
		$.myAutomatic.setValueTemp(dc,obj.deliveryCenterId,obj.deliveryCenterName);
		// 设置组织架构回显
		_common.setAutomaticVal(obj);

		//拼接检索参数
		setParamJson();
		paramGrid = "searchJson="+m.searchJson.val();
		tableGrid.setting("url",url_left+"/query");
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
		obj.orderStartDate=m.rt_start_date.val();
		obj.orderEndDate=m.rt_end_date.val();
		obj.page=tableGrid.getting("page");
		obj.flg='0';
		sessionStorage.setItem(KEY,JSON.stringify(obj));
	}

	//表格按钮事件
	var table_event = function(){
		$("#modify").on("click", function (e) {
			//验证当前batch处理是否禁用
			let batchFlg = false;
			_common.batchCheck(function (result) {
				if(!result.success){//禁用
					_common.prompt(result.message,5,"info");
					batchFlg = false;
				}else{
					batchFlg = true;
				}
			})
			if(!batchFlg){return batchFlg};
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}else{
				var cols = tableGrid.getSelectColValue(selectTrTemp,"orderSts,reviewStatus,status");
				var orderSts = cols["orderSts"];
				var reviewStatus = cols["reviewStatus"];
				//判断是否已经收货
				if(orderSts!='04'&&reviewStatus=='Approved'){
					//获取数据审核状态
					_common.getRecordStatus(tempTrOrderId,m.typeId.val(),function (result) {
						if(result.success){
							let flag = 2;
							if(cols["status"] == ""){
								flag = 1;
							}
							saveParamToSession();
							top.location = url_left+"/receipt/edit?flag="+flag+"&orderId="+ tempTrOrderId ;
						}else{
							_common.prompt(result.message,5,"error");
						}
					});
				}else{
					_common.prompt("Selected document is already returned,<br>please correct return quantity from receiving and return document correction entry screen.",3,"info");
				}
			}
		});

		$("#view").on("click", function (e) {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}else{
				saveParamToSession();
				top.location = url_left+"/receipt/edit?flag=0&orderId="+ tempTrOrderId;
			}
		});

		$("#print").on("click",function(){
			if(verifySearch()) {
				setParamJson();
				paramGrid = "searchJson=" + m.searchJson.val();
				var url = url_left + "/receipt/print?" + paramGrid;
				window.open(encodeURI(url), "excelprint", "width=1400,height=600,scrollbars=yes");
			}
		});
		// 导出按钮点击事件
		$("#export").on("click",function(){
			if(verifySearch()){
				// 拼接检索参数
				setParamJson();
				paramGrid = "searchJson=" + m.searchJson.val();
				var url = url_left + "/receipt/export?" + paramGrid;
				window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
			}
		});

		$("#approvalRecords").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			approvalRecordsParamGrid = "id="+tempTrOrderId+
				"&typeIdArray="+m.typeId.val();
			approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
			approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
			approvalRecordsTableGrid.setting("page", 1);
			approvalRecordsTableGrid.loadData(null);
			$('#approvalRecords_dialog').modal("show");
		});
	}

	//拼接检索参数
	var setParamJson = function(){
		// 创建请求字符串
		var	_reviewStatus = null;
		if(m.reviewStatus.val()!==""){
			_reviewStatus = m.reviewStatus.val()
		}
		var	_status = null;
		if(m.status.val()!==""){
			_status = m.status.val()
		}
		var searchJsonStr ={
			regionCd:$("#aRegion").attr("k"),
			cityCd:$("#aCity").attr("k"),
			districtCd:$("#aDistrict").attr("k"),
			storeCd:$("#aStore").attr("k"),
			// orderDate : fmtIntDate(String(m.rt_date.val())),
			orderStartDate:fmtIntDate(String(m.rt_start_date.val())),
			orderEndDate:fmtIntDate(String(m.rt_end_date.val())),
			orderId :m.order_id.val().trim(),
			orgOrderId :m.org_order_id.val().trim(),
			deliveryCenterId :$("#delivery_center_id").attr("k"),
			deliveryCenterName :$("#delivery_center_id").attr("v"),
			returnSts:"10",//退货状态  为审核通过的退货
			reviewStatus : _reviewStatus,
			status : _status
		};
		m.searchJson.val(JSON.stringify(searchJsonStr));
	}

	function judgeNaN (value) {
		return value !== value;
	}

	//验证检索项是否合法
	var verifySearch = function () {
		let _StartDate = null;
		if(!$("#rt_start_date").val()){
			_common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
			$("#rt_start_date").focus();
			return false;
		}else{
			_StartDate = new Date(fmtDate($("#rt_start_date").val())).getTime();
			if(judgeNaN(_StartDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#rt_start_date").focus();
				return false;
			}
		}
		let _EndDate = null;
		if(!$("#rt_end_date").val()){
			_common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
			$("#rt_end_date").focus();
			return false;
		}else{
			_EndDate = new Date(fmtDate($("#rt_end_date").val())).getTime();
			if(judgeNaN(_EndDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#rt_end_date").focus();
				return false;
			}
		}
		if(_StartDate>_EndDate){
			_common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
			$("#rt_end_date").focus();
			return false;
		}
		let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
		if(difValue >62){
			_common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
			$("#rt_end_date").focus();
			return false;
		}
		return true;
	}

	//画面按钮点击事件
	var but_event = function(){
		//检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()){
				//拼接检索参数
				setParamJson();
				paramGrid = "searchJson=" + m.searchJson.val();
				tableGrid.setting("url", url_left + "/query");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);
				tableGrid.loadData(null);
			}
		});
		m.reset.on("click",function(){
			$("#regionRemove").click();
			m.rt_start_date.val("");
			m.rt_end_date.val("");
			m.order_id.val("");
			m.org_order_id.val("");
			$("#dc_clear").click();
			m.reviewStatus.val("");
			m.searchJson.val("");
			selectTrTemp = null;
			_common.clearTable();
		});

		//dc
		dc = $("#delivery_center_id").myAutomatic({
			url: url_left+"/getDc",
			ePageSize: 10,
			startCount: 0
		});
	}

	// 选项选中事件
	var trClick_table1 = function(){
		var cols = tableGrid.getSelectColValue(selectTrTemp,"orderDate,orderId,storeCd");
		tempTrOrderId = cols["orderId"];
		tempTrOrderDate =fmtIntDate(cols["orderDate"]);
		tempTrStoreCd = cols["storeCd"];
	}

	//表格初始化-退仓库申请单样式（收货）
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Returning to Warehouse Request Note Query",
			param:paramGrid,
			colNames:["Date Returned","Document No.","Original Document No.","DC No.","DC Name",
				"Store No.","Store Name","Order Sts","Document Status","Status","Return Qty","Actual Return Qty","Return Amount"],
			colModel:[
				{name:"orderDate",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"orderId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"orgOrderId",type:"text",text:"right",width:"160",ishide:false,css:""},
				{name:"deliveryCenterId",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"deliveryCenterName",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"storeCd",type:"text",text:"right",width:"80",ishide:false,css:""},
				{name:"storeName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"orderSts",type:"text",text:"left",width:"80",ishide:true,css:""},
				{name:"reviewStatus",type:"text",text:"left",width:"100",ishide:false,css:"",getCustomValue:getStatus},
				{name:"status",type:"text",text:"left",width:"80",ishide:false,css:"",getCustomValue:getStatus},
				{name:"orderQty",type:"text",text:"right",width:"80",ishide:false,css:"",getCustomValue:getThousands},
				{name:"receiveQty",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
				{name:"orderAmt",type:"text",text:"right",width:"80",ishide:true,css:"",getCustomValue:getThousands}
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			sidx:"orderDate",//排序字段
			sord:"desc",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			eachTrClick:function(trObj){//正常左侧点击
				selectTrTemp = trObj;
				// 选中选项后的事件
				trClick_table1();
			},
			buttonGroup:[
				{
					butType: "update",
					butId: "modify",
					butText: "Modify",
					butSize: ""
				},
				{
					butType: "view",
					butId: "view",
					butText: "View",
					butSize: ""
				},
				{
					butType:"custom",
					butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
				},
				{
					butType:"custom",
					butHtml:"<button id='print' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-print'></span> Print</button>"
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

	// 获取审核状态
	var getStatus = function(tdObj, value){
		switch (value) {
			case "1":
				value = "Pending";
				break;
			case "5":
				value = "Rejected";
				break;
			case "6":
				value = "Withdrawn";
				break;
			case "7":
				value = "Expired";
				break;
			case "10":
				value = "Approved";
				break;
			case "20":
				value = "Returned";
				break;
			default:
				value = "";
		}
		return $(tdObj).text(value);
	}

	// 按钮权限验证
	var isButPermission = function () {
		var viewBut = $("#viewBut").val();
		if (Number(viewBut) != 1) {
			$("#view").remove();
		}
		var editBut = $("#editBut").val();
		if (Number(editBut) != 1) {
			$("#modify").remove();
		}
		var printBut = $("#printBut").val();
		if (Number(printBut) != 1) {
			$("#print").remove();
		}
		var exportBut = $("#exportBut").val();
		if (Number(exportBut) != 1) {
			$("#exportBut").remove();
		}
		var approvalBut = $("#approvalBut").val();
		if (Number(approvalBut) != 1) {
			$("#approvalRecords").remove();
		}
	}

	//小数格式化
	var floatFmt = function(tdObj, value){
		if(!!value){value = parseFloat(value);}
		return $(tdObj).text(value);
	}

	//日期字段格式化格式
	var dateFmt = function(tdObj, value){
		if(value!=null&&value.trim()!=''&&value.length==8) {
			value = fmtStringDate(value);
		}
		return $(tdObj).text(value);
	}
	// 格式化数字类型的日期 yyyyMMdd → dd/MM/yyyy
	function fmtStringDate(date){
		if(!!date) {
			return date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		}
	}
	// DD/MM/YYYY to YYYY-MM-DD  格式转换
	function fmtDate(date) {
		var res = '';
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
		return res;
	}
	//字符串日期格式转换：dd/MM/yyyy → yyyyMMdd
	function fmtIntDate(date){
		if(!!date){
			return date.substring(6,10)+date.substring(3,5)+date.substring(0,2);
		}
	}
	self.init = init;
	return self;
});
var _start = require('start');
var _index = require('receiptReturnWarehouse');
_start.load(function (_common) {
	_index.init(_common);
});
