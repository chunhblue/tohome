require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('dayOrderFailure', function () {
    var self = {};
    var url_left = "",
		url_root = "",
	    paramGrid = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
		checkValue = null,
    	common=null;
    var m = {
		toKen : null,
		use : null,
		main_box : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		bf_start_date : null,
		bf_end_date : null,
		clear_bf_date : null,
		failedReason : null,
		barcode : null,
		articleId : null,
		search : null,
		reset : null
    }

	// 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
		}
    }

    function init(_common) {
    	createJqueryObj();
		url_root = _common.config.surl;
    	url_left = url_root + "/orderFailed";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
    	// 首先验证当前操作人的权限是否混乱，
		if(m.use.val()=="0"){
			but_event();
		}else{
			m.error_pcode.show();
			m.main_box.hide();
		}
		// 加载下拉列表
		getSelectValue();
    	// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		table_event();
		//权限验证
		isButPermission();
		// 初始化组织架构
		_common.initOrganization();
		// 初始化检索日期
		_common.initDate(m.bf_start_date,m.bf_end_date);
    }

    //表格内按钮点击事件
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

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
    	switch(flgType) {
			case "1":
				initTable1();//列表初始化
				break;
			case "2":
			   break;
			case "3":
				break;
			case "4":
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
    }
    
    // 画面按钮点击事件
    var but_event = function(){
    	// 检索按钮点击事件
    	m.search.on("click",function(){
    		if(verifySearch()){
				// 拼接检索参数
				setParamJson();
				paramGrid = "searchJson="+m.searchJson.val();
				tableGrid.setting("url",url_left+"/getdata");
    			tableGrid.setting("param", paramGrid);
    			tableGrid.setting("page", 1);
				tableGrid.loadData();
    		}
    	});
    	// 清空按钮事件
    	m.reset.on("click",function(){
			$("#regionRemove").click();
			m.bf_start_date.val("");
			m.bf_end_date.val("");
			m.barcode.val("");
			m.query_type_val.val("");
			selectTrTemp = null;
			_common.clearTable();
    	});
		$('input:radio[name="query_type"]').on('change',function(){
			checkValue = $('input:radio[name="query_type"]:checked').val();
		});
    }

	// 判断是否是数字
	var checkNum = function(value){
		var reg = /^[0-9]*$/;
		return reg.test(value);
	}

	function judgeNaN (value) {
		return value !== value;
	}
    
    // 验证检索项是否合法
    var verifySearch = function(){
		let _StartDate = null;
		if(!$("#bf_start_date").val()){
			_common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
			$("#bf_start_date").focus();
			return false;
		}else{
			_StartDate = new Date(fmtDate($("#bf_start_date").val())).getTime();
			if(judgeNaN(_StartDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#bf_start_date").focus();
				return false;
			}
		}
		let _EndDate = null;
		if(!$("#bf_end_date").val()){
			_common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
			$("#bf_end_date").focus();
			return false;
		}else{
			_EndDate = new Date(fmtDate($("#bf_end_date").val())).getTime();
			if(judgeNaN(_EndDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#bf_end_date").focus();
				return false;
			}
		}
		if(_StartDate>_EndDate){
			_common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
			$("#bf_end_date").focus();
			return false;
		}
		let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
		if(difValue>62){
			_common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
			$("#bf_end_date").focus();
			return false;
		}
    	return true;
    }
    
    // 拼接检索参数
    var setParamJson = function(){

		// 订货日期
		var _orderStartDate = fmtStringDate(m.bf_start_date.val())||null;
		var _orderEndDate = fmtStringDate(m.bf_end_date.val())||null;
		// 创建请求字符串
		var searchJsonStr ={
			regionCd:$("#aRegion").attr("k"),
			cityCd:$("#aCity").attr("k"),
			districtCd:$("#aDistrict").attr("k"),
			storeCd:$("#aStore").attr("k"),
			orderStartDate:_orderStartDate,
			orderEndDate:_orderEndDate,
			articleId:m.articleId.val().trim(),
			barcode:m.barcode.val().trim(),
			orderDifferentiate: checkValue,
			vendorId: $("#query_type_val").val()
		};
 		m.searchJson.val(JSON.stringify(searchJsonStr));
    }


    //表格初始化-库存报废样式
    var initTable1 = function(){
    	tableGrid = $("#zgGridTtable").zgGrid({
    		title:"Query Result",
    		param:paramGrid,
			localSort: true,
    		colNames:["Order Date","Item Code","Item Barcode","Item Name","Order UOM","Order Quantity",
				"Order Price","Order Amount","Vendor Code","Vendor Name"],
    		colModel:[
				{name:"orderDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"articleId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"barcode",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"orderUom",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"orderQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				/*商品进货单价和税后订货总金额*/
				{name:"orderPrice",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
				{name:"orderAmt",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},

				{name:"vendorId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"vendorName",type:"text",text:"left",width:"150",ishide:false,css:""}
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

	var openNewPage = function (url, param) {
		param = param || "";
		location.href = url + param;
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

	// 格式化数字类型的日期
	function fmtStrToInt(strDate){
		var res = "";
		res = strDate.replace(/-/g,"");
		return res;
	}

	// 初始化下拉列表
	function initSelectOptions(title, selectId, code) {
		// 共通请求
		$.myAjaxs({
			url:url_root+"/cm9010/getCode",
			async:true,
			cache:false,
			type :"post",
			data :"codeValue="+code,
			dataType:"json",
			success:function(result){
				var selectObj = $("#" + selectId);
				selectObj.find("option:not(:first)").remove();
				for (var i = 0; i < result.length; i++) {
					var optionValue = result[i].codeValue;
					var optionText = result[i].codeName;
					selectObj.append(new Option(optionText, optionValue));
				}
			},
			error : function(e){
				_common.prompt(title+"Failed to load data!",5,"error");
			}
		});
	}

	// 请求加载下拉列表
	function getSelectValue(){
		// 加载select
		initSelectOptions("Failed Reason","failedReason", "00075");
	}
	// DD/MM/YYYY to YYYY-MM-DD  格式转换
	function fmtDate(date) {
		var res = '';
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
		return res;
	}

	// 为0返回'0'
	function getString0(tdObj, value){
		var _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}
    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('dayOrderFailure');
_start.load(function (_common) {
	_index.init(_common);
});
