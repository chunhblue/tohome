require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('reviewEdit', function () {
	var self = {};
	var url_left = "",
		url_back = "",
		systemPath = "",
		paramGrid = null,
		selectTrTemp = null,
		tempTrObjValue = {},//临时行数据存储
		a_store = null,
		common=null;
	var m = {
		toKen : null,
		reset: null,
		operateFlgByDialog:null,//Dialog操作类型
		operateFlg: null,//页面操作类型
		h_payCd: null,//隐藏支付编号
		payCd: null,//支付编号
		payName: null,//支付名称
		printName: null,//打印名称
		changeAttr: null,//找零属性
		payAttr: null,//付款属性
		paySeq: null,//顺序
		posId: null,//pos机号
		posDisplay: null,//pos是否显示
		cancel : null,//取消
		affirm : null,// 确认
		returnsViewBut : null,//返回
		submitBtn : null//提交按钮
	}
	// 创建js对象
	var  createJqueryObj = function(){
		for (x in m)  {
			m[x] = $("#"+x);
		}
	}
	function init(_common) {
		createJqueryObj();
		url_back = _common.config.surl+"/review";
		url_left =_common.config.surl+"/review/edit";
		systemPath = _common.config.surl;
		//初始化下拉
		getSelectValue();
		initAutoMatic();
		//事件绑定
		but_event();
		//列表初始化
		initTable1();
		//表格内按钮事件
		table_event();
		// 根据跳转加载数据，设置操作模式
		setValueByType();
	}
	// 请求加载下拉列表
	function getSelectValue(){
		// 加载select
		initSelectOptions("找零属性","changeAttr", "00405");
		initSelectOptions("付款属性","payAttr", "00410");
		initSelectOptions("POS是否隐藏","posDisplay", "00385");
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
				_common.prompt(title+"数据加载失败",5,"error");
			}
		});
	}

	//获取pos机号下拉
	var getSelectPosId = function(storeCd,val){
		if(storeCd!=null&&storeCd!=''){
			$.myAjaxs({
				url:url_left+"/getPosId",
				async:true,
				cache:false,
				type :"post",
				data :"storeCd="+storeCd,
				dataType:"json",
				success:function(result){
					if(result.success){
						m.posId.empty();
						for (var i = 0; i < result.data.length; i++) {
							var optionValue = result.data[i].posId;
							var optionText = result.data[i].posName;
							m.posId.append(new Option(optionText, optionValue));
						}
						m.posId.find("option:first").prop("selected",true);
						if(val!=null&&val!=""){
							m.posId.val(val);
						}
					}
				},
				error : function(e){
					_common.prompt("The request failed, Please try again!",5,"error");// 请求失败
				},
				complete:_common.myAjaxComplete
			});
		}
	}

	var table_event = function(){
		$("#add").on("click", function () {
			$("#a_store_clear").click();
			m.operateFlgByDialog.val("1");
			$('#reviewEdit_dialog').modal("show");
		});
		//修改权限
		$("#update").on("click", function () {
			if(selectTrTemp == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,storeName,posId,posDisplay");
			var storeCd = cols["storeCd"];
			var storeName = cols["storeName"];
			var posId = cols["posId"];
			var posDisplay = cols["posDisplay"];
			console.log(storeCd)
			console.log(storeName)
			$("#a_store").attr("k",storeCd).attr("v",storeName).val(storeName);
			$("#oldStoreCd").val(storeCd);
			getSelectPosId(storeCd,posId);
			$("#oldPosId").val(posId);
			m.posDisplay.val(posDisplay);
			m.operateFlgByDialog.val("2");
			$('#reviewEdit_dialog').modal("show");
		});
		//删除
		$("#delete").on("click",function(){
			if(selectTrTemp == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,posId");
			_common.myConfirm("请确认是否要删除选中的数据？",function(result){
				if(result=="true"){
					$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
						var storeCd = $(this).find('td[tag=storeCd]').text();
						var posId = $(this).find('td[tag=posId]').text();
						if(storeCd==cols["storeCd"]&&posId==cols["posId"]){
							$(this).remove();
							return;
						}
					});
				}
			});
		});
		//新增加确认
		m.affirm.on("click",function(){
			var storeCd = $("#a_store").attr("k");
			var storeName = $("#a_store").attr("v");
			var posId = m.posId.val();
			var posName = m.posId.find("option:selected").text();
			var posDisplay = m.posDisplay.val();
			var posDisplayName = m.posDisplay.find("option:selected").text();
			var operateFlgByDialog = m.operateFlgByDialog.val();
			if(storeCd==null||storeCd==""){
				_common.prompt("Please select a store first!",5,"error");
				$("#a_store").focus();
				return false;
			}
			if(posId==null||posId==""){
				_common.prompt("Please select POS ID!",5,"error");
				m.posId.focus();
				return false;
			}
			if(posDisplay==null||posDisplay==""){
				_common.prompt("Please set POS visibility!",5,"error");
				m.posDisplay.focus();
				return false;
			}
			var repArticle = 0;
			$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
				var posId_text = $(this).find('td[tag=posId]').text();
				var storeCd_text = $(this).find('td[tag=storeCd]').text();
				console.log(m.posId.val() +":"+posId_text)
				console.log(storeCd +":"+storeCd_text)
				if(operateFlgByDialog=="1"&&m.posId.val()==posId_text&&storeCd==storeCd_text){
					repArticle++;
				}
				if(operateFlgByDialog=="2"&&m.posId.val()==posId_text&&storeCd==storeCd_text&&(storeCd!=$("#oldStoreCd").val()||m.posId.val()!=$("#oldPosId").val())){
					repArticle++;
				}
			});
			if(repArticle>0){
				_common.prompt("The data already exists!",5,"error");
				return false;
			}
			_common.myConfirm("Please confirm whether you want to save?",function(result){
				if(result == "true"){
					if(operateFlgByDialog=="1"){
						appendTr(storeCd,storeName,posId,posName,posDisplay,posDisplayName);
					}else if(operateFlgByDialog=="2"){
						var updateFlg = false;
						$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
							var posId_text = $(this).find('td[tag=posId]').text();
							var storeCd_text = $(this).find('td[tag=storeCd]').text();
							if(storeCd_text==$("#oldStoreCd").val()&&posId_text==$("#oldPosId").val()){
								$(this).find('td[tag=storeCd]').text(storeCd);
								$(this).find('td[tag=storeName]').text(storeName);
								$(this).find('td[tag=posId]').text(posId);
								$(this).find('td[tag=posName]').text(posName);
								$(this).find('td[tag=posDisplay]').text(posDisplay);
								$(this).find('td[tag=posDisplayName]').text(posDisplayName);
								updateFlg = true;
							}
						});
						if(!updateFlg){
							appendTr(storeCd,storeName,posId,posName,posDisplay,posDisplayName);
						}
					}
					$('#reviewEdit_dialog').modal("hide");
				}
			});
		});
	}

	//表格新增
	var appendTr = function (storeCd,storeName,posId,posName,posDisplay,posDisplayName) {
		var rowindex = 0;
		var trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
		if(trId!=null&&trId!=''){
			rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
		}
		var tr = '<tr id="zgGridTtable_'+rowindex+'_tr" class="">' +
			'<td tag="storeCd" width="100" title="'+storeCd+'" align="center" id="zgGridTtable_'+rowindex+'_tr_storeCd" tdindex="zgGridTtable_storeCd">'+storeCd+'</td>' +
			'<td tag="storeName" width="130" title="'+storeName+'" align="center" id="zgGridTtable_'+rowindex+'_tr_storeName" tdindex="zgGridTtable_storeName">'+storeName+'</td>' +
			'<td tag="posId" width="130" hidden="true" title="'+posId+'" align="center" id="zgGridTtable_'+rowindex+'_tr_posId" tdindex="zgGridTtable_posId">'+posId+'</td>' +
			'<td tag="posName" width="130" title="'+posName+'" align="center" id="zgGridTtable_'+rowindex+'_tr_posName" tdindex="zgGridTtable_posName">'+posName+'</td>' +
			'<td tag="posDisplay" width="130" hidden="true" title="'+posDisplay+'" align="center" id="zgGridTtable_'+rowindex+'_tr_posDisplay" tdindex="zgGridTtable_posDisplay">'+posDisplay+'</td>' +
			'<td tag="posDisplayName" width="130" title="'+posDisplayName+'" align="center" id="zgGridTtable_'+rowindex+'_tr_posDisplayName" tdindex="zgGridTtable_posDisplayName">'+posDisplayName+'</td>' +
			'</tr>';
		$(".zgGrid-tbody").append(tr);
	}

	//画面按钮点击事件
	var but_event = function(){
		//返回一览
		m.returnsViewBut.on("click",function(){
			top.location = url_back;
		});
		//提交变价信息
		m.submitBtn.on("click",function(){
			var payCd = m.payCd.val();
			var payName = m.payName.val();
			var printName = m.printName.val();
			var payAttr = m.payAttr.val();
			var changeAttr = m.changeAttr.val();
			var paySeq = m.paySeq.val();
			var operateFlg = m.operateFlg.val();
			if(payName==null||payName==""){
				_common.prompt("Please enter payment name!",5,"error");
				m.payName.focus();
				return false;
			}
			if(printName==null||printName==""){
				_common.prompt("Please enter printed name!",5,"error");
				m.printName.focus();
				return false;
			}
			if(changeAttr==null||changeAttr==""){
				_common.prompt("Please select the charging property!",5,"error");
				m.changeAttr.focus();
				return false;
			}
			if(payAttr==null||payAttr==""){
				_common.prompt("Please select the payment property!",5,"error");
				m.payAttr.focus();
				return false;
			}
			if($("#zgGridTtable>.zgGrid-tbody tr").length<1){
				_common.prompt("Please add POS payment method maintenance details!",5,"error");
				return false;
			}
			var reviewDetail = [];
			$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
				var review = {
					payCd:payCd,
					storeCd:$(this).find('td[tag=storeCd]').text(),//店铺号
					posId:$(this).find('td[tag=posId]').text(),//pos机号
					posDisplay:$(this).find('td[tag=posDisplay]').text(),//pos是否隐藏
				}
				reviewDetail.push(review);
			});
			var reviewDetailJson = "";
			if(reviewDetail.length>0){
				reviewDetailJson = JSON.stringify(reviewDetail)
			}else{
				_common.prompt("Please add POS payment method maintenance details!",5,"error");
				return false;
			}
			_common.myConfirm("Are you sure you want to save?",function(result){
				if(result == "true"){
					$.myAjaxs({
						url:url_left+"/save",
						async:true,
						cache:false,
						type :"post",
						data :{
							payCd:payCd,
							payName:payName,
							printName:printName,
							payAttr:payAttr,
							changeAttr:changeAttr,
							paySeq:paySeq,
							reviewDetailJson:reviewDetailJson,
							operateFlg:operateFlg//操作状态
						},
						dataType:"json",
						success:function(result){
							if(result.success){
								_common.prompt("Data saved successfully!",5,"info");
								//设置支付编号
								m.payCd.val(result.data.payCd);
								//禁用页面按钮
								disableAll();
							}else{
								_common.prompt(result.message,5,"error");
							}
						},
						error : function(e){
							_common.prompt("Data failed to save!",5,"error");
						},
						complete:_common.myAjaxComplete
					});
				}
			});

		});
	}

	//禁用页面按钮
	var disableAll = function () {
		m.payCd.prop("disabled",true);
		m.payName.prop("disabled",true);
		m.printName.prop("disabled",true);
		m.payAttr.prop("disabled",true);
		m.changeAttr.prop("disabled",true);
		m.paySeq.prop("disabled",true);
		$("#add").prop("disabled",true);
		$("#update").prop("disabled",true);
		$("#delete").prop("disabled",true);
		m.submitBtn.prop("disabled",true);
	}

	//启用页面按钮
	var enableAll = function () {
		m.payCd.prop("disabled",false);
		m.payName.prop("disabled",false);
		m.printName.prop("disabled",false);
		m.payAttr.prop("disabled",false);
		m.changeAttr.prop("disabled",false);
		m.paySeq.prop("disabled",false);
		$("#add").prop("disabled",false);
		$("#update").prop("disabled",false);
		$("#delete").prop("disabled",false);
		m.submitBtn.prop("disabled",false);
	}

	// 根据跳转方式，设置画面可否编辑、加载内容
	var setValueByType = function(){
		var _sts = m.operateFlg.val();
		if(_sts == "add"){
			m.changeAttr.find("option:first").prop("selected",true);
			m.payAttr.find("option:first").prop("selected",true);
		}else if(_sts == "edit"){
			// 查询加载数据
			dataInitByPayCd();
		}else if(_sts == "view"){
			// 查询加载数据
			dataInitByPayCd();
			//禁用页面
			disableAll();
		}
	}

	//页面数据赋值
	var dataInitByPayCd = function () {
		$.myAjaxs({
			url:url_left+"/getMa4180",
			async:true,
			cache:false,
			type :"post",
			data :"payCd="+m.h_payCd.val(),
			dataType:"json",
			success:function(result){
				if(result.success){
					var ma4180Dto = result.data;
					//赋值POS支付方式维护基本信息
					m.payCd.val(ma4180Dto.payCd);
					m.payName.val(ma4180Dto.payName);
					m.printName.val(ma4180Dto.printName);
					// m.changeAttr.val(ma4180Dto.changeAttr);
					// m.payAttr.val(ma4180Dto.payAttr);
					m.paySeq.val(ma4180Dto.paySeq);
					//下拉找零属性支付属性 赋值
					var initInterval = setInterval(function () {
						var changeAttrLen=m.changeAttr.find("option").length;
						var payAttrLen=m.payAttr.find("option").length;
						if(changeAttrLen>1&&payAttrLen>1){
							m.changeAttr.val(ma4180Dto.changeAttr);
							m.payAttr.val(ma4180Dto.payAttr);
							clearInterval(initInterval);//停止
						}
					}, 200); //启动
					setTimeout(function () {
						clearInterval(addInterval);//停止
					},2000);
					//查询POS支付方式维护明细信息
					paramGrid = "payCd="+m.h_payCd.val();
					tableGrid.setting("url", url_left + "/getList");
					tableGrid.setting("param", paramGrid);
					tableGrid.loadData(null);
				}else{
					_common.prompt(result.message,5,"error");
				}
			},
			error : function(e){
				_common.prompt("The request failed, Please try again!",5,"error");// 请求失败
			},
			complete:_common.myAjaxComplete
		});
	}
	//初始化自动下拉
	var initAutoMatic = function () {
		a_store = $("#a_store").myAutomatic({
			url: systemPath + "/ma1000/getStoreByPM",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function (thisObj) {
				m.posId.empty();
			},
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					getSelectPosId(thisObj.attr("k"))
				}
			}
		});
	}

	//表格初始化-POS支付方式维护列表样式
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"POS支付方式维护明细",
			param:paramGrid,
			colNames:["Store No.","Store Name","POS Id","POS机号","POS Display","POS是否显示"],
			colModel:[
				{name:"storeCd",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"storeName",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"posId",type:"text",text:"center",width:"130",ishide:true,css:""},
				{name:"posName",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"posDisplay",type:"text",text:"center",width:"130",ishide:true,css:""},
				{name:"posDisplayName",type:"text",text:"center",width:"130",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			isPage:false,//是否需要分页
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
					butSize: ""//,
				},//新增
				{
					butType: "update",
					butId: "update",
					butText: "Modify",
					butSize: ""//,
				},//修改
				{
					butType: "delete",
					butId: "delete",
					butText: "Delete",
					butSize: ""//,
				},//删除
			],
		});
	}

	//日期字段格式化格式
	var dateFmt = function(tdObj, value){
		return $(tdObj).text(fmtIntDate(value));
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
var _index = require('reviewEdit');
_start.load(function (_common) {
	_index.init(_common);
});
