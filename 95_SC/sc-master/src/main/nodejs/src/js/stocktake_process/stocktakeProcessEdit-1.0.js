require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('receiptEdit', function () {
	var self = {};
	var url_left = "",
		paramGrid = null,
		selectTrTemp = null,
		reThousands = null,
		toThousands = null,
		tempTrObjValue = {},
		systemPath='',
		a_store='',
		dataForm1=[],
		dataForm2=[],
		dataForm3=[];
	const KEY = 'STOCKTAKING_VARIANCE_REPORT';
	var m = {
		toKen:null,
		item_name : null,
		tableGrid : null,
		selectTrObj : null,
		error_pcode : null,
		identity : null,
		search_btn:null,
		reject_dialog : null,
		input_item : null,
		division : null,
		searchstr:null,
		pd_date:null,
		pd_start_time:null,
		pd_end_time:null,
		clear_pd_time:null,
		returnsViewBut : null,
		store : null,
		search_item_but : null,
		searchJson:null,
		piCdParam:null,
		piDateParam:null,
		//审核
		approvalBut:null,
		approval_dialog:null,
		audit_cancel:null,
		audit_affirm:null,
		typeId:null,
		reviewId:null
	}
	// 创建js对象
	var  createJqueryObj = function(){
		for (x in m)  {
			m[x] = $("#"+x);
		}
	}
	function init(_common) {
		createJqueryObj();
		url_left=_common.config.surl+"/stocktakeProcess";
		systemPath=_common.config.surl;
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		but_event();
		// store 下拉
		initAutoMatic();
		// 获取下拉框数据
		getComboxData();
		// 获取明细数据
		getData(m.piCdParam.val(),m.piDateParam.val());
		//表格内按钮事件
		table_event();
		setDisable(true);
		//审核事件
		approval_event();

	}

	// 栏位显示千分位
	function getThousands(tdObj, value) {
		if (value==null||value=='') {
			return $(tdObj).text('');
		}
		return $(tdObj).text(toThousands(value));
	}

	// 06/03/2020 -> 20200306
	var formatDate = function (dateStr) {
		dateStr = dateStr.replace(/\//g,'');
		var day = dateStr.substring(0,2);
		var month = dateStr.substring(2,4);
		var year = dateStr.substring(4,8);
		return year+month+day;
	}

	var table_event = function(){
		$("#startQty").blur(function () {
			if (this.value==null||this.value=='') {
				return;
			}
			$("#startQty").val(toThousands(this.value));
		});
		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#startQty").focus(function(){
			$("#startQty").val(reThousands(this.value));
		});
		$("#endQty").blur(function () {
			if (this.value==null||this.value=='') {
				return;
			}
			$("#endQty").val(toThousands(this.value));
		});
		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#endQty").focus(function(){
			$("#endQty").val(reThousands(this.value));
		});
		$("#startAmt").blur(function () {
			if (this.value==null||this.value=='') {
				return;
			}
			$("#startAmt").val(toThousands(this.value));
		});
		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#startAmt").focus(function(){
			$("#startAmt").val(reThousands(this.value));
		});
		$("#endAmt").blur(function () {
			if (this.value==null||this.value=='') {
				return;
			}
			$("#endAmt").val(toThousands(this.value));
		});
		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#endAmt").focus(function(){
			$("#endAmt").val(reThousands(this.value));
		});

		// 导出数据
		$('#export_btn').on('click',function () {
			_common.loading();
			$.myAjaxs({
				url:url_left+"/queryExport",
				async:true,
				cache:false,
				data:'piCd='+m.piCdParam.val()+'&piDate='+m.piDateParam.val()+'&storeCd='+a_store.attr('k'),
				type :"post",
				dataType:"json",
				success:function(result){
					if(result.success){
						let store = encodeURIComponent(a_store.attr('v'));
						//导出excle
						window.location.href=url_left+"/download?store="+store;
					} else {
						_common.prompt(result.msg,5,"error");
					}
					_common.loading_close();
				},
				error : function(e){
					_common.prompt("Export failed!",5,"error");
					_common.loading_close();
				},
			});
		});

		$('#search_btn').on('click',function () {
			getTableData(true);
		});
		$('#reset_btn').on('click',function () {
			m.searchstr.val("");
			$("#startQty").val("");
			$("#endQty").val("");
			$("#startAmt").val("");
			$("#endAmt").val("");
			$('#search_btn').click();
		})
		// 返回一览
		m.returnsViewBut.on("click",function(){
			var black = m.approvalBut.attr("disabled");
			if (black!="disabled"){
				_common.myConfirm("Crrent change is not submitted yet,are you sure to exit?",function(result) {
					if (result == "true") {
						_common.updateBackFlg(KEY);
						top.location = url_left;
					}
				})
			}
			if(black=='disabled'){
				_common.updateBackFlg(KEY);
				top.location = url_left;
			}
		});

	}

	//验证检索项是否合法
	var verifySearch = function(){
		if(($("#startQty").val()!==null&&$("#startQty").val()!=="")&&($("#endQty").val()!==null&&$("#endQty").val()!=="")){
			let _startQty = reThousands($("#startQty").val());
			let _endQty = reThousands($("#endQty").val());
			if(_startQty>_endQty){
				_common.prompt("The start Qty cannot be greater than the end Qty!",3,"info"); // 结束数量不能低于开始数量
				$("#endQty").focus();
				return false;
			}
		}
		return true;
	};

	var setParamGrid = function () {
		let piCd = $("#storeNo").val();
		let piDate = formatDate($("#pd_date").val());
		var storeCd = a_store.attr('k');
		let startQty = reThousands($("#startQty").val().trim());
		let endQty = reThousands($("#endQty").val().trim());
		let startAmt = reThousands($("#startAmt").val().trim());
		let endAmt = reThousands($("#endAmt").val().trim());
		let searchVal = m.searchstr.val().trim().toLowerCase();
		paramGrid = 'piCd='+piCd+'&piDate='+piDate+'&storeCd='+storeCd+'&searchVal='+searchVal+'&startQty='+startQty+
			'&endQty='+endQty+'&startAmt='+startAmt+'&endAmt='+endAmt;
	}

	var getTableData = function () {
		if(verifySearch()) {
			setParamGrid();
			let activeTab = $('#card').find('li.active > a').attr('href');
			if (activeTab == '#card1') {
				getTableData1(paramGrid);
			} else if (activeTab == '#card2') {
				getTableData2(paramGrid);
			} else if (activeTab == '#card3') {
				getTableData3(paramGrid);
			}
		}
	}

	//审核事件
	var approval_event = function () {
		//点击审核按钮
		m.approvalBut.on("click",function(){
			var recordId = m.piCdParam.val();
			if(recordId!=null&&recordId!=""){
				$("#approval_dialog").modal("show");
				//获取审核记录
				_common.getStep(recordId,m.typeId.val());
			}else{
				_common.prompt("Record fetch failed, Please try again!",5,"error");
			}
		});
		//审核提交
		$("#audit_affirm").on("click",function () {
			//审核意见
			var auditContent = $("#auditContent").val();
			if(auditContent.length>200){
				_common.prompt("Approval comments cannot exceed 200 characters!",5,"error");
				return false;
			}
			//审核记录id
			var auditStepId = $("#auditId").val();
			//用户id
			var auditUserId = $("#auditUserId").val();
			//审核结果
			var auditStatus = $("#auditStatusGroup").find("input[type='radio']:checked").val();

			_common.myConfirm("Are you sure to save?", function (result) {
				if (result != "true") {
					return false;
				}
				var detailType = "tmp_stock_take";
				$.myAjaxs({
					url: _common.config.surl + "/audit/submit",
					async: true,
					cache: false,
					type: "post",
					data: {
						auditStepId: auditStepId,
						auditUserId: auditUserId,
						auditStatus: auditStatus,
						detailType:detailType,
						auditContent: auditContent,
						toKen: m.toKen.val()
					},
					dataType: "json",
					success: function (result) {
						if (result.success) {
							$("#approval_dialog").modal("hide");
							//更新主档审核状态值
							//_common.modifyRecordStatus(auditStepId,auditStatus);
							m.approvalBut.prop("disabled", true);

							// 判断是否审核完成
							let recordId = m.piCdParam.val();
							let startDate = m.piDateParam.val();
							let typeId = m.typeId.val();
							updateStatus(recordId, typeId, startDate);
							_common.prompt("Saved Successfully!",3,"success");// 保存审核信息成功
						} else {
							m.approvalBut.prop("disabled", false);
							_common.prompt("Saved Failure!",5,"error");// 保存审核信息失败
						}
						m.toKen.val(result.toKen);
					},
					complete:_common.myAjaxComplete
				});
			})
		});

		//检查是否允许审核
		_common.checkRole(m.piCdParam.val(),m.typeId.val(),function (success) {
			if(success){
				m.approvalBut.prop("disabled",false);
			}else{
				m.approvalBut.prop("disabled",true);
			}
		});
	}

	//初始化店铺下拉
	var initAutoMatic = function () {
		a_store = $("#store").myAutomatic({
			url: systemPath+"/ma1000/getStoreByPM",
			ePageSize: 10,
			startCount: 0
		});
	};

	var getData = function (piCd,piDate) {
		if (!piCd||!piDate) {
			return;
		}

		$.myAjaxs({
			url:url_left+"/getData",
			async:true,
			cache:false,
			type :"get",
			data:'piCd='+piCd+'&piDate='+piDate,
			dataType:"json",
			success:function(result) {
				if(result.success) {
					var record = result.o;
					$("#storeNo").val(record.piCd);
					$("#pd_date").val(record.piDate);
					$("#skType").val(record.piType);
					$("#skMethod").val(record.piMethod);
					$("#skStatus").val(record.piStatus);
					$("#pd_start_time").val(record.piStartTime);
					$("#pd_end_time").val(record.piEndTime);
					$("#createUser").val(record.createUserId);
					$("#updateUser").val(record.updateUserId);
					$("#remarks").val(record.remarks);
					$.myAutomatic.setValueTemp(a_store,record.storeCd,record.storeName);//赋值

					// 盘点完成才能导出
					$('#export_btn').prop('disabled',record.piStatus!='03');

					// 获取明细数据
					setParamGrid();
					// 列表初始化
					initTable1();
					initTable2();
					initTable3();
				}
			}
		});
	};

	var setDisable = function (flag) {
		$("#storeNo").prop("disabled", flag);
		$("#pd_date").prop("disabled", flag);
		$("#store").prop("disabled", flag);
		$("#search_item_but").prop("disabled", flag);
		$("#skType").prop("disabled", flag);
		$("#pd_start_time").prop("disabled", flag);
		$("#pd_end_time").prop("disabled", flag);
		$("#clear_pd_time").prop("disabled", flag);
		$("#remarks").prop("disabled", flag);
	}

	var getComboxData = function () {
		$.myAjaxs({
			url:systemPath+"/stocktakePlanEntry/getComboxData",
			async:false,
			cache:false,
			type :"get",
			dataType:"json",
			success:function(result){
				if(result.success){
					var record = result.o;
					var piMetList = record.piMethod;
					var piStsList = record.piStatus;
					var piTypeList = record.piType;
					piMetList.forEach(function (item) {
						var temp = '<option value="'+item.codevalue+'">'+item.codename+'</option>'
						$('#skMethod').append(temp);
					});

					piStsList.forEach(function (item) {
						var temp = '<option value="'+item.codevalue+'">'+item.codename+'</option>'
						$('#skStatus').append(temp);
					});

					piTypeList.forEach(function (item) {
						var temp = '<option value="'+item.codevalue+'">'+item.codename+'</option>'
						$('#skType').append(temp);
					});
				}
			}
		});
	}

	/**
	 * 审核完成修改状态
	 */
	var updateStatus = function (recordId,typeId,startDate) {
		$.myAjaxs({
			url:systemPath+"/stocktakeEntry/updateStatus",
			async:true,
			cache:false,
			type :"post",
			data :{
				piCd:recordId,
				typeId:typeId,
				piDate:startDate,
			},
			dataType:"json",
		});
	};

	//画面按钮点击事件
	var but_event = function(){
		//盘点日期
		m.pd_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		//开始时间
		m.pd_start_time.datetimepicker(
			{
				language:'en',
				autoclose: true,//选中之后自动隐藏日期选择框
				clearBtn: false,//清除按钮
				todayBtn: true,//今日按钮
				format: 'dd/mm/yyyy hh:ii:ss',
				startView: 'month',// 进来是月
				minView: 'hour',// 可以看到小时
				minuteStep:1, //分钟间隔为1分
				todayHighlight: true,
				forceParse: true,
			}).on('changeDate', function (ev) {
			if (ev.date) {
				$("#pd_end_time").datetimepicker('setStartDate', new Date(ev.date.valueOf()))
			} else {
				$("#pd_end_time").datetimepicker('setStartDate', null);
			}
		});
		//结束时间
		m.pd_end_time.datetimepicker({
			language:'en',
			autoclose: true,//选中之后自动隐藏日期选择框
			clearBtn: false,//清除按钮
			todayBtn: true,//今日按钮
			format: 'dd/mm/yyyy hh:ii:ss',
			startView: 'month',// 进来是月
			minView: 'hour',// 可以看到小时
			minuteStep:1, //分钟间隔为1分
			todayHighlight: true,
			forceParse: true,
		}).on('changeDate', function (ev) {
			if (ev.date) {
				$("#pd_start_time").datetimepicker('setEndDate', new Date(ev.date.valueOf()))
			} else {
				$("#pd_start_time").datetimepicker('setEndDate', null);
			}
		});
		//清空日期
		m.clear_pd_time.on("click",function(){
			m.pd_start_time.val("");
			m.pd_end_time.val("");
		});
	}

	var getTableData1 = function (paramGrid) {
		tableGrid1.setting("param", paramGrid);
		tableGrid1.setting("page", 1);
		tableGrid1.loadData();
	};
	var getTableData2 = function (paramGrid) {
		tableGrid2.setting("param", paramGrid);
		tableGrid2.setting("page", 1);
		tableGrid2.loadData();
	};
	var getTableData3 = function (paramGrid) {
		tableGrid3.setting("param", paramGrid);
		tableGrid3.setting("page", 1);
		tableGrid3.loadData();
	};

	//表格初始化-正常盘点商品
	var initTable1 = function(){
		tableGrid1 = $("#zgGridTtable1").zgGrid({
			title:"Item(Stocktaking Done)",
			url: url_left+"/getTableData1",
			param:paramGrid,
			colNames:["Item Barcode","Item Code","Item Name","Spec","UOM","Inventory(System)","Inventory Change","Bad Merchandising","Stocktaking Qty","Variance Qty","Variance Amount"],
			colModel:[
				{name:"barcode", type:"text", text:"right", width:"130", ishide:false,},
				{name:"articleId", type:"text", text:"right", width:"130", ishide:false, css:""},
				{name:"articleName", type:"text", text:"left", width:"130", ishide:false, css:""},
				{name:"spec", type:"text", text:"left", width:"130", ishide:false, css:""},
				{name:"uom", type:"text", text:"left", width:"130", ishide:false, css:""},
				{name:"onHandQty", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"changeQty", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"badQty", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"secondQty", type:"text", text:"right",sort:true,sortindex:"pi_qty", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"variance", type:"text", text:"right",sort:true,sortindex:"variance_qty", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"varianceAmt", type:"text", text:"right",sort:true,sortindex:"base_sale_price", width:"130", ishide:false, getCustomValue:getThousands},
			],//列内容
			traverseData:dataForm1,
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			sidx:"",//排序字段
			sord:"desc",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			footerrow:true,
			loadCompleteEvent:function(self) {


			},
			userDataOnFooter: true,
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			},
			buttonGroup:[
			],
		});
	}

	//表格初始化-未盘到商品
	var initTable2 = function(){
		tableGrid2 = $("#zgGridTtable2").zgGrid({
			title:"Item(Out of Stock in Store)",
			param:paramGrid,
			url: url_left+"/getTableData2",
			colNames:["Item Barcode","Item Code","Item Name","Spec","UOM","Inventory(System)","Inventory Change","Bad Merchandising","Stocktaking Qty","Variance Qty","Variance Amount"],
			colModel:[
				{name:"barcode", type:"text", text:"right", width:"130", ishide:false,},
				{name:"articleId", type:"text", text:"right", width:"130", ishide:false, css:""},
				{name:"articleName", type:"text", text:"left", width:"130", ishide:false, css:""},
				{name:"spec", type:"text", text:"left", width:"130", ishide:false, css:""},
				{name:"uom", type:"text", text:"left", width:"130", ishide:false, css:""},
				{name:"onHandQty", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"changeQty", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"badQty", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"secondQty", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"variance", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"varianceAmt", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
			],//列内容
			traverseData:dataForm2,
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			isCheckbox:false,
			// sidx:"",//排序字段
			// sord:"desc",//升降序
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			footerrow:true,
			loadCompleteEvent:function(self) {


			},
			userDataOnFooter: true,
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			},
		});
	}

	//表格初始化-账面无库存商品
	var initTable3 = function(){
		tableGrid3 = $("#zgGridTtable3").zgGrid({
			title:"Item(Out of Stock in Financially)",
			param:paramGrid,
			url: url_left+"/getTableData3",
			colNames:["Item Barcode","Item Code","Item Name","Spec","UOM","Inventory(System)","Inventory Change","Bad Merchandising","Stocktaking Qty","Variance Qty","Variance Amount"],
			colModel:[
				{name:"barcode", type:"text", text:"right", width:"130", ishide:false,},
				{name:"articleId", type:"text", text:"right", width:"130", ishide:false, css:""},
				{name:"articleName", type:"text", text:"left", width:"130", ishide:false, css:""},
				{name:"spec", type:"text", text:"left", width:"130", ishide:false, css:""},
				{name:"uom", type:"text", text:"left", width:"130", ishide:false, css:""},
				{name:"onHandQty", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"changeQty", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"badQty", type:"text", text:"right", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"secondQty", type:"text", text:"right",sort:true,sortindex:"pi_qty", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"variance", type:"text", text:"right",sort:true,sortindex:"variance_qty", width:"130", ishide:false, getCustomValue:getThousands},
				{name:"varianceAmt", type:"text", text:"right",sort:true,sortindex:"base_sale_price", width:"130", ishide:false, getCustomValue:getThousands},
			],//列内容
			traverseData:dataForm3,
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			sidx:"",//排序字段
			sord:"desc",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			footerrow:true,
			loadCompleteEvent:function(self) {


			},
			userDataOnFooter: true,
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			},
		});
	}
	self.init = init;
	return self;
});
var _start = require('start');
var _index = require('receiptEdit');
_start.load(function (_common) {
	_index.init(_common);
});
