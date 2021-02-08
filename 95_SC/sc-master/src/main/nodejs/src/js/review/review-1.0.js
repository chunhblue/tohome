require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('review', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
    	common=null;
    var m = {
		toKen:null,
		reset: null,
		search: null,
		c_reviewcode : null,//流程编号
		n_typeid : null,//流程类型
		c_reviewname : null//流程名称
    }
 // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(_common) {
    	createJqueryObj();
    	url_left =_common.config.surl+"/review";
		systemPath = _common.config.surl;
		//初始化检索部分内容
		initSearchArea();
    	//事件绑定
		but_event();
		//列表初始化
		initTable();
		//表格内按钮事件
		table_event();
    	m.search.click();
    }

	// 初始化检索部分内容
	var initSearchArea = function(){
		//加载区域信息下拉
		$.myAjaxs({
			url:url_left+"/getApprovalType",
			async:true,
			cache:false,
			type :"post",
			dataType:"json",
			success:function(result){
				m.n_typeid.find("option:not(:first)").remove();
				for (var i = 0; i < result.length; i++) {
					var optionValue = result[i].k;
					var optionText = result[i].v;
					m.n_typeid.append(new Option(optionText, optionValue));
				}
			},
			error : function(e){
				_common.prompt(title+"数据加载失败",5,"error");
			},
			complete:_common.myAjaxComplete
		});
	}

    //画面按钮点击事件
    var but_event = function(){
		//重置搜索
		m.reset.on("click",function(){
			m.c_reviewcode.val("");
			m.n_typeid.val("");
			m.c_reviewname.val("");
		});
    	//检索按钮点击事件
    	m.search.on("click",function(){
			paramGrid = "cReviewcode="+m.c_reviewcode.val()+
				"&nTypeid="+m.n_typeid.val()+
				"&cReviewname="+m.c_reviewname.val();
			tableGrid.setting("url",url_left+"/getReviewList");
			tableGrid.setting("param", paramGrid);
			tableGrid.setting("page", 1);
			tableGrid.loadData(null);
    	});

    }

	//表格按钮事件
	var table_event = function(){
		$("#add").on("click", function () {
			top.location = url_left+"/edit?operateFlg=add";
		})
		$("#modify").on("click", function () {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"nreviewid");
			var param = "operateFlg=edit&nReviewid="+cols["nreviewid"];
			top.location = url_left+"/edit?"+param;
		})
		$("#view").on("click", function () {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"nreviewid");
			var param = "operateFlg=view&nReviewid="+cols["nreviewid"];
			top.location = url_left+"/edit?"+param;
		})
		$("#delete").on("click", function () {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"nreviewid");
			var nReviewid = cols["nreviewid"];
			$.myAjaxs({
				url:url_left+"/deleteById?nReviewid="+ nReviewid,
				async:false,
				cache:false,
				type :"get",
				dataType:"json",
				success:function(res){
					if(res.success){
						//刷新数据
						m.search.click();
						_common.prompt("Delete successfully!",3,"success");
					}else{
						_common.prompt(res.message,5,"error");
					}
					m.toKen.val(res.toKen);
				},
				complete:_common.myAjaxComplete
			});
		})
	}

    //表格初始化-审核流程设定列表样式
    var initTable = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
    		title:"Approval Management List",
    		param:paramGrid,
    		colNames:["Approval Id","Approval No.","Approval Type","Approval Name"],
    		colModel:[
    		  {name:"nreviewid",type:"text",text:"center",width:"100",ishide:true,css:""},
			  {name:"creviewcode",type:"text",text:"center",width:"130",ishide:false,css:""},
			  {name:"ctypeName",type:"text",text:"center",width:"130",ishide:false,css:""},
			  {name:"creviewname",type:"text",text:"center",width:"130",ishide:false,css:""},
	          ],//列内容
	          width:"max",//宽度自动
			  lineNumber:true,//行号
	          isPage:true,//是否需要分页
			  page:10,//当前页
			  sidx:"n_reviewid",//排序字段
			  sord:"asc",//升降序
			  rowPerPage:1,//每页数据量
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
				{
					butType: "update",
					butId: "modify",
					butText: "Modify",
					butSize: ""
				},{
					butType: "view",
					butId: "view",
					butText: "View",
					butSize: ""
				},{
					  butType: "delete",
					  butId: "delete",
					  butText: "Delete",
					  butSize: ""
			  	}
			  ]
    	});
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
var _index = require('review');
_start.load(function (_common) {
	_index.init(_common);
});
