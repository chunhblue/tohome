require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('informList', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
    	common=null;
	const KEY = 'NOTIFICATIONS_MANAGEMENT';
    var m = {
		reset: null,
		search: null,
		inform_cd : null,//通报编号
		inform_title : null,//通报标题
		send_scope : null,//发送范围
		create_start_date : null,//录入日期 开始
		create_end_date : null,//录入日期 结束
		clear_create_date : null,//清空录入日期
    }
 // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(_common) {
    	createJqueryObj();
    	url_left =_common.config.surl+"/informHQ";
		systemPath = _common.config.surl;
		//初始化检索部分内容
		initSearchArea();
    	//事件绑定
		but_event();
		//列表初始化
		initTable1();
		//表格内按钮事件
		table_event();
		//权限验证
		isButPermission();
		// 初始化检索日期
		_common.initDate(m.create_start_date,m.create_end_date);
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
		m.inform_cd.val(obj.informCd);
		m.inform_title.val(obj.informTitle);
		m.send_scope.val(obj.sendScope);
		m.create_start_date.val(obj.startDate);
		m.create_end_date.val(obj.endDate);

		m.search.click();
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
		obj.startDate=m.create_start_date.val();
		obj.endDate=m.create_end_date.val();
		obj.flg='0';
		sessionStorage.setItem(KEY,JSON.stringify(obj));
	}

	// 初始化检索部分内容
	var initSearchArea = function(){
		m.clear_create_date.on("click",function(){
			m.create_start_date.val("");
			m.create_end_date.val("");
		});
		//初始化发送范围下拉
		// initSelectOptions("Sending range","send_scope", "00415");
	}
	// 验证检索项是否合法
	var verifySearch = function(){
		if(m.create_start_date.val()!==""&&m.create_start_date.val()!=null){
			if(_common.judgeValidDate(m.create_start_date.val())){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#create_start_date").focus();
				return false;
			}
		}
		if(m.create_end_date.val()!==""&&m.create_end_date.val()!=null){
			if(_common.judgeValidDate(m.create_end_date.val())){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#create_end_date").focus();
				return false;
			}
		}
		if(m.create_start_date.val()!==""&&m.create_end_date.val()!==""){
			var intStartDate = fmtStringDate(m.create_start_date.val());
			var intEndDate = fmtStringDate(m.create_end_date.val());
			if(intStartDate>intEndDate){
				// _common.prompt("[开始日]不能大于[结束日]",5,"info");
				_common.prompt("The start date cannot be greater than the end date!",5,"error");/*开始时间不能大于结束时间*/
				$("#create_end_date").focus();
				return false;
			}
			var _StartDate = new Date(fmtDate($("#create_start_date").val())).getTime();
			var _EndDate = new Date(fmtDate($("#create_end_date").val())).getTime();
			var difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
			if(difValue >62){
				_common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
				$("#create_end_date").focus();
				return false;
			}
		}
		return true;
	}

    //画面按钮点击事件
    var but_event = function(){
		//重置搜索
		m.reset.on("click",function(){
			m.inform_cd.val("");
			m.inform_title.val("");
			m.send_scope.val("");
			m.create_start_date.val("");
			m.create_end_date.val("");
			selectTrTemp = null;
			_common.clearTable();
		});
    	//检索按钮点击事件
    	m.search.on("click",function(){
			if(verifySearch()) {
				paramGrid = "informCd=" + m.inform_cd.val().trim() +
					"&informTitle=" + m.inform_title.val().trim()+
					"&sendScope=" + m.send_scope.val() +
					"&startDate=" + subfmtDate(m.create_start_date.val()) +
					"&endDate=" + subfmtDate(m.create_end_date.val());
				tableGrid.setting("url", url_left + "/getList");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);
				tableGrid.loadData(null);
			}
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
			var cols = tableGrid.getSelectColValue(selectTrTemp,"informCd");
			var param = "operateFlg=edit&informCd="+cols["informCd"];
			saveParamToSession();
			top.location = url_left+"/edit?"+param;
		})
		$("#view").on("click", function () {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"informCd");
			var param = "operateFlg=view&informCd="+cols["informCd"];
			saveParamToSession();
			top.location = url_left+"/edit?"+param;
		})
	}

    //表格初始化-通报消息头档列表样式
    var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
    		// title:"通报消息一览",
    		title:"Notification information list",
    		param:paramGrid,
    		colNames:["Business Date","Notification No.","Notification title","Start Date","End Date"],
    		colModel:[
				{name:"createYmd",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"informCd",type:"text",text:"right",width:"100",ishide:false,css:""},
		  		{name:"informTitle",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"informStartDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"informEndDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt}
	          ],//列内容
	          width:"max",//宽度自动
	          isPage:true,//是否需要分页
			  page:1,//当前页
			  sidx:"inform_cd",//排序字段
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
				},
				// {
				// 	butType: "update",
				// 	butId: "modify",
				// 	butText: "Modify",
				// 	butSize: ""
				// },
			  	{
					butType: "view",
					butId: "view",
					butText: "View",
					butSize: ""
				}
			  ]
    	});
    }

	// 初始化下拉列表
	function initSelectOptions(title, selectId, code) {
		// 共通请求
		$.myAjaxs({
			url:systemPath+"/cm9010/getCode",
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

	// 按钮权限验证
	var isButPermission = function () {
		var informButView = $("#informButView").val();
		if (Number(informButView) != 1) {
			$("#view").remove();
		}
		var informButAdd = $("#informButAdd").val();
		if (Number(informButAdd) != 1) {
			$("#add").remove();
		}
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


	function subfmtDate(date){
		var res = "";
		if(date!=null&&date!=""){
			res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
		}
		return res;
	}
	// DD/MM/YYYY to YYYY-MM-DD  格式转换
	function fmtDate(date) {
		var res = '';
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
		return res;
	}
	// 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
	function fmtStringDate(date){
		var res = "";
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
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
var _index = require('informList');
_start.load(function (_common) {
	_index.init(_common);
});
