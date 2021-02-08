require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
require("bootstrapValidator");
require("bootstrapValidator.css");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('fundEntryEdit', function () {
	var self = {};
	var url_left = "",
		url_root = "",
		paramGrid = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		selectTrTemp = null,
		//附件
		attachmentsParamGrid = null,
		common=null;
	const KEY = 'EXPENDITURE_MANAGEMENT';
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson: null,
		addFlag : null,
		viewSts: null,
		_url : null,
		// Key
		_storeCd : null,
		_accDate : null,
		_voucherNo : null,
		// 画面栏位
		storeName : null,
		businessDate : null,
		expenditureNo : null,
		department : null,
		expenditureSubject : null,
		expenditureAmt : null,
		expenseType : null,
		paymentType : null,
		expenditureStatus : null,
		operator : null,
		description : null,
		remarks : null,
		// 按钮
		refreshIcon : null,
		clearIcon : null,
		returnsViewBut : null,
		submitBtn : null,
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
		url_root = _common.config.surl;
		url_left = url_root + "/fundEntry";
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
		// 初始化下拉
		initSelectValue();
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		initAutomatic();
		//附件事件
		attachments_event();
		// 判断画面模式
		setValueByType();
		//审核事件
		approval_event();
	}

	//审核事件
	var approval_event = function () {
		//点击审核按钮
		m.approvalBut.on("click",function(){
			var recordId = m.expenditureNo.val();
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
		})
	};

	//附件
	var attachments_event = function () {
		//附件一览表格
		attachmentsTable = $("#attachmentsTable").zgGrid({
			title:"Attachments",
			param:attachmentsParamGrid,
			colNames:["File Name","Download","PreView"],
			colModel:[
				{name:"fileName",type:"text",text:"center",width:"130",ishide:false,css:""},
				{name:"filePath",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:function(tdObj, value){
						var obj = value.split(",");
						var html = '<a href="javascript:void(0);" title="DownLoad" class="downLoad" " fileName="'+obj[0]+'" filePath="'+obj[1]+'"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>';
						return $(tdObj).html(html);
					}
				},
				{name:"filePath1",type:"text",text:"center",width:"100",ishide:false,css:"",getCustomValue:function(tdObj, value){
						var obj = value.split(",");
						var html = '<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+obj[0]+'" filePath="'+obj[1]+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>';
						return $(tdObj).html(html);
					}
				}
			],//列内容
			width:"max",//宽度自动
			isPage:false,//是否需要分页
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent:function(self){
				selectTrTempFile = null;//清空选择的行
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTempFile = trObj;
			},
			buttonGroup:[
				{butType: "add",butId: "addByFile",butText: "Add",butSize: ""},//新增
				{butType: "update",butId: "updateByFile",butText: "Modify",butSize: ""},//修改
				{butType: "delete",butId: "deleteByFile",butText: "Delete",butSize: ""},//删除
			],
		});
		//附件表格新增
		var appendTrByFile = function (fileName,filePath) {
			var rowindex = 0;
			var trId = $("#attachmentsTable>.zgGrid-tbody tr:last").attr("id");
			if(trId!=null&&trId!=''){
				rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
			}
			var tr = '<tr id="attachmentsTable_'+rowindex+'_tr" class="">' +
				'<td tag="fileName" width="130" title="'+fileName+'" align="center" id="attachmentsTable_'+rowindex+'_tr_fileName" tdindex="attachmentsTable_fileName">'+fileName+'</td>' +
				'<td tag="filePath" width="100" title="Download" align="center" id="attachmentsTable_'+rowindex+'_tr_filePath" tdindex="attachmentsTable_filePath">'+
				'<a href="javascript:void(0);" title="DownLoad" class="downLoad" " fileName="'+fileName+'" filePath="'+filePath+'"><span class="glyphicon glyphicon-cloud-download icon-right"></span></a>'+
				'</td>' +
				'<td tag="filePath1" width="100" title="Preview" align="center" id="attachmentsTable_'+rowindex+'_tr_filePath1" tdindex="attachmentsTable_filePath1">'+
				'<a href="javascript:void(0);" title="Preview" class="preview" " fileName="'+fileName+'" filePath="'+filePath+'"><span class="glyphicon glyphicon-zoom-in icon-right"></span></a>'+
				'</td>' +
				'</tr>';
			$("#attachmentsTable>.zgGrid-tbody").append(tr);
		}

		//附件一览显示
		$("#attachments").on("click",function () {
			$('#attachments_dialog').modal("show");
		})
		//附件一览关闭
		$("#cancelByAttachments").on("click",function () {
			_common.myConfirm("Are you sure you want to cancel?",function(result){
				if (result=="true"){
					$('#attachments_dialog').modal("hide");
				}
			})
		})
		//添加文件
		$("#addByFile").on("click", function () {
			$('#fileUpload_dialog').modal("show");
			$("#operateFlgByFile").val("1");
			$("#file_name").val("");
			$("#fileData").val("");
			$("#fileData").parent().parent().show();
		});
		//修改文件名称
		$("#updateByFile").on("click", function () {
			if(selectTrTempFile == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			$('#fileUpload_dialog').modal("show");
			$("#operateFlgByFile").val("2");
			$("#fileData").parent().parent().hide();
			var cols = attachmentsTable.getSelectColValue(selectTrTempFile,"fileName");
			var fileName = cols["fileName"];
			$("#file_name").val(fileName);
		});
		//删除文件
		$("#deleteByFile").on("click",function(){
			if(selectTrTempFile == null){
				_common.prompt("Please select at least one row of data!",5,"info");
				return;
			}
			_common.myConfirm("Please confirm whether you want to delete the selected data？",function(result){
				if(result=="true"){
					$(selectTrTempFile[0]).remove();
					selectTrTempFile = null;
				}
			});
		});
		//提交按钮点击事件 文件上传
		$("#affirmByFile").on("click",function(){
			if($("#file_name").val()==null||$("#file_name").val()==''){
				$("#file_name").css("border-color","red");
				_common.prompt("File name cannot be empty!",5,"error");
				$("#file_name").focus();
				return;
			}else {
				$("#file_name").css("border-color","#CCCCCC");
			}
			_common.myConfirm("Are you sure you want to upload？",function(result){
				if(result=="true"){
					var flg = $("#operateFlgByFile").val();
					if(flg=="1"){
						if($('#fileData')[0].files[0]==undefined||$('#fileData')[0].files[0]==null){
							$("#fileData").css("border-color","red");
							_common.prompt("File cannot be empty!",5,"error");
							$("#fileData").focus();
							return;
						}else {
							$("#fileData").css("border-color","#CCCCCC");
						}
						var formData = new FormData();
						formData.append("fileData",$('#fileData')[0].files[0]);
						formData.append("toKen",m.toKen.val());
						$.myAjaxs({
							url:_common.config.surl+"/file/upload",
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
								if(data.success==true){
									var fileName = $("#file_name").val();
									var filePath = data.data.filePath;
									appendTrByFile(fileName,filePath);
									_common.prompt("Operation Succeeded!",2,"success");
									$('#fileUpload_dialog').modal("hide");
								}else{
									_common.prompt(data.message,5,"error");
								}
								m.toKen.val(data.toKen);
							},
							complete:_common.myAjaxComplete
						});
					}else if(flg=="2"){
						var fileName = $("#file_name").val();
						$(selectTrTempFile[0]).find('td').eq(0).text(fileName);
						_common.prompt("Operation Succeeded!",2,"success");
						$('#fileUpload_dialog').modal("hide");
					}
				}
			});
		});

		$("#cancelByFile").on("click",function () {
			_common.myConfirm("Are you sure you want to cancel?",function(result){
				if (result=="true"){
					$("#file_name").css("border-color","#CCCCCC");
					$("#fileData").css("border-color","#CCCCCC");
					$('#fileUpload_dialog').modal("hide");
				}
			})
		})

		//下载
		$("#attachments_dialog").on("click","a[class*='downLoad']",function(){
			var fileName = $(this).attr("fileName");
			var filePath = $(this).attr("filePath");
			window.open(_common.config.surl+"/file/download?fileName="+fileName+"&filePath="+filePath,"_self");
		});
		//预览
		$("#attachments_dialog").on("click","a[class*='preview']",function(){
			var fileName = $(this).attr("fileName");
			var filePath = $(this).attr("filePath");
			var url = _common.config.surl+"/file/preview?fileName="+fileName+"&filePath="+filePath;
			window.open(encodeURI(url),"toPreview");
		});
	};

	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				break;
			case "2":
				break;
			case "3":
				break;
			case "4":
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}

	// 根据跳转方式，设置画面可否编辑、加载内容
	var setValueByType = function(){
		var _sts = m.viewSts.val();
		if(_sts == "add"){
			m.approvalBut.prop("disabled", true);
			m.approvalBut.hide();
		} else {
			// 设置可编辑栏位
			if(_sts === "edit"){
				setSomeDisable(false);
				m.approvalBut.prop("disabled",true);
				getVoucher();
				$.myAutomatic.replaceParam(operator,"&storeCd="+m._storeCd.val());
			} else {
				setSomeDisable(true);
				//检查是否允许审核
				_common.checkRole(m._voucherNo.val(),m.typeId.val(),function (success) {
					if(success){
						m.approvalBut.prop("disabled",false);
					}else{
						m.approvalBut.prop("disabled",true);
					}
				});
			}
			// 查询加载数据
			getVoucher();
		}
	}

	// 设置栏位是否允许编辑
	var setSomeDisable = function (flag) {
		$('#refreshIcon').hide();
		$('#clearIcon').hide();
		$('#storeName').prop("disabled", true);
		$('#businessDate').prop("disabled", true);
		$('#expenditureNo').prop("disabled", true);
		$('#department').prop("disabled", flag);
		$('#expenditureSubject').prop("disabled", flag);
		$('#expenditureAmt').prop("disabled", flag);
		$('#paymentType').prop("disabled", flag);
		$('#expenditureStatus').prop("disabled", flag);
		$('#description').prop("disabled", flag);
		$('#expenseType').prop("disabled", flag);
		$('#remarks').prop("disabled", flag);
		$('#submitBtn').prop("disabled", flag);
		//附件按钮
		$("#addByFile").prop("disabled",flag);
		$("#updateByFile").prop("disabled",flag);
		$("#deleteByFile").prop("disabled",flag);
		$("#operator").attr("disabled",flag);
		if(flag){
			$("#operatorRefresh").hide();
			$("#operatorRemove").hide();
		}else {
			$("#operatorRefresh").show();
			$("#operatorRemove").show();
		}
	}

	// 判断编号是否存在
	var checkVoucherNo = function(voucherNo){
		$.myAjaxs({
			url:url_left+"/check",
			async:true,
			cache:false,
			type :"post",
			data :"voucherNo="+voucherNo,
			dataType:"json",
			success: function(result){
				if(result.success){
					m.addFlag.val("1");
				}else{
					_common.prompt(result.msg,5,"error");
				}
			}
		});
	}

	// 加载详细信息
	var getVoucher = function(){
		// 获取Key
		var _accDate = m._accDate.val();
		var _voucherNo = m._voucherNo.val();
		if(_accDate == null ||$.trim(_accDate) == ''
			|| _voucherNo == null ||$.trim(_voucherNo) == ''){
			_common.prompt("Parameter is empty!",5,"error");/*获取参数为空*/
			return false;
		}
		// 查询详情数据
		var _data = {
			storeCd : m._storeCd.val(),
			accDate : _accDate,
			voucherNo : _voucherNo
		}
		var param = JSON.stringify(_data);
		$.myAjaxs({
			url:url_left+"/getData",
			async:true,
			cache:false,
			type :"post",
			data :"searchJson="+param,
			dataType:"json",
			success: function(result){
				if(result.success){
					$('#storeName').val(result.o.storeName);
					$('#businessDate').val(fmtIntDate(result.o.accDate));
					$('#expenditureNo').val(result.o.voucherNo);
					$('#department').val(result.o.depCd);
					$('#expenditureSubject').val(result.o.itemId);
					$('#expenditureAmt').val(toThousands(result.o.expenseAmt));
					$('#paymentType').val(result.o.payTypeId);
					$('#expenseType').val(result.o.expenseType);
					$('#expenditureStatus').val(result.o.recordSts);
					// $('#operator').val(result.o.userId);
					$.myAutomatic.setValueTemp(operator,result.o.userId,result.o.userId+''+result.o.userName);
					$('#description').val(result.o.description);
					$('#remarks').val(result.o.remark);
					//加载附件信息
					attachmentsParamGrid = "recordCd="+_voucherNo+"&fileType=06";
					attachmentsTable.setting("url", url_root + "/file/getFileList");//加载附件
					attachmentsTable.setting("param", attachmentsParamGrid);
					attachmentsTable.loadData(null);
				}else{
					_common.prompt(result.msg,5,"error");
				}
			},
			error : function(e){
				_common.prompt("Request for funding details failed!",5,"error");/*请求经费详细信息失败*/
			}
		});
	}

	// 画面按钮点击事件
	var but_event = function(){

		$("#expenditureAmt").blur(function () {
			$("#expenditureAmt").val(toThousands(this.value));
		});

		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#expenditureAmt").focus(function(){
			$("#expenditureAmt").val(reThousands(this.value));
		});

		// 业务日期
		m.businessDate.datetimepicker({
			language:'en',
			format: 'dd/mm/yyyy',
			maxView:4,
			startView:2,
			minView:2,
			autoclose:true,
			todayHighlight:true,
			todayBtn:true
		});
		// 返回按钮
		// m.returnsViewBut.on("click",function(){
		// 	top.location = url_root + "/fundQuery";
		// });

		m.returnsViewBut.on("click",function(){
			var bank = $("#submitBtn").attr("disabled");
			if (bank!='disabled' ) {
				_common.myConfirm("Current change is not submitted yet，are you sure to exit?",function(result) {
					$("#storeName").css("border-color","#CCCCCC");
					m._storeCd.css("border-color","#CCCCCC");
					$("#businessDate").css("border-color","#CCCCCC");
					$("#expenditureNo").css("border-color","#CCCCCC");
					$("#department").css("border-color","#CCCCCC");
					$("#expenditureSubject").css("border-color","#CCCCCC");
					$("#expenditureAmt").css("border-color","#CCCCCC");
					$("#paymentType").css("border-color","#CCCCCC");
					$("#expenditureStatus").css("border-color","#CCCCCC");
					$("#operator").css("border-color","#CCCCCC");
					if (result==="true") {
						_common.updateBackFlg(KEY);
						top.location = url_root + "/fundQuery";
					}
				});
			}
			if (bank==='disabled' ) {
				_common.updateBackFlg(KEY);
				top.location = url_root + "/fundQuery";
			}
		});
		// 保存按钮
		m.submitBtn.on("click",function(){
			if (verifySearch()) {
				// 拼接参数
				setParamJson();
				_common.myConfirm("Are you sure you want to save?", function (result) {
					if (result != "true") {
						return false;
					}
					//附件信息
					var fileDetail = [],fileDetailJson = "";
					$("#attachmentsTable>.zgGrid-tbody tr").each(function () {
						var file = {
							informCd:m.expenditureNo.val(),
							fileType:'06',//文件类型 - 费用支出
							fileName:$(this).find('td[tag=fileName]').text(),//文件名称
							filePath:$(this).find('td>a').attr("filepath"),//文件路径
						}
						fileDetail.push(file);
					});
					if(fileDetail.length>0){
						fileDetailJson = JSON.stringify(fileDetail)
					}
					$.myAjaxs({
						url: url_left + m._url.val(),
						async: true,
						cache: false,
						type: "post",
						data: {
							searchJson:	m.searchJson.val(),
							fileDetailJson:fileDetailJson
						},
						dataType: "json",
						success: function (result) {
							if (result.success) {
								_common.prompt("Data saved successfully！", 2, "success", function () {
									m.viewSts.val("edit");
									setSomeDisable(false);
									//发起审核
									var typeId = m.typeId.val();
									var nReviewid = m.reviewId.val();
									var recordCd = m.expenditureNo.val();
									var _storeCd = m._storeCd.val();
									_common.initiateAudit(_storeCd,recordCd, typeId, nReviewid, m.toKen.val(), function (token) {
										// 变为查看模式
										setSomeDisable(true);
										m.viewSts.val("view");
										//审核按钮禁用
										m.approvalBut.prop("disabled", true);
										m.toKen.val(token);
									})
								}, true);
							} else {
								_common.prompt(result.msg, 5, "error");
							}
						},
						error: function (e) {
							_common.prompt("Data saved failed！", 5, "error");/*保存失败*/
						}
					});
				})
			}
		});
		// 编号监听事件
		m.expenditureNo.on('change',function () {
			m.addFlag.val("0");
			var inputVal = $.trim(m.expenditureNo.val());
			if(inputVal==""){
				m.expenditureNo.focus();
				_common.prompt("Expenditure No. cannot be empty!",5,"error");/*业务编号不能为空*/
				return false;
			}
			checkVoucherNo(inputVal);
		});
	}

	// 验证输入项是否合法
	var verifySearch = function(){
		var temp = null;
		var _sts = m.viewSts.val();
		if(_sts=='add'){
			temp = $("#storeName").attr("k");
			if(temp==null||$.trim(temp)==''){
				$("#storeName").css("border-color","red");
				_common.prompt("Please select a store first!",5,"error");/*请选择店铺*/
				$("#storeName").focus();
				return false;
			}else {
				$("#storeName").css("border-color","#CCCCCC");
			}
			// 设置url,存入栏位方便取值
			m._storeCd.val(temp);
			m._url.val("/add");
		}else if(_sts=='edit'){
			temp = m._storeCd.val();
			if(temp==null||$.trim(temp)==''){
				m._storeCd.css("border-color","red");
				_common.prompt("Failed to load Store No.!",5,"error");/*店铺编号获取失败*/
				m._storeCd.focus();
				return false;
			}else {
				m._storeCd.css("border-color","#CCCCCC");
			}
			// 设置url
			m._url.val("/update");
		}else{
			return false;
		}
		temp = m.businessDate.val();
		if(temp==null||$.trim(temp)==''){
			$("#businessDate").css("border-color","red");
			_common.prompt("Business date cannot be empty!",5,"error");/*业务日期不能为空*/
			$("#businessDate").focus();
			return false;
		}else {
			$("#businessDate").css("border-color","#CCCCCC");
		}
		temp = m.expenditureNo.val();
		if(temp==null||$.trim(temp)==''){
			$("#expenditureNo").css("border-color","red");
			_common.prompt("Expenditure No. cannot be empty!",5,"error");/*业务编号不能为空*/
			$("#expenditureNo").focus();
			return false;
		}else{
			if(_sts=='add' && m.addFlag.val()!="1"){
				$("#expenditureNo").css("border-color","red");
				_common.prompt("Please check that the Expenditure No. is duplicate",5,"error");/*请检查业务编号是否重复*/
				$("#expenditureNo").focus();
				return false;
			}
			$("#expenditureNo").css("border-color","#CCCCCC");
		}
		temp = m.department.val();
		if(temp==null||$.trim(temp)==''){
			$("#department").css("border-color","red");
			_common.prompt("Department cannot be empty!",5,"error");/*部门信息不能为空*/
			$("#department").focus();
			return false;
		}else {
			$("#department").css("border-color","#CCCCCC");
		}
		temp = m.expenditureSubject.val();
		if(temp==null||$.trim(temp)==''){
			$("#expenditureSubject").css("border-color","red");
			_common.prompt("Expenditure Subject cannot be empty!",5,"error");/*经费科目不能为空*/
			$("#expenditureSubject").focus();
			return false;
		}else {
			$("#expenditureSubject").css("border-color","#CCCCCC");
		}

		temp = m.expenseType.val();
		if(temp==null||$.trim(temp)===''){
			$("#expenseType").css("border-color","red");
			_common.prompt("Expense type cannot be empty!",5,"error");/*经费类型不能为空*/
			$("#expenseType").focus();
			return false;
		}else {
			$("#expenseType").css("border-color","#CCCCCC");
		}
		temp = reThousands(m.expenditureAmt.val());
		if(temp==null||$.trim(temp)==''){
			$("#expenditureAmt").css("border-color","red");
			_common.prompt("Expenditure Amount cannot be empty!",5,"error");/*经费金额不能为空*/
			$("#expenditureAmt").focus();
			return false;
		}else{
			var reg = /^\d+(\.{0,2}\d+){0,2}$/;
			if(temp !== "" && !reg.test(temp)){
				m.expenditureAmt.focus();
				$("#expenditureAmt").css("border-color","red");
				_common.prompt("Expenditure Amount must be a positive number!",5,"error");/*经费金额必须是正数*/
				return false;
			}
			$("#expenditureAmt").css("border-color","#CCCCCC");
		}
		temp = m.paymentType.val();
		if(temp==null||$.trim(temp)==''){
			$("#paymentType").css("border-color","red");
			_common.prompt("Payment Type cannot be empty!",5,"error");/*支付方式不能为空*/
			$("#paymentType").focus();
			return false;
		}else {
			$("#paymentType").css("border-color","#CCCCCC");
		}

		temp = m.expenditureStatus.val();
		if(temp==null||$.trim(temp)==''){
			$("#expenditureStatus").css("border-color","red");
			_common.prompt("Expenditure Status cannot be empty!",5,"error");/*经费状态不能为空*/
			$("#expenditureStatus").focus();
			return false;
		}else {
			$("#expenditureStatus").css("border-color","#CCCCCC");
		}
		temp = m.operator.val();
		if(temp==null||$.trim(temp)==''){
			$("#operator").css("border-color","red");
			_common.prompt("Operator cannot be empty!",5,"error");/*经办人不能为空*/
			$("#operator").focus();
			return false;
		}else {
			$("#operator").css("border-color","#CCCCCC");
		}
		return true;
	}


	// 拼接检索参数
	var setParamJson = function(){
		// 日期格式转换
		var _date = fmtStringDate(m.businessDate.val())||null;
		// 创建请求字符串
		var searchJsonStr ={
			accDate:_date,
			storeCd:m._storeCd.val(),
			voucherNo:m.expenditureNo.val(),
			depCd:m.department.val(),
			recordSts:m.expenditureStatus.val(),
			payTypeId:m.paymentType.val(),
			itemId:m.expenditureSubject.val(),
			description:m.description.val(),
			remark:m.remarks.val(),
			expenseAmt:reThousands(m.expenditureAmt.val()),
			expenseType:m.expenseType.val(),
			// userId:m.operator.val()
			userId:$("#operator").attr("k")
		};
		m.searchJson.val(JSON.stringify(searchJsonStr));
	}

	// 日期字段格式化格式
	var dateFmt = function(tdObj, value){
		return $(tdObj).text(fmtIntDate(value));
	}

	// 格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		return res;
	}

	// 字符串日期格式转换：dd/mm/yyyy → yyyymmdd
	function fmtStringDate(date){
		var res = "";
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
		return res;
	}

	// 格式化数字类型的日期
	function fmtStrToInt(strDate){
		var res = "";
		res = strDate.replace(/-/g,"");
		return res;
	}

	// 初始化店铺选择
	function initAutomatic() {
		$("#operator").attr("disabled",true);
		$("#operatorRefresh").hide();
		$("#operatorRemove").hide();
		$("#storeName").myAutomatic({
			url: url_root + "/ma1000/getStoreByPM",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				$("#hidStore").val($(thisObj).attr("k"));
				$("#operator").attr("disabled",false);
				$("#operatorRefresh").show();
				$("#operatorRemove").show();
				$.myAutomatic.replaceParam(operator,"&storeCd="+$(thisObj).attr("k"));
			},
			cleanInput : function(){
				$("#operator").attr("disabled",true);
				$("#operatorRefresh").hide();
				$("#operatorRemove").hide();
				$.myAutomatic.cleanSelectObj(operator);
			}
		});
	}

	// 加载部门下拉,加载数据
	function initDepartment(){
		getSelectOptions("Department","/ma0070/getDpt","",function(res){
			var selectObj = $("#department");
			selectObj.find("option:not(:first)").remove();
			for (var i = 0; i < res.length; i++) {
				var optionValue = res[i].depCd;
				var optionText = res[i].depCd+' '+res[i].depName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
	}

	// 加载支取方式下拉
	function initPaymentType(){
		getSelectOptions("Payment Type","/sa0000/getList", null,function(res){
			var selectObj = $("#paymentType");
			selectObj.find("option:not(:first)").remove();
			for (var i = 0; i < res.length; i++) {
				var optionValue = res[i].payCd;
				var optionText = res[i].payName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
	}

	// 加载经费科目下拉
	function initExpenditureSubject(){
		getSelectOptions("Expenditure Subject","/ma1060/getList", null,function(res){
			var selectObj = $("#expenditureSubject");
			selectObj.find("option:not(:first)").remove();
			for (var i = 0; i < res.length; i++) {
				var optionValue = res[i].itemId;
				var optionText = res[i].itemName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
	}

	// 加载经费状态下拉
	function initExpenditureStatus(){
		var param = "codeValue=00225";
		getSelectOptions("Expenditure Status","/cm9010/getCode", param,function(res){
			var selectObj = $("#expenditureStatus");
			selectObj.find("option:not(:first)").remove();
			for (var i = 0; i < res.length; i++) {
				var optionValue = res[i].codeValue;
				var optionText = res[i].codeValue+' '+res[i].codeName;
				selectObj.append(new Option(optionText, optionValue));
			}
		});
	}

	// 加载经办人下拉
	// function initOperator(){
	// 	getSelectOptions("Operator","/cm9030/getList", null,function(res){
	// 		var selectObj = $("#operator");
	// 		selectObj.find("option:not(:first)").remove();
	// 		for (var i = 0; i < res.length; i++) {
	// 			var optionValue = res[i].userId;
	// 			var optionText = res[i].userName;
	// 			selectObj.append(new Option(optionText, optionValue));
	// 		}
	// 	});
	// }

	// 请求下拉数据
	function getSelectOptions(title, url, param, fun) {
		$.myAjaxs({
			url:url_root+url,
			async:true,
			cache:false,
			type :"post",
			data : param,
			dataType:"json",
			success : fun,
			error : function(e){
				_common.prompt(title+" Failed to load data!",5,"error");/*数据加载失败*/
			}
		});
	}

	// 初始化下拉列表
	function initSelectValue(){

		operator=$("#operator").myAutomatic({
			url:url_root+"/fundQuery/getOperator",
			ePageSize:5,
			startCount: 0
		});

		// 加载部门下拉
		initDepartment();
		// 加载支取方式下拉
		initPaymentType();
		// 加载经费科目下拉
		initExpenditureSubject();
		// 加载经费状态下拉
		initExpenditureStatus();
		// // 加载经办人下拉
		// initOperator();
	}

	self.init = init;
	return self;
});
var _start = require('start');
var _index = require('fundEntryEdit');
_start.load(function (_common) {
	_index.init(_common);
});
