require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("datetimepicker");
var _myAjax=require("myAjax");
define('bmhis', function () {
    var self = {};
    var url_left = "",
	    paramGrid = null,
	    tableGrid = null,
	    _common=null;
    var m = {
    	toKen:null,
    	main_box:null,
    	s_start_date:null,
    	s_end_date:null,
    	s_div:null,
    	s_dpt:null,
    	search:null,
    	searchJson:null,
    	s_store:null,
    	s_new_flg:null,
    	s_op_flg:null,
    	s_bm_type:null,
    	s_bm_code:null,
    	s_buyer:null,
    	s_item:null,
    	p_code_excel:null,
    	n : null
    }
    // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(common) {
    	createJqueryObj();
    	_common  = common;
    	url_left=common.config.surl+"/bmhistory";
    	initSearchArea();
    	initTable();
    	initEvent();
    	
    	if(m.p_code_excel.val()!="1"){
			//没有excel权限
			$("#outExcel").remove();
		}
    	m.search.click();
    }
    
    //事件初始化
    var initEvent = function(){
    	//检索
    	m.search.on("click",function(){
    		getParam();
    		paramGrid = "searchJson="+m.searchJson.val();
			tableGrid.setting("url",url_left+"/getdata");
			tableGrid.setting("param", paramGrid);
			tableGrid.setting("page", 1);
			tableGrid.loadData(null);
    	});
    	
    	m.s_div.on("change",function(){
    		var thisObj = $(this);
    		var thisVal = thisObj.val();
    		var htmlStr = '<option value="">-- All/Please Select --</option>';
    		if(thisVal!=""){
    			$.myAjaxs({
    				  url:url_left+"/getdepartments",
    				  async:false,
    				  cache:false,
    				  type :"post",
    				  data :"division="+thisVal,
    				  dataType:"json",
    				  success:function(res){
    					  $.each(res,function(ix,node){
    						  htmlStr+='<option value="'+node.k+'">'+node.k+' '+node.v+'</option>';
    					  });
    					  m.s_dpt.html(htmlStr);
    				  },
    				  complete:_common.myAjaxComplete
    			  }); 
    		}else{
    			m.s_dpt.html(htmlStr);
    		}
    	});
    	//view
    	m.main_box.on("click","a[class*='view']",function(){
    		var bmtype = $(this).attr("bmtype");
    		var newno = $(this).attr("newno");
    		var newnosub = $(this).attr("newnosub");
    		top.location = url_left+"/view?&bmType="+bmtype+"&newNo="+newno+"&newNoSub="+newnosub;
    	});
    	$("#outExcel").on("click",function(){
			paramGrid = "searchJson="+m.searchJson.val();
    		var url = url_left +"/excel?"+ paramGrid;
            window.open(encodeURI(url), "excelExportWin", "width=400,height=300,scrollbars=yes");
    	});
    }
    
    //得到检索条件
    var getParam = function(){
    	m.searchJson.val("");
    	// 创建请求字符串
 		var searchJsonStr ={
 				div:m.s_div.val()||null,
 				dpt:m.s_dpt.val()||null,
 				store:m.s_store.val()||null,	
 				newFlg:m.s_new_flg.val()||null,
 				opFlg:m.s_op_flg.val()||null,
 				bmType:m.s_bm_type.val()||null,
 				bmCode:m.s_bm_code.val()||null,
 				buyer:m.s_buyer.val()||null,
 				item:m.s_item.val()||null,
 				startDate:fmtStrToInt(m.s_start_date.val())||null,	
 				endDate:fmtStrToInt(m.s_end_date.val())||null	
 		};
 		m.searchJson.val(JSON.stringify(searchJsonStr));
    }
    
    // 初始化检索部分内容
    var initSearchArea = function(){
//    	//销售开始日
    	m.s_start_date.datetimepicker({
    		 language:'en',
    		 format: 'yyyy-mm-dd',
    		 maxView:4,
    		 startView:2,
    		 minView:2,
    		 autoclose:true,
    		 todayHighlight:true,
    		 todayBtn:true
		});
    	//销售结束日
    	m.s_end_date.datetimepicker({
    		language:'en',
    		format: 'yyyy-mm-dd',
    		maxView:4,
    		startView:2,
    		minView:2,
    		autoclose:true,
    		todayHighlight:true,
    		todayBtn:true
    	});
    }
    //初始化列表
    var initTable = function(){
    	tableGrid = $("#zgGridTtable").zgGrid({
    		title:"BM历史查询-结果一览",
    		//url :url_left+"/getdata",
    		param:paramGrid,
    		colNames:["No.","状态","区分","发起部门DPT",
    		          "BM编码","BM数量","BM销售价格","销售开始日",
    		          "销售结束日","审核区分","操作类型","操作人",
    		          "更新时间","驳回理由"],
    		colModel:[
    		          {name:"od",type:"text",text:"center",width:"50"},
    		          {name:"statusText",type:"text",text:"center",width:"80"},
    		          {name:"bmTypeText",type:"text",text:"center",width:"60"},
    		          {name:"createDpt",type:"text",text:"center",width:"80"},
    		          {name:"key",type:"text",text:"center",width:"50",getCustomValue:keyFmt},
    		          {name:"bmCount",type:"text",text:"right",width:"50"},
    		          {name:"bmPrice",type:"text",text:"right",width:"70",getCustomValue:bmPriceFmt},
    		          {name:"startDate",type:"text",text:"center",width:"80",getCustomValue:dateStrFmt},
    		          {name:"endDate",type:"text",text:"center",width:"80",getCustomValue:dateStrFmt},
    		          {name:"newFlgText",type:"text",text:"center",width:"50"},
    		          {name:"opFlgText",type:"text",text:"center",width:"80"},
    		          {name:"userName",type:"text",text:"center",width:"50"},
    		          {name:"updateDate",type:"text",text:"center",width:"100",getCustomValue:dateFmt},
    		          {name:"rejectreason",type:"text",text:"center",width:"300"}
	          ],//列内容
	          width:"max",//宽度自动
	          page:1,//当前页
	          rowPerPage:10,//每页数据量
	          isPage:true,//是否需要分页
	          sidx:"bm.bm_type,order.bm_code",//排序字段
	          sord:"asc",//升降序
	          loadCompleteEvent:function(self){
	        	  return self;
	          },
	          buttonGroup:[
	              {butType:"custom",butHtml:"<button id='outExcel' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-export'></span> Export</button>"}//增加 默认："",lg:最大，sm小，xs超小
               ]
    		                       
    	});
    
    }
    //bmBM销售价格小数位补齐
    var bmPriceFmt = function(tdObj, value){
    	if(value==null||value==""){
    		return $(tdObj);
    	}else{
    		return $(tdObj).html(Number(value).toFixed(2));
    	}
    }
    //bmkey格式化
    var keyFmt = function(tdObj, value){
    	var obj = value.split("-");
    	var html = '<a href="javascript:void(0);" title="进入详情" class="view" " bmcode="'+obj[0]+'" bmtype="'+obj[1]+'" newno="'+obj[2]+'" newnosub="'+obj[3]+'" ><span class="glyphicon glyphicon-expand icon-right"></span>'+obj[0]+'</a>';
    	return $(tdObj).html(html);
    }
    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
    	return $(tdObj).text(_common.dateFormat(new Date(value),"yyyy-MM-dd hh:mm:ss"));
    }
    //日期字段格式化格式
    var dateStrFmt = function(tdObj, value){
    	return $(tdObj).text(fmtIntDate(value));
    }
  //格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
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
var _index = require('bmhis');
_start.load(function (_common) {
	_index.init(_common);
});
