require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('promotionList', function () {
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
		searchJson: null,
		promotionStartDate:null,
		promotionEndDate:null,
		entry_start_date:null,
		entry_end_date:null,
		aRegion : null,
		aCity : null,
		aDistrict : null,
		aStore : null,
		dep_cd:null,
		item_code:null,
		item_name:null,
		promotionDays:null,
		trackingDays:null,
		promotionDetail_dialog:null,
		informType: null,
		promotionCd: null,
		promotionName: null
	}
	// 创建js对象
	var  createJqueryObj = function(){
		for (x in m)  {
			m[x] = $("#"+x);
		}
	}
	function init(_common) {
		createJqueryObj();
		url_left =_common.config.surl+"/informPromotion";
		systemPath = _common.config.surl;
		//初始化检索部分内容
		initSearchArea();
		// 初始化店铺运营组织检索
		_common.initOrganization();
		//事件绑定
		but_event();
		//时间默认选中第一个
		$('input:radio:first').attr('checked', 'checked').trigger('change');
		//列表初始化
		initTable();
		//表格事件初始化
		table_event();
		//权限验证
		isButPermission();
		var inform = getUrlParam("inform");//判断是否是通知
		// if(inform!=null){
		m.informType.val("2");
		// }
		// 初始化检索日期
		_common.initDate(m.promotionStartDate,m.promotionEndDate);
	}

	// 初始化检索部分内容
	var initSearchArea = function(){
		/*m.promotionStartDate.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		}).on('changeDate', function (ev) {
			if (ev.date) {
				$("#promotionEndDate").datetimepicker('setStartDate', new Date(ev.date.valueOf()))
			} else {
				$("#promotionEndDate").datetimepicker('setStartDate', null);
			}
		});
		m.promotionEndDate.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		}).on('changeDate', function (ev) {
			if (ev.date) {
				$("#promotionStartDate").datetimepicker('setEndDate', new Date(ev.date.valueOf()))
			} else {
				$("#promotionStartDate").datetimepicker('setEndDate', new Date());
			}
		});*/

		m.entry_start_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		}).on('changeDate', function (ev) {
			if (ev.date) {
				$("#entry_end_date").datetimepicker('setStartDate', new Date(ev.date.valueOf()))
			} else {
				$("#entry_end_date").datetimepicker('setStartDate', null);
			}
		});
		m.entry_end_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		}).on('changeDate', function (ev) {
			if (ev.date) {
				$("#entry_start_date").datetimepicker('setEndDate', new Date(ev.date.valueOf()))
			} else {
				$("#entry_start_date").datetimepicker('setEndDate', new Date());
			}
		});
		//加载top department下拉
		$.myAjaxs({
			url:systemPath+"/ma0070/getDpt",
			async:true,
			cache:false,
			type :"post",
			dataType:"json",
			success:function(result){
				m.dep_cd.find("option:not(:first)").remove();
				for (var i = 0; i < result.length; i++) {
					var optionValue = result[i].depCd;
					var optionText = result[i].depCd+' '+result[i].depName;
					m.dep_cd.append(new Option(optionText, optionValue));
				}
			},
			error : function(e){
				_common.prompt(title+"Failed to load data!",5,"error");
			},
			complete:_common.myAjaxComplete
		});
	}

	var initDate = function () {
		m.promotionStartDate.val("");
		m.promotionEndDate.val("");
		m.entry_start_date.val("");
		m.entry_start_date.val("");
		m.promotionStartDate.attr("disabled",true);
		m.promotionEndDate.attr("disabled",true);
		m.entry_start_date.attr("disabled",true);
		m.entry_end_date.attr("disabled",true);
	}
	// 验证检索项是否合法
	var verifySearch = function(){
		if(m.promotionStartDate.val()!=""&&m.promotionEndDate.val()!=""){
			var _ctStartDate = new Date(fmtDate($("#promotionStartDate").val())).getTime();
			var _ctEndDate = new Date(fmtDate($("#promotionEndDate").val())).getTime();
			var ctdifValue = parseInt(Math.abs((_ctEndDate-_ctStartDate)/(1000*3600*24)));
			if(ctdifValue >62){
				_common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
				$("#promotionEndDate").focus();
				return false;
			}
		}

		if(m.entry_start_date.val()!=""&&m.entry_end_date.val()!=""){
			var _eStartDate = new Date(fmtDate($("#entry_start_date").val())).getTime();
			var _eEndDate = new Date(fmtDate($("#entry_end_date").val())).getTime();
			var edifValue = parseInt(Math.abs((_eEndDate-_eStartDate)/(1000*3600*24)));
			if(edifValue >62){
				_common.prompt("Query Period cannot exceed 62 days!",5,"error"); // 日期期间取值范围不能大于62天
				$("#entry_end_date").focus();
				return false;
			}
		}
		return true;
	}
	//画面按钮点击事件
	var but_event = function(){
		$('input:radio').on("change",function(){
			initDate();
			if (this.value == '1') {
				m.promotionStartDate.attr("disabled",false);
				m.promotionEndDate.attr("disabled",false);
			}else if(this.value == '3'){
				m.entry_start_date.attr("disabled",false);
				m.entry_end_date.attr("disabled",false);
			}
		});
		//重置搜索
		m.reset.on("click",function(){
			m.promotionStartDate.val("");
			m.promotionEndDate.val("");
			m.entry_start_date.val("");
			m.entry_end_date.val("");
			$("#regionRemove").click();
			m.dep_cd.val("");
			m.item_code.val("");
			m.item_name.val("");
			selectTrTemp = null;
			_common.clearTable();
		});


		//检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()) {
				paramGrid = "regionCd=" + $("#aRegion").attr("k") +
					"&cityCd=" + $("#aCity").attr("k") +
					"&districtCd=" + $("#aDistrict").attr("k") +
					"&storeCd=" + $("#aStore").attr("k") +
					"&promotionStartDate=" + subfmtDate(m.promotionStartDate.val()) +
					"&promotionEndDate=" + subfmtDate(m.promotionEndDate.val()) +
					"&createYmd=" + subfmtDate(m.entry_start_date.val()) +
					"&createEndYmd=" + subfmtDate(m.entry_end_date.val()) +
					"&depCd=" + m.dep_cd.val().trim() +
					"&itemCode=" + m.item_code.val().trim()+
					"&itemName=" + m.item_name.val().trim() +
					"&informType="+m.informType.val().trim()+
					"&promotionCd="+m.promotionCd.val().trim()+
					"&promotionName="+m.promotionName.val().trim();
				if(m.informType.val().trim()=="2"){
					_common.getInform();
				}
				tableGrid.setting("url", url_left + "/getList");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);
				tableGrid.loadData(null);
			}
		});

	}

	function getUrlParam(name) {
		const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
		const urlObj = window.location;
		var r = urlObj.href.indexOf('#') > -1 ? urlObj.hash.split("?")[1].match(reg) : urlObj.search.substr(1).match(reg);
		if (r != null) return unescape(r[2]); return null;
	}

	//表格按钮事件
	var table_event = function(){
		// 查看按钮
		$("#view").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select the data to view!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"promotionCd");
			var param = "promotionCd=" + cols["promotionCd"];
			top.location = url_left+"/view?"+param;
		});
	}


	//表格初始化-新品信息列表样式
	var initTable = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Promotion information list",
			param:paramGrid,
			colNames:["Promotion No.","Promotion Theme","Promotion Start Date","Promotion End Date","Approval Status","Date Created","Created By"],
			colModel:[
				{name:"promotionCd",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"promotionName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"promotionStartDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"promotionEndDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"reviewStatus",type:"text",text:"left",width:"130",ishide:true,css:""},
				{name:"createYmd",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:_common.formatDateAndTime},
				{name:"createUser",type:"text",text:"left",width:"130",ishide:false}
			],//列内容
			width:"max",//宽度自动
			isPage:true,//是否需要分页
			page:1,//当前页
			sidx:"create_ymd",//排序字段
			sord:"desc",//升降序
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
					butType: "view",
					butId: "view",
					butText: "View",
					butSize: ""
				}
			]
		});

	}

	// 按钮权限验证
	var isButPermission = function () {
		var promotionButView = $("#promotionButView").val();
		if (Number(promotionButView) != 1) {
			$("#view").remove();
		}
	}

	//日期字段格式化格式
	var dateFmt = function(tdObj, value){
		return $(tdObj).text(fmtIntDate(value));
	}


	//百分比格式化
	var percentFmt = function (tdObj, value) {
		var percent = Number(value*100).toFixed(2);
		percent+="%";
		return $(tdObj).text(percent);
	}

	//格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
		return res;
	}

	// 格式化数字类型的日期
	// function fmtDate(date){
	// 	var res = "";
	// 	res = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
	// 	return res;
	// }

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
var _index = require('promotionList');
_start.load(function (_common) {
	_index.init(_common);
});
