require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("jquery-ui");

var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('orderEdit', function () {
	var self = {};
	var url_left = "",
		systemPath = "",
		paramGrid = null,
		paramGrid1 = null,
		selectTrTemp = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		order_url = "",
		tempTrObjValue = {},//临时行数据存储
		i_shelf = 0,
		i_dpt = 0,
		minOrderQty = null,
		maxOrderQty = null,
		orderBatchQty = null,
		loadPage = 1,
		common=null;
	const KEY = 'ORDER_ENTRY_TO';
	var inputResources = [];
	var nextResources = {};
	var shelfInputResources = [];
	var shelfNextResources = {};

	var shelfInputParam = {
		shelf:"",
		subShelf:""
	};

	var m = {
		toKen : null,
		use : null,
		up : null,
		tableGrid : null,
		selectTrObj : null,
		error_pcode : null,
		identity : null,
		type : null,
		reject_dialog : null,
		returnsViewBut : null,//返回按钮
		orderType : null,
		orderDate : null,
		orderQty : null,
		storeCd : null,
		store_name : null,
		depCd : null,
		pmaCd : null,
		categoryCd : null,
		subCategoryCd : null,
		param_category:null,
		param_subCategory:null,
		shelf : null,
		subShelf : null,
		param_shelf : null,
		param_subShelf : null,
		input_item : null,
		barcode : null,
		itemCode : null,
		itemName : null,
		cancel : null,
		affirm : null,
		submitBut : null,
		submitAuditBut : null,
		division : null,
		dpt: null,
		order_date: null,
		alert_div : null,//页面提示框
		search : null,//检索按钮
		reset : null,//重置按钮
		main_box : null,//检索按钮
		store : null,
		search_item_but : null,
		searchJson:null,
		storeSts : null,//是否为新店 true为新店 false 则不是
		//审核
		approvalBut:null,
		approval_dialog:null,
		audit_cancel:null,
		audit_affirm:null,
		typeId:null,
		reType:null,
		aStore:null,
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
		systemPath = _common.config.surl;
		order_url = _common.config.surl+"/order";
		url_left=_common.config.surl+"/storeOrder";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		but_event();
		initCategoryAutoMatic();
		//设定不同画面样式
		initPageBytype(m.type.val());
		//表格内按钮事件
		table_event();
		shelf_init();
		//权限验证
		isButPermission();
		m.search.click();
		//审核事件
		// approval_event();
		m.approvalBut.hide();
		m.submitAuditBut.hide();
		if(m.use.val()==="0"){
			$("#update").attr("disabled", "disabled");
			m.submitBut.attr("disabled","disabled");
		}
		$(document).on("show.bs.modal", ".modal", function(){
			$(this).draggable();
			$(this).css("overflow", "hidden");//禁止模态对话框的半透明背景滚动
		});
	}


	var initCategoryAutoMatic = function () {
		$("#category").attr("disabled", false);
		$("#subCategory").attr("disabled", true);
		$("#categoryRefresh").show();
		$("#categoryRemove").show();
		$("#subCategoryRefresh").hide();
		$("#subCategoryRemove").hide();
		a_category = $("#category").myAutomatic({
			url:systemPath + "/roleStore/categorys",
			ePageSize: 10,
			startCount: 0,
			param:[
				{
					'k':'pmaId',
					'v':'pmaCd'

				},{
					'k':'depId',
					'v':'depCd'
				}],
			selectEleClick: function (thisObj) {
				$.myAutomatic.cleanSelectObj(a_subCategory);
				$("#subCategory").attr("disabled", true);
				$("#subCategoryRefresh").hide();
				$("#subCategoryRemove").hide();
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					$("#subCategory").attr("disabled", false);
					$("#subCategoryRefresh").show();
					$("#subCategoryRemove").show();
				}
				var cinput = thisObj.attr("k");
				var str ="&categoryId=" + cinput;
				$.myAutomatic.replaceParam(a_subCategory, str);//赋值
			}
		});
		a_subCategory = $("#subCategory").myAutomatic({
			url: systemPath + "/roleStore/subCategorys",
			ePageSize: 10,
			startCount: 0,
		});
		$.myAutomatic.cleanSelectObj(a_category);
		$.myAutomatic.cleanSelectObj(a_subCategory);
		$("#categoryRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_subCategory);
			$("#subCategory").attr("disabled", true);
			$("#subCategoryRefresh").hide();
			$("#subCategoryRemove").hide();
		});
	}
	var shelf_init = function(){
		var inputs = [];
		if(m.param_shelf) {
			inputs.push("param_shelf");
		}
		if(m.param_subShelf) {
			inputs.push("param_subShelf");
		}
		shelfInputResources = [];
		if(m.param_shelf) {
			shelfInputResources.push("shelf");
		}
		if(m.param_subShelf) {
			shelfInputResources.push("subShelf");
		}
		shelfNextResources = {};
		for(i=0;i<shelfInputResources.length-1;i++) {
			shelfNextResources[shelfInputResources[i]] = shelfInputResources[i+1];
		}
		if(shelfInputResources.length > 0) {
			var firstResource = shelfInputResources[0];
			initShelfResourceList(firstResource, null);
		}
	}

	function setShelfResourceValue(resource, value) {
		shelfInputParam[resource] = value;
		var inputId = "param_" + resource;
		if(m[inputId]) {
			m[inputId].val(value);
		}
		var next = shelfNextResources[resource];
		if(next) {
			initShelfResourceList(shelfNextResources[resource], value);
		}
	}

	function initShelfResourceList(resource, parentValue) {
		switch(resource) {
			case "shelf":
				initShelfList(parentValue);
				break;
			case "subShelf":
				initSubShelfList(parentValue);
				break;
		}
	}

	function initShelfList(parentValue) {
		if(parentValue == null || parentValue.length > 0) {
			$.myAjaxs({
				url:systemPath+"/storeItemRelationship/getShelf",
				async:true,
				cache:false,
				type :"get",
				data :"storeCd="+m.storeCd.val(),
				dataType:"json",
				success:function(res){
					var htmlStr = '<option value="">-- All/Please Select --</option>';
					$.each(res,function(ix,node){
						htmlStr+='<option value="'+node+'">'+node+'</option>';
					});
					m.param_shelf.html(htmlStr);
					if(i_shelf==0){
						m.param_shelf.val(m.shelf.val()).trigger('change');
						i_shelf++;
					}
					setShelfResourceValue("shelf", shelfInputParam.shelf);
				},
				complete:_common.myAjaxComplete
			});
		} else {
			m.param_shelf.html('<option value="">-- All/Please Select --</option>');
			setShelfResourceValue("shelf", "");
		}
	}

	function initSubShelfList(parentValue) {
		if(parentValue == null || parentValue.length > 0) {
			$.myAjaxs({
				url:systemPath+"/storeItemRelationship/getSubShelf",
				async:true,
				cache:false,
				type :"get",
				data :"storeCd="+m.storeCd.val()+"&shelf="+m.shelf.val(),
				dataType:"json",
				success:function(res){
					var htmlStr = '<option value="">-- All/Please Select --</option>';
					$.each(res,function(ix,node){
						htmlStr+='<option value="'+node+'">'+node+'</option>';
					});
					m.param_subShelf.html(htmlStr);
					if(i_shelf==1){
						if(m.subShelf.val()!=null&&m.subShelf.val()!=''){
							m.param_subShelf.val(m.subShelf.val()).trigger('change');
							i_shelf++;
							m.search.click();
						}
					}
					setShelfResourceValue("subShelf", shelfInputParam.subShelf);
				},
				complete:_common.myAjaxComplete
			});
		} else {
			m.param_subShelf.html('<option value="">-- All/Please Select --</option>');
			setShelfResourceValue("subShelf", "");
		}
	}

	var table_event = function(){
		//添加修改商品
		$("#add").on("click", function () {
			$('#i_articleId').removeAttr("disabled");
			$('#i_articleName').removeAttr("disabled");
			$('#i_realtimeStock').removeAttr("disabled");
			$('#i_autoOrderQty').removeAttr("disabled");
			$('#i_articleId').val("");
			$('#i_realtimeStock').val("");
			$('#i_autoOrderQty').val("");
			$('#i_orderQty').val("");
			$('#orderAdd_dialog').modal("show");
		});

		$("#i_autoOrderQty").blur(function () {
			$("#i_autoOrderQty").val(toThousands(this.value));
		});

		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#i_autoOrderQty").focus(function(){
			$("#i_autoOrderQty").val(reThousands(this.value));
		});

		$("#i_orderQty").blur(function () {
			$("#i_orderQty").val(toThousands(this.value));
		});

		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#i_orderQty").focus(function(){
			$("#i_orderQty").val(reThousands(this.value));
		});



		// $("#update").on("click", function () {
		// 	if(selectTrTemp==null){
		// 		_common.prompt("Please select at least one item!",5,"error"); // 请选择要订货的商品
		// 		return false;
		// 	}
		// 	//正在修改的行不能再修改
		// 	if($("#grid_orderQty").length >= 1){
		// 		return false;
		// 	}
		// 	if($("#articleId").val()==null||$("#articleId").val()==""){
		// 		_common.prompt("No relevant item information was found!",5,"error"); // 获取商品详细信息失败
		// 		return false;
		// 	}
		// 	var cols = tableGrid.getSelectColValue(selectTrTemp,"articleId,articleName,realtimeStock,psd,autoOrderQty,orderQty,vendorId,purchaseUnitId,orderPrice,dcItem");
		// 	$(selectTrTemp).find('td[tag=orderQty]').text("").append("<input type='text' class='form-control my-automatic input-sm' id='grid_orderQty' oldValue='"+cols["orderQty"]+"' value='"+cols["orderQty"]+"'>");
		// });


		m.cancel.on("click",function () {
			$('#orderAdd_dialog').modal("hide");
		})
		m.affirm.on("click", function () {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one item!",5,"info");
				return false;
			}
			var orderQty = reThousands($('#i_orderQty').val().trim());
			var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
			if(!reg.test(orderQty)){
				_common.prompt("Please enter with correct data type!",3,"info");
				$('#i_orderQty').focus();
				return false;
			}
			var _orderQty = parseFloat(orderQty),
				_minOrderQty = parseFloat(minOrderQty),
				_maxOrderQty = parseFloat(maxOrderQty),
				_orderBatchQty = parseFloat($("#orderBatchQty").val());
			if(_orderQty==0){//订货量不为空
				_common.prompt("Order Qty can not be 0!",3,"info");
				$('#i_orderQty').focus();
				return false;
			}

			var colDcItem = tableGrid.getSelectColValue(selectTrTemp,"dcItem");
			var dcItem = colDcItem["dcItem"];
			if(!isNaN(_minOrderQty)&&_orderQty<_minOrderQty){//不得低于最低订货量
				if(dcItem=="1"){
					_common.prompt("Order Qty can not be less than DC MOQ!",3,"info");
				}else{
					_common.prompt("Order Qty can not be less than Min. Order Qty!",3,"info");
				}
				$('#i_orderQty').focus();
				return false;
			}
			if(_orderQty>999999){//不得大于最高订货量
				_common.prompt("Order Qty can not be greater than 999999!",3,"info");
				$('#i_orderQty').focus();
				return false;
			}
			if(isNaN(_minOrderQty)){
				_minOrderQty = 0;
			}

			if(isNaN(_orderBatchQty)) {//必须为正确的订货批量
				_common.prompt("The Incremental Quantity error!",3,"error");
				return false;
			}
			if(_orderBatchQty!==0){
				var remainder = (_orderQty-_minOrderQty)%_orderBatchQty;
				if(remainder!=0){
					_common.prompt("Order Qty need to be multiples of Incremental Quantity!",3,"info");
					$('#i_orderQty').focus();
					return false;
				}
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"articleId,articleName,realtimeStock,autoOrderQty,orderQty,uploadOrderQty,vendorId,purchaseUnitId,orderPrice,dcItem");
			var data = {
				"articleId" : cols["articleId"],
				"storeCd" : m.storeCd.val(),
				"orderDate" : m.orderDate.val(),
				"vendorId" : (cols["vendorId"]).trim(),
				// "orderQty" : cols["orderQty"],
				"orderQty" : reThousands(orderQty),
				"uploadOrderQty" : reThousands(cols["uploadOrderQty"]),
				"dcItem" : cols["dcItem"],
				"orderPrice" : cols["orderPrice"],
				"purchaseUnitId" : cols["purchaseUnitId"]
			};
			_common.myConfirm("Are you sure to submit?", function (result) {
				if (result != "true") {
					return false;
				}
				$.myAjaxs({
					url:order_url+"/updateOrder",
					async:true,
					cache:false,
					type :"post",
					data :data,
					dataType:"json",
					success:function(data,textStatus, xhr){
						var resp = xhr.responseJSON;
						if( resp.result == false){
							top.location = resp.s+"?errMsg="+resp.errorMessage;
							return ;
						}
						if(data.success==true){
							$('#orderAdd_dialog').modal("hide");
							m.search.click();
							_common.prompt("Modify Succeeded!",2,"success",function(){ // 修改成功
								// m.search.click();
							},false);
						}else{
							_common.prompt(data.message,5,"error");
						}
					},
					complete:_common.myAjaxComplete
				});
			})
		});

		m.submitBut.on("click", function () {
			//正在修改的行不能再修改
			if($("#grid_orderQty").length >= 1){
				_common.prompt('There are items being modified',3,"info");
				return false;
			}
			_common.myConfirm("Are you sure to save?", function (result) {
				if (result != "true") {
					return false;
				}
				$.myAjaxs({
					url:order_url+"/save",
					async:true,
					cache:false,
					type :"post",
					data :{
						toKen:m.toKen.val(),
						storeCd:m.storeCd.val()
					},
					dataType:"json",
					success:function(result){
						m.toKen.val(result.toKen);
						if(result.success){
							_common.prompt("Operation Succeeded!",2,"success");// 成功
						}else{
							_common.prompt(result.message,5,"error");
						}
					},
					complete:_common.myAjaxComplete
				});
			})
		})



		//返回一览
		m.returnsViewBut.on("click",function(){
			var url_param = "";
			var orderType = m.orderType.val();
			var storeCd = m.storeCd.val();
			var orderDate = fmtIntDate(m.orderDate.val());
			url_param += "orderDate="+orderDate;
			_common.updateBackFlg(KEY);
			top.location = order_url+"?"+url_param;
		});

		//是否有冻结列的标记
		var freeze = tableGrid.getting("freezeIndex");
		//重写选择背景变色事件
		tableGrid.find("tbody").unbind('mousedown').on('mousedown','td', function (e) {
			var thisObj = $(this),
				thisParent = $(this).parent();
			if(3 == e.which){ //鼠标右键
				thisParent.addClass("info").siblings().removeClass("info");
				var eachTrRightclickFun = $.isFunction(tableGrid.getting("eachTrRightclick")) ? true : false;
				if(eachTrRightclickFun){
					tableGrid.getting("eachTrRightclick").call(this,thisParent,thisObj);
				}
				if(freeze>=0){
					var freeToTr = thisParent.prop("id");
					$("#"+freeToTr+"_free").addClass("info").siblings().removeClass("info");
				}
			}else if(1 == e.which){ //鼠标左键
				var cols = tableGrid.getSelectColValue(thisParent,"articleId");
				var articleId = $(selectTrTemp).find('td[tag=articleId]').text();
				if(cols["articleId"] != articleId && $('#grid_orderQty').length == 1){
					var flg = checkOrderQty();
					if(flg){
						//还原表格中的input框
						let orderQty = toThousands($('#grid_orderQty').val().trim());
						$(selectTrTemp).find('td[tag=orderQty]').text(orderQty);
					}else{
						let orderQty = toThousands($('#grid_orderQty').attr("oldValue"));
						$(selectTrTemp).find('td[tag=orderQty]').text(orderQty);
						return false;
					}
				}
				//改变背景颜色
				thisParent.addClass("info").siblings().removeClass("info");
				if(freeze>=0){
					var freeToTr = thisParent.prop("id");
					$("#"+freeToTr+"_free").addClass("info").siblings().removeClass("info");
				}
				$(".zgGrid-modal").hide();
				var eachTrClickFun = $.isFunction(tableGrid.getting("eachTrClick")) ? true : false;
				if(eachTrClickFun){
					tableGrid.getting("eachTrClick").call(this,thisParent,thisObj);
				}
			}
		});

		//光标移出
		m.main_box.on("blur","#grid_orderQty",function(){
			$("#grid_orderQty").val(this.value);
			// var flg = checkOrderQty();
			// if(flg){
			// 	//还原表格中的input框
			// 	var orderQty = toThousands($('#grid_orderQty').val().trim());
			// 	$(selectTrTemp).find('td[tag=orderQty]').text(orderQty);
			// }else{
			// 	return false;
			// }
		});

		//光标进入，去除金额千分位，并去除小数后面多余的0
		m.main_box.on("focus","#grid_orderQty",function(){
			$("#grid_orderQty").val(this.value);
		})

		//回车保存
		m.main_box.on("keydown","#grid_orderQty",function(e){
			if(e.keyCode==13){
				var flg = checkOrderQty();
				if(flg){
					//还原表格中的input框
					var orderQty = toThousands($('#grid_orderQty').val().trim());
					$(selectTrTemp).find('td[tag=orderQty]').text(orderQty);
				}else{
					let orderQty = $('#grid_orderQty').attr("oldValue");
					$(selectTrTemp).find('td[tag=orderQty]').text(orderQty);
					return false;
				}
			}
		})
	}

	var checkOrderQty = function () {

		var orderQty = reThousands($('#grid_orderQty').val().trim());
		var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
		if(!reg.test(orderQty)){
			_common.prompt("Please enter with correct data type!",3,"info");
			$('#grid_orderQty').focus();
			return false;
		}
		var _orderQty = parseFloat(orderQty),
			_minOrderQty = parseFloat(minOrderQty),
			_maxOrderQty = parseFloat(maxOrderQty),
			_orderBatchQty = parseFloat($("#orderBatchQty").val());
		// if(_orderQty==0){//订货量不为空
		// 	_common.prompt("Order Qty can not be 0!",3,"info");
		// 	$('#grid_orderQty').focus();
		// 	return false;
		// }
		var colDcItem = tableGrid.getSelectColValue(selectTrTemp,"dcItem,uploadFlg,uploadOrderQty,uploadOrderNochargeQty");
		var dcItem = colDcItem["dcItem"];
		var uploadFlg = colDcItem["uploadFlg"];
		var uploadOrderQty = Number(reThousands(colDcItem["uploadOrderQty"]));
		var uploadOrderNochargeQty = Number(reThousands(colDcItem["uploadOrderNochargeQty"]));
		if(!isNaN(_minOrderQty)&&_orderQty<_minOrderQty&&_orderQty!=0){//不得低于最低订货量
			if(dcItem=="1"){
				_common.prompt("Order Qty can not be less than DC MOQ!",3,"info");
			}else{
				_common.prompt("Order Qty can not be less than Min. Order Qty!",3,"info");
			}
			$('#grid_orderQty').focus();
			return false;
		}
		if(_orderQty>999999){//不得大于最高订货量
			_common.prompt("Order Qty can not be greater than 999999!",3,"info");
			$('#grid_orderQty').focus();
			return false;
		}
		if(isNaN(_minOrderQty)){
			_minOrderQty = 0;
		}

		if(isNaN(_orderBatchQty)) {//必须为正确的订货批量
			_common.prompt("The Incremental Quantity error!",3,"error");
			return false;
		}
		if(_orderBatchQty!==0){
			var remainder = (_orderQty-_minOrderQty)%_orderBatchQty;
			if(remainder!==0){
				_common.prompt("Order Qty need to be multiples of Incremental Quantity!",3,"info");
				$('#grid_orderQty').focus();
				return false;
			}
		}
		//订货量大于上传的订货量
		/*if(uploadFlg=="1"&&uploadOrderQty>0){
			if(_orderQty<uploadOrderQty){
				_common.prompt("Order Qty can not be less "+uploadOrderQty+"!",3,"info");
				return false;
			}
		}*/
		var cols = tableGrid.getSelectColValue(selectTrTemp,"articleId,articleName,realtimeStock,autoOrderQty,orderQty,vendorId,purchaseUnitId,orderPrice,dcItem");
		var data = {
			"articleId" : cols["articleId"],
			"storeCd" : m.storeCd.val(),
			"orderDate" : m.orderDate.val(),
			"vendorId" : (cols["vendorId"]).trim(),
			// "orderQty" : cols["orderQty"],
			"orderQty" : reThousands(orderQty),
			"dcItem" : cols["dcItem"],
			"orderPrice" : cols["orderPrice"],
			"purchaseUnitId" : cols["purchaseUnitId"]
		};
		var flg = false;
		$.myAjaxs({
			url:order_url+"/updateOrder",
			async:false,
			cache:false,
			type :"post",
			data :data,
			dataType:"json",
			success:function(data,textStatus, xhr){
				var resp = xhr.responseJSON;
				flg = data.success;
				if( resp.result == false){
					top.location = resp.s+"?errMsg="+resp.errorMessage;
					return ;
				}
				if(data.success==true){
					// $('#orderAdd_dialog').modal("hide");
					m.search.click();
					_common.prompt("Modify Succeeded!",2,"success",function(){ // 修改成功
						// m.search.click();
					},true);
				}else{
					_common.prompt(data.message,5,"error");
				}
			},
			complete:_common.myAjaxComplete
		});
		return flg;
	}
	var addInputValue=function () {
		   var cols = tableGrid.getSelectColValue(selectTrTemp,"articleId,articleName,realtimeStock,psd,autoOrderQty,orderQty,vendorId,purchaseUnitId,orderPrice,dcItem");
		   	$(selectTrTemp).find('td[tag=orderQty]').text("").append("<input type='text' class='form-control my-automatic input-sm' id='grid_orderQty' oldValue='"+cols["orderQty"]+"' value='"+cols["orderQty"]+"'>");
	   }
	var showResponse = function(data,textStatus, xhr){
		selectTrTemp = null;
		var resp = xhr.responseJSON;
		if( resp.result == false){
			top.location = resp.s+"?errMsg="+resp.errorMessage;
			return ;
		}
		if(data.success==true){
			_common.prompt("Operation Succeeded!",1,"success",function(){ // 操作成功
				m.search.click();
			},true);
		}else{
			_common.prompt(data.message,5,"error");
		}
		m.toKen.val(data.toKen);
	}
	// 初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				//列表初始化
				initTable1();
				initTable2();
				if(m.orderType.val()=="1"){

				}else{
					shelf_init();
				}
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}

	}

	//画面按钮点击事件
	var but_event = function(){
		//清空搜索条件
		m.reset.on("click",function(){
			var orderType = m.orderType.val();
			$("#zgGridTtable  tr:not(:first)").remove();
			$("#item_info").find("input").val("");
			$("#vendorIdRemove").click();
			if(orderType=="1"){
				$("#categoryRemove").click();
				$("#subCategoryRemove").click();
				m.barcode.val("");
				m.itemCode.val("");
				m.itemName.val("");
			}else if(orderType == "2"){
				m.param_subShelf.val("");
				m.barcode.val("");
				m.itemCode.val("");
				m.itemName.val("");
			}
		})
		//检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()){
				var orderType = m.orderType.val();
				var orderdate = m.orderDate.val();
				var storeCd = m.storeCd.val();
				var depCd = m.depCd.val();
				var pmaCd = m.pmaCd.val();
				var categoryCd=$("#category").attr("k");
				var subCategoryCd=$("#subCategory").attr("k");
				var shelf = m.shelf.val();
				var subShelf = m.param_subShelf.val();
				var barcode = m.barcode.val();
				var articleId = m.itemCode.val();
				var articleName = m.itemName.val();
				var vendorId = $("#vendorId").attr("k");
				if(orderType=="1"){
					paramGrid = "orderDate="+orderdate+"&storeCd="+storeCd+
						"&orderType="+orderType+"&depCd="+depCd+"&pmaCd="+pmaCd+
						"&categoryCd="+categoryCd+"&subCategoryCd="+subCategoryCd+
						"&barcode="+barcode+"&articleId="+articleId+"&articleName="+articleName+"&vendorId="+vendorId;
				}else if(orderType == "2"){
					paramGrid = "orderDate="+orderdate+"&storeCd="+storeCd+
						"&orderType="+orderType+"&shelf="+shelf+
						"&subShelf="+subShelf+"&barcode="+barcode+
						"&articleId="+articleId+"&articleName="+articleName+"&vendorId="+vendorId;
				}
				tableGrid.setting("url",order_url+"/getDetailList");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", $("#loadPage").val());
				tableGrid.loadData(null);
			}
		});

		if(m.param_shelf) {
			m.param_shelf.on("change", function(){
				shelfInputParam.subShelf = "";
				setShelfResourceValue("shelf", $(this).val());
			});
		}
		if(m.param_subShelf) {
			m.param_subShelf.on("change", function(){
				setShelfResourceValue("subShelf", $(this).val());
			});
		}

		// Item Barcode输入口 焦点离开事件
		m.input_item.on("blur",function(){
			if($(this).val()==""){
				m.input_item.attr("status","0");
				m.item_name.text("");
			}
		});
		m.input_item.on("keydown",function(){
			if(event.keyCode == "13"){
				var thisObj = $(this);
				if(thisObj.val()==""){
					m.item_name.text("");
				}else{
					m.search_item_but.click();
				}
			}
		});
		//点击查找Item Barcode按钮事件
		m.search_item_but.on("click",function(){
			var inputVal = m.input_item.val();
			m.input_item.attr("status","0");
			itemTempObj = null;
			m.item_name.text("");
			if($.trim(inputVal)==""){
				_common.prompt("Item Barcode cannot be empty!",5,"error"); // Item Barcode不能为空
				m.input_item.attr("status","2").focus();
				return false;
			}
			var reg = /^[0-9]*$/;
			if(!reg.test(inputVal)){
				_common.prompt("Item Barcode must be a number!",5,"error"); // Item Barcode必须是纯数字
				m.input_item.attr("status","2").focus();
				return false;
			}
			m.item_name.text("Loading...");
			getItemInfoByItem1(inputVal,function(res){
				if(res.success){
					m.item_name.text($.trim(res.data.itemName));
					m.input_item.attr("status","1");
					return true;
				}else{
					m.item_name.text("Item Name is not obtained!"); // 未取得Item Name
					m.input_item.attr("status","2");
					_common.prompt(res.message,5,"error");
					return false;
				}
			});

		});

		//供应商模糊下拉
		vendorId = $("#vendorId").myAutomatic({
			url: systemPath + "/vendorMaster/get",
			ePageSize: 8,
			startCount: 0
		});
	}

	//验证检索项是否合法
	var verifySearch = function(){
		var orderType = m.orderType.val();
		var storeCd = m.storeCd.val();
		var depCd = m.depCd.val();
		var pmaCd = m.pmaCd.val();
		var shelf = m.shelf.val();
		if(storeCd==null || storeCd==""){
			_common.prompt("Store No. cannot be empty!",5,"error"); // 店铺编号为空
			return false;
		}
		if(orderType==null || orderType==""){
			_common.prompt("The order type is empty！",5,"error");  // 订货类型为空
			return false;
		}else{
			if(orderType=="1"){
				if(pmaCd == null || pmaCd ==""){
					_common.prompt("Department is empty！",5,"error"); // Department 为空
					return false;
				}
			}else if (orderType=="2"){
				if(shelf == null || shelf ==""){
					_common.prompt("Shelf is empty！",5,"error"); // Shelf 为空
					return false;
				}
			}else{
				_common.prompt("Wrong order type！",5,"error"); // 订货类型错误
				return false;
			}
		}
		return true;
	}

	//根据Item Barcode取得该商品的详细对象，
	var getItemInfoByItem1 = function(item1,fun){
		$.myAjaxs({
			url:order_url+"/getItemInfo",
			async:true,
			cache:false,
			type :"get",
			data :"itemCode="+item1,
			dataType:"json",
			success:fun,
			complete:_common.myAjaxComplete
		});
	}

	// 点击copy后事件
	var trClick_copy = function (trObj,tdObj) {
		let id = $(tdObj[0]).context.id;
		// 不是点的copy按钮, 不执行此函数
		if (id.indexOf("copy")==-1) {
			return;
		}

		// 将推荐订货复制到订货数量上面
		var cols = tableGrid.getSelectColValue(trObj,"articleId,articleName,realtimeStock,psd,autoOrderQty,orderQty,vendorId,purchaseUnitId,orderPrice,dcItem");
		let autoOrderQty = reThousands(cols["autoOrderQty"]);
		var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
		if(!reg.test(autoOrderQty)){
			_common.prompt("Please enter with correct data type!",3,"info");
			return false;
		}
		var _orderQty = parseFloat(autoOrderQty),
			_minOrderQty = parseFloat(minOrderQty),
			_orderBatchQty = parseFloat($("#orderBatchQty").val());
		var colDcItem = tableGrid.getSelectColValue(trObj,"dcItem,uploadFlg,uploadOrderQty,uploadOrderNochargeQty");
		var dcItem = colDcItem["dcItem"];
		if(!isNaN(_minOrderQty)&&_orderQty<_minOrderQty){//不得低于最低订货量
			if(dcItem=="1"){
				_common.prompt("Order Qty can not be less than DC MOQ!",3,"info");
			}else{
				_common.prompt("Order Qty can not be less than Min. Order Qty!",3,"info");
			}
			return false;
		}
		if(_orderQty>999999){//不得大于最高订货量
			_common.prompt("Order Qty can not be greater than 999999!",3,"info");
			return false;
		}
		if(isNaN(_minOrderQty)){
			_minOrderQty = 0;
		}

		if(isNaN(_orderBatchQty)) {//必须为正确的订货批量
			_common.prompt("The Incremental Quantity error!",3,"error");
			return false;
		}
		if(_orderBatchQty!==0){
			var remainder = (_orderQty-_minOrderQty)%_orderBatchQty;
			if(remainder!==0){
				_common.prompt("Order Qty need to be multiples of Incremental Quantity!",3,"info");
				$('#grid_orderQty').focus();
				return false;
			}
		}
		var data = {
			"articleId" : cols["articleId"],
			"storeCd" : m.storeCd.val(),
			"orderDate" : m.orderDate.val(),
			"vendorId" : (cols["vendorId"]).trim(),
			"orderQty" : reThousands(autoOrderQty),
			"dcItem" : cols["dcItem"],
			"orderPrice" : cols["orderPrice"],
			"purchaseUnitId" : cols["purchaseUnitId"]
		};
		$.myAjaxs({
			url:order_url+"/updateOrder",
			async:false,
			cache:false,
			type :"post",
			data :data,
			dataType:"json",
			success:function(data,textStatus, xhr){
				var resp = xhr.responseJSON;
				if( resp.result == false){
					top.location = resp.s+"?errMsg="+resp.errorMessage;
					return ;
				}
				if(data.success==true){
					// 将推荐订货量复制到 订货数量
					$(trObj).find('td[tag=orderQty]').text(toThousands(autoOrderQty));
					_common.prompt("Modify Succeeded!",2,"success");
				}else{
					_common.prompt(data.message,5,"error");
				}
			},
			complete:_common.myAjaxComplete
		});
	}


	//点击tr后事件
	var trClick_table1 = function(){
		var cols = tableGrid.getSelectColValue(selectTrTemp,"articleId,dcItem,orderQty,promotionDescription,articleQty,articleNoChargeId,articleNoChargeQty");
		var storeCd = m.storeCd.val();
		var orderDate = m.orderDate.val();
		var dcItem = cols["dcItem"];
		$("#freeItemQty").val("");
		if(cols["articleQty"]>0){
			var qty = cols["orderQty"]*cols["articleNoChargeQty"]/cols["articleQty"];
			var freeQty = Math.floor(qty); // 向下取整
			$("#freeItemQty").val(toThousands(freeQty));
		}
		$("#articleNoChargeId").val(cols["articleNoChargeId"]);
		var promotionDescription = cols["promotionDescription"];
		$.myAjaxs({
			url:order_url+"/getItemInfo",
			async:false,
			cache:false,
			type :"get",
			data:"orderDate="+orderDate+"&articleId="+cols["articleId"]+"&storeCd="+storeCd,
			dataType:"json",
			success:function(rest){
				if(rest.success){
					$("#articleId").val(rest.data.articleId);
					$("#vendorName").val(rest.data.vendorName);
					// $("#vendorMinOrderQty").val(rest.data.vendorMinOrderQty);//供应商最低订货量
					$("#preserveTypeName").val(rest.data.preserveTypeName);
					$("#spec").val(rest.data.spec);
					$("#minDisplayQty").val(rest.data.minDisplayQty);
					$("#unitName").val(rest.data.unitName);
					$("#orderUnitQty").val(toThousands(rest.data.orderUnitQty));
					$("#promotionDescription").val(promotionDescription);
					if(dcItem=="1"){
						$("#label_minOrderQty").text("DC MOQ");
						$("#label_i_minOrderQty").text("DC MOQ");
						$("#minOrderQty").val(toThousands(rest.data.dcMinOrderQty));//这里为dc商品，为dc moq
						minOrderQty = rest.data.dcMinOrderQty;
					}else{
						$("#label_minOrderQty").text("Min. Order Qty");
						$("#label_i_minOrderQty").text("Min. Order Qty");
						$("#minOrderQty").val(toThousands(rest.data.minOrderQty));//这里为非dc商品，为minOrderQty
						minOrderQty = rest.data.minOrderQty;
					}
					$("#maxOrderQty").val(toThousands(rest.data.maxOrderQty));
					$("#orderBatchQty").val(toThousands(rest.data.orderBatchQty));
					//设置选中商品的 最大订货量 订货批量
					maxOrderQty = rest.data.maxOrderQty;
					orderBatchQty = rest.data.orderBatchQty;
					$("#orderPromotionType").val(rest.data.orderPromotionType);
					$("#adviseSalePrice").val(toThousands(rest.data.adviseSalePrice));
					$("#dms").val(toThousands(rest.data.dms));
					$("#warrantyDays").val(rest.data.warrantyDays);
					// $("#receiveDateLimit").val(rest.data.receiveDateLimit);
					// $("#saleDateLimit").val(rest.data.saleDateLimit);
				}else{
					$("#item_info").find("input").val("");
					_common.prompt("No relevant item information was found!",5,"error"); // 获取商品详细信息失败
				}
			},
			complete:_common.myAjaxComplete
		});

		//进销存基本信息获取
		paramGrid1 ="articleId="+cols["articleId"]+"&storeCd="+storeCd+"&orderDate="+orderDate;
		tableGrid1.setting("url",systemPath+"/order/getReferenceInfo");
		tableGrid1.setting("param",paramGrid1);
		tableGrid1.loadData(null);
	}

	//表格初始化样式
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Order Detail",
			param:paramGrid,
			colNames:["No.","Item Code","Item Name","Real-time Inventory","SOQ",
				"PSD","Write-off Qty","Order Qty","Free Order Qty","Order Total Qty","Vendor Id","Purchase Unit Id",
				"Order Price","DC Item","uploadFlg","Order Sts","Upload Order Qty","Upload Free Order Qty","Article Qty","Free Item Code","Free Initial Qty","Promotion Description","Copy SOQ"],
			colModel:[
				{name:"num",type:"text",text:"center",width:"50",ishide:false, cellattr: addCellAttr,css:"",getCustomValue:num},
				{name:"articleId",type:"text",text:"right",width:"90",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"120",ishide:false,css:""},
				{name:"realtimeStock",type:"text",text:"right",width:"150",ishide:false,getCustomValue:getThousands},
				{name:"autoOrderQty",type:"text",text:"right",width:"180",ishide:false,getCustomValue:getThousands1},
				{name:"psd",type:"text",text:"right",width:"80",ishide:false,getCustomValue:getThousands1},
				{name:"writeOffQty",type:"text",text:"right",width:"100",ishide:false,getCustomValue:getThousands1},
				{name:"orderQty",type:"text",text:"right",width:"110",ishide:false,css:"",getCustomValue:getThousands},
				{name:"orderNochargeQty",type:"text",text:"right",width:"110",ishide:true,css:"",getCustomValue:getThousands},
				{name:"totalQty",type:"text",text:"right",width:"110",ishide:true,css:"",getCustomValue:getThousands},
				{name:"vendorId",type:"text",text:"right",width:"120",ishide:true,css:""},
				{name:"purchaseUnitId",type:"text",text:"right",width:"120",ishide:true,css:""},
				{name:"orderPrice",type:"text",text:"right",width:"120",ishide:true,css:"",getCustomValue:zeroFmt},
				{name:"dcItem",type:"text",text:"right",width:"80",ishide:true,css:"",getCustomValue:zeroFmt},
				{name:"uploadFlg",type:"text",text:"right",width:"80",ishide:true,css:"",getCustomValue:zeroFmt},
				{name:"orderSts",type:"text",text:"right",width:"80",ishide:true,css:""},
				{name:"uploadOrderQty",type:"text",text:"right",width:"120",ishide:true,css:"",getCustomValue:getThousands},
				{name:"uploadOrderNochargeQty",type:"text",text:"right",width:"110",ishide:true,css:"",getCustomValue:getThousands},
				{name:"articleQty",type:"text",text:"right",width:"110",ishide:true,css:""},
				{name:"articleNoChargeId",type:"text",text:"left",width:"110",ishide:true,css:""},
				{name:"articleNoChargeQty",type:"text",text:"right",width:"110",ishide:true,css:""},
				{name:"promotionDescription",type:"text",text:"left",width:"160",ishide:false},
				{name:"copy",type:"text",text:"center",width:"110",getCustomValue:btn},
			],//列内容
			lineNumber:false,
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			sidx:"ma8040.article_id",//排序字段
			sord:"asc",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				loadPage = resData.page;
				$("#loadPage").val(loadPage);
				return resData;
			},
			loadBeforeEvent:function(self){
				return self;
			},
			loadCompleteEvent:function(self){
				selectTrTemp = null;//清空选择的行
				if (m.use.val()=='1'){
					tableGrid.find("tr").on('dblclick', function (e) {
						addInputValue();
					});
					return self;
				}
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
				trClick_table1();

				if (m.use.val()=="1") {
					// 点击copy 后,执行的函数
					trClick_copy(trObj,tdObj);
				}
				/*var cols = tableGrid.getSelectColValue(trObj,"uploadFlg");
				if(cols["uploadFlg"]!="1"&&m.use.val()!="0"){
					$("#update").removeAttr("disabled");
				}else{
					$("#update").attr("disabled", "disabled");
				}*/
			},
			// buttonGroup:[
			// 	{
			// 		butType: "update",
			// 		butId: "update",
			// 		butText: "Modify",
			// 		butSize: ""//,
			// 	},//修改
			// ],

		});
	};

	//进销存参考信息
	var initTable2 = function(){
		tableGrid1 = $("#zgGridTtable2").zgGrid({
			title:"Purchase-sell-stock Reference Information",//进销存参考信息
			param:paramGrid1,
			colNames:["o1","o2","o3","o4","o5","o6","o7","o8","o9","o10","o11","o12","o13","o14","o15","o16"],
			colModel:[
				{name:"o1",type:"text",text:"center",width:"70",ishide:false,css:""},
				{name:"o2",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o3",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o4",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o5",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o6",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o7",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o8",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o9",type:"text",text:"center",width:"66",ishide:false,css:"grid-bg"},
				{name:"o10",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o11",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o12",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o13",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o14",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o15",type:"text",text:"center",width:"66",ishide:false,css:""},
				{name:"o16",type:"text",text:"center",width:"66",ishide:false,css:""}
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:false,//是否需要分页
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				return trObj;
			},
			loadCompleteEvent:function(self){
				//隐藏表头
				$("#zgGridTtable2_thead").hide();
				$(self).find("td").each(function(){
					let title = $(this).attr("title");
					$(this).attr("title",title.replace(/<br>/g,"  "));
				});


			},
			ajaxSuccess:function(resData){
				return resData;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				//去掉点击变色
				trObj.removeClass("info");
			},
		});
	};

	function addCellAttr(rowId, val, rawObject, cm, rdata) {
		return "style='color:#428bca'";
	}

	// 按钮权限验证
	var isButPermission = function () {
		var orderButEdit = $("#orderButEdit").val();
		if (Number(orderButEdit) != 1) {
			$("#update").remove();
		}
	}

	var num = function (tdObj, value) {
		value = $("#zgGridTtable>.zgGrid-tbody").find("tr").length + 1;
		return $(tdObj).text(value);
	}

	var btn = function (tdObj, value) {
		let num = $(tdObj[0]).context.id.replace(/[^0-9]/ig,"");;
		// cursor:pointer;
		// 纯图标的形式
		let val = "<div class='btn-group btn-group-xs' id='copy_"+num+"' role='group' style='cursor:pointer;width: 100%' title='Copy'><span class='glyphicon glyphicon-plus'></span></div>";
		// 有按钮的样式
		// let val = "<div class='btn-group btn-group-xs' role='group'>" +
		// 	"<button type='button' class='btn btn-default' style='outline: none;'>" +
		// 	"<span class='glyphicon glyphicon-plus'></span> Copy</button>" +
		// 	"</div>";

		return $(tdObj).html(val);
	}


	//数值null判断
	var zeroFmt = function(tdObj, value){
		if(value==null||value==""){
			return $(tdObj).text("0");
		}
		return $(tdObj).text(value);
	}

	// 设置千分位, 四舍五入 保留小数位 dit
	function toThousands1(num,dit) {
		if (num === 0) {
			return '0';
		}
		dit = typeof dit !== 'undefined' ? dit : 0;
		num = num + '';
		const reg = /\d{1,3}(?=(\d{3})+$)/g;
		let intNum = '';
		let decimalNum = '';
		if (num.indexOf('.') > -1) {
			if (num.indexOf(',') != -1) {
				num = num.replace(/\,/g, '');
			}
			num = parseFloat(num).toFixed(dit);
			return (num + '').replace(reg, '$&,');
		} else {
			return (num + '').replace(reg, '$&,');
		}
	}

	// 栏位显示千分位
	function getThousands1(tdObj, value) {
		return $(tdObj).text(toThousands1(value));
	}

	//日期字段格式化格式
	var dateFmt = function(tdObj, value){
		return $(tdObj).text(fmtIntDate(value));
	}

	var openNewPage = function (url, param) {
		param = param || "";
		location.href = url + param;
	}

	//格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
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
var _index = require('orderEdit');
_start.load(function (_common) {
	_index.init(_common);
});
