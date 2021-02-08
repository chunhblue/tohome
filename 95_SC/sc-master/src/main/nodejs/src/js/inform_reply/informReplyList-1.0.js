require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('informReplyList', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
    	common=null;
    var m = {
		reset: null,
		search: null,
		inform_cd : null,//通报编号
		inform_title : null,//通报标题
		send_scope : null,//发送范围
		reply_start_date : null,//查看日期 开始
		reply_end_date : null,//查看日期 结束
		clear_reply_date : null,//清空查看日期
    }
 // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(_common) {
    	createJqueryObj();
    	url_left =_common.config.surl+"/informReply";
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
		//查看回复下拉是否加载完毕
		var initInterval = setInterval(function () {
			var reply = $("#isReply").find("option").length;
			if(reply>1){
				m.search.click();
				clearInterval(initInterval);//停止
			}
		}, 200); //启动
		setTimeout(function () {
			clearInterval(initInterval);//停止
		},2000);
    }

	// 初始化检索部分内容
	var initSearchArea = function(){
		m.reply_start_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		m.reply_end_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		m.clear_reply_date.on("click",function(){
			m.reply_start_date.val("");
			m.reply_end_date.val("");
		});
		//初始化下拉
		getSelectValue();
	}

	// 请求加载下拉列表
	function getSelectValue(){
		// 加载select
		initSelectOptions("Reply Status","isReply", "00415");
	}

    //画面按钮点击事件
    var but_event = function(){
		//重置搜索
		m.reset.on("click",function(){
			m.inform_cd.val("");
			m.inform_title.val("");
			m.reply_start_date.val("");
			m.reply_end_date.val("");
			$("#isReply").val("");
		});
    	//检索按钮点击事件
    	m.search.on("click",function(){
			paramGrid = "informCd="+m.inform_cd.val()+
				"&informTitle="+m.inform_title.val()+
				"&startDate="+subfmtDate(m.reply_start_date.val())+
				"&endDate="+subfmtDate(m.reply_end_date.val())+
				"&isReply="+$("#isReply").val();
			tableGrid.setting("url",url_left+"/getReplyList");
			tableGrid.setting("param", paramGrid);
			tableGrid.setting("page", 1);
			tableGrid.loadData(null);
    	});

    }

	//表格按钮事件
	var table_event = function(){
		$("#modify").on("click", function () {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"informCd,storeCd");
			var param = "operateFlg=edit&informCd="+cols["informCd"]+
				"&storeCd="+cols["storeCd"];
			top.location = url_left+"/edit?"+param;
		})
		$("#view").on("click", function () {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"informCd,storeCd");
			var param = "operateFlg=view&informCd="+cols["informCd"]+
				"&storeCd="+cols["storeCd"];
			top.location = url_left+"/edit?"+param;
		})
	}

    //表格初始化-通报消息回复头档列表样式
    var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
    		title:"Bulletin log list",
    		param:paramGrid,
    		colNames:["Notification Date","Notification No.","Notification title","Store No.","Store Name","Reply Status"],
    		colModel:[
				{name:"createYmd",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"informCd",type:"text",text:"center",width:"100",ishide:false,css:""},
		  		{name:"informTitle",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"storeCd",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"storeName",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"isReply",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:replyFmt}
	          ],//列内容
	          width:"max",//宽度自动
	          isPage:true,//是否需要分页
			  page:1,//当前页
			  sidx:"ma4300.create_ymd desc,ma4300.inform_cd desc",//排序字段
			  sord:"",//升降序
			  rowPerPage:10,//每页数据量
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
					butId: "modify",
					butText: "Reply",
					butSize: ""
				},{
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
				selectObj.val("0")
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
		var informButReply = $("#informButReply").val();
		if (Number(informButReply) != 1) {
			$("#modify").remove();
		}
	}

	var replyFmt = function (tdObj, value) {
    	var text = '';
    	if(value=="0"){//未回复
			text = "No Reply"
		}else if(value=="1"){//回复
			text = "Reply"
		}
		return $(tdObj).text(text);
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
var _index = require('informReplyList');
_start.load(function (_common) {
	_index.init(_common);
});
