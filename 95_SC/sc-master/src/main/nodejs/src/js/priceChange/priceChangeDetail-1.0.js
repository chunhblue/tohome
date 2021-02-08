require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('priceChangeDetail', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
	    paramGrid = null,
	    selectTrTemp = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
	    tempTrObjValue = {},//临时行数据存储
    	common=null;
    var m = {
		reset: null,
		search: null,
		startDate : null,//搜索开始时间
		endDate : null,//搜索结束时间
		clear_date : null,//清空时间按钮
		createUserId : null,//操作者
		barcode : null,//商品条码
		search_article_btn : null,//商品查询按钮
    	articleId : null,//商品Id
		articleName : null,//商品名称
		startChangeId : null,//搜索开始变价单号
		endChangeId : null,//搜索结束变价单号
		aRegion : null,
		aCity : null,
		aDistrict : null,
		aStore : null,
    }
 // 创建js对象
    var  createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  }  
    }
    function init(_common) {
    	createJqueryObj();
    	url_left =_common.config.surl+"/priceChangeDetail";
		systemPath = _common.config.surl;
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		// 初始化店铺运营组织检索
		_common.initOrganization();
    	//事件绑定
		but_event();
		//列表初始化
		initTable1();
	//	initArticleId();
		table_event();
		// 初始化检索日期
		_common.initDate(m.startDate,m.endDate);
    }

	function judgeNaN (value) {
		return value !== value;
	}

	// 验证检索项是否合法
	var verifySearch = function(){
		let _StartDate = null;
		if(!$("#startDate").val()){
			_common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
			$("#startDate").focus();
			return false;
		}else{
			_StartDate = new Date(fmtDate($("#startDate").val())).getTime();
			if(judgeNaN(_StartDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#startDate").focus();
				return false;
			}
		}
		let _EndDate = null;
		if(!$("#endDate").val()){
			_common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
			$("#endDate").focus();
			return false;
		}else{
			_EndDate = new Date(fmtDate($("#endDate").val())).getTime();
			if(judgeNaN(_EndDate)){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#endDate").focus();
				return false;
			}
		}
		if(_StartDate>_EndDate){
			$("#endDate").focus();
			_common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
			return false;
		}
		let difValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
		if(difValue>62){
			_common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
			$("#endDate").focus();
			return false;
		}
		let reg = /^[0-9]*$/;
		if(m.startChangeId.val()!=null&&m.startChangeId.val()!=''){
			if(!reg.test(m.startChangeId.val())){
				_common.prompt("Document Numbers can only be entered numerically!",3,"info");/*单据编号只能输入数字*/
				m.startChangeId.focus();
				return false;
			}
		}
		if(m.endChangeId.val()!=null&&m.endChangeId.val()!=''){
			if(!reg.test(m.endChangeId.val())){
				_common.prompt("Document Numbers can only be entered numerically!",3,"info");/*单据编号只能输入数字*/
				m.endChangeId.focus();
				return false;
			}
		}
		return true;
	}

	//表格内按钮点击事件
	var table_event = function(){
		// 导出按钮点击事件
		$("#export").on("click",function(){
			if(verifySearch()){
			var regionCd = $("#aRegion").attr("k");
			var cityCd = $("#aCity").attr("k");
			var districtCd = $("#aDistrict").attr("k");
			var storeCd = $("#aStore").attr("k");
			var articleId = m.articleId.val().trim();
			var articleName = m.articleName.val().trim();
			var startDate = subfmtDate(m.startDate.val());
			var endDate = subfmtDate(m.endDate.val());
			var barcode = m.barcode.val().trim();

			var searchJsonStr ={
				createUserId:m.createUserId.val().trim(),
				startDate:startDate,
				endDate:endDate,
				articleId:articleId,
				barcode:barcode,
				articleName:articleName,
				regionCd:regionCd,
				cityCd:cityCd,
				districtCd:districtCd,
				storeCd:storeCd,
				startChangeId:m.startChangeId.val().trim(),
				endChangeId:m.endChangeId.val().trim()
			};

			paramGrid = "searchJson=" + JSON.stringify(searchJsonStr);
			var url = url_left + "/export?" + paramGrid;
			window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
			}
		});
	}
    //画面按钮点击事件
    var but_event = function(){
		//清空日期
		m.clear_date.on("click",function(){
			m.startDate.val("");
			m.endDate.val("");
		});
		//重置搜索
		m.reset.on("click",function(){
			$("#regionRemove").click();
			m.startDate.val("");
			m.endDate.val("");
			m.createUserId.val("");
			m.articleName.val("");
			m.articleId.val("");
			m.barcode.val("");
			m.startChangeId.val("");
			m.endChangeId.val("");
			$("#startDate").css("border-color","#CCC");
			$("#endDate").css("border-color","#CCC");
			selectTrTemp = null;
			_common.clearTable();
		});
    	//检索按钮点击事件
			m.search.on("click",function(){
			if(verifySearch()) {
				var regionCd = $("#aRegion").attr("k");
				var cityCd = $("#aCity").attr("k");
				var districtCd = $("#aDistrict").attr("k");
				var storeCd = $("#aStore").attr("k");
				var startDate = subfmtDate(m.startDate.val());
				var endDate = subfmtDate(m.endDate.val());
				var articleId = m.articleId.val().trim();
				var articleName = m.articleName.val().trim();
				var barcode = m.barcode.val().trim();
				paramGrid = "createUserId=" + m.createUserId.val().trim() +
					"&startDate=" + startDate +
					"&endDate=" + endDate +
					"&barcode=" + barcode +
					"&articleId=" + articleId +
					"&articleName=" + articleName +
					"&regionCd=" + regionCd +
					"&cityCd=" + cityCd +
					"&districtCd=" + districtCd +
					"&storeCd=" + storeCd +
					"&startChangeId=" + m.startChangeId.val().trim()+
					"&endChangeId=" + m.endChangeId.val().trim();
				tableGrid.setting("url", url_left + "/getChangePriceDetail");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);
				tableGrid.loadData(null);
			}
    	});
    }

    //表格初始化-紧急变价一览列表样式
    var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
    		title:"Details",
    		param:paramGrid,/*紧急变价单号*/
    		colNames:["Business Date","Document No.","Store No.","Store Name","Barcode","Item Code","Item Name","Spec","Unit","Price Before Price Change","Selling Price After Price Change","Price Change Date","Created By","User Id"],
    		colModel:[
    		  {name:"accDate",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:dateFmt},
			  {name:"changeId",type:"text",text:"right",width:"130",ishide:false,css:""},
			  {name:"storeCd",type:"text",text:"right",width:"100",ishide:false,css:""},
			  {name:"storeName",type:"text",text:"left",width:"150",ishide:false,css:""},
			  {name:"barcode",type:"text",text:"right",width:"150",ishide:false,css:""},
			  {name:"articleId",type:"text",text:"right",width:"100",ishide:false,css:""},
			  {name:"articleName",type:"text",text:"left",width:"150",ishide:false,css:""},
	          {name:"spec",type:"text",text:"left",width:"100",ishide:false,css:""},
	          {name:"unitName",type:"text",text:"left",width:"130",ishide:false,css:""},
			  {name:"oldPrice",type:"text",text:"right",width:"190",ishide:false,getCustomValue:getThousands},
	          {name:"newPrice",type:"text",text:"right",width:"220",ishide:false,getCustomValue:getThousands},
			  {name:"createDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmtYMDHMS},
			  {name:"createUserName",type:"text",text:"left",width:"130",ishide:false,css:""},
			  {name:"createUserId",type:"text",text:"right",width:"130",ishide:true,css:""}
	          ],//列内容
	          width:"max",//宽度自动
			  page:1,//当前页
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
					butType:"custom",
					butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"
				}
			]
    	});
    }    

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
    	return $(tdObj).text(fmtIntDate(value));
    }

	var dateFmtYMDHMS = function(tdObj, value){
		return $(tdObj).text(fmtYMDHMSDate(value));
	}

	var openNewPage = function (url, param) {
		param = param || "";
		location.href = url + param;
	}

	/**
	 * 自定义函数名：PrefixZero
	 * @param num： 被操作数
	 * @param n： 固定的总位数
	 */
	function PrefixZero(num, n) {
		return (Array(n).join(0) + num).slice(-n);
	}

  //格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
		return res;
	}

	//dd/MM/yyyy H:M:S
	function fmtYMDHMSDate(date){
		var res = "";
		res = date.replace(/^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/, '$3/$2/$1 $4:$5:$6');
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
	// DD/MM/YYYY to YYYY-MM-DD  格式转换
	function fmtDate(date) {
		var res = '';
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
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
var _index = require('priceChangeDetail');
_start.load(function (_common) {
	_index.init(_common);
});
