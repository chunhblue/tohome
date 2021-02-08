require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('storeItemRelationship', function () {
    var self = {};
    var url_left = "",
		systemPath = "",
		approvalRecordsParamGrid = null,
	    paramGrid = null,
	    selectTrTemp = null,
	    tempTrObjValue = {},//临时行数据存储
		aStore = null,
		a_store = null,
		getStoreGroup = null,
		getStoreCluster = null,
		getGroup = null,
		getCluster = null,
    	common=null;
		var inputResources = [];
		var nextResources = {};

	// var inputParam = {
	// 	dep:"",
	// 	pma:"",
	// 	category:"",
	// 	subCategory:""
	// };

    var m = {
		toKen : null,
		// clear_date : null,
		createDate: null,
		alert_div : null,//页面提示框
		search : null,//检索按钮
		reset : null,//清空按钮
		main_box : null,//检索按钮
		searchJson:null,
		checkCount:null,
		urgencyCount:null,
		del_alert:null,
		param_dep:null,
		dep:null,
		param_pma:null,
		pma:null,
		param_category:null,
		category:null,
		param_subCategory:null,
		subCategory:null,
		storeTypeCd:null,
		affirm:null, //提交按钮
		storeCd :null,
		storeName :null,
		//审核
		approvalBut: null,
		approval_dialog: null,
		audit_cancel: null,
		audit_affirm: null,
		typeId: null,
		reviewId: null
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
    	url_left=_common.config.surl+"/storeItemRelationship";
		_common.initCategoryAutoMatic();
		//初始化下拉
		initSelectValue();
		but_event();
		//初始化表格
		initTable1();
		table_event();
		//权限验证
		isButPermission();
		// 初始化店铺名称下拉
		aStore = $("#aStore").myAutomatic({  //加上名字可以存入自动填充
			url: systemPath + "/ma1000/getStoreByPM",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function (thisObj) {
				m.approvalBut.prop("disabled", true);
			},
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					//检查是否允许审核
					_common.checkRole(thisObj.attr("k"), m.typeId.val(), function (success) {
						if (success) {
							m.approvalBut.prop("disabled", false);
						} else {
							m.approvalBut.prop("disabled", true);
						}
					});
				}
			}
		});
		// 栏位清空
		$("#storeRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(aStore);
		});
		m.approvalBut.prop("disabled", true);
		if(!!m.storeCd.val()){
			$.myAutomatic.setValueTemp(aStore,m.storeCd.val(),m.storeCd.val()+" "+m.storeName.val());
			m.search.click();
			//检查是否允许审核
			_common.checkRole(m.storeCd.val(), m.typeId.val(), function (success) {
				if (success) {
					m.approvalBut.prop("disabled", false);
				} else {
					m.approvalBut.prop("disabled", true);
				}
			});
		}
		//审核事件
		approval_event();
		//初始搜索
		// m.search.click();
    }

	//审核事件
	var approval_event = function () {
		//点击审核按钮
		m.approvalBut.on("click", function () {
			var recordId = $("#aStore").attr("k");
			if (recordId != null && recordId != "") {
				$("#approval_dialog").modal("show");
				//获取审核记录
				_common.getStep(recordId, m.typeId.val());
			} else {
				_common.prompt("Record fetch failed, Please try again!", 5, "error");
			}
		});
		//审核提交
		$("#audit_affirm").on("click", function () {
			//审核意见
			var auditContent = $("#auditContent").val();
			if (auditContent.length > 200) {
				_common.prompt("Approval comments cannot exceed 200 characters!", 5, "error");
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
				$.myAjaxs({
					url: _common.config.surl + "/audit/submit",
					async: true,
					cache: false,
					type: "post",
					data: {
						auditStepId: auditStepId,
						auditUserId: auditUserId,
						auditStatus: auditStatus,
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
							_common.prompt("Saved Successfully!", 3, "success");// 保存审核信息成功
						} else {
							m.approvalBut.prop("disabled", false);
							_common.prompt("Saved Failure!", 5, "error");// 保存审核信息失败
						}
						m.toKen.val(result.toKen);
					},
					complete: _common.myAjaxComplete
				});
			})
		})
	}

	// 初始化下拉列表
	function initSelectValue(){

		$("#maCd").prop("disabled", true);
		$("#maCdRefresh").hide();
		$("#maCdRemove").hide();
		// 获取StoreCluster
		getStoreCluster = $("#storeTypeCd").myAutomatic({
			url:systemPath+"/storeMaster/getMa0030",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function() {
				$.myAutomatic.replaceParam(getStoreGroup,null);
				$.myAutomatic.cleanSelectObj(getStoreGroup);
				$("#maCd").prop("disabled","true");
				$("#maCdRefresh").hide();
				$("#maCdRemove").hide();
			},
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") !== null && thisObj.attr("k") !== "") {
					$("#maCd").prop("disabled", false);
					$("#maCdRefresh").show();
					$("#maCdRemove").show();
					$.myAutomatic.cleanSelectObj(getStoreGroup);
					var strm ="&storeTypeCd="+ trim(m.storeTypeCd.attr('k'));
					$.myAutomatic.replaceParam(getStoreGroup,strm);
				}
			},
		});

		a_store = $("#a_store").myAutomatic({  //加上名字可以存入自动填充
			url: systemPath + "/ma1000/getStoreByPM",
			ePageSize: 5,
			startCount: 0,
			cleanInput: function() {
				$("#r_storeTypeCd").val("");
				$("#r_maCd").val("");
				m.affirm.prop("disabled",true);
			},
			selectEleClick:function (thisObj) {
				let store = thisObj.attr("k");
				if (store !== null && store !== "") {
					//获取数据审核状态
					_common.getRecordStatus(store,m.typeId.val(),function (result) {
						console.log(result.data)
						if(result.success||result.data==10){
							m.affirm.prop("disabled",false);
						}else{
							m.affirm.prop("disabled",true);
							_common.prompt(result.message,5,"error");
						}
					});
					getCluster(store, function(res){
						if(res.success){
							$("#r_storeTypeCd").val(res.o.storeTypeName);
						}
					});
					getGroup(store,function(res) {
						if(res.success){
							$("#r_maCd").val(res.o.storeGroupName);
						}
					});
				}
			}
		});

		getCluster = function(a_store,fun){
			$.myAjaxs({
				url:systemPath + "/storeMaster/getCluster",
				async:true,
				cache:false,
				data :"a_store="+a_store,
				type:"post",
				dataType:"json",
				success:fun
			})
		};

		getGroup = function(a_store,fun){
			$.myAjaxs({
				url:systemPath + "/storeMaster/getGroup",
				async:true,
				cache:false,
				data:"a_store="+a_store,
				type:"post",
				dataType:"json",
				success:fun
			});
		}


		// 获取StoreGroup
		getStoreGroup = $("#maCd").myAutomatic({
			url: systemPath+"/storeMaster/getMa0035",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function() {

			},
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


	//表格内按钮点击事件
	var table_event = function(){
		// 导出按钮点击事件
		$("#export").on("click",function(){
			// 拼接检索参数
			var searchJsonStr ={
				storeCd:$("#aStore").attr("k"),
				storeTypeCd:trim($("#storeTypeCd").attr("k")),
				maCd:$("#maCd").attr("k"),
				shelf:$("#shelf").val().trim(),
				subShelf:$("#subShelf").val().trim(),
				descG:$("#descG").val().trim(),
				loc:$("#loc").val().trim(),
				itemCode:$("#itemCode").val().trim(),
				vFacing:$("#vFacing").val().trim(),
				hFacing:$("#hFacing").val().trim(),
				dFacing:$("#dFacing").val().trim(),
				totalFacing:$("#totalFacing").val().trim(),
				createDate:subfmtDate($("#createDate").val()),
				depCd:$("#dep").attr("k"),
				pmaCd:$("#pma").attr("k"),
				categoryCd:$("#category").attr("k"),
				subCategoryCd:$("#subCategory").attr("k"),
			};
			paramGrid = "searchJson=" + JSON.stringify(searchJsonStr);
			var url = url_left + "/export?" + paramGrid;
			window.open(encodeURI(url), "excelExportWin", "width=450,height=300,scrollbars=yes");
		});

		// 栏位清空
		$("#a_store_clear").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(a_store);
		});

		//审核记录
		$("#approvalRecords").on("click",function(){
			var _storeCd = $("#aStore").attr("k");
			if (_storeCd == null || _storeCd == "") {
				_common.prompt("Please select a store first!", 3, "info");
				return false;
			}
			approvalRecordsParamGrid = "id="+_storeCd+
				"&typeIdArray="+m.typeId.val();
			approvalRecordsTableGrid.setting("url",_common.config.surl+"/approvalRecords/getApprovalRecords");
			approvalRecordsTableGrid.setting("param", approvalRecordsParamGrid);
			approvalRecordsTableGrid.setting("page", 1);
			approvalRecordsTableGrid.loadData(null);
			$('#approvalRecords_dialog').modal("show");
		});
	}

    var showResponse = function(data,textStatus, xhr){
    	selectTrTemp = null;
	  	  var resp = xhr.responseJSON;
	  	  if( resp.result == false){
	  		  top.location = resp.s+"?errMsg="+resp.errorMessage;
	  		  return ;
	  	  }
	  	  if(data.success==true){
	  		  _common.prompt("Operation Succeeded!",2,"success",function(){
	  			m.search.click();
	  		  },true);
	  	  }else{
	  		  _common.prompt(data.message,5,"error");
	  	  }
	  	  m.toKen.val(data.toKen);
	    }

    //画面按钮点击事件
    var but_event = function(){

		m.reset.on("click",function(){
			$("#aStore").css("border-color","#CCC");
			$("#storeRemove").click();
			$("#storeTypeCd").val("");
			$("#shelf").val("");
			$("#subShelf").val("");
			$("#descG").val("");
			$("#loc").val("");
			$("#itemCode").val("");
			$("#vFacing").val("");
			$("#hFacing").val("");
			$("#dFacing").val("");
			$("#totalFacing").val("");
			$("#createDate").val("");
			$("#depRemove").click();
			$("#maCd").prop("disabled", true);
			$("#maCdRefresh").hide();
			$("#maCdRemove").hide();
			$.myAutomatic.cleanSelectObj(getStoreCluster);
			$.myAutomatic.cleanSelectObj(getStoreGroup);
			selectTrTemp = null;
			_common.clearTable();
		})
		// 导入excle
		$("#import").on("click", function () {
			$.myAutomatic.cleanSelectObj(a_store);
			$('#storeItemRelationship_dialog').modal("show");
			m.affirm.prop("disabled",true);
		});
		// 栏位清空
		// $("#typeCdRemove").on("click", function (e) {
		// 	$("#maCd").prop("disabled", true);
		// 	$("#maCdRefresh").hide();
		// 	$("#maCdRemove").hide();
		// 	$.myAutomatic.cleanSelectObj(getStoreGroup);
		// });
		// $("#maCdRemove").on("click", function (e) {
		// 	$.myAutomatic.cleanSelectObj(getStoreGroup);
		// });
		//提交按钮点击事件
		m.affirm.on("click",function(){
			var storeCd = $("#a_store").attr("k");
			if(storeCd==null||storeCd==''){
				_common.prompt("Please select a store first!",5,"error");
				return;
			}
			if($('#fileData')[0].files[0]==undefined||$('#fileData')[0].files[0]==null){
				_common.prompt("Import file cannot be empty!",5,"error");
				return;
			}
			_common.myConfirm("Are you sure want to upload？",function(result){
				if(result=="true"){
					var formData = new FormData();
					formData.append("storeCd",storeCd);
					formData.append("fileData",$('#fileData')[0].files[0]);
					formData.append("toKen",m.toKen);
					$.myAjaxs({
						url:url_left+"/upload",
						async:false,
						cache:false,
						type :"post",
						data :formData,
						dataType:"json",
						processData:false,
						contentType:false,
						success:function(data,textStatus, xhr){
							var resp = xhr.responseJSON;
							if( resp.result == false){
								top.location = resp.s+"?errMsg="+resp.errorMessage;
								return ;
							}
							var typeId = m.typeId.val();
							var nReviewid = m.reviewId.val();
							var recordCd =storeCd;
							if(data.success==true){
								_common.prompt("Operation Succeeded!",2,"success",function(){
									$('#storeItemRelationship_dialog').modal("hide");
									m.search.click();
									//发起审核
									_common.initiateAudit(storeCd, storeCd, typeId, nReviewid, m.toKen.val(), function (token) {
										//审核按钮禁用
										m.approvalBut.prop("disabled", true);
										m.toKen.val(token);
									})
								},true);
							}else{
								if(data.source!=null&&data.source!=''){
									_common.myConfirm(data.message+"Is coverage determined？",function(result){//是否确认覆盖
										if(result=="true"){
											formData.append("isCoverage",true);
											$.myAjaxs({
												url: url_left + "/upload",
												async: false,
												cache: false,
												type: "post",
												data: formData,
												dataType: "json",
												processData: false,
												contentType: false,
												success: function (data, textStatus, xhr) {
													var resp = xhr.responseJSON;
													if( resp.result == false){
														top.location = resp.s+"?errMsg="+resp.errorMessage;
														return ;
													}
													if(data.success==true){
														_common.prompt("Operation Succeeded!",2,"success",function(){
															$('#storeItemRelationship_dialog').modal("hide");
															m.search.click();
															//发起审核
															_common.initiateAudit(storeCd, storeCd, typeId, nReviewid, m.toKen.val(), function (token) {
																//审核按钮禁用
																m.approvalBut.prop("disabled", true);
																m.toKen.val(token);
															})
														},true);
													}else{
														_common.prompt(data.message,5,"error");
													}
												}
											})
										}
									});
								}else{
									_common.prompt(data.message,5,"error");
								}
							}
						},
						complete:_common.myAjaxComplete
					});
				}
			});
		});
    	//创建日
    	m.createDate.datetimepicker({
    		 language:'en',
    		 format: 'dd/mm/yyyy',
    		 maxView:4,
    		 startView:2,
    		 minView:2,
    		 autoclose:true,
    		 todayHighlight:true,
    		 todayBtn:true
		});
    	//检索按钮点击事件
    	m.search.on("click",function(){
			var _storeCd = $("#aStore").attr("k");
			if (_storeCd == null || _storeCd == "") {
				$("#aStore").css("border-color","red");
				$("#aStore").focus();
				_common.prompt("Please select a store first!", 3, "info");
				return false;
			}else {
				$("#aStore").css("border-color","#CCC");
			}
			// 创建请求字符串
			var searchJsonStr ={
				storeCd:$("#aStore").attr("k"),
				storeTypeCd:trim($("#storeTypeCd").attr("k")),
				maCd:$("#maCd").attr("k"),
				shelf:$("#shelf").val().trim(),
				subShelf:$("#subShelf").val().trim(),
				descG:$("#descG").val().trim(),
				loc:$("#loc").val().trim(),
				itemCode:$("#itemCode").val().trim(),
				vFacing:$("#vFacing").val().trim(),
				hFacing:$("#hFacing").val().trim(),
				dFacing:$("#dFacing").val().trim(),
				totalFacing:$("#totalFacing").val().trim(),
				createDate:subfmtDate($("#createDate").val()),
				depCd:$("#dep").attr("k"),
				pmaCd:$("#pma").attr("k"),
				categoryCd:$("#category").attr("k"),
				subCategoryCd:$("#subCategory").attr("k"),
			};
			m.searchJson.val(JSON.stringify(searchJsonStr));
			paramGrid = "searchJson="+m.searchJson.val();
			tableGrid.setting("url",url_left+"/getdata");
			tableGrid.setting("param", paramGrid);
			tableGrid.setting("page", 1);
			tableGrid.loadData(null);
    	});
    }
    
    //表格样式
    var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
    		title:"Query Result",
    		param:paramGrid,
    		colNames:["Store No.","Store Name","Store Cluster","Store Group","Shelf","Sub-Shelf","Product Type","Location","Item Code","Product Name","Category","Sub Category","V. Facing","H. Facing","D. Facing","Total Facing","Date Created"],
    		colModel:[
				{name:"storeCd",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"storeName",type:"text",text:"left",width:"200",ishide:false,css:""},
				{name:"storeTypeName",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"storeGroupName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"shelf",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"subShelf",type:"text",text:"right",width:"100",ishide:false,css:""},
	          	{name:"descG",type:"text",text:"left",width:"100",ishide:false,css:""},
	          	{name:"loc",type:"text",text:"right",width:"100",ishide:false,css:""},
	          	{name:"itemCode",type:"text",text:"right",width:"130",ishide:false,css:""},
	          	{name:"productName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"categoryName",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"subCategoryName",type:"text",text:"left",width:"150",ishide:false,css:""},
	          	{name:"vfacing",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"hfacing",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"dfacing",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"totalFacing",type:"text",text:"right",width:"120",ishide:false,css:""},
				{name:"createDate",type:"text",text:"center",width:"150",ishide:false,css:"",getCustomValue:dateFmt}
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			sidx:"ma1105.store_cd,ma1105.shelf,ma1105.sub_shelf",//排序字段
			sord:"asc",//升降序
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
				{butType:"custom",butHtml:"<button id='export' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-export'></span> Export</button>"},
				{
					butType:"custom",
					butHtml:"<button id='approvalRecords' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-record'></span> Approval Records</button>"
				},
			]
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
		var storeShelfButUpload = $("#storeShelfButUpload").val();
		if (Number(storeShelfButUpload) != 1) {
			$("#import").remove();
		}
		var storeShelfButExport = $("#storeShelfButExport").val();
		if (Number(storeShelfButExport) != 1) {
			$("#export").remove();
		}
		var approvalButBm = $("#approvalButBm").val();
		if (Number(approvalButBm) != 1) {
			$("#approvalRecords").remove();
		}
	}

	// 去除字符串左右空格
	function trim(s){
		return s.replace(/(^\s*)|(\s*$)/g, "");
	}

	//number格式化
	var formatNum = function(tdObj,value){
		return $(tdObj).text(fmtIntNum(value));
	}
    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
    	return $(tdObj).text(fmtIntDate(value));
    }
	var openNewPage = function (url, param) {
		param = param || "";
		location.href = url + param;
	}

	//格式化数字带千分位
	function fmtIntNum(val){
		var reg=/\d{1,3}(?=(\d{3})+$)/g;
		return (val + '').replace(reg, '$&,');
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
var _index = require('storeItemRelationship');
_start.load(function (_common) {
	_index.init(_common);
});
