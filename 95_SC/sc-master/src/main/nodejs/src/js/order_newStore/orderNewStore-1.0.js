require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('orderNewStore', function () {
	var self = {};
	var url_left = "",
		systemPath = "",
		paramGrid = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		selectTrTemp = null,
		approvalRecordsParamGrid = null,
		tempTrObjValue = {},//临时行数据存储
		orderType = null,
		vendorId = null,
		common=null;
	const KEY = 'NEW_STORE_ORDER_ENTRY';
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
		order_date: null,
		alert_div : null,//页面提示框
		search : null,//检索按钮
		reset : null,//重置按钮
		main_box : null,//检索按钮
		show_status : null,
		store : null,
		store_cd : null,
		inputStore : null,
		param_dep:null,
		param_pma:null,
		param_category:null,
		param_subCategory:null,
		param_shelf:null,
		param_subShelf:null,
		search_store_but : null,
		checkResources:null,
		tempTableType:null,
		searchJson:null,
		del_alert:null,
		item_type:null,
		businessDate:null,
		shelf_type:null,
		aRegion : null,
		aCity : null,
		aDistrict : null,
		dep:null,
		pma:null,
		category:null,
		subCategory:null,
		aStore : null,
		typeId : null,
		reviewId : null
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
		url_left=_common.config.surl+"/storeOrder";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		//首先验证当前操作人的权限是否混乱，
		if(m.use.val()=="0"){
			but_event();
		}else{
			m.error_pcode.show();
			m.main_box.hide();
		}
		// 初始化店铺运营组织检索
		initStore();
		// initOrganization();
		//列表初始化
		initTable1();
		//表格内按钮事件
		table_event();
		//权限验证
		isButPermission();
		// 初始化分类查询
		// initAutoMatic();
		_common.initCategoryAutoMatic();
		// 		//大中小分类初始化
		// dpt_init();

		$("#show_status").find("input[type='radio']").eq(1).click();
		// m.search.click();
		var businessDate = m.businessDate.val();
		m.order_date.val(fmtIntDate(businessDate));
		initParam();
	}

	// 从明细返回时解除禁用
	var initNotDisabled = function (excelCd,shelfCd,subShelfCd) {
		if(excelCd!==null || excelCd!==""){
			$("#excelName").prop("disabled", false);
			$("#excelNameRefresh").show();
			$("#excelNameRemove").show();
			if(shelfCd!==null || shelfCd!==""){
				$("#param_shelf").prop("disabled", false);
				$("#param_shelfRefresh").show();
				$("#param_shelfRemove").show();
				if(subShelfCd!==null || subShelfCd!==""){
					$("#param_subShelf").prop("disabled", false);
					$("#param_subShelfRefresh").show();
					$("#param_subShelfRemove").show();
				}
			}
		}
	};

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
		m.order_date.val(obj.orderDate);
		$.myAutomatic.setValueTemp(a_store,obj.storeCd,obj.storeName);
		$("#show_status").find("input[type='radio']").eq(Number(obj.orderType-1)).click();
		$.myAutomatic.setValueTemp(vendorId,obj.vendorId,obj.vendorName);
		if (obj.orderType=='1') {
			// 设置 大中小分类回显
			_common.setCategoryAutomaticVal(obj);
		} else {
			// shelf_init();
			$.myAutomatic.setValueTemp(excelName,obj.excelCd,obj.excelName);
			$.myAutomatic.setValueTemp(paramShelf,obj.shelfCd,obj.shelfName);
			$.myAutomatic.setValueTemp(paramSubShelf,obj.subShelfCd,obj.subShelfName);
			initNotDisabled(obj.excelCd,obj.shelfCd,obj.subShelfCd);
			// m.param_shelf.val(obj.shelf);
			// m.param_shelf.change();
			// m.param_subShelf.val(obj.subShelf);
			// m.param_subShelf.change();
		}
		// 执行查询操作
		m.search.click();
	}

	// 保存 参数信息
	var saveParamToSession = function () {
		let searchJson = m.searchJson.val();
		if (!searchJson) {
			return;
		}
		let obj = {};
		searchJson.replace(/([^?&]+)=([^?&]+)/g, function(s, v, k) {
			obj[v] = decodeURIComponent(k);//解析字符为中文
			return k + '=' +  v;
		});
		obj.page=tableGrid.getting("page");
		obj.flg='0';
		obj.storeName=m.aStore.attr("v");
		obj.orderDate=m.order_date.val();
		obj.vendorName=$("#vendorId").attr("v");
		if (obj.orderType=='1') {
			obj.depName=m.dep.attr('v');
			obj.pmaName=m.pma.attr('v');
			obj.categoryName=m.category.attr('v');
			obj.subCategoryName=m.subCategory.attr('v');
		}else {
			obj.excelCd=$("#excelName").attr('k');
			obj.excelName=$("#excelName").attr('v');
			obj.shelfCd=$("#param_shelf").attr('k');
			obj.shelfName=$("#param_shelf").attr('v');
			obj.subShelfCd=$("#param_subShelf").attr('k');
			obj.subShelfName=$("#param_subShelf").attr('v');
		}
		sessionStorage.setItem(KEY,JSON.stringify(obj));
	};

	// 初始化禁用
	var initDisabled = function () {
		$("#excelName").prop("disabled", true);
		$("#excelNameRefresh").hide();
		$("#excelNameRemove").hide();
		$("#param_shelf").prop("disabled", true);
		$("#param_shelfRefresh").hide();
		$("#param_shelfRemove").hide();
		$("#param_subShelf").prop("disabled", true);
		$("#param_subShelfRefresh").hide();
		$("#param_subShelfRemove").hide();
	};


	/**
	 * 初始化店铺下拉
	 */
	var initStore = function () {
		initDisabled();
		a_store = $("#aStore").myAutomatic({
			url: systemPath + "/storeOrder/getStoreByPM",
			ePageSize: 8,
			startCount: 0,
			selectEleClick: function (thisObj) {
				// shelf_init();
				if(thisObj.attr('k')!== null && thisObj.attr('k')!==''){
					$.myAutomatic.replaceParam(excelName, "&storeCd=" +thisObj.attr('k'));
					$("#update").removeAttr("disabled");
					$("#excelName").prop("disabled", false);
					$("#excelNameRefresh").show();
					$("#excelNameRemove").show();
					//获取数据审核状态
					_common.getRecordStatus(m.aStore.attr('k')+subfmtDate(m.order_date.val()),m.typeId.val(),function (result) {
						if(result.success||result.data===1){
							//检查是否允许submit 店长sm才拥有权限
							$.myAjaxs({
								url:url_left+"/checkSubmit",
								async:true,
								cache:false,
								type :"post",
								data :{
									storeCd:m.aStore.attr('k')
								},
								dataType:"json",
								success:function(result){
									if(result.success){
										$("#submitAuditBut").removeAttr("disabled");
										// 北京时间下午两点之后，不允许订货，不允许审核
										if(parseInt($("#hms").val()) > 130000){
											$("#update").attr("disabled","disabled");
											$("#submitAuditBut").attr("disabled","disabled");
										}
									}else{
										$("#submitAuditBut").attr("disabled","disabled");
									}
								},
								complete:_common.myAjaxComplete
							});
						}else{
							$("#submitAuditBut").attr("disabled","disabled");
						}
						// 北京时间下午两点之后，不允许订货，不允许审核
						if(parseInt($("#hms").val()) > 130000){
							$("#update").attr("disabled","disabled");
							$("#submitAuditBut").attr("disabled","disabled");
						}
					});
				}
			},
			cleanInput: function (thisObj) {
				m.param_shelf.html('<option value="">-- All/Please Select --</option>');
				setShelfResourceValue("shelf", "");
				$.myAutomatic.cleanSelectObj(excelName);
				$("#submitAuditBut").removeAttr("disabled");
				$("#update").removeAttr("disabled");
				initDisabled();
			}
		});
		// 下拉上传文件名
		excelName = $("#excelName").myAutomatic({
			url: url_left+"/getMa1107",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				let str = "&excelName="+thisObj.attr('k')+"&storeCd=" +$("#aStore").attr('k');
				$.myAutomatic.replaceParam(paramShelf, str);
				$("#param_shelf").prop("disabled", false);
				$("#param_shelfRefresh").show();
				$("#param_shelfRemove").show();
			},
			cleanInput: function() {
				$.myAutomatic.cleanSelectObj(paramShelf);
				$("#param_shelf").prop("disabled", true);
				$("#param_shelfRefresh").hide();
				$("#param_shelfRemove").hide();
			},
		});

		paramShelf = $("#param_shelf").myAutomatic({
			url: url_left+"/getMa1108",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				let str = "&paramShelf="+thisObj.attr('k')+"&excelName="+$("#excelName").attr('k')+"&storeCd=" +$("#aStore").attr('k');
				$.myAutomatic.replaceParam(paramSubShelf, str);
				$("#param_subShelf").prop("disabled", false);
				$("#param_subShelfRefresh").show();
				$("#param_subShelfRemove").show();
			},
			cleanInput: function() {
				$.myAutomatic.cleanSelectObj(paramSubShelf);
				$("#param_subShelf").prop("disabled", true);
				$("#param_subShelfRefresh").hide();
				$("#param_subShelfRemove").hide();
			}
		});

		paramSubShelf = $("#param_subShelf").myAutomatic({
			url: url_left+"/getMa1109",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function() {

			}
		});

		//供应商模糊下拉
		vendorId = $("#vendorId").myAutomatic({
			url: systemPath + "/vendorMaster/get",
			ePageSize: 5,
			startCount: 0
		});
	};

	var shelf_init = function() {
		var inputs = [];
		if (m.param_shelf) {
			inputs.push("param_shelf");
		}
		if (m.param_subShelf) {
			inputs.push("param_subShelf");
		}

		shelfInputResources = [];
		if (m.param_shelf) {
			shelfInputResources.push("shelf");
		}
		if (m.param_subShelf) {
			shelfInputResources.push("subShelf");
		}
		shelfNextResources = {};
		for (i = 0; i < shelfInputResources.length - 1; i++) {
			shelfNextResources[shelfInputResources[i]] = shelfInputResources[i + 1];
		}
		if (shelfInputResources.length > 0) {
			var firstResource = shelfInputResources[0];
			initShelfResourceList(firstResource, null);
		}
	};
	//
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
				async:false,
				cache:false,
				type :"get",
				data :"storeCd="+$("#aStore").attr("k"),
				dataType:"json",
				success:function(res){
					var htmlStr = '<option value="">-- All/Please Select --</option>';
					$.each(res,function(ix,node){
						htmlStr+='<option value="'+node+'">'+node+'</option>';
					});
					m.param_shelf.html(htmlStr);
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
				async:false,
				cache:false,
				type :"get",
				data :"storeCd="+$("#aStore").attr("k")+"&shelf="+shelfInputParam.shelf,
				dataType:"json",
				success:function(res){
					var htmlStr = '<option value="">-- All/Please Select --</option>';
					$.each(res,function(ix,node){
						htmlStr+='<option value="'+node+'">'+node+'</option>';
					});
					m.param_subShelf.html(htmlStr);
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
		//进入编辑页面
		$("#update").on("click", function (e) {
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
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var storeInput = $("#aStore").attr("k");
			if(storeInput==null||storeInput==""){
				_common.prompt("Please select a store first!",5,"error");
				$("#aStore").focus();
				$("#aStore").css("border-color","red");
				return false;
			}else {
				$("#aStore").css("border-color","#CCC");
			}
			if(!batchFlg){return batchFlg};
			var cols = tableGrid.getSelectColValue(selectTrTemp,"shelf,depCd,depName,pmaCd,pmaName");
			var param = "use=1&storeCd="+$("#aStore").attr("k")+"&storeName="+$("#aStore").attr("v")+"&orderDate="+subfmtDate(m.order_date.val())+"&orderType="+
				orderType+"&shelf="+cols["shelf"]+"&subShelf="+m.param_subShelf.val()+"&depCd="+cols["depCd"]+"&depName="+cols["depName"]+"&pmaCd="+cols["pmaCd"]+"&pmaName="+
				cols["pmaName"]+"&categoryCd="+m.param_category.val()+"&subCategoryCd="+m.param_subCategory.val();
			if(orderType=="1"){
				if(cols["pmaCd"]==null||cols["pmaCd"]==""){
					_common.prompt("Query data is inconsistent with the conditions, please search again!",5,"error");
					return false;
				}
			}else if(orderType=="2"){
				if(cols["shelf"]==null||cols["shelf"]==""){
					_common.prompt("Query data is inconsistent with the conditions, please search again!",5,"error");
					return false;
				}
			}

			//获取数据审核状态
			getRecordStatus($("#aStore").attr("k")+subfmtDate(m.order_date.val()),m.typeId.val(),$("#aStore").attr("k"),function (result) {
				if(result.success||result.data=='0'){
					saveParamToSession();
					top.location = url_left+"/edit?"+param;
				}else{
					_common.prompt(result.message,5,"error");
				}
			});
		});

		//view
		$("#view").on("click", function (e) {
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			var storeInput = $("#aStore").attr("k");
			if(storeInput==null||storeInput==""){
				_common.prompt("Please select a store first!",5,"error");
				$("#aStore").focus();
				$("#aStore").css("border-color","red");
				return false;
			}else {
				$("#aStore").css("border-color","#CCC");
			}
			var cols = tableGrid.getSelectColValue(selectTrTemp,"shelf,depCd,depName,pmaCd,pmaName");
			var param = "use=0&storeCd="+$("#aStore").attr("k")+"&storeName="+$("#aStore").attr("v")+"&orderDate="+subfmtDate(m.order_date.val())+"&orderType="+
				orderType+"&shelf="+cols["shelf"]+"&subShelf="+m.param_subShelf.val()+"&depCd="+cols["depCd"]+"&depName="+cols["depName"]+"&pmaCd="+cols["pmaCd"]+"&pmaName="+
				// cols["pmaName"]+"&categoryCd="+m.param_category.val()+"&subCategoryCd="+m.param_subCategory.val();
				cols["pmaName"]+"&categoryCd="+m.category.attr("k")+"&subCategoryCd="+m.subCategory.attr("k");
			if(orderType=="1"){
				if(cols["pmaCd"]==null||cols["pmaCd"]==""){
					_common.prompt("Query data is inconsistent with the conditions, please search again!",5,"error");
					return false;
				}
			}else if(orderType=="2"){
				if(cols["shelf"]==null||cols["shelf"]==""){
					_common.prompt("Query data is inconsistent with the conditions, please search again!",5,"error");
					return false;
				}
			}
			saveParamToSession();
			top.location = url_left+"/edit?"+param;
		});

		//审核记录
		$("#approvalRecords").on("click",function(){
			var storeInput = $("#aStore").attr("k");
			if(storeInput==null||storeInput==""){
				_common.prompt("Please select a store first!",5,"error");
				return false;
			}
			approvalRecordsParamGrid = "id="+storeInput+subfmtDate(m.order_date.val()),m.typeId.val()+
				"&typeIdArray="+m.typeId.val();
			approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
			approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
			approvalRecordsTableGrid.setting("page", 1);
			approvalRecordsTableGrid.loadData(null);
			$('#approvalRecords_dialog').modal("show");
		});

		// 审核按钮
		$("#submitAuditBut").on("click", function () {
			var storeInput = $("#aStore").attr("k");
			if(storeInput==null||storeInput==""){
				_common.prompt("Please select a store first!",5,"error");
				$("#aStore").focus();
				$("#aStore").css("border-color","red");
				return false;
			}else {
				$("#aStore").css("border-color","#CCC");
			}

			_common.myConfirm("Are you sure to submit?", function (result) {
				if (result !== "true") {
					return false;
				}
				$.myAjaxs({
					url:url_left+"/checkItem",
					async:true,
					cache:false,
					type :"post",
					data :{
						storeCd:m.aStore.attr('k')
					},
					dataType:"json",
					success:function(result){
						if(result.success){ // 可以审核
							$.myAjaxs({
								url: url_left + "/submit",
								async: true,
								cache: false,
								type: "post",
								data: {
									storeCd: m.aStore.attr('k'),
									orderDate: subfmtDate(m.order_date.val())
								},
								dataType: "json",
								success: function (result) {
									if (result.success) {
										var msg = "";
										//判断是否更改
										$.myAjaxs({
											url:url_left+"/checkVendorOrder",
											async:false,
											cache:false,
											type :"post",
											data :{
												storeCd: m.aStore.attr('k'),
												orderDate: subfmtDate(m.order_date.val())
											},
											dataType:"json",
											success:function(result){
												if(result.success){
													// 订货时订货了供应商的商品，但是订货的这些商品有未生效的
													// msg = "All ordered items didn't satisfy supplier MOA/MOQ requirement, are you sure to continue? ";
													_common.prompt(result.message,5,"error");
													msg = result.message;
												}
											},
											complete:_common.myAjaxComplete
										});
										if(msg !== ""){
											return false;
										}

										// _common.prompt("Operation Succeeded!", 2, "success");// 成功
										var recordCd = m.aStore.attr('k') + subfmtDate(m.order_date.val());
										var typeId = m.typeId.val();
										var nReviewid = m.reviewId.val();
										_common.initiateAudit(m.aStore.attr('k'), recordCd, typeId, nReviewid, m.toKen.val(), function (token) {
											// m.submitAuditBut.attr("disabled","disabled");
											m.toKen.val(token);
											//审核通过
											approval(recordCd);
										})
									} else {
										_common.prompt(result.message, 5, "error");
									}
								},
								complete: _common.myAjaxComplete
							});
						}else{
							_common.prompt(result.message,5,"error");
						}
					},
					complete:_common.myAjaxComplete
				});
			})
		})
	};

	/**
	 * 发起审核并通过
	 * @param recordId
	 */
	var approval = function (recordId) {
		$.myAjaxs({
			url:systemPath+"/audit/getStep",
			async:true,
			cache:false,
			type :"post",
			data :{
				recordId : recordId,
				typeId : m.typeId.val()
			},
			dataType:"json",
			success:function(result){
				if(result.success){
					if(result.data) {
						// 设置审核记录ID
						$("#auditId").val(result.data);
						$("#audit_affirm").prop('disabled', false);

						var auditStepId = result.data;
						//审核结果
						var auditStatus = "1";//通过

						$.myAjaxs({
							url: systemPath + "/audit/submit",
							async: true,
							cache: false,
							type: "post",
							data: {
								auditStepId: auditStepId,
								auditStatus: auditStatus,
								auditContent: "",
								toKen: m.toKen.val()
							},
							dataType: "json",
							success: function (result) {
								if (result.success) {
									$("#approval_dialog").modal("hide");
									//更新主档审核状态值
									//_common.modifyRecordStatus(auditStepId,auditStatus);
									$("#submitAuditBut").attr("disabled","disabled");
									_common.prompt("Operation Succeeded!", 3, "success");// 保存审核信息成功
								} else {
									$("#submitAuditBut").removeAttr("disabled");
									_common.prompt("Saved Failure!", 5, "error");// 保存审核信息失败
								}
								m.toKen.val(result.toKen);
							},
							complete: _common.myAjaxComplete
						});
					}
				}else{
					if(result.data === 'Param'){
						prompt('Record fetch failed, Please try again!',5,"error"); // 没有查询到记录（参数为空）
						return ;
					}
					if(result.data === 'Null'){
						prompt('No Matching!',5,"error"); // 没有查询到记录（返回结果为空）
						return ;
					}
					if(result.data === 'Inconformity'){
						prompt('You do not have permission to process!',5,"error"); // 角色不符合
						return ;
					}
					prompt('Query failed, Please try again!',5,"error"); // 查询失败
					$("#audit_affirm").prop('disabled', true);
				}
			},
			error : function(e){
				prompt("The request failed, Please try again!",5,"error");// 请求失败
			}
		});
	};

	//画面按钮点击事件
	var but_event = function(){
		//订货类型按钮事件
		$("#show_status").find("input[type='radio']").on("click",function() {
			var thisObj = $(this);
			var thisVal = thisObj.val();
			//订货类型切换
			if(thisVal=="1"){
				m.item_type.show();
				m.shelf_type.hide();
				// tableGrid.setColNames("Shelf","Department");
			}else{
				m.item_type.hide();
				m.shelf_type.show();
				$.myAutomatic.replaceParam(excelName, "&storeCd=" +$("#aStore").attr('k'));
				if($("#aStore").attr('k') != null && $("#aStore").attr('k') !== ''){
					$("#excelName").prop("disabled", false);
					$("#excelNameRefresh").show();
					$("#excelNameRemove").show();
				}
				// tableGrid.setColNames("Department","Shelf");
			}
			orderType = thisVal;
		});
		//订货日
		m.order_date.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			startDate: fmtDate(m.businessDate.val()),
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		//检索按钮点击事件
		m.search.on("click",function(){
			if(verifySearch()){
				//订货类型
				var tempRadioValue = m.show_status.find("input[type='radio']:checked").val();
				orderType = tempRadioValue;
				paramGrid = "storeCd="+$("#aStore").attr("k")+"&orderDate="+subfmtDate(m.order_date.val())+"&orderType="+tempRadioValue+"&vendorId="+$("#vendorId").attr("k");
				if(tempRadioValue=="1"){
					// paramGrid+="&pmaCd="+m.param_pma.val()+"&categoryCd="+m.param_category.val()+"&subCategoryCd="+m.param_subCategory.val();
					paramGrid+="&depCd="+m.dep.attr("k")+"&pmaCd="+m.pma.attr("k")+"&categoryCd="+m.category.attr("k")+"&subCategoryCd="+m.subCategory.attr("k");
				}else{
					// paramGrid+="&shelf="+m.param_shelf.val()+"&subShelf="+m.param_subShelf.val();
					paramGrid+="&pogCd="+$("#excelName").attr('k')+"&shelf="+m.param_shelf.attr('k')+"&subShelf="+m.param_subShelf.attr('k');
				}
				m.searchJson.val(paramGrid);
				tableGrid.setting("url",url_left+"/getList");
				tableGrid.setting("param", paramGrid);
				tableGrid.setting("page", 1);
				tableGrid.loadData(null);
			}
		});

		m.reset.on("click",function(){
			$("#aStore").css("border-color","#CCC");
			m.order_date.css("border-color","#CCC");
			$.myAutomatic.cleanSelectObj(a_store);
			// $("#depRemove").click();
			// $("#regionRemove").click();
			selectTrTemp = null;
			m.searchJson.val('');
			_common.clearTable();
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
	}


	//验证检索项是否合法
	var verifySearch = function(){
		var tempRadioValue = m.show_status.find("input[type='radio']:checked").val();
		if(tempRadioValue==null||tempRadioValue==""){
			_common.prompt("Please select the order type!",5,"info"); // 请选择订货类型
			return false;
		}
		var storeCd = $("#aStore").attr("k");
		if(storeCd==null||storeCd==""){
			_common.prompt("Please select a store first!",5,"info"); // 请输入店铺
			$("#aStore").focus();
			$("#aStore").css("border-color","red");
			return false;
		}else {
			$("#aStore").css("border-color","#CCC");
		}

		var orderDate = m.order_date.val();
		if(orderDate==null||orderDate==""){
			_common.prompt("Please enter the order date!",5,"info"); // 请输入订货时间
			m.order_date.css("border-color","red");
			m.order_date.focus();
			return false;
		}else {
			m.order_date.css("border-color","#CCC");
		}
		return true;
	}

	//点击tr后事件
	var trClick_table1 = function(){
		var delBut =  $("#del");//
		delBut.prop("disabled",false);
		var cols = tableGrid.getSelectColValue(selectTrTemp,"shelf,depCd,pmaCd");
	}

	//表格初始化-订货样式
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Order List",//订货一览
			param:paramGrid,
			colNames:["Shelf","Category","Sub Category","depCd","Top Department","pmaCd","Department","Item SKU","Item SKU(Can Order)","Item SKU(Orderd)"],
			colModel:[
				{name:"shelf",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"categoryName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"subCategoryName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"depCd",type:"text",text:"right",width:"130",ishide:true,css:""},
				{name:"depName",type:"text",text:"left",width:"130",ishide:true,css:""},
				{name:"pmaCd",type:"text",text:"right",width:"130",ishide:true,css:""},
				{name:"pmaName",type:"text",text:"left",width:"130",ishide:true,css:""},
				{name:"articleCount",type:"text",text:"right",width:"130",ishide:false,getCustomValue:getThousands},
				{name:"canOrderCount",type:"text",text:"right",width:"130",ishide:false,getCustomValue:getThousands},
				{name:"doOrderCount",type:"text",text:"right",width:"130",ishide:false,getCustomValue:getThousands}
			],//列内容
			// traverseData:data,
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			// sidx:"",//排序字段
			// sord:"asc",//升降序
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
				//列段显示
				if(orderType=="1"){
					tableGrid.hideColumn("shelf");
					tableGrid.hideColumn("categoryName");
					tableGrid.hideColumn("subCategoryName");
					tableGrid.showColumn("pmaName");
					tableGrid.showColumn("depName");
				}else{
					tableGrid.hideColumn("pmaName");
					tableGrid.hideColumn("depName");
					tableGrid.showColumn("shelf");
					tableGrid.showColumn("categoryName");
					tableGrid.showColumn("subCategoryName");
				}
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
				trClick_table1();
			},
			buttonGroup:[
				{
					butType: "view",
					butId: "view",
					butText: "Preview",
					butSize: ""//,
				},//查看
				{
					butType: "update",
					butId: "update",
					butText: "Store Order Details",
					butSize: ""
				},//修改
				{
					butType:"custom",
					butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
				},
				{
					butType:"custom",
					butHtml:"<button id='submitAuditBut' type='button' class='btn btn-info btn-sm submitBut'><span class='glyphicon glyphicon-saved icon-right'></span>Submit</button>"

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
		var orderButView = $("#orderButView").val();
		if (Number(orderButView) != 1) {
			$("#view").remove();
		}
		var orderButEdit = $("#orderButEdit").val();
		if (Number(orderButEdit) != 1) {
			$("#update").remove();
		}
	};
	var getRecordStatus = function (recordId,typeId,storeCd,callback,reviewId) {
		if(!reviewId){
			reviewId = 0;
		}
		$.myAjaxs({
			url:url_left+"/getRecordStatus",
			async:true,
			cache:false,
			type :"get",
			data :{
				recordId : recordId,
				typeId : typeId,
				storeCd : storeCd,
				reviewId : reviewId
			},
			dataType:"json",
			success:function(result){
				//回调
				callback(result);
			},
			error : function(e){
				prompt("The request failed, Please try again!",5,"error");// 请求失败
			}
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
var _index = require('orderNewStore');
_start.load(function (_common) {
	_index.init(_common);
});
