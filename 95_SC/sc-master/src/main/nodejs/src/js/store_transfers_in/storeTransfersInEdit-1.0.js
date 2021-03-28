require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('storeTransfersInEdit', function () {
    var self = {};
    var url_left = "",
		url_root = "",
	    paramGrid = null,
		itemInput = null,
		vstore = null,
		tstore = null,
		getSelectDOC1 = null,
		adjustReason = null,
		differenceReason = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
	    selectTrTemp = null,
		submitFlag=false,
	    tempTrObjValue = {},//临时行数据存储
		//附件
		attachmentsParamGrid = null,
    	common=null;
	const KEY = 'STORE_TRANSFER_IN_ENTRY';
    var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		viewSts : null,
		searchJson:null,
		search:null,
		// 基础信息
		v:null,
		tf_cd : null,
		tf_date : null,
		out_cd : null,
		dj_type : null,
		dj_status : null,
		vstore : null,
		tstore : null,
		cRemark : null,
		creater : null,
		ca_date : null,
		modifier : null,
		md_date : null,
		ap_name : null,
		ap_date : null,
		saveBtn:null,
		resetBtn:null,
		returnsViewBut : null,
		differeQty:null,
		// 弹窗
		item_input : null,
		item_input_cd : null,
		item_input_uom : null,
		item_input_spec : null,
		item_input_price : null,
		tax_rate : null,
		on_hand_qty : null,
		item_input_tamount : null,
		actualQty : null,
		adjustReason:null,
		isChange:null,
		dialog_cancel : null,
		dialog_affirm : null,
		differenceReason : null,
		// 主键参数
		_storeCd : null,
		_voucherNo : null,
		_voucherType : null,
		_voucherDate : null,
		_storeCd1 : null,
		_voucherNo1 : null,
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
		url_root = _common.config.surl;
    	url_left = url_root + "/storeTransferIn";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		// 加载下拉列表
		//getSelectValue();
    	// 首先验证当前操作人的权限是否混乱，
    	if(m.use.val()=="0"){
    		but_event();
    	}else{
    		m.error_pcode.show();
    		m.main_box.hide();
    	}
    	// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// 初始化选择
		initAutomatic();
    	// 表格内按钮事件
    	table_event();
		//附件事件
		attachments_event();
		// 根据跳转加载数据，设置操作模式
		setValueByType();
		//审核事件
		approval_event();

    }

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

	// 表格内按钮事件
    var table_event = function(){
		//返回一览
		m.returnsViewBut.on("click",function(){
			var bank = $("#saveBtn").attr("disabled");
			if (submitFlag===false && bank!='disabled' ) {
				_common.myConfirm("Current change is not submitted yet，are you sure to exit?",function(result) {
					if (result==="true") {
						_common.updateBackFlg(KEY);
						top.location = url_left;
					}});
			}
			if (submitFlag===true){
				_common.updateBackFlg(KEY);
				top.location=url_left;
			}
			if (submitFlag===false && bank==='disabled' ) {
				_common.updateBackFlg(KEY);
				top.location = url_left;
			}
		});
		// 添加
		$("#addItemDetails").on("click", function () {
			let flg = m.viewSts.val();
			if(flg!="add"&&flg!="edit"){return false;}
			let _storeCd = $("#vstore").attr('k');
			if(!_storeCd){
				_common.prompt("Please select the store first!",3,"info");
				$("#vstore").css("border-color","red");
				$("#vstore").focus();
				return false;
			}else {
				$("#vstore").css("border-color","#CCC");
			}
			clearDialog(true);
			setDialogDisable(0);
			$('#update_dialog').attr("flg","add");
			$('#update_dialog').modal("show");
		});

		// 修改
		$("#updateItemDetails").on("click",function() {
			if (selectTrTemp == null) {
				_common.prompt("Please select at least one row of data!", 3, "info");
				return false;
			}
			let _storeCd = $("#tstore").attr('k');
			if (!_storeCd) {
				_common.prompt("Please select the store first!", 3, "info");
				$("#tstore").css("border-color", "red");
				$("#tstore").focus();
				return false;
			} else {
				$("#tstore").css("border-color", "#CCC");
			}
			let cols = tableGrid.getSelectColValue(selectTrTemp, "barcode,articleId,articleName,uom,spec,priceNoTax,taxRate,qty2,qty1,adjustReason,differenQty,differenceReason,differenceReasonText,adjustReasonText");
			$("#item_input_cd").val(cols['barcode']);
			$.myAutomatic.setValueTemp(itemInput, cols['articleId'], cols['articleName']);
			$("#item_input_uom").val(cols['uom']);
			$("#item_input_spec").val(cols['spec']);
			$("#item_input_price").val(cols['priceNoTax']);
			$("#tax_rate").val(cols['taxRate']);
			$("#differeQty").val(cols['differenQty']);
			// 调出
			$("#item_input_tamount").val(cols['qty2']);
			//调入
			$("#actualQty").val(cols['qty1']);
			if(cols['qty2'] !== cols['qty1']){
				m.differenceReason.prop("disabled",false);
				$("#diffreasonRefresh").show();
				$("#diffreasonRemove").show();
				$.myAutomatic.setValueTemp(differenceReason, cols['differenceReason'],cols['differenceReasonText']);
			}else {
				m.differenceReason.prop("disabled",true);
				$("#diffreasonRefresh").hide();
				$("#diffreasonRemove").hide();
				$.myAutomatic.cleanSelectObj(differenceReason);
			}

			$.myAutomatic.setValueTemp(adjustReason,cols['adjustReason'],cols['adjustReasonText']);
			let flg = 0;
			let e = m.actualQty.attr("e");
			if(e=='1'){flg = 2;}
			setDialogDisable(flg);
			$('#update_dialog').attr("flg","upt");
			$('#update_dialog').modal("show");
			// 查询实时库存
			getStock(_storeCd, cols['articleId']);
		});

		// 查看
		$("#viewItemDetails").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",3,"info");
				return false;
			}else{
				let cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,uom,spec,priceNoTax,qty2,qty1,differenQty,differenceReason,differenceReasonText,adjustReasonText");
				$("#item_input_cd").val(cols['barcode']);
				$.myAutomatic.setValueTemp(itemInput,cols['articleId'],cols['articleName']);
				$("#item_input_uom").val(cols['uom']);
				$("#item_input_spec").val(cols['spec']);
				$("#item_input_price").val(cols['priceNoTax']);
				$("#item_input_tamount").val(cols['qty2']);
				$("#actualQty").val(cols['qty1']);

				$("#differeQty").val(cols['differenQty']);
				$.myAutomatic.setValueTemp(differenceReason, cols['differenceReason'],cols['differenceReasonText']);
				$.myAutomatic.setValueTemp(adjustReason,cols['adjustReason'],cols['adjustReasonText']);
				setDialogDisable(1);
				$('#update_dialog').attr("flg","view");
				$('#update_dialog').modal("show");
			}
		});

		// 删除按钮
		$("#deleteItemDetails").on("click",function(){
			let flg = m.viewSts.val();
			if(flg!="add"&&flg!="edit"){return false;}
			let _list = tableGrid.getCheckboxTrs();
			if(_list == null || _list.length == 0){
				_common.prompt("Please select the data you want to delete！",3,"info"); // 请选择要确认删除的数据
				return false;
			}else{
				_common.myConfirm("Are you sure to delete?",function(result){
					if(result == "true"){
						for(let i = 0; i < _list.length; i++){
							$(_list[i].selector).remove();
						}
					}
				});
			}
		});
    }

    //审核事件
	var approval_event = function () {
		//点击审核按钮
		m.approvalBut.on("click",function(){
			var recordId = $("#tf_cd").val();
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
			let auditContent = $("#auditContent").val();
			if(auditContent.length>200){
				_common.prompt("Approval comments cannot exceed 200 characters!",5,"error");
				return false;
			}
			//审核记录id
			let auditStepId = $("#auditId").val();
			//用户id
			let auditUserId = $("#auditUserId").val();
			//审核结果
			let auditStatus = $("#auditStatusGroup").find("input[type='radio']:checked").val();
			// 先更新记录信息
			let _storeCd = $("#vstore").attr('k');
			let _voucherNo = $("#tf_cd").val();
			let _type = '501';
			let _date = subfmtDate($("#tf_date").val());
			let _storeCd1 = $("#tstore").attr('k');
			let itemDetail = [], num = 0;
			$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
				++num;
				let _price = $(this).find('td[tag=priceNoTax]').text();
				let _qty = reThousands($(this).find('td[tag=qty2]').text());
				let _actualQty = reThousands($(this).find('td[tag=qty1]').text())||0;
				let _rate = reThousands($(this).find('td[tag=taxRate]').text());
				let _amtNoTax = Number(accMul(_price, _qty)).toFixed(2);
				let _amt = Number(accMul(_amtNoTax, accAdd(1,_rate))).toFixed(2);
				let _tax = Number(accMul(_amtNoTax, _rate)).toFixed(2);
				let _amt1 = Number(accMul(_price, _actualQty)).toFixed(2);
				let orderItem = {
					storeCd:_storeCd,
					voucherNo:_voucherNo,
					voucherType:_type,
					voucherDate:_date,
					articleId:$(this).find('td[tag=articleId]').text(),
					storeCd1:_storeCd1,
					adjustReason:$(this).find('td[tag=adjustReason]').text(),
					barcode:$(this).find('td[tag=barcode]').text(),
					salesUnitId:$(this).find('td[tag=uom]').text(),
					qty2:_qty,
					priceNoTax:_price,
					amtNoTax:_amtNoTax,
					amt:_amt,
					amtTax:_tax,
					taxRate:_rate,
					qty1:_actualQty,
					actualAmt:_amt1,
					displaySeq:num,
					differenceReason:$(this).find('td[tag=differenceReason]').text()
				}
				itemDetail.push(orderItem);
			});
			if(itemDetail.length < 1){
				_common.prompt("Commodity information cannot be empty!",3,"info"); // 传票商品信息不能为空
				return false;
			}
			let _data = {
				listJson : JSON.stringify(itemDetail),
				flg : m.isChange.val(),
				toKen:m.toKen.val()
			};
			_common.myConfirm("Are you sure to save?", function (result) {
				if (result != "true") {
					return false;
				}
				var detailType = "tmp_transfer_in";
				$.myAjaxs({
					url: url_left + '/update',
					async: true,
					cache: false,
					type: "post",
					data: _data,
					dataType: "json",
					success: function (result) {
						if (result.success) {
							// 提交后实际调拨数量不能再编辑
							m.actualQty.attr("e", "0");
							$("#updateItemDetails").prop("disabled", true);
							// 执行审核提交
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
									detailType:detailType,
									toKen: m.toKen.val()
								},
								dataType: "json",
								success: function (result) {
									if (result.success) {
										$("#approval_dialog").modal("hide");
										$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
											let actualQty = reThousands($(this).find('td[tag=qty1]').text())||0;
											let differentReason = $(this).find('td[tag=differenceReason]').text();
											let articleId = $(this).find('td[tag=articleId]').text();
											modifyQty1(actualQty,$("#out_cd").val(),articleId);
											updateDiffReasons(differentReason,$("#out_cd").val(),articleId);
										});
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
						} else {
							_common.prompt("Data update failed, Please try again!", 2, "error");
						}
					},
					complete: _common.myAjaxComplete
				});

			})
		})
	}

    // 根据跳转方式，设置画面可否编辑、加载内容
	var setValueByType = function(){
    	var _sts = m.viewSts.val();
    	if(_sts == "add"){
			// 设置业务日期
			getBusinessDate();
			// 新增默认未审核
			m.dj_status.val("1");
			// 编辑按钮可用
			m.actualQty.attr("e","1");
			//禁用审核按钮
			m.approvalBut.prop("disabled",true);
			$("#search").prop("disabled", false);
		}else if(_sts == "edit"){
			setDisable(false);
			// 查询加载数据
			getInventoryVouchers();
			// 编辑按钮可用
			m.actualQty.attr("e","1");
			//禁用审核按钮
			m.approvalBut.prop("disabled",true);
		}else if(_sts == "view"){
			setDisable(true);
			// 查询加载数据
			getInventoryVouchers();
			//检查是否允许审核
			_common.checkRole(m._voucherNo.val(),m.typeId.val(),function (success) {
				if(success){
					// 审核状态可以更改实际调拨量，因此编辑按钮可用
					m.actualQty.attr("e","1");
					m.approvalBut.prop("disabled",false);
					$("#updateItemDetails").prop("disabled", false);
				}else{
					m.approvalBut.prop("disabled",true);
				}
			});
		}
	}

	// 设置为查看模式
	var setDisable = function (flag) {
		$("#search").prop("disabled", true);
		$("#tf_cd").prop("disabled", true);
		$("#tf_date").prop("disabled", true);
		// $("#dj_type").prop("disabled", true);
		$("#dj_status").prop("disabled", true);
		$("#vstore").prop("disabled", true);
		$("#tstore").prop("disabled", true);
		$("#out_cd").prop("disabled", true);
		$("#creater").prop("disabled", true);
		$("#ca_date").prop("disabled", true);
		$("#modifier").prop("disabled", true);
		$("#md_date").prop("disabled", true);
		// $("#ap_name").prop("disabled", true);
		// $("#ap_date").prop("disabled", true);
		$("#resetBtn").prop("disabled", true);
		$("#vstoreRefresh").hide();
		$("#vstoreRemove").hide();
		$("#tstoreRefresh").hide();
		$("#tstoreRemove").hide();
		$("#outcdRefresh").hide();
		$("#outcdRemove").hide();
		$("#cRemark").prop("disabled", flag);
		$("#saveBtn").prop("disabled", flag);
		$("#addItemDetails").prop("disabled", flag);
		$("#updateItemDetails").prop("disabled", flag);
		$("#deleteItemDetails").prop("disabled", flag);
		//附件按钮
		$("#addByFile").prop("disabled",flag);
		$("#updateByFile").prop("disabled",flag);
		$("#deleteByFile").prop("disabled",flag);
	}

	// 设置弹窗内容是否允许编辑
	var setDialogDisable = function (flag) {
    	let flg = false;
    	if(flag>0){flg = true;}
		$("#item_input_tamount").prop("disabled", true);
		$("#adjustReason").prop("disabled", true);
		if(flag==1){
			m.differenceReason.prop("disabled",true);
			$("#diffreasonRefresh").hide();
			$("#diffreasonRemove").hide();
		}
		if(flag==2){
			$("#dialog_affirm").prop("disabled", false);
			m.actualQty.prop("disabled",false);
			if(m.differeQty.val() != 0){
				m.differenceReason.prop("disabled",false);
				$("#diffreasonRefresh").show();
				$("#diffreasonRemove").show();
			}else {
				m.differenceReason.prop("disabled",true);
				$("#diffreasonRefresh").hide();
				$("#diffreasonRemove").hide();
			}
		}else{
			$("#dialog_affirm").prop("disabled", flg);
			m.actualQty.prop("disabled",flg);
		}
		$("#item_input").prop("disabled", true);
		$("#itemRefresh").hide();
		$("#itemRemove").hide();
		$("#reasonRefresh").hide();
		$("#reasonRemove").hide();
	}

	// 清空弹窗内容
	var clearDialog = function (flag) {
    	if(flag){ // 非0为true
			$("#item_input").val("").attr("k","").attr("v","");
			$("#actualQty").val("");
			$.myAutomatic.cleanSelectObj(adjustReason);
			$.myAutomatic.cleanSelectObj(differenceReason);
		}
		$("#item_input_cd").val("");
		$("#item_input_uom").val("");
		$("#item_input_spec").val("");
		$("#item_input_price").val("");
		$("#item_input_tamount").val("");
		$("#on_hand_qty").val("");
		$("#tax_rate").val("");
	}

	// 根据权限类型的不同初始化不同的画面样式
	var initPageBytype = function(flgType){
		switch(flgType) {
			case "1":
				initTable1();//列表初始化
				break;
			case "2":
				break;
			default:
				m.error_pcode.show();
				m.main_box.hide();
		}
	}

	// 验证录入项是否符合
	var verifySearch = function(){
		let temp = null;
		if(m.viewSts.val() == 'edit'){
			temp = m.tf_cd.val();
			if(temp == null || $.trim(temp)==""){
				_common.prompt("The Document No. cannot be empty, please refresh the page!",3,"error"); // 传票编号不能为空
				$("#tf_cd").css("border-color","red");
				m.tf_cd.focus();
				return false;
			}else {$("#tf_cd").css("border-color","#CCC");}
		}

    	temp = $("#vstore").attr('k');
    	if(temp == null || $.trim(temp)==""){
			_common.prompt("Please check whether the transfer out of the store is correct!",5,"error"); // 请检查转出门店是否正确
			$("#vstore").css("border-color","red");
			m.vstore.focus();
			return false;
		}else {$("#vstore").css("border-color","#CCC");}

    	temp = $("#tstore").attr('k');
    	if(temp == null || $.trim(temp)==""){
			_common.prompt("Please check whether the transfer to the store is correct!",5,"error"); // 请检查转入门店是否正确
			$("#tstore").css("border-color","red");
			m.tstore.focus();
			return false;
		}else {$("#tstore").css("border-color","#CCC");}
		return true;
	}

	// 验证弹窗页面录入项是否符合
	var verifyDialogSearch = function(){
		let flg = $('#update_dialog').attr("flg");
		if(flg!="add"&&flg!="upt"){return false;}
		let temp = $("#item_input").attr('k');
		if(temp == null || $.trim(temp)==""){
			_common.prompt("The item cannot be empty!",3,"error");
			$("#item_input").css("border-color","red");
			$("#item_input").focus();
			return false;
		}else {
			$("#item_input").css("border-color","#CCC");
		}

		flg = m.actualQty.attr("e");
		let outtemp = reThousands($("#item_input_tamount").val());
		if(flg==='1'){
			temp = reThousands($("#actualQty").val());
			if(temp == null || $.trim(temp)==""){
				_common.prompt("The Tranfer In Qty cannot be empty!",3,"error");
				$("#actualQty").css("border-color","red");
				$("#actualQty").focus();
				return false;
			}else{
				let reg = /^[0-9]*$/;
				if(!reg.test(temp)){
					_common.prompt("Transfer In Qty cannot be empty, please enter again!",3,"error");
					$("#actualQty").css("border-color","red");
					$("#actualQty").focus();
					return false;
				}else{
					if(parseInt(temp) > parseInt(outtemp)){
						_common.prompt("The Tranfer In Qty cannot be more than Tranfer Out Qty!",3,"error");
						$("#actualQty").css("border-color","red");
						$("#actualQty").focus();
						return false;
					}else{
						$("#actualQty").css("border-color","#CCC");
					}
				}
			}
		}
		let adtemp = m.adjustReason.attr("k");
		if(adtemp == null || $.trim(adtemp)==""){
			_common.prompt("Please select the write-off reason!",3,"error");
			$("#adjustReason").css("border-color","red");
			$("#adjustReason").focus();
			return false;
		}else {
			$("#adjustReason").css("border-color","#CCC");
		}
		let diff = m.differenceReason.attr("k");
		if(m.actualQty.val() !== m.item_input_tamount.val()){
			if(diff == null || $.trim(diff)==""){
				_common.prompt("Please select the difference reason!",3,"error");
				$("#differenceReason").css("border-color","red");
				$("#differenceReason").focus();
				return false;
			}else {
				$("#differenceReason").css("border-color","#CCC");
			}
		}
		return true;
	}


	// 画面按钮点击事件
    var but_event = function(){
    	$("#reasonRemove").on("click",function (e) {
			$.myAutomatic.cleanSelectObj(adjustReason);
		});

		$("#item_input_tamount").blur(function () {
			$("#item_input_tamount").val(toThousands(this.value));
		});
		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#item_input_tamount").focus(function(){
			$("#item_input_tamount").val(reThousands(this.value));
		});

		$("#actualQty").blur(function () {
			$("#actualQty").val(toThousands(this.value));
			if($("#actualQty").val() !== $("#item_input_tamount").val()){
				$("#differeQty").val(toThousands(reThousands($("#item_input_tamount").val())-reThousands($("#actualQty").val())));
				m.differenceReason.prop("disabled",false);
				$("#diffreasonRefresh").show();
				$("#diffreasonRemove").show();
				$.myAutomatic.replaceParam(differenceReason, '');
			}else {
				m.differenceReason.prop("disabled",true);
				$("#diffreasonRefresh").hide();
				$("#diffreasonRemove").hide();
				$.myAutomatic.cleanSelectObj(differenceReason);
			}
		});
		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#actualQty").focus(function(){
			$("#actualQty").val(reThousands(this.value));
		});



		// 栏位清空
		$("#outcdRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(getSelectDOC1);
			$.myAutomatic.replaceParam(getSelectDOC1, '');
		});
		$("#diffreasonRemove").on("click", function (e) {
			$.myAutomatic.cleanSelectObj(differenceReason);
			$.myAutomatic.replaceParam(differenceReason, '');
		});
		// 重置按钮
		m.resetBtn.on("click",function(){
			$("#tf_cd").css("border-color","#CCC");
			$("#tf_date").css("border-color","#CCC");
			$("#vstore").css("border-color","#CCC");
			$("#tstore").css("border-color","#CCC");
			//还原颜色
			$("#out_cd").css("border-color","#CCC");
			$("#differenceReason").css("border-color","#CCC");
			_common.myConfirm("Are you sure to reset?",function(result){
				if(result=="true"){
					getBusinessDate();
					m.tf_cd.val("");
					$("#vstore").prop("disabled",false);
					m.dj_status.val("1");
					m.cRemark.val("");
					$("#cRemark").prop("disabled",false);
					// $("#outcdRemove").click();
					$("#out_cd").prop("disabled",true);
					$("#outcdRefresh").hide();
					$("#outcdRemove").hide();
					$("#tstore").prop("disabled",false);
					$.myAutomatic.cleanSelectObj(vstore);
					$.myAutomatic.cleanSelectObj(tstore);
					$.myAutomatic.cleanSelectObj(getSelectDOC1);
					$.myAutomatic.cleanSelectObj(adjustReason);
					$.myAutomatic.cleanSelectObj(differenceReason);
					$("#search").prop("disabled", false);
					//还原选项辅助
					$("#vstoreRefresh").show();
					$("#vstoreRemove").show();
					$("#tstoreRefresh").show();
					$("#tstoreRemove").show();
				}
			});
		});
		// 检索按钮点击事件
		m.search.on("click",function(){
			if (m.vstore.attr('k') == null || m.vstore.attr('k')==""){
				_common.prompt("Please select the To Store!",3,"error");
				$("#vstore").css("border-color","red");
				$("#vstore").focus();
				return false;
			}
			if (m.tstore.attr('k') == null || m.tstore.attr('k')==""){
				_common.prompt("Please select the From Store!",3,"error");
				$("#tstore").css("border-color","red");
				$("#tstore").focus();
				return false;
			}
			if (m.out_cd.val() == null || m.out_cd.val()==""){
				_common.prompt("Please select the Tranfer Out Document No.!",3,"error");
				$("#out_cd").css("border-color","red");
				$("#out_cd").focus();
				return false;
			}
			//确定要确认吗?
			_common.myConfirm("Transfer out document cannot be changed after click 'Confirm, are you sure to continue?'",function(result){
				//还原颜色
				$("#vstore").css("border-color","#CCC");
				$("#tstore").css("border-color","#CCC");
				$("#out_cd").css("border-color","#CCC");
				if (result == "true"){
					var flg = getapprovedDoc($("#vstore").attr('k'),$("#tstore").attr('k'));
					if (!flg){
						_common.prompt("The item was not approved when it was transferred out and cannot be transferred in!",3,"error");
						$("#search").prop("disabled", true);
						return;
					}
					var flag = getExistsDoc($("#vstore").attr('k'),$("#tstore").attr('k'));
					if (!flag){
						return;
					}
					let _data = {
						storeCd:m.tstore.attr('k'),
						voucherNo:m.out_cd.val(),
						voucherType:'502',
						voucherDate:fmtStringDate(m.tf_date.val()),
						storeCd1:m.vstore.attr('k')
					}
					let param = JSON.stringify(_data);
					// 获取商品详情
					selectDetails(param);
					//查询后禁用选项
					$(".input-sm").prop("disabled",true);
					$(".refresh").hide();
					$(".circle").hide();
					$("#file_name").prop("disabled",false);
					$("#fileData").prop("disabled",false);
				}
			});
		});

		// 保存按钮
		m.saveBtn.on("click",function(){
			if(verifySearch()){
				let _storeCd = $("#vstore").attr('k');
				let _voucherNo = $("#tf_cd").val();
				let _voucherNo1 = $("#out_cd").val();
				let _type = '501';
				let _date = subfmtDate($("#tf_date").val());
				let _storeCd1 = $("#tstore").attr('k');
				let itemDetail = [], num = 0, _amount = 0, _amountNoTax = 0, _taxAmt = 0;
				$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
					++num;
					let _price = $(this).find('td[tag=priceNoTax]').text();
					let _qty1 = reThousands($(this).find('td[tag=qty1]').text());
					let _qty2 = reThousands($(this).find('td[tag=qty2]').text());
					let _rate = reThousands($(this).find('td[tag=taxRate]').text());
					let _amtNoTax = accMul(_price, _qty1);
					let _amt = Number(accMul(_amtNoTax, accAdd(1,_rate))).toFixed(2);
					let _tax = Number(accMul(_amtNoTax, _rate)).toFixed(2);
					let orderItem = {
						storeCd:_storeCd,
						voucherNo:_voucherNo,
						voucherType:_type,
						voucherDate:_date,
						voucherNo1:_voucherNo1,
						articleId:$(this).find('td[tag=articleId]').text(),
						storeCd1:_storeCd1,
						adjustReason:$(this).find('td[tag=adjustReason]').text(),
						barcode:$(this).find('td[tag=barcode]').text(),
						salesUnitId:$(this).find('td[tag=uom]').text(),
						qty1:_qty1,
						priceNoTax:_price,
						amtNoTax:_amtNoTax,
						amt:_amt,
						amtTax:_tax,
						taxRate:_rate,
						qty2:_qty2,
						actualAmt:_amt,
						displaySeq:num,
						differenceReason:$(this).find('td[tag=differenceReason]').text()
					}
					itemDetail.push(orderItem);
					_amountNoTax = accAdd(_amountNoTax, _amtNoTax);
					_amount = accAdd(_amount, _amt);
					_taxAmt = accAdd(_taxAmt, _tax);
				});
				if(itemDetail.length < 1){
					_common.prompt("Commodity information cannot be empty!",5,"error"); // 传票商品信息不能为空
					return false;
				}

				//附件信息
				var fileDetail = [],fileDetailJson = "";
				$("#attachmentsTable>.zgGrid-tbody tr").each(function () {
					var file = {
						informCd:_voucherNo,
						fileType:'04',//文件类型 - 调拨
						fileName:$(this).find('td[tag=fileName]').text(),//文件名称
						filePath:$(this).find('td>a').attr("filepath"),//文件路径
					}
					fileDetail.push(file);
				});
				if(fileDetail.length>0){
					fileDetailJson = JSON.stringify(fileDetail)
				}

				let bean = {
					storeCd:_storeCd,
					voucherNo:_voucherNo,
					voucherType:_type,
					voucherDate:_date,
					voucherNo1:_voucherNo1,
					storeCd1:_storeCd1,
					voucherAmtNoTax:_amountNoTax,
					voucherTax:_taxAmt,
					voucherAmt:_amount,
					remark:$("#cRemark").val()
				}
				let _data = {
					searchJson : JSON.stringify(bean),
					listJson : JSON.stringify(itemDetail),
					fileDetailJson : fileDetailJson,
					pageType : '0'
				}
				let _url = '/inventoryVoucher/modify';
				let _sts =  m.viewSts.val();
				if(_sts == 'add'){
					_url = '/inventoryVoucher/save';
				}
				_common.myConfirm("Are you sure to save?",function(result){
					if(result!="true"){return false;}
					$.myAjaxs({
						url:url_root+_url,
						async:true,
						cache:false,
						type :"post",
						data :_data,
						dataType:"json",
						success:function(result){
							if(result.success){
								// 变为查看模式
								setDisable(true);
								m.viewSts.val("view");
								if(_sts == 'add'){
									$("#tf_cd").val(result.o);
								}
								_common.prompt("Data saved successfully！",2,"success",function(){/*保存成功*/
									submitFlag=true;
									//发起审核
									let typeId =m.typeId.val();
									let	nReviewid =m.reviewId.val();
									let	recordCd = $("#tf_cd").val();
									_common.initiateAudit(_storeCd,recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
										//审核按钮禁用
										m.approvalBut.prop("disabled",true);
										m.toKen.val(token);
									})
								}, true);
							}else{
								_common.prompt(result.msg,5,"error");
							}
						},
						error : function(e){
							_common.prompt("Data saved failed！",5,"error"); // 传票保存失败
						}
					});
				})
			}
		});

		// 详情弹窗Close按钮
		m.dialog_cancel.on("click",function(){
			var  flg = $('#update_dialog').attr("flg");
			if(flg=="view"){
				$('#update_dialog').modal("hide");
				return false;
			}
			_common.myConfirm("Are you sure to close?",function(result){
				$("#item_input").css("border-color","#CCC");
				$("#actualQty").css("border-color","#CCC");
				$("#item_input_tamount").css("border-color","#CCC");
				$("#adjustReason").css("border-color","#CCC");
				$("#differenceReason").css("border-color","#CCC");
				if(result == "true"){
					m.differenceReason.prop("disabled",true);
					$('#update_dialog').modal("hide");
				}
			});
		});

		// 详情弹窗Submit按钮
		m.dialog_affirm.on("click",function(){
			if(!verifyDialogSearch()){return false;}
			// 判断商品是否存在
			let flg = $('#update_dialog').attr("flg");
			let _itemId = m.item_input.attr('k');
			let _select = null;
			if(selectTrTemp!=null){
				let cols = tableGrid.getSelectColValue(selectTrTemp,"articleId");
				_select = cols['articleId'];
			}
			if(flg=='add' || _select != _itemId){
				let _addFlg = true;
				$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
					let articleId = $(this).find('td[tag=articleId]').text();
					if (articleId == _itemId) {
						_addFlg = false;
						return false; // 结束遍历
					}
				});
				if(!_addFlg){
					_common.prompt("Item already exists!", 3, "info"); // 该商品已经存在
					return false; // 停止执行
				}
			}
			if(m.actualQty.val() != m.item_input_tamount.val()){
				_common.myConfirm("Transfer in qty is not equal to trans out qty,are you sure to submit?",function(result){
					if(result == "true"){
						if($("#differenceReason").val() == null || $("#differenceReason").val() == ""){return false;}
						if(flg=='add'){
							let rowindex = 0;
							let trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
							if(trId!=null&&trId!=''){
								rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
							}
							let tr = '<tr id="zgGridTtable_'+rowindex+'_tr" class="">' +
								'<td tag="ckline" align="center" style="color:#428bca;" id="zgGridTtable_'+rowindex+'_tr_ckline" tdindex="zgGridTtable_ckline"><input type="checkbox" value="zgGridTtable_'+rowindex+'_tr" name="zgGridTtable"> </td>' +
								'<td align="right" tag="barcode" title="'+m.item_input_cd.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+m.item_input_cd.val()+'</td>' +
								'<td align="right" tag="articleId" title="'+m.item_input.attr('k')+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+m.item_input.attr('k')+'</td>' +
								'<td align="left" tag="articleName" title="'+m.item_input.attr('v')+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+m.item_input.attr('v')+'</td>' +
								'<td align="left" tag="uom" title="'+m.item_input_uom.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_uom" tdindex="zgGridTtable_uom">'+m.item_input_uom.val()+'</td>' +
								'<td align="left" tag="spec" title="'+m.item_input_spec.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_spec" tdindex="zgGridTtable_spec">'+m.item_input_spec.val()+'</td>' +
								'<td align="right" tag="priceNoTax" style="display: none;" title="'+m.item_input_price.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_priceNoTax" tdindex="zgGridTtable_priceNoTax">'+m.item_input_price.val()+'</td>' +
								'<td align="right" tag="taxRate" style="display: none;" title="'+m.tax_rate.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_taxRate" tdindex="zgGridTtable_taxRate">'+m.tax_rate.val()+'</td>' +
								'<td align="right" tag="qty2" title="'+m.item_input_tamount.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_qty2" tdindex="zgGridTtable_qty2">'+m.item_input_tamount.val()+'</td>' +
								'<td align="right" tag="qty1" title="'+m.actualQty.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_qty1" tdindex="zgGridTtable_qty1">'+m.actualQty.val()+'</td>' +
								'<td align="right" tag="differeQty" title="'+m.differeQty.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_differeQty" tdindex="zgGridTtable_differeQty">'+m.differeQty.val()+'</td>' +
								'<td align="left" tag="adjustReason" style="display: none;" title="'+m.adjustReason.attr("k")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_adjustReason" tdindex="zgGridTtable_adjustReason">'+m.adjustReason.attr("k")+'</td>' +
								'<td align="left" tag="adjustReasonText" title="'+m.adjustReason.attr("v")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_adjustReasonText" tdindex="zgGridTtable_adjustReasonText">'+m.adjustReason.attr("v")+'</td>' +
								'<td align="left" tag="differenceReason" title="'+m.differenceReason.attr("k")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_differenceReason" tdindex="zgGridTtable_differenceReason">'+m.differenceReason.attr("k")+'</td>' +
								'<td align="left" tag="differenceReasonText" title="'+m.differenceReason.attr("v")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_differenceReason" tdindex="zgGridTtable_differenceReason">'+m.differenceReason.attr("v")+'</td>' +
								'</tr>';
							$(".zgGrid-tbody").append(tr);
						}else{
							if (selectTrTemp==null){
								_common.prompt("Please select a record first!",5,"error"); // 未选中商品
								return false;
							}else{
								let cols = tableGrid.getSelectColValue(selectTrTemp,"articleId");
								$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
									let articleId = $(this).find('td[tag=articleId]').text();
									if(articleId==cols["articleId"]){
										$(this).find('td[tag=barcode]').text(m.item_input_cd.val());
										$(this).find('td[tag=articleId]').text(m.item_input.attr('k'));
										$(this).find('td[tag=articleName]').text(m.item_input.attr('v'));
										$(this).find('td[tag=uom]').text(m.item_input_uom.val());
										$(this).find('td[tag=spec]').text(m.item_input_spec.val());
										$(this).find('td[tag=priceNoTax]').text(m.item_input_price.val());
										$(this).find('td[tag=taxRate]').text(m.tax_rate.val());
										$(this).find('td[tag=qty2]').text(m.item_input_tamount.val());
										$(this).find('td[tag=qty1]').text(m.actualQty.val());
										$(this).find('td[tag=differenQty]').text($("#differeQty").val());
										$(this).find('td[tag=differenceReason]').text(m.differenceReason.attr("k"));
										$(this).find('td[tag=differenceReasonText]').text(m.differenceReason.attr("v"));
										$(this).find('td[tag=adjustReason]').text(m.adjustReason.attr("k"));
										$(this).find('td[tag=adjustReasonText]').text(m.adjustReason.attr("v"));
									}
								});
							}
						}
						m.isChange.val('1');
						$('#update_dialog').modal("hide");
					}
				});
			}else {
				_common.myConfirm("Are you sure to submit?",function(result){
					if(result == "true"){
						if(flg=='add'){
							let rowindex = 0;
							let trId = $("#zgGridTtable>.zgGrid-tbody tr:last").attr("id");
							if(trId!=null&&trId!=''){
								rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
							}
							let tr = '<tr id="zgGridTtable_'+rowindex+'_tr" class="">' +
								'<td tag="ckline" align="center" style="color:#428bca;"id="zgGridTtable_'+rowindex+'_tr_ckline" tdindex="zgGridTtable_ckline"><input type="checkbox" value="zgGridTtable_'+rowindex+'_tr" name="zgGridTtable"> </td>' +
								'<td align="right" tag="barcode" title="'+m.item_input_cd.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+m.item_input_cd.val()+'</td>' +
								'<td align="right" tag="articleId" title="'+m.item_input.attr('k')+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+m.item_input.attr('k')+'</td>' +
								'<td align="left" tag="articleName" title="'+m.item_input.attr('v')+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+m.item_input.attr('v')+'</td>' +
								'<td align="left" tag="uom" title="'+m.item_input_uom.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_uom" tdindex="zgGridTtable_uom">'+m.item_input_uom.val()+'</td>' +
								'<td align="left" tag="spec" title="'+m.item_input_spec.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_spec" tdindex="zgGridTtable_spec">'+m.item_input_spec.val()+'</td>' +
								'<td align="right" tag="priceNoTax" style="display: none;" title="'+m.item_input_price.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_priceNoTax" tdindex="zgGridTtable_priceNoTax">'+m.item_input_price.val()+'</td>' +
								'<td align="right" tag="taxRate" style="display: none;" title="'+m.tax_rate.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_taxRate" tdindex="zgGridTtable_taxRate">'+m.tax_rate.val()+'</td>' +
								'<td align="right" tag="qty2" title="'+m.item_input_tamount.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_qty2" tdindex="zgGridTtable_qty2">'+m.item_input_tamount.val()+'</td>' +
								'<td align="right" tag="qty1" title="'+m.actualQty.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_qty1" tdindex="zgGridTtable_qty1">'+m.actualQty.val()+'</td>' +
								'<td align="right" tag="differenQty" title="'+m.differeQty.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_differeQty" tdindex="zgGridTtable_differenQty">'+m.differeQty.val()+'</td>' +
								'<td align="left" tag="adjustReason" style="display: none;" title="'+m.adjustReason.attr("k")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_adjustReason" tdindex="zgGridTtable_adjustReason">'+m.adjustReason.attr("k")+'</td>' +
								'<td align="left" tag="adjustReasonText" title="'+m.adjustReason.attr("v")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_adjustReasonText" tdindex="zgGridTtable_adjustReasonText">'+m.adjustReason.attr("v")+'</td>' +
								'<td align="left" tag="differenceReason" title="'+m.differenceReason.attr("k")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_differenceReason" tdindex="zgGridTtable_differenceReason">'+m.differenceReason.attr("k")+'</td>' +
								'<td align="left" tag="differenceReasonText" title="'+m.differenceReason.attr("v")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_differenceReason" tdindex="zgGridTtable_differenceReason">'+m.differenceReason.attr("v")+'</td>' +
								'</tr>';
							$(".zgGrid-tbody").append(tr);
						}else{
							if (selectTrTemp==null){
								_common.prompt("Please select a record first!",5,"error"); // 未选中商品
								return false;
							}else{
								let cols = tableGrid.getSelectColValue(selectTrTemp,"articleId");
								$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
									let articleId = $(this).find('td[tag=articleId]').text();
									if(articleId==cols["articleId"]){
										$(this).find('td[tag=barcode]').text(m.item_input_cd.val());
										$(this).find('td[tag=articleId]').text(m.item_input.attr('k'));
										$(this).find('td[tag=articleName]').text(m.item_input.attr('v'));
										$(this).find('td[tag=uom]').text(m.item_input_uom.val());
										$(this).find('td[tag=spec]').text(m.item_input_spec.val());
										$(this).find('td[tag=priceNoTax]').text(m.item_input_price.val());
										$(this).find('td[tag=taxRate]').text(m.tax_rate.val());
										$(this).find('td[tag=qty2]').text(m.item_input_tamount.val());
										$(this).find('td[tag=qty1]').text(m.actualQty.val());
										$(this).find('td[tag=differenQty]').text(m.differeQty.val());
										$(this).find('td[tag=differenceReason]').text(m.differenceReason.attr("k"));
										$(this).find('td[tag=differenceReasonText]').text(m.differenceReason.attr("v"));
										$(this).find('td[tag=adjustReason]').text(m.adjustReason.attr("k"));
										$(this).find('td[tag=adjustReasonText]').text(m.adjustReason.attr("v"));
									}
								});
							}
						}
						m.isChange.val('1');
						$('#update_dialog').modal("hide");
					}
				});
			}
		});
    }

	// 获取传票头档信息
	var getInventoryVouchers = function(){
    	let _data = {
			storeCd:m._storeCd.val(),
			voucherNo:m._voucherNo.val(),
			voucherType:m._voucherType.val(),
			voucherDate:m._voucherDate.val(),
			voucherNo1:m._voucherNo1.val(),
			storeCd1:m._storeCd1.val()
		}
		let param = JSON.stringify(_data);
		$.myAjaxs({
			url:url_root+"/inventoryVoucher/get",
			async:false,
			cache:false,
			type :"post",
			data :"searchJson="+param,
			dataType:"json",
			success: function(result){
				if(result.success){
					// 获取商品详情
					getDetails(param);
					// 加载数据
					m.tf_cd.val(result.o.voucherNo);
					m.tf_date.val(fmtIntDate(result.o.voucherDate));
					// m.dj_type.val(result.o.voucherType);
					m.dj_status.val(result.o.reviewSts);
					m.cRemark.val(result.o.remark);
					$.myAutomatic.setValueTemp(tstore,result.o.storeCd1,result.o.storeName1);
					$.myAutomatic.setValueTemp(vstore,result.o.storeCd,result.o.storeName);
					$.myAutomatic.replaceParam(getSelectDOC1, "&vstore=" + $("#vstore").attr('k')+"&tstore=" + $("#tstore").attr("k"));
					$.myAutomatic.setValueTemp(getSelectDOC1,result.o.voucherNo1,result.o.voucherNo1);
					m.md_date.val(_common.formatCreateDate(result.o.updateYmd));
					m.ca_date.val(_common.formatCreateDate(result.o.createYmd));
					// 获取名称
					let _val = result.o.updateUserId;
					if(!!_val){
						getUserNameById(_val,function(res){
							if(res.success){
								m.modifier.val(res.o);
							}
						});
					}
					_val = result.o.createUserId;
					if(!!_val){
						getUserNameById(_val,function(res){
							if(res.success){
								m.creater.val(res.o);
							}
						});
					}
					// 替换商品下拉查询参数
					let str = "&storeCd=" + result.o.storeCd;
					$.myAutomatic.replaceParam(itemInput, str);

					//加载附件信息
					attachmentsParamGrid = "recordCd="+result.o.voucherNo+"&fileType=04";
					attachmentsTable.setting("url", url_root + "/file/getFileList");//加载附件
					attachmentsTable.setting("param", attachmentsParamGrid);
					attachmentsTable.loadData(null);
				}else{
					_common.prompt(result.msg,3,"info");
					let _sts = m.viewSts.val();
					if(_sts == "edit"){
						setDisable();
						m.viewSts.val("view");
					}
				}
			},
			error : function(e){
				_common.prompt("Failed to load voucher info!",5,"error"); // 传票信息加载失败
				var _sts = m.viewSts.val();
				if(_sts == "edit"){
					setDisable();
					m.viewSts.val("view");
				}
			}
		});
	}

	// 获取传票商品详情
	var getDetails = function(param){
		paramGrid = "searchJson=" + param;
		tableGrid.setting("url", url_root + "/inventoryVoucher/getDetails");
		tableGrid.setting("param", paramGrid);
		tableGrid.loadData();
	}
	var selectDetails = function(param){
		paramGrid = "searchJson=" + param;
		tableGrid.setting("url", url_root + "/inventoryVoucher/selectDetails");
		tableGrid.setting("param", paramGrid);
		tableGrid.loadData();
	}

	// 根据Item Code取得该商品的详细对象
	var getItemInfoByItem = function(store, item, fun){
		$.myAjaxs({
			url:url_root+"/inventoryVoucher/getItem",
			async:true,
			cache:false,
			type :"post",
			data :"itemCode="+item+"&storeCd="+store,
			dataType:"json",
			success: fun
		});
	}

	// 根据Store No.取得该店铺信息
	var initAutomatic = function(){
		adjustReason = $("#adjustReason").myAutomatic({
			url:url_root+"/cm9010/getReasonCode",
			ePageSize:10,
			startCount:0,
		})

		differenceReason = $("#differenceReason").myAutomatic({
			url:url_root+"/cm9010/getDifferenceReason",
			ePageSize:5,
			startCount:0,
		})

		$("#out_cd").prop("disabled",true);
		$("#outcdRefresh").hide();
		$("#outcdRemove").hide();
		// 转入门店选择
		vstore = $("#vstore").myAutomatic({
			url: url_root+"/inventoryVoucher/getStoreList",
			ePageSize: 5,
			startCount: 0,
			cleanInput: function() {
				$("#zgGridTtable>.zgGrid-tbody tr").empty();
				$("#search").prop("disabled",true);
				$.myAutomatic.cleanSelectObj(getSelectDOC1);
			},
			selectEleClick: function (thisObject) {
				$("#out_cd").val("");
				$("#zgGridTtable>.zgGrid-tbody tr").empty();
				let tstore = $("#tstore").attr('k');
				if(thisObject.attr('k') == m.tstore.attr('k')){
					m.vstore.focus();
					m.vstore.val("").attr('k','').attr('v','');
					_common.prompt("A transfer out of a store cannot be the same as a transfer into a store!",3,"info"); // 转出门店不能和转入门店相同
					return false;
				}else if(tstore!==null&&tstore!=="" && thisObject.attr('k')!==null&&thisObject.attr('k') !== ""){
					$("#out_cd").prop("disabled",false);
					$("#outcdRefresh").show();
					$("#outcdRemove").show();
					$.myAutomatic.cleanSelectObj(getSelectDOC1);
					var strm = "&vstore=" + thisObject.attr("k")+"&tstore=" + tstore;
					$.myAutomatic.replaceParam(getSelectDOC1, strm);
				}
				getSouthOrNorth(thisObject.attr('k'));
				// 替换商品下拉查询参数
				let str = "&storeCd=" + thisObject.attr("k");
				$.myAutomatic.replaceParam(itemInput, str);
			}
		});

		// 转出门店选择
		tstore = $("#tstore").myAutomatic({
			url: url_root+"/inventoryVoucher/getOutStoreList",
			ePageSize: 5,
			startCount: 0,
			param:[{
				'k':'zoCd',
				'v':'nouthOrsouth'

			}],
			cleanInput: function() {
				$("#zgGridTtable>.zgGrid-tbody tr").empty();
				$("#search").prop("disabled",true);
				$.myAutomatic.cleanSelectObj(getSelectDOC1);
			},
			selectEleClick: function (thisObject) {
				let vstore = $("#vstore").attr('k');
				$("#out_cd").val("");
				if(thisObject.attr('k') == m.vstore.attr('k')){
					m.tstore.focus();
					m.tstore.val("").attr('k','').attr('v','');
					_common.prompt("A transfer out of a store cannot be the same as a transfer into a store!",3,"info"); // 转出门店不能和转入门店相同
					return false;
				}else if(vstore!==null&&vstore!=="" && thisObject.attr('k')!==null&&thisObject.attr('k') !== ""){
					$("#out_cd").prop("disabled",false);
					$("#outcdRefresh").show();
					$("#outcdRemove").show();
					$.myAutomatic.cleanSelectObj(getSelectDOC1);
					var str = "&vstore=" + vstore+"&tstore=" + thisObject.attr("k");
					$.myAutomatic.replaceParam(getSelectDOC1, str);
				}
				// 根据所选商店确定南北方
				getSouthOrNorth(thisObject.attr('k'));
			}

		});

		// 选择转出单号
		getSelectDOC1 = $("#out_cd").myAutomatic({
			url: url_root+"/inventoryVoucher/getDOCList",
			ePageSize: 5,
			startCount: 0,
			cleanInput: function() {
				$("#zgGridTtable>.zgGrid-tbody tr").empty();
				var str = "&vstore=" + $("#vstore").attr('k')+"&tstore=" + $("#tstore").attr("k");
				$.myAutomatic.replaceParam(getSelectDOC1, str);
			},
			selectEleClick: function (thisObject) {
				$("#search").prop("disabled", false);
			}
		});

		// 判断是否商品已经转移
		getExistsDoc = function(vstore,tstore){
			if(tstore!=null&&tstore!=''&&vstore!=null&&vstore!='') {
				var existDoc = '1';
				$.myAjaxs({
					url: url_root + "/inventoryVoucher/getExistsDoc",
					async: false,
					cache: false,
					type: "post",
					data: "vstore="+vstore+"&tstore="+tstore,
					dataType: "json",
					success: function (result) {
						if(result.success){
							for (var i = 0; i < result.data.length; i++) {
								var optionValue = result.data[i].voucherNo1;
								console.log(optionValue);
								if(optionValue == m.out_cd.val()){
									// 货物在店铺之间不可以进行多次转移
									_common.prompt("No multiple transfers between stores,please select other Tranfer Out Document No.!",3,"error");
									$("#search").prop("disabled", false);
									existDoc = '0';
									break;
								}
							};
							// $("#search").prop("disabled", false);
						}
					},
					error: function (e) {
						_common.prompt("The request failed!", 5, "error");
					},
					complete: _common.myAjaxComplete
				});
				if(existDoc == '0'){
					return false;
				}else{
					return true;
				}
			}
		}

		// 判断是否转出商品已经审核通过
		getapprovedDoc = function(vstore,tstore){
			if(tstore!=null&&tstore!=''&&vstore!=null&&vstore!='') {
				var approvedDoc = '0';
				$.myAjaxs({
					url: url_root + "/inventoryVoucher/getApprovedList",
					async: false,
					cache: false,
					type: "post",
					data: "vstore="+vstore+"&tstore="+tstore,
					dataType: "json",
					success: function (result) {
						if(result.success){
							for (var i = 0; i < result.data.length; i++) {
								var optionValue = result.data[i].voucherNo;
								if(optionValue === m.out_cd.val()){
									$("#search").prop("disabled", true);
									approvedDoc = '1';
									break;
								}
							};
							// $("#search").prop("disabled", false);
						}
					},
					error: function (e) {
						_common.prompt("The request failed!", 5, "error");
					},
					complete: _common.myAjaxComplete
				});
				if(approvedDoc == '0'){
					return false;
				}else{
					return true;
				}
			}
		}
		// 更新转出商品的转入数量
		modifyQty1 = function(qty1,voucherNo1,articleId){
			$.myAjaxs({
				url:url_root+"/inventoryVoucher/updateQty1",
				async:true,
				cache:false,
				type :"post",
				data :"qty1="+qty1+"&voucherNo="+voucherNo1+"&articleId="+articleId,
				// dataType:"json",
				success: function (result) {
					console.log("Modify Success!");
				},
				error:function (e){
					_common.prompt("Modify failed！",3,"error");
				}
			});
		};
		// 同步调拨商品数量不一致的原因
		updateDiffReasons = function(differenceReason,voucherNo1,articleId){
			$.myAjaxs({
				url:url_root+"/inventoryVoucher/updateDiffReasons",
				async:true,
				cache:false,
				type :"post",
				data :"differenceReason="+differenceReason+"&voucherNo="+voucherNo1+"&articleId="+articleId,
				dataType:"json",
				success: function (result) {
					console.log("Modify Difference Reasons Success!");
				},
				error:function (e){
					_common.prompt("Modify Difference Reasons failed！",3,"error");
				}
			});
		}
		// 商品选择
		itemInput = $("#item_input").myAutomatic({
			url: url_root+"/inventoryVoucher/getItemList",
			ePageSize: 5,
			startCount: 3,
			cleanInput: function() {
				clearDialog(false);
			},
			selectEleClick: function (thisObject) {
				clearDialog(false);
				let _storeCd = $("#tstore").attr('k');
				let _itemId = thisObject.attr('k');
				if(!!_storeCd && !!_itemId){
					checkParent(_itemId, function(res){
						if(res.success){
							getItemInfoByItem(_storeCd, _itemId,function(res){
								if(res.success){
									$("#item_input_cd").val(res.o.barcode);
									$("#item_input_uom").val(res.o.uom);
									$("#item_input_spec").val(res.o.spec);
									$("#item_input_price").val(res.o.baseOrderPrice);
									$("#tax_rate").val(res.o.taxRate);
									$("#item_input_tamount").val(res.o.qty2);
									// 查询实时库存
									getStock(_storeCd, _itemId);
								}else{
									$.myAutomatic.cleanSelectObj(itemInput);
									_common.prompt(res.msg,3,"info");
								}
							});
						}else{
							$.myAutomatic.cleanSelectObj(itemInput);
							_common.prompt(res.message,3,"info");
						}
					});
				}
			}
		});
	}

	// 根据User Id取得用户名称
	var getUserNameById = function(userId, fun){
		$.myAjaxs({
			url:url_root+"/inventoryVoucher/getuser",
			async:true,
			cache:false,
			type :"post",
			data :"userId="+userId,
			dataType:"json",
			success: fun
		});
	}

	// 查询商品库存数量
	var getStock = function(storeCd, itemId){
		$.myAjaxs({
			url:url_root+"/inventoryVoucher/getStock",
			async:true,
			cache:false,
			type :"post",
			data :"storeCd="+storeCd+"&itemId="+itemId,
			dataType:"json",
			success: function (result) {
				if(result.success){
					m.on_hand_qty.val(result.o.realtimeQty);
				}else{
					_common.prompt(result.msg, 3, "error");
				}
			},
			error : function(e){
				_common.prompt("Query inventory quantity failed, please try again!",3,"error");
			}
		});
	}

	// 表格初始化
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Document Details",
			param:paramGrid,
			localSort: true,
			colNames:["Item Barcode","Item Code","Item Name","UOM","Specification","Price","Rate","Tranfer Out Qty",
				"Tranfer In Qty","Variance  Qty ","VarianceReason","Variance Reason","adjustReason","Transfer Reason"],
			colModel:[
				{name:"barcode",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"articleId",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"uom",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"spec",type:"text",text:"left",width:"130",ishide:false,css:""},
				{name:"priceNoTax",type:"text",text:"right",ishide:true,css:"",getCustomValue:getString0},
				{name:"taxRate",type:"text",text:"right",ishide:true,css:"",getCustomValue:getString0},
				{name:"qty2",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"qty1",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"differenQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"differenceReason",type:"text",text:"left",ishide:true},
				{name:"differenceReasonText",type:"text",text:"right",width:"130",ishide:false,css:""},
				{name:"adjustReason",type:"text",text:"left",ishide:true},
				{name:"adjustReasonText",type:"text",text:"left",width:"130",ishide:false,css:""},
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:false,//是否需要分页
			// sidx:"bm.bm_type,order.bm_code",//排序字段
			// sord:"asc",//升降序
			isCheckbox:true,
			freezeHeader:true,

			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			},
			buttonGroup:[
				/*{
					butType: "add",
					butId: "addItemDetails",
					butText: "Add",
					butSize: ""
				},*/
				{
					butType: "update",
					butId: "updateItemDetails",
					butText: "Modify",
					butSize: ""
				},{
					butType: "view",
					butId: "viewItemDetails",
					butText: "View",
					butSize: ""//,
				},{
					butType: "delete",
					butId: "deleteItemDetails",
					butText: "Delete",
					butSize: ""//,
				},
				{butType:"custom",butHtml:"<button id='attachments' type='button' class='btn btn-primary btn-sm'><span class='glyphicon glyphicon glyphicon-file'></span> Attachments</button>"},//附件
			],
		});
	}

    // 日期字段格式化格式
    var dateFmt = function(tdObj, value){
		if(value!=null&&value.trim()!=''&&value.length==8) {
			value = fmtIntDate(value);
		}
    	return $(tdObj).text(fmtIntDate(value));
    }
	var getSouthOrNorth=function (storeCd) {
		$.myAjaxs({
			url:url_root+"/storeTransfers/SouthOrNorth",
			async:true,
			cache:false,
			type :"post",
			data:"storeCd="+storeCd,
			dataType:"json",
			success: function (result) {
				if (result.success){
					$("#nouthOrsouth").val(result.o.zoCd);
				};
			}
		})
	}
	// 格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		if(date!=null&&date.trim()!=''&&date.length==8) {
			res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		}
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

	// 运算
	function accAdd(arg1,arg2){
		var r1,r2,m;
		try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
		try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
		m=Math.pow(10,Math.max(r1,r2));
		return (accMul(arg1,m)+accMul(arg2,m))/m;
	}
	function accMul(arg1,arg2){
		var m=0,s1=arg1.toString(),s2=arg2.toString();
		try{m+=s1.split(".")[1].length}catch(e){}
		try{m+=s2.split(".")[1].length}catch(e){}
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
	}

	// 日期转换
	function subfmtDate(date){
		let res = "";
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
		return res;
	}

	// 为0返回'0'
	function getString0(tdObj, value){
		let _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}


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
				_common.prompt(title+" Failed to load data!",3,"info");
			}
		});
	}

	// 初始化下拉列表
	// function getReasonSelect(title, selectId, code) {
	// 	let param = "codeValue="+code;
	// 	getSelectOptions(title,"/cm9010/getCode", param,function(res){
	// 		let selectObj = $("#" + selectId);
	// 		selectObj.find("option:not(:first)").remove();
	// 		for (let i = 0; i < res.length; i++) {
	// 			let optionValue = res[i].codeValue;
	// 			let optionText = res[i].codeName;
	// 			selectObj.append(new Option(optionText, optionValue));
	// 		}
	// 	});
	// }

	// 获取业务日期
	function getBusinessDate(){
		getSelectOptions("Document Date","/cm9060/getDate", null,function(res){
			let date = "";
			if(!!res && res.toString().length==8){
				res = res.toString();
				date = res.substring(6,8)+"/"+res.substring(4,6)+"/"+res.substring(0,4);
			}
			$('#tf_date').val(date);
		});
	}

	// 请求加载下拉列表
	// function getSelectValue(){
	// 	// 加载select
	// 	getReasonSelect("Transfer Reason","adjustReason", "00235");
	// }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('storeTransfersInEdit');
_start.load(function (_common) {
	_index.init(_common);
});
