require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('storeDetails', function () {
    var self = {};
    var url_left = "",
	    paramGrid = null,
		toThousands = null,
	    selectTrTemp = null,
    	common=null;
	const KEY = 'STORE_MASTER_QUERY';
	var m = {
		searchJson: null,
		_storeCd : null,
		_effectiveStartDate : null,
		back: null
	}

 	// 创建js对象
    var createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  	}
    }

    function init(_common) {
		createJqueryObj();
		url_left = _common.config.surl + "/storeMaster";
		toThousands = _common.toThousands;
		but_event();
		initTable();// 列表初始化
		// 查询加载数据
		getArticleMaster();
		//加载附加属性单选/多选 框
		getAttachedGroup("00","0");
		getAttachedGroup("10","1");
	}

	// 画面按钮点击事件
	var but_event = function(){
		// 返回按钮
		m.back.on("click",function(){
			_common.updateBackFlg(KEY);
			top.location = url_left;
		});
	}

	var getAttachedGroup = function (attributeType,type) {
		$.myAjaxs({
			url:url_left+"/getAttachedGroup",
			async:true,
			cache:false,
			type :"get",
			data :{
				code:'00',
				attributeType:attributeType
			},
			dataType:"json",
			success: function(result){
				if(result.success){
					var attachedGroup = result.data;
					var attachedStr = "";
					if(type=="0"){
						for (let i = 0; i < attachedGroup.length; i++) {
							let attached = attachedGroup[i];
							attachedStr += "<div class=\"radio-inline col-lg-3\">" +
								"<label>" +
								"<input type=\"radio\" disabled=\"disabled\" name=\"current_location\" id=\""+attached.attributeSelectCd+"\" value=\""+attached.attributeSelectCd+"\">" +
								attached.attributeSelectName+
								"</label>" +
								" </div>"
						}
						$("#current_location").html(attachedStr);
					}else if(type=="1"){
						for (let i = 0; i < attachedGroup.length; i++) {
							let attached = attachedGroup[i];
							attachedStr += "<div class=\"checkbox-inline col-lg-3\" style='margin-left: 0px'>" +
								"<label>" +
								"<input type=\"checkbox\" disabled=\"disabled\" name=\"surroundings\" id=\""+attached.attributeSelectCd+"\" value=\""+attached.attributeSelectCd+"\">" +
								attached.attributeSelectName+
								"</label>" +
								" </div>"
						}
						$("#surroundings").html(attachedStr);
					}
					//设置选中
					getAttachedInfo(attributeType);
				}
			},
			error : function(e){
				_common.prompt("Request Failed!",5,"error");
			}
		});
	}


	var getAttachedInfo = function (attributeType) {
		var storeCd = m._storeCd.val();
		var startDate = m._effectiveStartDate.val();
		if(storeCd == null ||$.trim(storeCd) == ''
			|| startDate == null ||$.trim(startDate) == ''){
			_common.prompt("Parameter is empty!",5,"error");
			return false;
		}
		$.myAjaxs({
			url:url_left+"/getAttachedInfo",
			async:true,
			cache:false,
			type :"get",
			data :{
				storeCd:storeCd,
				effectiveStartDate:startDate,
				attributeType:attributeType
			},
			dataType:"json",
			success: function(result){
				if(result.success){
					var attachedInfo = result.data;
					if(attachedInfo!=null){
						for (let i = 0; i < attachedInfo.length; i++) {
							let attached = attachedInfo[i];
							// if(attached.additionalAttribureValue=="1"){
								$("#"+attached.attributeSelectCd).prop("checked",true);
							// }
						}
					}
				}
			},
			error : function(e){
				_common.prompt("Request Failed!",5,"error");
			}
		});
	}

	// 获取主档基本信息
	var getArticleMaster = function(){
		var storeCd = m._storeCd.val();
		var startDate = m._effectiveStartDate.val();
		if(storeCd == null ||$.trim(storeCd) == ''
			|| startDate == null ||$.trim(startDate) == ''){
			_common.prompt("Parameter is empty!",5,"error");
			return false;
		}
		$.myAjaxs({
			url:url_left+"/getData",
			async:true,
			cache:false,
			type :"get",
			data :{
				storeCd : storeCd,
				effectiveStartDate : startDate
			},
			dataType:"json",
			success: function(result){
				if(result.success){
					// 加载基本信息
					$.each(result.data, function (key, value) {
						//日期格式
						if(key=="effectiveStartDate"||key=="effectiveEndDate"||key=="openDate"||key=="closeDate"
							||key=="renovationStartDate"||key=="renovationEndDate"||key=="systemStartDate"||key=="systemEndDate"
							||key=="testOrderStartDate"||key=="testOrderEndDate"||key=="orderStartDate"||key=="orderEndDate"
							||key=="saleStartDate"||key=="saleEndDate"||key=="leaseStartDate"||key=="leaseEndDate"){
							$("#storeForm #" + key).val(fmtIntDate(value));
						}else if(key=="storeFrontageLength"||key=="tradingArea"||key=="seatingArea"||key=="storageArea"
							||key=="totalStoreArea"||key=="storeArea"||key=="sharingArea"||key=="subLeaseArea"
							||key=="idleArea"||key=="totalLeasedArea"||key=="outSeatArea"||key=="numOfPos"
							||key=="unusedLevels"||key=="chillerDoors"||key=="racksOne"||key=="racksTwo"
							||key=="qtyOpenCase"||key=="lenOpenCase"||key=="lenCheckCounter"||key=="rent"
							||key=="depositAmount") {
							if(!!value)
								$("#storeForm #" + key).val(toThousands(value,2));
						}else{
							$("#storeForm #" + key).val(value);
						}
					})
					// 获取表格信息
					loadGridData();
				}else{
					_common.prompt(result.message,5,"error");
				}
			},
			error : function(e){
				_common.prompt("Request Failed!",5,"error");
			}
		});
	}

	// 表格初始化
	var initTable = function(){
		licenseTableGrid = $("#licenseGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			colNames:["Type of the License","License No.","Effective Start Date","Effective End Date","License Remark"],
			colModel:[
				{name:"licenseType",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"licenseNum",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"effectiveStartDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"effectiveEndDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"licenseRemark",type:"text",text:"left",width:"150",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
		accountingGridTable = $("#accountingGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			colNames:["Accounting Item","Account Code","Account Name"],
			colModel:[
				{name:"accountingItemName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"accountCd",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"accountName",type:"text",text:"left",width:"130",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
		competitorGridTable = $("#competitorGridTable").zgGrid({
			title:"Result",
			param:paramGrid,
			colNames:["Competitor Name","Competitor Code","Address","Distance from CK(m)","Approx Open Date","Open Date","Remarks"],
			colModel:[
				{name:"competitorName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"competitorId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"address",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"distanceCk",type:"text",text:"right",width:"120",ishide:false,css:""},
				{name:"approxOpen",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"openDate",type:"text",text:"center",width:"120",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"remarks",type:"text",text:"left",width:"150",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			isPage:false//是否需要分页
		});
	}

	// 加载表格信息
	var loadGridData = function(){
		paramGrid = "storeCd="+m._storeCd.val()+"&effectiveStartDate="+m._effectiveStartDate.val();
		licenseTableGrid.setting("url", url_left + "/getLicenseInfo");
		licenseTableGrid.setting("param", paramGrid);
		licenseTableGrid.loadData();
		accountingGridTable.setting("url", url_left + "/getAccountingInfo");
		accountingGridTable.setting("param", paramGrid);
		accountingGridTable.loadData();
		competitorGridTable.setting("url", url_left + "/getCompetitorInfo");
		competitorGridTable.setting("param", paramGrid);
		competitorGridTable.loadData();
	}


	// 为0返回'0'
	function getString0(tdObj, value){
		var _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}

	//日期字段格式化格式
	var dateFmt = function(tdObj, value){
		return $(tdObj).text(fmtIntDate(value));
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
var _index = require('storeDetails');
_start.load(function (_common) {
	_index.init(_common);
});
