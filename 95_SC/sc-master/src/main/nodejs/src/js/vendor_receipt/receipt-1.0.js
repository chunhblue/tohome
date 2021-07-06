require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("checkboxGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('receipt', function () {
	var self = {};
	var url_left = "",
		url_root = "",
	    paramGrid = null,
		approvalRecordsParamGrid = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
	    selectTrTemp = null,
		selectTrTemp_1 = null,
		tempTrObjValue = {},//临时行数据存储
		bmCodeOrItem = 0,//0 bmCode 1：item
		common=null;
	const KEY = 'ITEM_RECEIVING_ENTRY_FROM_SUPPLIER';
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson:null,
		main_box : null,
		// 栏位
		aRegion : null,
		aCity : null,
		aDistrict : null,
		aStore : null,
		ys_start_date: null,
		ys_end_date: null,
		yy_start_date: null,
		yy_end_date: null,
		ck_start_date: null,
		ck_end_date: null,
		orderId : null,
		receiveId : null,
		vendorId : null,
		dj_status : null,
		itemInfo : null,
		clear_ys_date : null,
		clear_yy_date : null,
		clear_ck_date : null,
		// 按钮
		search : null,
		reset : null,
		typeId:null,//审核类型id
		reviewId:null,//审核流程
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
		url_left = url_root+"/vendorReceipt";
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
		// 初始化店铺运营组织检索
		_common.initOrganization();
		// 初始化供应商、商品检索
		initAutomatic();
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// 表格内按钮事件
		table_event();
		//权限验证
		isButPermission();
		// 初始化检索日期
		_common.initDate(m.yy_start_date,m.yy_end_date);
		// 初始化 参数
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

		m.ys_start_date.val(obj.receivingStartDate);
		m.ys_end_date.val(obj.receivingEndDate);
		m.yy_start_date.val(obj.orderStartDate);
		m.yy_end_date.val(obj.orderEndDate);
		// m.ck_start_date.val(obj.deliveryStartDate);
		// m.ck_end_date.val(obj.deliveryEndDate);
		m.orderId.val(obj.orderId);
		m.receiveId.val(obj.receiveId);
		m.dj_status.val(obj.voucherStatus);
		$.myAutomatic.setValueTemp(itemInfo,obj.itemId,obj.itemName);
		$.myAutomatic.setValueTemp(vendorId,obj.vendorId,obj.vendorName);
		// 设置组织架构回显
		_common.setAutomaticVal(obj);

		//拼接检索参数
		setParamJson();
		paramGrid = "searchJson="+m.searchJson.val();
		tableGrid.setting("url",url_left+"/getdata");
		tableGrid.setting("param", paramGrid);
		tableGrid.setting("page", obj.page);
		tableGrid.loadData();
	}

	// 保存 参数信息
	var saveParamToSession = function () {
		let searchJson = m.searchJson.val();
		if (!searchJson) {
			return;
		}

		let obj = eval("("+searchJson+")");
		obj.regionName=$('#aRegion').attr('v');
		obj.cityName=$('#aCity').attr('v');
		obj.districtName=$('#aDistrict').attr('v');
		obj.storeName=$('#aStore').attr('v');
		obj.itemName=$('#itemInfo').attr('v');
		obj.vendorName=$('#vendorId').attr('v');
		obj.page=tableGrid.getting("page");
		obj.receivingStartDate=m.ys_start_date.val();
		obj.receivingEndDate=m.ys_end_date.val();
		obj.orderStartDate=m.yy_start_date.val();
		obj.orderEndDate=m.yy_end_date.val();
		// obj.deliveryStartDate=m.ck_start_date.val();
		// obj.deliveryEndDate=m.ck_end_date.val();
		obj.flg='0';
		sessionStorage.setItem(KEY,JSON.stringify(obj));
	}

	var table_event = function(){
		/*// 查看订单收货记录
		$("#receiveRecord").on("click",function(){
			let _list = tableGrid.getCheckboxTrs();
			if(_list == null || _list.length != 1){
				_common.prompt("Please select at least one row of data!",5,"info");
				return false;
			}
			let cols = tableGrid.getSelectColValue(_list[0],"storeCd,orderId");
			let param = "storeCd="+cols["storeCd"]+"&orderId="+cols["orderId"];
			rrTableGrid.setting("url",url_root+"/receive/getList");
			rrTableGrid.setting("param", param);
			rrTableGrid.setting("page", 1);
			rrTableGrid.loadData();
			saveParamToSession();
			$('#receiveRecords_dialog').modal("show");
		})

		$("#receiveView").on("click",function(){
			if(selectTrTemp_1==null){
				_common.prompt("Please select the data to view!",3,"info");/!*请选择要查看的数据*!/
				return false;
			}
			let cols = rrTableGrid.getSelectColValue(selectTrTemp_1,"receiveId");
			let receiveId = cols["receiveId"];
			if(!!receiveId){
				top.location = url_left+"/view?receiveId="+receiveId+"&type=1";
			}else{
				_common.prompt("Failed to get receiving no., please refresh the page!",3,"info");
			}
		})*/

		// 收货记录查看按钮
		$("#view").on("click",function(){
			let _list = tableGrid.getCheckboxTrs();
			if(_list == null || _list.length != 1){
				_common.prompt("Please select at least one row of data!",5,"info");
				return false;
			}
			let cols = tableGrid.getSelectColValue(_list[0],"receiveId");
			let receiveId = cols["receiveId"];
			if(!!receiveId){
				saveParamToSession();
				top.location = url_left+"/view?receiveId="+receiveId+"&type=1";
			}else{
				_common.prompt("Failed to get receiving no., please item receiving first!",3,"info");
			}
		})


		/*// 订单查看按钮
		$("#view").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select the data to view!",3,"info");/!*请选择要查看的数据*!/
				return false;
			}
			let cols = tableGrid.getSelectColValue(selectTrTemp,"storeCd,orderId");
			let storeCd = cols["storeCd"];
			let orderId = cols["orderId"];
			//xx
			top.location = url_left+"/view?&storeCd="+storeCd+"&orderId="+orderId;
		})*/

		// 详情&确认按钮
		$("#update").on("click",function(){
			//验证当前batch处理是否禁用
			let batchFlg = false;
			_common.batchCheck(function (result) {
				if(!result.success){//禁用
					_common.prompt(result.message,5,"info");
					batchFlg = false;
				}else{
					batchFlg = true;
				}
			})
			if(!batchFlg){return batchFlg};
			let _list = tableGrid.getCheckboxTrs();
			if(_list == null || _list.length != 1){
				_common.prompt("Please select at least one row of data!",5,"info");
				return false;
			}
			let cols = tableGrid.getSelectColValue(_list[0],"receiveId,reviewSts,storeCd,orderId,orderSts,multipleFlg");
			if(cols["orderSts"]=='04'){
				_common.prompt("This order does not allow for duplicate receiving!",3,"info");/*订单不允许重复收货*/
				return false;
			}
			let storeCd = cols["storeCd"];
			let orderId = cols["orderId"];
			let param = "storeCd="+storeCd+"&orderId="+orderId+"&type=0";
			let orderSts = "add";
			saveParamToSession();
			if(!!cols["receiveId"]){
				orderSts = "edit";
				param += "&orderSts="+orderSts;
				param += "&receiveId="+cols["receiveId"];
				param += "&reviewSts="+cols["reviewSts"];
				//获取数据审核状态
				_common.getRecordStatus(cols["receiveId"],m.typeId.val(),function (result) {
					if(result.success){
						top.location = url_left+"/details?"+param;
					}else{
						_common.prompt(result.message,5,"error");
					}
				});
			}else{
				param += "&orderSts="+orderSts;
				param += "&reviewSts="+cols["reviewSts"];
				top.location = url_left+"/details?"+param;
			}
		})

		// 快速确认按钮
		$("#quickReceiving").on("click",function(){
			//验证当前batch处理是否禁用
			let batchFlg = false;
			_common.batchCheck(function (result) {
				if(!result.success){//禁用
					_common.prompt(result.message,5,"info");
					batchFlg = false;
				}else{
					batchFlg = true;
				}
			})
			if(!batchFlg){return batchFlg};
			let _list = tableGrid.getCheckboxTrs();
			if(_list == null || _list.length == 0){
				_common.prompt("Please select the data to confirm receipt!",3,"info");
				return false;
			}
			var positionFlg = true;
			let _params = [], _flg = false, _temp = '';
			for(let i = 0; i < _list.length; i++){
				let cols = tableGrid.getSelectColValue(_list[i],"receiveId,reveiveStatus,storeCd,orderId,orderSts,multipleFlg,orderDifferentiate,reviewSts");
				_common.checkPosition(cols['storeCd'],function (result) {
					if(!result.success){
						_common.prompt("You do not have permission to quick receive "+cols['storeCd']+"!",5,"info");
						positionFlg = false;
					}
				});

				if(cols["orderSts"]=='04'||!!cols["reveiveStatus"]){
					if(!_flg){
						_flg = true;
						_temp = 'No.' + cols['orderId'];
					}else{
						_temp = _temp + ', No.' + cols['orderId'];
					}
				}
				let _param ={
					storeCd:cols['storeCd'],
					orderId:cols['orderId'],
					orderDifferentiate:cols['orderDifferentiate'],
					reviewSts:getIntStatus(cols['reviewSts']),
				};
				_params.push(_param);
			}
			if(!positionFlg){
				return false;
			}
			if(_flg){
				_common.prompt(_temp +" order does not allow for duplicate receiving!",3,"info");/* 订单不允许重复收货 */
				return false;
			}
			if(_params.length == 0){
				// _common.prompt("请选择待收货状态的数据",5,"error");
				_common.prompt("Please select the data of the status of goods to be received!",3,"info");
				return false;
			}
			let params = JSON.stringify(_params);
			_common.myConfirm("Are you sure you want to receiving?",function(result){
				if(result=="true"){
					$.myAjaxs({
						url:url_root+"/receive/quick",
						async:true,
						cache:false,
						type :"post",
						data :"searchJson="+params+"&toKen="+m.toKen.val(),
						dataType:"json",
						success:function(result){
							if(!result.success){
								_common.prompt("Order acceptance failed!",3,"error");
								return;
							}
							m.toKen.val(result.toKen);
							_common.prompt("Order accepted successfully!",3,"success");
							m.search.click();
						},
						complete:_common.myAjaxComplete
					});
				}
			})
		})

		// 打印按钮点击事件
		$("#print").on("click",function(){
			if(verifySearch()) {
				setParamJson();
				paramGrid = "searchJson=" + m.searchJson.val();
				var url = url_left + "/print?" + paramGrid;
				window.open(encodeURI(url), "print", "width=1400,height=600,scrollbars=yes");
			}
		});

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

		//审核记录
		$("#approvalRecords").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",3,"info");/*请选择要查看的数据*/
				return false;
			}
			let cols = tableGrid.getSelectColValue(selectTrTemp,"receiveId");
			if(cols["receiveId"]==null||cols["receiveId"]==""){
				_common.prompt("This order is not allowed to be viewed!",3,"info");
				return false;
			}
			approvalRecordsParamGrid = "id="+cols["receiveId"]+
				"&typeIdArray="+m.typeId.val();
			approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
			approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
			approvalRecordsTableGrid.setting("page", 1);
			approvalRecordsTableGrid.loadData(null);
			$('#approvalRecords_dialog').modal("show");
		});
    }

	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				initTable();//列表初始化
				isDisabledBtn();
				break;
			case "2":
			   break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}

	//画面按钮点击事件
	var but_event = function(){
		//清空日期
		m.clear_ys_date.on("click",function(){
			m.ys_start_date.val("");
			m.ys_end_date.val("");
		});
		//清空日期
		m.clear_yy_date.on("click",function(){
			m.yy_start_date.val("");
			m.yy_end_date.val("");
		});
		//开始日
		m.ys_start_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		//结束日
		m.ys_end_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		// 清空按钮
		m.reset.on("click",function(){
			$("#regionRemove").click();
			$("#itemIdRemove").click();
			$("#vendorIdRemove").click();
			m.ys_start_date.val("");
			m.ys_end_date.val("");
			m.yy_start_date.val("");
			m.yy_end_date.val("");
			m.orderId.val("");
			m.receiveId.val("");
			m.dj_status.val("");
			selectTrTemp = null;
			_common.clearTable();
			m.searchJson.val("");
			isDisabledBtn();
		});
		//检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()){
				//拼接检索参数
				setParamJson();
				paramGrid = "searchJson="+m.searchJson.val();
				tableGrid.setting("url",url_left+"/getdata");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);
				tableGrid.loadData();
			}
		});
		// 收货记录弹出窗-关闭按钮事件
		$("#receiveRecord_cancel").on("click",function(){
			selectTrTemp_1 = null;
			$('#receiveRecords_dialog').modal("hide");
		});
	}

	function judgeNaN (value) {
		return value !== value;
	}

	//验证检索项是否合法
	var verifySearch = function(){
		let _StartDate = null;
		if(!$("#yy_start_date").val()){
			_common.prompt("Please select a start date!",3,"info");/*请选择开始日期*/
			$("#yy_start_date").focus();
			return false;
		}else{
			_StartDate = new Date(fmtDate($("#yy_start_date").val())).getTime();
			if(_common.judgeValidDate($("#yy_start_date").val())){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#yy_start_date").focus();
				return false;
			}
		}
		let _EndDate = null;
		if(!$("#yy_end_date").val()){
			_common.prompt("Please select a end date!",3,"info");/*请选择结束日期*/
			$("#yy_end_date").focus();
			return false;
		}else{
			_EndDate = new Date(fmtDate($("#yy_end_date").val())).getTime();
			if(_common.judgeValidDate($("#yy_end_date").val())){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#yy_end_date").focus();
				return false;
			}
		}
		if(_StartDate>_EndDate){
			_common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
			$("#yy_end_date").focus();
			return false;
		}
		let ysdifValue = parseInt(Math.abs((_EndDate-_StartDate)/(1000*3600*24)));
		if(ysdifValue >62){
			_common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
			$("#yy_end_date").focus();
			return false;
		}

		let _yyStartDate = null, _yyEndDate = null;
		if(m.ys_start_date.val()){
			_yyStartDate = new Date(fmtDate($("#ys_start_date").val())).getTime();
			if(_common.judgeValidDate($("#ys_start_date").val())){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#ys_start_date").focus();
				return false;
			}
		}
		if(m.ys_end_date.val()){
			_yyEndDate = new Date(fmtDate($("#ys_end_date").val())).getTime();
			if(_common.judgeValidDate($("#ys_end_date").val())){
				_common.prompt("Please enter a valid date!",3,"info");
				$("#ys_end_date").focus();
				return false;
			}
		}
		if(_yyStartDate && _yyEndDate){
			if(_yyStartDate>_yyEndDate){
				_common.prompt("The start date cannot be greater than the end date!",3,"info");/*开始时间不能大于结束时间*/
				$("#ys_end_date").focus();
				return false;
			}
			let yydifValue = parseInt(Math.abs((_yyEndDate-_yyStartDate)/(1000*3600*24)));
			if(yydifValue >62){
				_common.prompt("Query Period cannot exceed 62 days!",3,"info"); // 日期期间取值范围不能大于62天
				$("#ys_end_date").focus();
				return false;
			}
		}
		return true;
	}

	//拼接检索参数
	var setParamJson = function(){
		// 收货日期
		var _receivingStartDate = fmtStringDate(m.ys_start_date.val())||null;
		var _receivingEndDate = fmtStringDate(m.ys_end_date.val())||null;

		// 订货日期
		var _orderStartDate = fmtStringDate(m.yy_start_date.val())||null;
		var _orderEndDate = fmtStringDate(m.yy_end_date.val())||null;


		// 创建请求字符串
		var searchJsonStr ={
			regionCd:$("#aRegion").attr("k"),
			cityCd:$("#aCity").attr("k"),
			districtCd:$("#aDistrict").attr("k"),
			storeCd:$("#aStore").attr("k"),
			receivingStartDate:_receivingStartDate,
			receivingEndDate:_receivingEndDate,
			orderStartDate:_orderStartDate,
			orderEndDate:_orderEndDate,
			orderId:$.trim(m.orderId.val()),
			vendorId:$("#vendorId").attr("k"),
			voucherStatus:m.dj_status.val(),
			itemId:$("#itemInfo").attr("k"),
			receiveId:$("#receiveId").val(),
		};
		m.searchJson.val(JSON.stringify(searchJsonStr));
	}

	// 多选时查看、编辑禁用
	function isDisabledBtn(){
		let _list = tableGrid.getCheckboxTrs();
		if(_list == null || _list.length != 1){
			$("#update").prop("disabled",true);
			$("#receiveRecord").prop("disabled",true);
			$("#approvalRecords").prop("disabled",true);
		}else{
			$("#update").prop("disabled",false);
			$("#receiveRecord").prop("disabled",false);
			$("#approvalRecords").prop("disabled",false);
		}
	}

	//表格初始化-进货单一览
	var initTable = function(){
		tableGrid = $("#zgGridTable").zgGrid({
			title:"Query Result",
			param:paramGrid,
			localSort: false,
			colNames:["Order Differentiate","Document No.","Status","Flg","Store No.","Receiving Store","Submission Date",
				"PO No.","Order Date","Document Status","Receive Status","Order Amount","Receiving Amount"],
			colModel:[
				{name:"orderDifferentiate",type:"text",text:"right",width:"140",ishide:true,css:""},
				{name:"receiveId",type:"text",text:"right",width:"140",ishide:false,css:""},
				{name:"orderSts",type:"text",text:"right",ishide:true},
				{name:"multipleFlg",type:"text",text:"right",ishide:true},
				{name:"storeCd",type:"text",text:"right",ishide:true},
				{name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"receiveDateT",type:"text",text:"center",width:"120",ishide:false,css:"",getCustomValue:dateFmt},
				// {name:"physicalReceivingDate",type:"text",text:"center",width:"120",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"orderId",type:"text",text:"right",width:"140",ishide:false,css:""},
				{name:"orderDate",type:"text",text:"center",width:"120",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"reviewSts",type:"text",text:"left",width:"130",ishide:false,getCustomValue:getStatus},
                {name:"reveiveStatus",type:"text",text:"left",width:"130",ishide:true},
				{name:"orderAmt",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
				{name:"receiveAmt",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands}
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent:function(self){
				isDisabledBtn()
				selectTrTemp = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
				isDisabledBtn()
				let _list = tableGrid.getCheckboxTrs();
				if(_list.length<1){
					selectTrTemp = null;//清空选择的行
				}
			},
			buttonGroup:[
				// {butType:"custom",butHtml:"<button id='receiveRecord' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-list-alt'></span> Receiving Records</button>"},
				{
					butType: "view",
					butId: "view",
					butText: "View",
					butSize: ""
				},//查看
				{
					butType: "update",
					butId: "update",
					butText: "Confirm Order Details and Receiving",
					butSize: ""
				},//修改
				{butType:"custom",butHtml:"<button id='quickReceiving' type='button' class='btn  btn-primary   btn-sm ' ><span class='glyphicon glyphicon glyphicon-ok'></span> Quick Receiving</button>"},
				{
					butType:"custom",
					butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
				},
				{butType:"custom",butHtml:"<button id='print' type='button' class='btn  btn-info   btn-sm ' ><span class='glyphicon glyphicon-print'></span> Print</button>"},
				{butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"}
			],
    	});

		//表格初始化-收货单一览
		rrTableGrid = $("#rrGridTable").zgGrid({
			title:"Receiving Records",
			param:paramGrid,
			localSort: false,
			colNames:["Status","Flg","Store No.","Receiving Store","Receiving Date",
				"Receiving No.","PO No.","Order Date","Document Status","Order Amount","Receiving Amount"],
			colModel:[
				{name:"orderSts",type:"text",text:"right",ishide:true},
				{name:"multipleFlg",type:"text",text:"right",ishide:true},
				{name:"storeCd",type:"text",text:"right",ishide:true},
				{name:"storeName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"receiveDate",type:"text",text:"center",width:"120",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"receiveId",type:"text",text:"right",width:"140",ishide:false,css:""},
				{name:"orderId",type:"text",text:"right",width:"140",ishide:false,css:""},
				{name:"orderDate",type:"text",text:"center",width:"120",ishide:false,css:"",getCustomValue:dateFmt},
				{name:"reviewSts",type:"text",text:"left",width:"130",ishide:false,getCustomValue:getStatus},
				{name:"orderAmt",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
				{name:"receiveAmt",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands}
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:5,//每页数据量
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
				selectTrTemp_1 = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp_1 = trObj;
			},
			buttonGroup:[
				{
					butType: "view",
					butId: "receiveView",
					butText: "Preview",
					butSize: ""
				}
			],
		});

		//审核记录
		approvalRecordsTableGrid = $("#approvalRecordsTable").zgGrid({
			title:"Approval Records",
			param:approvalRecordsParamGrid,
			colNames:["Approval Type","Result","User Code","User Name","Date Time","Comments"],
			colModel:[
				{name:"typeName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"result",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"userCode",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"userName",type:"text",text:"center",width:"100",ishide:false,css:""},
				{name:"dateTime",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"comments",type:"text",text:"center",width:"200",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			sidx:"",//排序字段
			sord:"",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			}
		});
    }

	// 按钮权限验证
	var isButPermission = function () {
		var viewBut = $("#viewBut").val();
		if (Number(viewBut) != 1) {
			$("#view").remove();
		}
		var confirmBut = $("#confirmBut").val();
		if (Number(confirmBut) != 1) {
			$("#update").remove();
		}
		var receiveBut = $("#receiveBut").val();
		if (Number(receiveBut) != 1) {
			$("#quickReceiving").remove();
		}
		var printBut = $("#printBut").val();
		if (Number(printBut) != 1) {
			$("#print").remove();
		}
		var exportBut = $("#exportBut").val();
		if (Number(exportBut) != 1) {
			$("#export").remove();
		}
		var approvalBut = $("#approvalBut").val();
		if (Number(approvalBut) != 1) {
			$("#approvalRecords").remove();
		}
	}

	// 日期字段格式化格式
	var dateFmt = function(tdObj, value){
		if(value!=null&&value.trim()!=''&&value.length==8) {
			value = fmtIntDate(value);
		}
    	return $(tdObj).text(value);
    }

	// 获取审核状态
	var getStatus = function(tdObj, value){
		let temp = '' + value;
		switch (temp) {
			case "1":
				value = "Pending";
				break;
			case "5":
				value = "Rejected";
				break;
			case "6":
				value = "Withdrawn";
				break;
			case "7":
				value = "Expired";
				break;
			case "10":
				value = "Approved";
				break;
			case "15":
				value = "Receiving Pending";
				break;
			case "20":
				value = "Received";
				break;
			case "30":
				value = "Paid";
				break;
			default:
				value = "";
		}
		return $(tdObj).text(value);
	};

	var getIntStatus = function(value){
		let temp = 0;
		switch (value) {
			case "Pending":
				temp = 1;
				break;
			case "Rejected":
				temp = 5;
				break;
			case "Withdrawn":
				temp = 6;
				break;
			case "Expired":
				temp = 7;
				break;
			case "Approved":
				temp = 10;
				break;
			case "Receiving Pending":
				temp = 15;
				break;
			case "Received":
				temp = 20;
				break;
			case "Paid":
				temp = 30;
				break;
			default:
				temp = 0;
		}
		return temp;
	};

	//字符串日期格式转换：dd/mm/yyyy → yyyymmdd
	function fmtStringDate(date){
		var res = "";
		date = date.replace(/\//g,"");
		res = date.substring(4,8)+date.substring(2,4)+date.substring(0,2);
		return res;
	}
	// DD/MM/YYYY to YYYY-MM-DD  格式转换
	function fmtDate(date) {
		var res = '';
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/,"$3-$2-$1");
		return res;
	}

	//格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		return res;
	}

	// 初始化供应商、商品检索
	function initAutomatic(){
		itemInfo = $("#itemInfo").myAutomatic({
			url: url_root + "/itemMaster/get",
			ePageSize: 10,
			startCount: 0
		});
		$("#itemIdRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(itemInfo);
		});
		vendorId = $("#vendorId").myAutomatic({
			url: url_root + "/vendorMaster/get",
			ePageSize: 10,
			startCount: 0
		});
		$("#vendorIdRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(vendorId);
		});
	}

	self.init = init;
	return self;
});
var _start = require('start');
var _index = require('receipt');
_start.load(function (_common) {
	_index.init(_common);
});
