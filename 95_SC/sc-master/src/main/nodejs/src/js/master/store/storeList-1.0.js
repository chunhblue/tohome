require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");

define('storeList', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
		a_province = null,
		a_district = null,
		a_ward = null,
		getStoreCluster = null,
		getStoreGroup = null,
    	common=null;
	const KEY = 'STORE_MASTER_QUERY';
	var m = {
		searchJson: null,
		storeCd : null,
		storeName : null,
		corpCd : null,
		maCd : null,
		storeTypeCd : null,
		storeGroupCd : null,
		licenseType : null,
		storeType : null,
		effectiveSts : null,
		province : null,
		district : null,
		ward : null,
		surroundings : null,
		currentLocation : null,
		search: null,
		reset: null
	}

 	// 创建js对象
    var createJqueryObj = function(){
    	for (x in m)  {
    		m[x] = $("#"+x);
	  	}
    }

    function init(_common) {
		createJqueryObj();
		systemPath = _common.config.surl;
		url_left = systemPath + "/storeMaster";
		// 首先验证当前操作人的权限是否混乱，
		but_event();
        //列表初始化
		initTable1();
		// 表格内按钮事件
		table_event();
		// 初始化下拉列表
		initAutoMatic();
		 initSelectValue();
		//权限验证
		isButPermission();
		// 初始化查询加载
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

		m.storeCd.val(obj.storeCd);
		m.storeName.val(obj.storeName);
		m.corpCd.val(obj.corpCd);
		m.maCd.val(obj.maCd);
		$.myAutomatic.setValueTemp(getStoreCluster,obj.storeTypeCd,obj.storeTypeName);
		$.myAutomatic.setValueTemp(getStoreGroup,obj.storeGroupCd,obj.storeGroupName);
		m.licenseType.val(obj.licenseType);
		// m.storeType.val(obj.storeType);
		m.effectiveSts.val(obj.effectiveSts);
		m.surroundings.val(obj.surroundings);
		m.currentLocation.val(obj.currentLocation);
		$.myAutomatic.setValueTemp(a_province,obj.province,obj.provinceName);
		$.myAutomatic.setValueTemp(a_district,obj.district,obj.districtName);
		$.myAutomatic.setValueTemp(a_ward,obj.ward,obj.wardName);

		m.search.click();
	}

	// 保存 参数信息
	var saveParamToSession = function () {
		let searchJson = m.searchJson.val();
		if (!searchJson) {
			return;
		}

		let obj = eval("("+searchJson+")");
		obj.storeTypeName=m.storeTypeCd.attr("v");
		obj.storeGroupName=m.storeGroupCd.attr("v");
		obj.provinceName=m.province.attr("v");
		obj.districtName=m.district.attr("v");
		obj.wardName=m.ward.attr("v");
		obj.page=tableGrid.getting("page");
		obj.flg='0';
		sessionStorage.setItem(KEY,JSON.stringify(obj));
	}

	// 表格内按钮点击事件
	var table_event = function(){
		// 查看按钮
		$("#view").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select the data to view!",5,"error");
				return false;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,effectiveStartDate");
			var param = "storeCd=" + cols["storeCd"] + "&effectiveStartDate=" + subfmtDate(cols["effectiveStartDate"]);
			saveParamToSession();
			top.location = url_left+"/view?"+param;
		});
		// 导出按钮点击事件
		$("#export").on("click",function(){
			// 拼接检索参数
			var searchJsonStr ={
				storeCd:m.storeCd.val().trim(),
				storeName:m.storeName.val().trim(),
				corpCd:m.corpCd.val().trim(),
				maCd:m.maCd.val().trim(),
				storeTypeCd:m.storeTypeCd.attr("k"),
				storeGroupCd:m.storeGroupCd.attr("k"),
				licenseType:m.licenseType.val().trim(),
				// storeType:m.storeType.val(),
				effectiveSts:m.effectiveSts.val(),
				province:m.province.attr("k"),
				district:m.district.attr("k"),
				ward:m.ward.attr("k"),
				surroundings:m.surroundings.val().trim(),
				currentLocation:m.currentLocation.val().trim()
			};
			paramGrid = jsonToUrl(searchJsonStr);
			var url = url_left + "/export?searchJson=" + JSON.stringify(searchJsonStr);
			window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
		});
	}

	// 画面按钮点击事件
	var but_event = function(){
		m.reset.on("click",function(){
			m.storeCd.val("");
			m.storeName.val("");
			m.corpCd.val("");
			m.maCd.val("");
			$.myAutomatic.cleanSelectObj(getStoreGroup);
			$.myAutomatic.cleanSelectObj(getStoreCluster);
			m.licenseType.val("");
			// m.storeType.val("");
			m.effectiveSts.val("");
			$.myAutomatic.cleanSelectObj(a_province);
			$.myAutomatic.cleanSelectObj(a_district);
			$.myAutomatic.cleanSelectObj(a_ward);
			m.surroundings.val("");
			m.currentLocation.val("");
			m.searchJson.val("");
			selectTrTemp = null;
			_common.clearTable();
		});
		// 检索按钮点击事件
		m.search.on("click",function(){
			// 拼接检索参数
			var searchJsonStr ={
				storeCd:m.storeCd.val().trim(),
				storeName:m.storeName.val().trim(),
				corpCd:m.corpCd.val().trim(),
				maCd:m.maCd.val().trim(),
				storeTypeCd:m.storeTypeCd.attr("k"),
				storeGroupCd:m.storeGroupCd.attr("k"),
				licenseType:m.licenseType.val().trim(),
				// storeType:m.storeType.val(),
				effectiveSts:m.effectiveSts.val(),
				province:m.province.attr("k"),
				district:m.district.attr("k"),
				ward:m.ward.attr("k"),
				surroundings:m.surroundings.val().trim(),
				currentLocation:m.currentLocation.val().trim()
			};
			m.searchJson.val(JSON.stringify(searchJsonStr));
			paramGrid = jsonToUrl(searchJsonStr);
			tableGrid.setting("url",url_left+"/getList");
			tableGrid.setting("param", paramGrid);
			tableGrid.setting("page", 1);
			tableGrid.loadData();
		});

	}
	// 表格初始化-样式
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Query Result",//查询结果
			param:paramGrid,
			colNames:["Store No.","Store Name","Effective Start Date","Effective End Date",/*"Store Type",*/
				"Area Manager","Zone","DO","Price Tier","Store Cluster","Store Group","Contract Type","Store Size-Group",
				"Store Open Date","Store Close Date","Current Location"],
			colModel:[
				{name:"storeCd",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"storeName",type:"text",text:"left",width:"200",ishide:false,css:""},
				{name:"effectiveStartDate",type:"text",text:"center",width:"150",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"effectiveEndDate",type:"text",text:"center",width:"150",ishide:false,css:"",getCustomValue:dateFmt},
				// {name:"storeType",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"userName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"zoName",type:"text",text:"left",width:"130",ishide:true,css:""},
				{name:"doName",type:"text",text:"left",width:"130",ishide:true,css:""},
				{name:"maName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"storeTypeName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"storeGroupName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"qtypeCodeName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"storeSizeGroupName",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"openDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"closeDate",type:"text",text:"center",width:"130",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"currentLocation",type:"text",text:"left",width:"180",ishide:false,css:""}
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
					butType: "view",
					butId: "view",
					butText: "View",
					butSize: ""
				},
				{butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"}
			]
		});
	}

	// 初始化法人下拉列表
	function getMa0010Selects() {
		// 共通请求
		$.myAjaxs({
			url:url_left+"/getMa0010",
			async:true,
			cache:false,
			type :"get",
			dataType:"json",
			success:function(result){
				var selectObj = $("#corpCd");
				selectObj.find("option:not(:first)").remove();
				if(result!=null){
					for (var i = 0; i < result.length; i++) {
						var optionValue = result[i].k;
						var optionText = result[i].v;
						selectObj.append(new Option(optionText, optionValue));
					}
				}
			},
			complete:_common.myAjaxComplete
		});
	}

	function getSelects(url,selectId) {
		// 共通请求
		$.myAjaxs({
			url:url,
			async:false,
			cache:false,
			type :"get",
			dataType:"json",
			success:function(result){
				var selectObj = $("#"+selectId);
				selectObj.find("option:not(:first)").remove();
				if(result!=null){
					for (var i = 0; i < result.length; i++) {
						var optionValue = result[i].k.trim();
						var optionText = result[i].v;
						selectObj.append(new Option(optionText, optionValue));
					}
				}
			},
			complete:_common.myAjaxComplete
		});
	}

	// 初始化下拉列表
	function initSelectOptions(title, selectId, code) {
		// 共通请求
		$.myAjaxs({
			url:systemPath+"/cm9010/getCode",
			async:false,
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
					selectObj.append(new Option(optionValue+" "+optionText, optionValue));
				}
			},
			error : function(e){
				_common.prompt(title+"Failed to load data!",5,"error");
			}
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

	function subfmtDate(date){
		var res = "";
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
		return res;
	}

	// 按钮权限验证
	var isButPermission = function () {
		var viewBut = $("#viewBut").val();
		if (Number(viewBut) != 1) {
			$("#view").remove();
		}
		var exportBut = $("#exportBut").val();
		if (Number(exportBut) != 1) {
			$("#export").remove();
		}
	}

	// 判断是否为空
	function isNotNull(value) {
		if(value==null||$.trim(value)==""){
			return false;
		}
		return true;
	}

	// 清空下拉
	function clearOptions(selectId) {
		var selectObj = $("#" + selectId);
		selectObj.find("option:not(:first)").remove();

	}

	// 初始化下拉列表
	function initSelectValue(){
		//初始化法人下拉
		getSelects(url_left+"/getMa0010","corpCd");
		//price group下拉
		getSelects(url_left+"/getMa0200","maCd");
		//Store Cluster下拉
		// getSelects(url_left+"/getMa0030","storeTypeCd");
		//Contract Type下拉
		initSelectOptions("Contract Type","licenseType","00140");
		//Store Type下拉
		// initSelectOptions("Store Type","storeType","00500");
		//Province下拉
		getSelects(url_left+"/getMa0025?level=0","province");
		//surroundings下拉
		getSelects(url_left+"/getMa0050?attributeType=10","surroundings");
		//Current Location下拉
		getSelects(url_left+"/getMa0050?attributeType=00","currentLocation");
	}

	var jsonToUrl = function (json) {
		var params = Object.keys(json).map(function (key) {
			return encodeURIComponent(key) + "=" + encodeURIComponent(json[key]);
		}).join("&");
		return params;
	}

	var initAutoMatic=function(){
		$("#district").attr("disabled", true);
		$("#ward").attr("disabled", true);
		$("#districtRefresh").hide();
		$("#wardRefresh").hide();
		$("#districtRemove").hide();
		$("#wardRemove").hide();
		a_province = $("#province").myAutomatic({
			url: url_left + "/getMa0025",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_district);
				$.myAutomatic.cleanSelectObj(a_ward);
				$("#district").attr("disabled", true);
				$("#ward").attr("disabled", true);
				$("#districtRefresh").hide();
				$("#wardRefresh").hide();
				$("#districtRemove").hide();
				$("#wardRemove").hide();
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					$("#district").attr("disabled", false);
					$("#districtRefresh").show();
					$("#districtRemove").show();
				}
				var province = thisObj.attr("k");
				var str = "&adminAddressCd=" + province+"&level=1";
				$.myAutomatic.replaceParam(a_district, str);//赋值
			}
		});
		a_district = $("#district").myAutomatic({
			url: url_left + "/getMa0025",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_ward);
				$("#ward").attr("disabled", true);
				$("#wardRefresh").hide();
				$("#wardRemove").hide();
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					$("#ward").attr("disabled", false);
					$("#wardRefresh").show();
					$("#wardRemove").show();
				}
				var district = thisObj.attr("k");
				var str = "&adminAddressCd=" + district+"&level=2";
				$.myAutomatic.replaceParam(a_ward, str);//赋值
			}
		});
		a_ward = $("#ward").myAutomatic({
			url: url_left + "/getMa0025",
			ePageSize: 10,
			startCount: 0
		});
		$.myAutomatic.cleanSelectObj(a_province);
		$.myAutomatic.cleanSelectObj(a_district);
		$.myAutomatic.cleanSelectObj(a_ward);

		// 选值栏位清空按钮事件绑定
		$("#provinceRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_district);
			$.myAutomatic.cleanSelectObj(a_ward);
			$("#district").attr("disabled", true);
			$("#ward").attr("disabled", true);
			$("#districtRefresh").hide();
			$("#wardRefresh").hide();
			$("#districtRemove").hide();
			$("#wardRemove").hide();
		});
		$("#districtRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_ward);
			$("#ward").attr("disabled", true);
			$("#wardRefresh").hide();
			$("#wardRemove").hide();
		});

		$("#storeGroupCd").prop("disabled", true);
		$("#storeGroupCdRefresh").hide();
		$("#storeGroupCdRemove").hide();
		// 获取StoreCluster
		getStoreCluster = $("#storeTypeCd").myAutomatic({
			url:systemPath+"/storeMaster/getMa0030",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function() {
				$("#storeGroupCd").val("");
				$("#storeGroupCd").prop("disabled","true");
				$("#storeGroupCdRefresh").hide();
				$("#storeGroupCdRemove").hide();
			},
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") !== null && thisObj.attr("k") !== "") {
					$("#storeGroupCd").prop("disabled", false);
					$("#storeGroupCdRefresh").show();
					$("#storeGroupCdRemove").show();
					$.myAutomatic.cleanSelectObj(getStoreGroup);
					var strm ="&storeTypeCd="+ m.storeTypeCd.attr('k').trim();
					$.myAutomatic.replaceParam(getStoreGroup,strm);
				}
			},
		});

		// 获取StoreGroup
		getStoreGroup = $("#storeGroupCd").myAutomatic({
			url: systemPath+"/storeMaster/getMa0035",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function() {

			},
		});
	}



	self.init = init;
	return self;
});

var _start = require('start');
var _index = require('storeList');
_start.load(function (_common) {
	_index.init(_common);
});
