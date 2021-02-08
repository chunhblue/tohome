require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('paymentMode', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
	    paramGrid = null,
		approvalRecordsParamGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
    	common=null;
	const KEY = 'POS_PAYMENT_METHODS_MANAGEMENT';
    var m = {
		reset: null,
		search: null,
		pay_cd : null,//支付编号
		pay_name : null,//支付名称
		reviewStatus : null,//审核状态
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
    	url_left =_common.config.surl+"/paymentMode";
		systemPath = _common.config.surl;
    	//事件绑定
		but_event();
		//列表初始化
		initTable1();
		//表格内按钮事件
		table_event();
		//权限验证
		isButPermission();
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
		// 日期格式转换
		m.pay_cd.val(obj.payCd);
		m.pay_name.val(obj.payName);
		m.reviewStatus.val(obj.reviewStatus);
		paramGrid = "payCd="+m.pay_cd.val().trim()+
			"&payName="+m.pay_name.val().trim()+
			"&reviewStatus="+m.reviewStatus.val();
		tableGrid.setting("url",url_left+"/getList");
		tableGrid.setting("param", paramGrid);
		tableGrid.setting("page", obj.page);
		tableGrid.loadData(null);
	}

	// 保存 参数信息
	var saveParamToSession = function () {
		if (!paramGrid) {
			return;
		}
		let obj = {};
		paramGrid.replace(/([^?&]+)=([^?&]+)/g, function(s, v, k) {
			obj[v] = decodeURIComponent(k);//解析字符为中文
			return k + '=' +  v;
		});
		obj.page=tableGrid.getting("page");
		obj.flg='0';
		sessionStorage.setItem(KEY,JSON.stringify(obj));
	}

    //画面按钮点击事件
    var but_event = function(){
		//重置搜索
		m.reset.on("click",function(){
			m.pay_cd.val("");
			m.pay_name.val("");
			m.reviewStatus.val("");
			selectTrTemp = null;
			paramGrid = '';
			_common.clearTable();
		});
    	//检索按钮点击事件
    	m.search.on("click",function(){
			paramGrid = "payCd="+m.pay_cd.val().trim()+
				"&payName="+m.pay_name.val().trim()+
				"&reviewStatus="+m.reviewStatus.val().trim();
			tableGrid.setting("url",url_left+"/getList");
			tableGrid.setting("param", paramGrid);
			tableGrid.setting("page", 1);
			tableGrid.loadData(null);
    	});

    }

	//表格按钮事件
	var table_event = function(){
		$("#add").on("click", function () {
			saveParamToSession();
			top.location = url_left+"/edit?operateFlg=add";
		})
		$("#modify").on("click", function () {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"payCd");
			var param = "operateFlg=edit&payCd="+cols["payCd"];
			//获取数据审核状态
			_common.getRecordStatus(cols["payCd"],m.typeId.val(),function (result) {
				if (!result.success &&  result.data=="10"){
					saveParamToSession();
					top.location = url_left+"/edit?"+param+"&auditFlag=auditted";
				}else if(result.success){
					saveParamToSession();
					top.location = url_left+"/edit?"+param;
				}else{
					_common.prompt(result.message,5,"error");
				}
			});
		})
		$("#view").on("click", function () {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"payCd");
			var param = "operateFlg=view&payCd="+cols["payCd"];
			saveParamToSession();
			top.location = url_left+"/edit?"+param;
		})
		//审核记录
		$("#approvalRecords").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"payCd");
			approvalRecordsParamGrid = "id="+cols["payCd"]+
				"&typeIdArray="+m.typeId.val().trim();
			approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
			approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
			approvalRecordsTableGrid.setting("page", 1);
			approvalRecordsTableGrid.loadData(null);
			$('#approvalRecords_dialog').modal("show");
		});
	}

    //表格初始化-POS支付方式设定头档列表样式
    var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
    		title:"Query Result",
    		param:paramGrid,
    		colNames:["Payment No.","Payment Name","Print Name","Status","Modified By","Date Modified"],
    		colModel:[
    		  {name:"payCd",type:"text",text:"right",width:"100",ishide:false,css:""},
			  {name:"payName",type:"text",text:"left",width:"130",ishide:false,css:""},
			  {name:"payNamePrint",type:"text",text:"left",width:"130",ishide:false,css:""},
			  {name:"reviewStatus",type:"text",text:"left",width:"130",ishide:false,css:"",getCustomValue:getStatus},
			  {name:"updateUserName",type:"text",text:"left",width:"130",ishide:true,css:""},
			  {name:"updateDate",type:"text",text:"center",width:"130",ishide:true,css:"",getCustomValue:dateFmtYMDHMS},
			],//列内容
			width:"max",//宽度自动
			isPage:true,//是否需要分页
			page:1,//当前页
			sidx:"pay_cd",//排序字段
			sord:"desc",//升降序
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
					butType: "add",
					butId: "add",
					butText: "Add",
					butSize: ""
				}, {
					butType: "update",
					butId: "modify",
					butText: "Modify",
					butSize: ""
				}, {
					butType: "view",
					butId: "view",
					butText: "View",
					butSize: ""
				},
				{
					butType:"custom",
					butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
				},
			]
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
		var paymentButView = $("#paymentButView").val();
		if (Number(paymentButView) != 1) {
			$("#view").remove();
		}
		var paymentButAdd = $("#paymentButAdd").val();
		if (Number(paymentButAdd) != 1) {
			$("#add").remove();
		}
		var paymentButEdit = $("#paymentButEdit").val();
		if (Number(paymentButEdit) != 1) {
			$("#modify").remove();
		}
		var approvalBut = $("#approvalBut").val();
		if (Number(approvalBut) != 1) {
			$("#approvalRecords").remove();
		}
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
			default:
				value = "Unknown";
		}
		return $(tdObj).text(value);
	}

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
    	return $(tdObj).text(fmtIntDate(value));
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

	var dateFmtYMDHMS = function(tdObj, value){
		return $(tdObj).text(fmtYMDHMSDate(value));
	}

	function fmtYMDHMSDate(date){
		var res = "";
		res = date.replace(/^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/, '$3/$2/$1 $4:$5:$6');
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
var _index = require('paymentMode');
_start.load(function (_common) {
	_index.init(_common);
});
