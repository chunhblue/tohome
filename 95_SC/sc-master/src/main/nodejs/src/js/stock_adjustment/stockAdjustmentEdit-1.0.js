require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");
var _datetimepicker = require("datetimepicker");

var _myAjax=require("myAjax");
define('stockAdjustmentEdit', function () {
	var self = {};
	var url_left = "",
		url_root = "",
		paramGrid = null,
		selectTrTemp = null,
		vstore = null,
		itemInput = null,
		generalReason= null,
		adjustReason = null,
		expenditureNo = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		tempTrObjValue = {},//临时行数据存储
		bmCodeOrItem = 0,//0 bmCode 1：item
		common=null;
	const KEY = 'INVENTORY_ADJUSTMENT_ENTRY';
	var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		viewSts : null,
		searchJson:null,
		// 基础信息
		tf_cd : null,
		tf_date : null,
		dj_type : null,
		dj_status : null,
		vstore : null,
		adjustmentSum : null,
		cRemark : null,
		modifier : null,
		creater : null,
		ca_date : null,
		md_date : null,
		ap_name : null,
		ap_date : null,
		inventoryQty: null, // 实时库存
		saveBtn:null,
		resetBtn:null,
		returnsViewBut : null,
		// 弹窗
		item_input: null,
		item_input_cd : null,
		item_input_uom : null,
		item_input_spec : null,
		item_input_price : null,
		tax_rate : null,
		item_input_tamount : null,
		generalReason:null,
		adjustReason:null,
		dialog_cancel : null,
		dialog_affirm : null,
		expenditureNo : null,
		description : null,
		// 主键参数
		_storeCd : null,
		_voucherNo : null,
		_voucherType : null,
		_voucherDate : null,
		_storeCd1 : null,
		//审核
		approvalBut:null,
		approval_dialog:null,
		audit_cancel:null,
		audit_affirm:null,
		typeId:null,
		input_store:null,
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
		url_left = url_root+"/stockAdjustment";
		reThousands=_common.reThousands;
		toThousands=_common.toThousands;
		getThousands=_common.getThousands;
		// 加载下拉列表
		// getSelectValue();
		// 首先验证当前操作人的权限是否混乱，
		if(m.use.val()=="0"){
			but_event();
		}else{
			m.error_pcode.show();
			m.main_box.hide();
		}
		// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
		// 表格内按钮事件
		table_event();
		// 根据跳转加载数据，设置操作模式
		setValueByType();
		//审核事件
		approval_event();
		// 初始化选择
		initAutomatic();
	}

	// 表格内按钮事件
	var table_event = function(){
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
			setDialogDisable(false);
			// $("#adjustReason").prop("disabled","true");
			// $("#reasonRefresh").hide();
			// $("#reasonRemove").hide();
			$('#update_dialog').attr("flg","add");
			$('#update_dialog').modal("show");
		});

		// 修改
		$("#updateItemDetails").on("click",function(){
			let flg = m.viewSts.val();
			if(flg!="add"&&flg!="edit"){return false;}
			let _list = tableGrid.getCheckboxTrs();
			if((_list == null || _list.length == 0) && selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",3,"info");
				return false;
			}
			if(_list.length >= 2){
				_common.prompt("Please select only one row of data!",3,"info");
				return false;
			}
			if(_list.length == 1){
				selectTrTemp = _list[0];
			}
			let _storeCd = $("#vstore").attr('k');
			if(!_storeCd){
				_common.prompt("Please select the store first!",3,"info");
				$("#vstore").css("border-color","red");
				$("#vstore").focus();
				return false;
			}else {$("#vstore").css("border-color","#CCC");}
			let cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,uom,spec,priceNoTax,taxRate,qty1,generalReason,generalReasonText,adjustReason,adjustReasonText");
			var attribute1 = $(selectTrTemp[0]).find('td').eq(4).text();
			if (attribute1==="Total:"){
				_common.prompt("Please select the effective data to modify!", 5, "error");
				$("#update_dialog").hide();
				return false;
			}
			$("#item_input_cd").val(cols['barcode']);
			$.myAutomatic.setValueTemp(itemInput,cols['articleId'],cols['articleName']);
			$("#item_input_uom").val(cols['uom']);
			$("#item_input_spec").val(cols['spec']);
			$("#item_input_price").val(cols['priceNoTax']);
			$("#tax_rate").val(cols['taxRate']);
			$("#item_input_tamount").val(cols['qty1']);
			$.myAutomatic.setValueTemp(generalReason,cols['generalReason'],cols['generalReasonText']);
			$.myAutomatic.setValueTemp(adjustReason,cols['adjustReason'],cols['adjustReasonText']);
			setDialogDisable(false);

				let strs = tableGrid.getSelectColValue(selectTrTemp,"expenditureNoText,description");
				$.myAutomatic.setValueTemp(expenditureNo,'',strs['expenditureNoText']);
				let accDate = subfmtDate(m.tf_date.val());
				let storeCd = $("#vstore").attr("k");
				if(strs['expenditureNoText'] == null || strs['expenditureNoText'] == ''){
					$.myAutomatic.replaceParam(expenditureNo,"&accDate="+accDate+"&storeCd="+storeCd);
				}
			getDescription($("#expenditureNo").attr("k"),storeCd,accDate);
				setMinDialogDisable(false);
				$("#description").val(strs['description']);
			$('#update_dialog').attr("flg","upt");
			$('#update_dialog').modal("show");
			$("#adjustReason").attr("disabled",false);
			// 查询实时库存
			getStock(_storeCd, cols['articleId']);
		});

		// 查看
		$("#viewItemDetails").on("click",function(){
			$("#adjustReason").attr("disabled",true);
			let _storeCd = $("#vstore").attr('k');
			let _list = tableGrid.getCheckboxTrs();
			if((_list == null || _list.length == 0) && selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",3,"info");
				return false;
			}
			if(_list.length >= 2){
				_common.prompt("Please select only one row of data!",3,"info");
				return false;
			}
			if(_list.length == 1){
				selectTrTemp = _list[0];
			}
				let cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,uom,spec,priceNoTax,qty1,generalReason,generalReasonText,adjustReasonText");
				var attribute1 = $(selectTrTemp[0]).find('td').eq(4).text();
				if (attribute1==="Total:"){
					_common.prompt("Please select the effective data to view!", 5, "error");
					$("#update_dialog").hide();
					return false;
				}
				$("#item_input_cd").val(cols['barcode']);
				$.myAutomatic.setValueTemp(itemInput,cols['articleId'],cols['articleName']);
				$("#item_input_uom").val(cols['uom']);
				$("#item_input_spec").val(cols['spec']);
				$("#item_input_price").val(cols['priceNoTax']);
				$("#item_input_tamount").val(cols['qty1']);
				$.myAutomatic.setValueTemp(generalReason,cols['generalReason'],cols['generalReasonText']);
				$.myAutomatic.setValueTemp(adjustReason,'',cols['adjustReasonText']);
				setDialogDisable(true);
                let strs = tableGrid.getSelectColValue(selectTrTemp,"expenditureNoText,description");
				$.myAutomatic.setValueTemp(expenditureNo,'',strs['expenditureNoText']);
				let accDate = subfmtDate(m.tf_date.val());
				let storeCd = $("#vstore").attr("k");
				getDescription($("#expenditureNo").attr("k"),storeCd,accDate);
				setMinDialogDisable(true);
				$("#description").val(strs['description']);
				$("#expenditureNo_refresh").hide();
				$("#expenditureNo_clear").hide();
				$('#update_dialog').attr("flg","view");
				$('#update_dialog').modal("show");
				// 查询实时库存
				getStock(_storeCd, cols['articleId']);

		});

		// 删除按钮
		$("#deleteItemDetails").on("click",function(){
			let _list = tableGrid.getCheckboxTrs();
			if(_list == null || _list.length == 0){
				_common.prompt("Please select the data to confirm deletion!",3,"info");/*请选择要确认删除的数据*/
				return false;
			}else{
				_common.myConfirm("Are you sure you want to delete?",function(result){
					if(result == "true"){
						for(let i = 0; i < _list.length; i++){
							$(_list[i].selector).remove();
						}
					}
				});
			}
		});
	}

	// 根据跳转方式，设置画面可否编辑、加载内容
	var setValueByType = function(){
		let _sts = m.viewSts.val();
		if(_sts == "add"){
			// 设置业务日期
			getBusinessDate();
			// 新增默认未审核
			m.dj_status.val("1");
			// 禁用审核按钮
			m.approvalBut.prop("disabled",true);
		}else if(_sts == "edit"){
			setDisable(false);
			// 禁用审核按钮
			m.approvalBut.prop("disabled",true);
			// 查询加载数据
			getInventoryVouchers();
		}else if(_sts == "view"){
			setDisable(true);
			// 查询加载数据
			getInventoryVouchers();
			//检查是否允许审核
			_common.checkRole(m._voucherNo.val(),m.typeId.val(),function (success) {
				if(success){
					m.approvalBut.prop("disabled",false);
				}else{
					m.approvalBut.prop("disabled",true);
				}
			});
		}
	}

	// 设置为查看模式
	var setDisable = function (flag) {
		$("#tf_cd").prop("disabled", true);
		$("#tf_date").prop("disabled", true);
		$("#dj_type").prop("disabled", true);
		$("#dj_status").prop("disabled", true);
		$("#vstore").prop("disabled", true);
		$("#adjustmentSum").prop("disabled", true);
		$("#modifier").prop("disabled", true);
		$("#md_date").prop("disabled", true);
		$("#creater").prop("disabled", true);
		$("#ca_date").prop("disabled", true);
		$("#resetBtn").prop("disabled", true);
		$("#vstoreRefresh").hide();
		$("#vstoreRemove").hide();
		$("#cRemark").prop("disabled", flag);
		$("#saveBtn").prop("disabled", flag);
		$("#addItemDetails").prop("disabled", flag);
		$("#updateItemDetails").prop("disabled", flag);
		$("#deleteItemDetails").prop("disabled", flag);
	}

	// 设置弹窗内容是否允许编辑
	var setDialogDisable = function (flag) {
		$("#item_input").prop("disabled", flag);
		$("#item_input_cd").prop("disabled", flag);
		$("#item_input_uom").prop("disabled", flag);
		$("#item_input_spec").prop("disabled", flag);
		$("#item_input_price").prop("disabled", flag);
		$("#tax_rate").prop("disabled", flag);
		$("#item_input_tamount").prop("disabled", flag);
		// $("#generalReason").prop("disabled", flag);
		$("#adjustReason").prop("disabled", flag);
		$("#dialog_affirm").prop("disabled", flag);
		$("#expenditureCode").hide();
		$("#expenditureNo").hide();
		$("#expenditureNo_refresh").hide();
		$("#expenditureNo_clear").hide();
		$("#expenditureDescription").hide();
		if(flag){
			$("#itemRefresh").hide();
			$("#itemRemove").hide();
			$("#reasonRefresh").hide();
			$("#reasonRemove").hide();
		}else{
			$("#itemRefresh").show();
			$("#itemRemove").show();
			$("#reasonRefresh").show();
			$("#reasonRemove").show();
		}
	};

	var setMinDialogDisable = function (flag) {
		$("#expenditureNo").prop("disabled", flag);
		$("#expenditureCode").show();
		$("#expenditureNo").show();
		$("#expenditureNo_refresh").show();
		$("#expenditureNo_clear").show();
		$("#expenditureDescription").show();
	};

	// 清空弹窗内容
	var clearDialog = function (flag) {
		if(flag){
			$("#item_input").val("").attr("k","").attr("v","");
			$.myAutomatic.cleanSelectObj(generalReason);
			$.myAutomatic.cleanSelectObj(adjustReason);
		}
		$("#item_input_cd").val("");
		$("#item_input_uom").val("");
		$("#item_input_spec").val("");
		$("#item_input_price").val("");
		$("#item_input_tamount").val("");
		$("#tax_rate").val("");
		$("#inventoryQty").val("");
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
				$("#audit_affirm").prop("disabled",true);
				var detailType = "tmp_adjustment";
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
							//更新主档审核状态值
							// _common.modifyRecordStatus(auditStepId,auditStatus);
							m.approvalBut.prop("disabled", true);
							// 判断是否更新库存调整的值
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
				return false;
			}
		}
		temp = m.tf_date.val();
		if(temp == null || $.trim(temp)==""){
			_common.prompt("The Document Date cannot be empty, please refresh the page!",3,"error"); // 传票时间不能为空
			return false;
		}
		temp = m.dj_type.val();
		if(temp == null || $.trim(temp)==""){
			_common.prompt("The Document Type cannot be empty, please refresh the page!",3,"error"); // 传票类型不能为空
			return false;
		}
		temp = m.vstore.attr('k');
		if(temp == null || $.trim(temp)==""){
			_common.prompt("Please check the Store No. is correct!",3,"error");
			m.vstore.focus();
			return false;
		}
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
			$("#item_input").css("border-color","#CCCCCC");
		}
		let handQty = reThousands(m.inventoryQty.val());
		/*if(parseInt(handQty) <= 0){
			_common.prompt("Target item is out of stock, cannot adjust!",3,"info");
			$("#actualQty").prop("disabled", true);
			//1/29
			return false;
		}else {*/
		temp = reThousands($("#item_input_tamount").val());
		var temp1 = $("#item_input_tamount").val();
		if(temp == null || $.trim(temp)==""){
			_common.prompt("The adjustment quantity cannot be empty!",3,"error");
			$("#item_input_tamount").css("border-color","red");
			$("#item_input_tamount").focus();
			return false;
		}else if(temp == '0'){
			_common.prompt("The adjustment quantity cannot be 0!",3,"error");
			$("#item_input_tamount").css("border-color","red");
			$("#item_input_tamount").focus();
			return false;
		}else if(temp > 999999){ //check
			_common.prompt("The adjustment quantity cannot be more than 999,999!",3,"error");
			$("#item_input_tamount").css("border-color","red");
			$("#item_input_tamount").focus();
			return false;
		}else {
			let reg = /^-?[1-9]\d*$/;
			if (!reg.test(temp)|| temp1.indexOf(",")>0) {
				_common.prompt("The quantity can only be integers!", 3, "error");
				$("#item_input_tamount").css("border-color", "red");
				$("#item_input_tamount").focus();
				return false;
			}else {
				$("#item_input_tamount").css("border-color", "#CCC");
			}
			/*if(parseInt(temp) < 0){
                if (Math.abs(parseInt(temp)) > parseInt(handQty)) {
                    _common.prompt("Reduction of adjustment quantity cannot be more than actual stock quantity!", 3, "error");
                    $("#item_input_tamount").css("border-color", "red");
                    $("#item_input_tamount").focus();
                    return false;
                } else {
                    $("#item_input_tamount").css("border-color", "#CCC");
                }
            }*/
		}
		temp = m.adjustReason.val();

		if(temp == null || $.trim(temp)==""){
			_common.prompt("Please select the Reason for the adjustment!",3,"error");
			$("#adjustReason").css("border-color","red");
			$("#adjustReason").focus();
			return false;
		}else {
			$("#adjustReason").css("border-color","#CCCCCC");
		}
		if (m.adjustReason.attr("k")!=null && m.adjustReason.attr("k")!=""){
			if (m.generalReason.attr("k")==null || m.generalReason.attr("k")==""){
				var strm ="&detailedLevelCd="+ m.adjustReason.attr('k');
				getGeneralLevelReason(strm);
			}
		}
		return true;
	}

	// 画面按钮点击事件
	var but_event = function(){
		$("#item_input_tamount").blur(function () {
			let reg = /^-?[1-9]\d*$/;
			if (reg.test( this.value)|| this.value.indexOf(",")<0){
				$("#item_input_tamount").val(this.value);
			}
		});
		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#item_input_tamount").focus(function(){
			let reg = /^-?[1-9]\d*$/;
			if (reg.test( this.value)|| this.value.indexOf(",")<0){
				$("#item_input_tamount").val(this.value);
			}
		});

		// 重置按钮
		m.resetBtn.on("click",function(){
			_common.myConfirm("Are you sure you want to reset?",function(result){
				$("#vstore").css("border-color","#CCC");
				if(result=="true"){
					$("#zgGridTtable_tbody").each(function () {
						var trList = $("#zgGridTtable_tbody  tr:not(:last)");
						trList.remove();
						if ("#zgGridTtable_tbody  tr:last"){
							$(this).find('td[tag=total_item2]').text("total Item:0");
							$(this).find('td[tag=total_qty1]').text("total qty:0");
						}
					})
					getBusinessDate();
					m.tf_cd.val("");
					m.dj_status.val("1");
					m.adjustmentSum.val("0");
					m.cRemark.val("");
					$.myAutomatic.cleanSelectObj(vstore);
				}
			});
		});

		// 保存按钮
		m.saveBtn.on("click",function(){
			if(verifySearch()){
				let _storeCd = $("#vstore").attr('k');
				let _voucherNo = $("#tf_cd").val();
				let _type = $("#dj_type").val();
				let _date = subfmtDate($("#tf_date").val());
				// 处理传票商品信息
				let itemDetail = [], num = 0, _amount = 0, _amountNoTax = 0, _taxAmt = 0;
				$("#zgGridTtable>.zgGrid-tbody tr:not(:last-child)").each(function () {
					++num;
					let _price = $(this).find('td[tag=priceNoTax]').text();
					let _qty = reThousands($(this).find('td[tag=qty1]').text());
					let _rate = reThousands($(this).find('td[tag=taxRate]').text());
					let _amtNoTax = accMul(_price, _qty);
					let _amt = Number(accMul(_amtNoTax, accAdd(1,_rate))).toFixed(2);
					let _tax = Number(accMul(_amtNoTax, _rate)).toFixed(2);
					let orderItem = {
						storeCd:_storeCd,
						voucherNo:_voucherNo,
						voucherType:_type,
						voucherDate:_date,
						articleId:$(this).find('td[tag=articleId]').text(),
						storeCd1:_storeCd,
						adjustReason:$(this).find('td[tag=adjustReason]').text(),
						expenditureNo:$(this).find('td[tag=expenditureNo]').text(),
						barcode:$(this).find('td[tag=barcode]').text(),
						salesUnitId:$(this).find('td[tag=uom]').text(),
						qty1:_qty,
						priceNoTax:_price,
						amtNoTax:_amtNoTax,
						amt:_amt,
						amtTax:_tax,
						taxRate:_rate,
						displaySeq:num
					}
					itemDetail.push(orderItem);
					_amountNoTax = accAdd(_amountNoTax, _amtNoTax);
					_amount = accAdd(_amount, _amt);
					_taxAmt = accAdd(_taxAmt, _tax);
				});
				if(itemDetail.length < 1){
					_common.prompt("Commodity information cannot be empty!",5,"error");
					return false;
				}
				// 处理传票头档信息
				let bean = {
					storeCd:_storeCd,
					voucherNo:_voucherNo,
					voucherType:_type,
					voucherDate:_date,
					storeCd1:_storeCd,
					voucherAmtNoTax:_amountNoTax,
					voucherTax:_taxAmt,
					voucherAmt:_amount,
					remark:$("#cRemark").val()
				}
				let _data = {
					searchJson : JSON.stringify(bean),
					listJson : JSON.stringify(itemDetail),
					pageType : '9'
				}
				let _url = '/inventoryVoucher/save';
				let _sts =  m.viewSts.val();
				if(_sts == 'edit'){
					_url = '/inventoryVoucher/modify';
				}
				_common.myConfirm("Are you sure you want to save?",function(result){
					if(result!="true"){return false;}
					setDisable(true);
					m.returnsViewBut.prop("disabled",true);
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
								m.adjustmentSum.val(toThousands(_amount));
								m.ca_date.val(_common.formatCreateDate(result.date+result.hms));
								getUserNameById(result.createUserId,function(res){
									if(res.success){
										m.creater.val(res.o);
									}
								});
								if(_sts == 'add'){
									$("#tf_cd").val(result.o);
								}
								_common.prompt("Data saved successfully！",3,"success",function(){
									m.returnsViewBut.prop("disabled",false);
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
								_common.prompt(result.msg,3,"info");
							}
						},
						error : function(e){
							_common.prompt("Data saved failed！",3,"error");/* 保存失败 */
						}
					});
				})
			}
		});

		// 返回按钮
		m.returnsViewBut.on("click",function(){
			_common.updateBackFlg(KEY);
			top.location = url_left;
		});

		// 详情弹窗Close按钮
		m.dialog_cancel.on("click",function(){
			var  flg = $('#update_dialog').attr("flg");
			if(flg=="view"){
				$('#update_dialog').modal("hide");
				return false;
			}
			_common.myConfirm("Are you sure you want to close?",function(result){
				$("#item_input").css("border-color","#CCCCCC");
				$("#item_input_tamount").css("border-color","#CCCCCC");
				$("#adjustReason").css("border-color","#CCCCCC");
				if(result == "true"){
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
					_common.prompt("The item does exist!",5,"info"); // 该商品已经存在
					return false; // 停止执行
				}
			}
			_common.myConfirm("Are you sure you want to submit?",function(result){

				if(result == "true"){
					if(flg=='add'){
						let rowindex = 0;
						let trId = $("#zgGridTtable>.zgGrid-tbody tr:nth-last-child(2)").attr("id");
						if(trId!=null&&trId!=''){
							rowindex = parseInt(trId.substring(trId.indexOf("_")+1,trId.indexOf("_")+2))+1;
						}
						let tr = '<tr id="zgGridTtable_'+rowindex+'_tr" class="">' +
							'<td tag="ckline" align="center" width="50" style="color:#428bca;" id="zgGridTtable_'+rowindex+'_tr_ckline" tdindex="zgGridTtable_ckline"><input type="checkbox" value="zgGridTtable_'+rowindex+'_tr" name="zgGridTtable"> </td>' +
							'<td align="right" width="120" tag="barcode" title="'+m.item_input_cd.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_barcode" tdindex="zgGridTtable_barcode">'+m.item_input_cd.val()+'</td>' +
							'<td align="right" width="110" tag="articleId" title="'+m.item_input.attr('k')+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleId" tdindex="zgGridTtable_articleId">'+m.item_input.attr('k')+'</td>' +
							'<td align="left" width="180" tag="articleName" title="'+m.item_input.attr('v')+'" align="center" id="zgGridTtable_'+rowindex+'_tr_articleName" tdindex="zgGridTtable_articleName">'+m.item_input.attr('v')+'</td>' +
							'<td align="left" width="100" tag="uom" title="'+m.item_input_uom.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_uom" tdindex="zgGridTtable_uom">'+m.item_input_uom.val()+'</td>' +
							'<td align="left" width="100" tag="spec" title="'+m.item_input_spec.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_spec" tdindex="zgGridTtable_spec">'+m.item_input_spec.val()+'</td>' +
							'<td align="right" tag="priceNoTax" style="display: none;" title="'+m.item_input_price.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_priceNoTax" tdindex="zgGridTtable_priceNoTax">'+m.item_input_price.val()+'</td>' +
							'<td align="right" tag="taxRate" style="display: none;" title="'+m.tax_rate.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_taxRate" tdindex="zgGridTtable_taxRate">'+m.tax_rate.val()+'</td>' +
							'<td align="right" width="110" tag="qty1" title="'+m.item_input_tamount.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_qty1" tdindex="zgGridTtable_qty1">'+m.item_input_tamount.val()+'</td>' +
							'<td align="right" width="110" tag="inventoryQty" title="'+m.inventoryQty.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_inventoryQty" tdindex="zgGridTtable_inventoryQty">'+m.inventoryQty.val()+'</td>' +
							'<td align="left" tag="generalReason" style="display: none;" title="'+m.generalReason.attr("k")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_generalReason" tdindex="zgGridTtable_generalReason">'+m.generalReason.attr("k")+'</td>' +
							'<td align="left" tag="generalReasonText" style="display: none;" title="'+m.generalReason.attr("v")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_generalReasonText" tdindex="zgGridTtable_generalReasonText">'+m.generalReason.attr("v")+'</td>' +
							'<td align="left" tag="adjustReason" style="display: none;" title="'+m.adjustReason.attr("k")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_adjustReason" tdindex="zgGridTtable_adjustReason">'+m.adjustReason.attr("k")+'</td>' +
							'<td align="left" width="150" tag="adjustReasonText" title="'+m.adjustReason.attr("v")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_adjustReasonText" tdindex="zgGridTtable_adjustReasonText">'+m.adjustReason.attr("v")+'</td>' +
							'<td align="left" width="130" tag="expenditureNo" style="display: none;" title="'+m.expenditureNo.attr("k")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_expenditureNo" tdindex="zgGridTtable_expenditureNo">'+m.expenditureNo.attr("k")+'</td>' +
							'<td align="left" width="130" tag="expenditureNoText" style="display: none;" title="'+m.expenditureNo.attr("v")+'" align="center" id="zgGridTtable_'+rowindex+'_tr_expenditureNoText" tdindex="zgGridTtable_expenditureNoText">'+m.expenditureNo.attr("v")+'</td>' +
							'<td align="right" width="130" tag="description" style="display: none;" title="'+m.description.val()+'" align="center" id="zgGridTtable_'+rowindex+'_tr_description" tdindex="zgGridTtable_description">'+m.description.val()+'</td>' +
							'</tr>';
						$("#zgGridTtable_tbody tr:last-child").before(tr);
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
									$(this).find('td[tag=qty1]').text(m.item_input_tamount.val());
									$(this).find('td[tag=inventoryQty]').text(m.inventoryQty.val());
									$(this).find('td[tag=generalReason]').text(m.generalReason.attr("k"));
									$(this).find('td[tag=generalReasonText]').text(m.generalReason.attr("v"));
									// $(this).find('td[tag=adjustReason]').text(adjustReasonNum(m.adjustReason.attr("v")));
									$(this).find('td[tag=adjustReason]').text(m.adjustReason.attr("k"));
									$(this).find('td[tag=adjustReasonText]').text(m.adjustReason.attr("v"));
									$(this).find('td[tag=expenditureNo]').text(m.expenditureNo.attr("k"));
									$(this).find('td[tag=expenditureNoText]').text(m.expenditureNo.attr("v"));
									$(this).find('td[tag=description]').text(m.description.val());
								}
							});
						}
					}
					$('#update_dialog').modal("hide");
					add_total();
				}
			});
		});
	}

	// 获取传票头档信息
	var getInventoryVouchers = function(){
		let _data = {
			storeCd:m._storeCd.val(),
			voucherNo:m._voucherNo.val(),
			voucherType:m._voucherType.val(),
			voucherDate:m._voucherDate.val(),
			storeCd1:m._storeCd1.val()
		}
		let param = JSON.stringify(_data);
		$.myAjaxs({
			url:url_root+"/inventoryVoucher/get",
			async:true,
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
					m.dj_type.val(result.o.voucherType);
					m.dj_status.val(result.o.reviewSts);
					$.myAutomatic.setValueTemp(vstore,result.o.storeCd,result.o.storeName);
					m.adjustmentSum.val(toThousands(result.o.voucherAmtNoTax));
					m.cRemark.val(result.o.remark);
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
				}else{
					_common.prompt(result.msg,5,"error");
					let _sts = m.viewSts.val();
					if(_sts == "edit"){
						setDisable(true);
						m.viewSts.val("view");
					}
				}
			},
			error : function(e){
				_common.prompt("Failed to load data!",5,"error");
				let _sts = m.viewSts.val();
				if(_sts == "edit"){
					setDisable(true);
					m.viewSts.val("view");
				}
			}
		});
	}
//  add时候的统计
	var add_total=function () {
			if ($("#zgGridTtable_tbody> tr").length-1 > 0) {
				var sum_qty1 = 0, article_id = 0, sumItemSku = 0;
				$("#zgGridTtable_tbody>tr:not(:last-child)").each(function () {
					var td_qty1 = parseFloat($(this).find('td[tag=qty1]').text().replace(/,/g, ""));
					sum_qty1 += td_qty1;
					var td_articleId = parseFloat($(this).find('td[tag=articleId]').text().replace(/,/g, ""));
					if (td_articleId != article_id) {
						article_id = td_articleId;
						sumItemSku++;
					}
				})
				$("#totalItemQty").val(sumItemSku)
				$("#totalQty").val(sum_qty1)
				total();
			}
		};

	// 获取传票商品详情
	var getDetails = function(param){
		paramGrid = "searchJson=" + param;
		tableGrid.setting("url", url_root + "/stockAdjustment/getDetails");
		tableGrid.setting("param", paramGrid);
		tableGrid.loadData();
		total_qty(paramGrid);

	}
	var total_qty = function (paramGrid2) {
		$.myAjaxs({
			url:  url_root + "/stockAdjustment/getTotal",
			async: false,
			cache: false,
			type: "post",
			data: paramGrid2,
			dataType: "json",
			success: function (result) {
				if(result.success){
					$("#totalQty").val(result.o.totalQty);
					$("#totalItemQty").val(result.o.totalItemSKU);
				}

			},
	       error: function (result) {
			$("#totalItemQty").val("");
			$("#totalQty").val("");
		   }

		})
	};

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

	// 判断商品是否为母货号
	var checkParent = function(item, fun){
		$.myAjaxs({
			url:url_root+"/ma1200/check",
			async:true,
			cache:false,
			type :"post",
			data :"itemId="+item,
			dataType:"json",
			success: fun
		});
	}

	// 根据Store No.取得该店铺信息
	var initAutomatic = function(){
		// // $("#adjustReason").prop("disabled","true");
		// // $("#reasonRefresh").hide();
		// // $("#reasonRemove").hide();
		$("#expenditureCode").hide();
		$("#expenditureNo").hide();
		$("#expenditureNo_refresh").hide();
		$("#expenditureNo_clear").hide();
		$("#expenditureDescription").hide();
		generalReason = $("#generalReason").myAutomatic({
			url:url_root+"/inventoryVoucher/generalReason",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function() {
				$.myAutomatic.replaceParam(adjustReason,null);
				// $.myAutomatic.cleanSelectObj(adjustReason);
				// $("#adjustReason").prop("disabled",true);
				// $("#reasonRefresh").hide();
				// $("#reasonRemove").hide();
			},
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") !== null && thisObj.attr("k") !== "") {
					// $("#adjustReason").prop("disabled", false);
					// $("#reasonRefresh").show();
					// $("#reasonRemove").show();
					$.myAutomatic.cleanSelectObj(adjustReason);
					var strm ="&generalLevelCd="+ m.generalReason.attr('k');
					$.myAutomatic.replaceParam(adjustReason,strm);
				}
			},
		});

		adjustReason = $("#adjustReason").myAutomatic({
			url:url_root+"/inventoryVoucher/detailReason",
			ePageSize:10,
			startCount:0,
			cleanInput: function () {
				$("#expenditureCode").hide();
				$("#expenditureNo").hide();
				$("#expenditureNo_refresh").hide();
				$("#expenditureNo_clear").hide();
				$.myAutomatic.cleanSelectObj(expenditureNo);
			},
			selectEleClick: function (thisObj) {
				if (thisObj.attr('v') !== null && thisObj.attr('v') !== "") {

					let accDate = subfmtDate(m.tf_date.val());
					let storeCd = $("#vstore").attr("k");
					$("#expenditureCode").show();
					$("#expenditureNo").show();
					$("#expenditureNo").prop("disabled",false);
					$("#expenditureNo_refresh").show();
					$("#expenditureNo_clear").show();
					$.myAutomatic.replaceParam(expenditureNo,"&accDate="+accDate+"&storeCd="+storeCd);

					var strm ="&generalLevelCd="+ m.adjustReason.attr('k');
					getGeneralLevelCd(strm);
					// alert(adjustNum(thisObj.attr('v')))
				}
			}

		});

		// 输入框事件绑定
		vstore = $("#vstore").myAutomatic({
			url: url_root+"/ma1000/getStoreByPM",
			ePageSize: 5,
			startCount: 0,
			cleanInput: function() {
				m.adjustmentSum.val("0");
				//12 8
				//var trList = $("#zgGridTtable  tr:not(:first)");
				//trList.remove();
			},
			selectEleClick: function (thisObject) {
				m.adjustmentSum.val("0");
				//12 8
				// var trList = $("#zgGridTtable  tr:not(:first)");
				// trList.remove();
				// 替换商品下拉查询参数
				let accDate = subfmtDate(m.tf_date.val());
				let str = "&storeCd=" + thisObject.attr("k");
				$.myAutomatic.replaceParam(itemInput, str);
				$.myAutomatic.replaceParam(expenditureNo,"&accDate="+accDate+str);

			}
		});

		itemInput = $("#item_input").myAutomatic({
			url: url_root+"/inventoryVoucher/inventoryItemList",
			ePageSize: 5,
			startCount: 3,
			cleanInput: function() {
				clearDialog(true);
			},
			selectEleClick: function (thisObject) {
				clearDialog(false);
				let _storeCd = $("#vstore").attr('k');
				$.myAutomatic.setValueTemp(itemInput,thisObject.attr("k"),thisObject.text());
				let _itemId = thisObject.attr('k');
				if(!!_storeCd && !!_itemId){
					checkParent(_itemId, function(res){
						if(res.success){

							// 2021/3/26占时注解
							getItemInfoByItem(_storeCd, _itemId,function(res){
								if(res.success){
									$("#item_input_cd").val(res.o.barcode);
									$("#item_input_uom").val(res.o.uom);
									$("#item_input_spec").val(res.o.spec);
									$("#item_input_price").val(res.o.baseOrderPrice);
									$("#tax_rate").val(res.o.taxRate);
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

		// 经费管理序号下拉
		expenditureNo = $("#expenditureNo").myAutomatic({
			url: url_root + "/fundEntry/getExpenditureList",
			ePageSize: 10,
			startCount: 0,
			cleanInput: function () {
				$("#expenditureDescription").hide();
				let accDate = subfmtDate(m.tf_date.val());
				let storeCd = $("#vstore").attr("k");
				$.myAutomatic.replaceParam(expenditureNo,"&accDate="+accDate+"&storeCd="+storeCd);
			},
			selectEleClick: function (thisObj) {
				if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
					let accDate = subfmtDate(m.tf_date.val());
					let storeCd = $("#vstore").attr("k");
					getDescription(thisObj.attr("k"),storeCd,accDate);
					$("#expenditureDescription").show();
				}
			}
		});
	};
	function getGeneralLevelReason(strm) {
		$.myAjaxs({
			url:url_root+"/inventoryVoucher/getGeneralLevelReason",
			async:true,
			cache:false,
			type :"get",
			data :strm,
			dataType:"json",
			success:function (result) {
				$.myAutomatic.setValueTemp(generalReason,result.generalLevelCd,result.generalLevelReason);
			},
			error:function (e) {

			}

		})
	}
	function getGeneralLevelCd(strm) {
		$.myAjaxs({
			url:url_root+"/inventoryVoucher/getGeneralLevel",
			async:true,
			cache:false,
			type :"get",
			data :strm,
			dataType:"json",
			success:function (result) {
				$.myAutomatic.setValueTemp(generalReason,result.generalLevelCd,result.generalLevelReason);
			},
			error:function (e) {

			}

		})
	}
	var getDescription = function (expenditureNo,storeCd,accDate) {
		$.myAjaxs({
			url: url_root + "/fundEntry/getDescription",
			async: false,
			cache: false,
			type: "post",
			data: "voucherNo="+expenditureNo+"&storeCd="+storeCd+"&accDate="+accDate,
			dataType: "json",
			success: function (result) {
				if (result.success) {
					$("#description").val(result.o.description);
				}
			},
			error: function (e) {
				_common.prompt("request failed!", 5, "error");
			},
			complete: _common.myAjaxComplete
		});
	};

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
					if(result.o.message != null){
						_common.prompt(result.o.message, 5, "error");
					}
					m.inventoryQty.val(toThousands(parseInt(result.o.realtimeQty)));
				}else{
					_common.prompt(result.msg, 3, "error");
				}
			},
			error : function(e){
				_common.prompt("Query inventory quantity failed, please try again!",3,"error");
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

	// 表格初始化
	var initTable1 = function(){
		tableGrid = $("#zgGridTtable").zgGrid({
			title:"Document Details",
			param:paramGrid,
			localSort: true,
			colNames:["Item Barcode","Item Code","Item Name","UOM","Specification","Price","Rate","Quantity",
				"Inventory Qty","GeneralReason","General Reason","AdjustReason","Adjustment Reason","Expenditure No.","Expenditure Code","Description"],
			colModel:[
				{name:"barcode",type:"text",text:"right",width:"120",ishide:false,css:""},
				{name:"articleId",type:"text",text:"right",width:"110",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"uom",type:"text",text:"left",width:"100",ishide:false,css:""},
				{name:"spec",type:"text",text:"left",width:"100",ishide:false,css:""},
				{name:"priceNoTax",type:"text",text:"right",ishide:true,css:"",getCustomValue:getString0},
				{name:"taxRate",type:"text",text:"right",ishide:true,css:"",getCustomValue:getString0},
				{name:"qty1",type:"text",text:"right",width:"110",ishide:false,css:"",getCustomValue:getThousands},
				{name:"inventoryQty",type:"text",text:"right",width:"110",ishide:false,css:"",getCustomValue:getThousands},
				{name:"generalReason",type:"text",text:"left",ishide:true},
				{name:"generalReasonText",type:"text",text:"left",width:"150",ishide:true,css:""},
				{name:"adjustReason",type:"text",text:"left",ishide:true},
				{name:"adjustReasonText",type:"text",text:"left",width:"150",ishide:false,css:""},
				{name:"expenditureNo",type:"text",text:"left",width:"130",ishide:true},
				{name:"expenditureNoText",type:"text",text:"left",width:"130",ishide:true,css:""},
				{name:"description",type:"text",text:"left",width:"130",ishide:true,css:""},
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:false,//是否需要分页
			// sidx:"bm.bm_type,order.bm_code",//排序字段
			// sord:"asc",//升降序
			isCheckbox:true,
			freezeHeader:false, // 是否冻结表头
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};

				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			loadCompleteEvent: function (self) {
				total();
				isDisabledBtn();
				return self;
			},
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
				isDisabledBtn();
				let _list = tableGrid.getCheckboxTrs();
				if(_list.length<1){
					selectTrTemp = null;//清空选择的行
				}
			},
			buttonGroup:[
				{
					butType: "add",
					butId: "addItemDetails",
					butText: "Add",
					butSize: ""
				},{
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
				}
			],
		});
	};

	// 多选时查看、编辑禁用
	function isDisabledBtn(){
		let flg = m.viewSts.val();
		let _list = tableGrid.getCheckboxTrs();
		if(_list == null || _list.length > 1){
			$("#updateItemDetails").prop("disabled",true);
			$("#viewItemDetails").prop("disabled",true);
		}else{
			if(flg=="add" || flg=="edit"){
				$("#updateItemDetails").prop("disabled",false);
			}
			$("#viewItemDetails").prop("disabled",false);
		}
	}

	// 日期字段格式化格式
	var dateFmt = function(tdObj, value){
		if(value!=null&&value.trim()!=''&&value.length==8) {
			value = fmtIntDate(value);
		}
		return $(tdObj).text(fmtIntDate(value));
	}
	var total = function () {
		$("#total_qty").remove();
		$("#total_qty1").val("");
		$("#total_item").val("");
		let td_toItemQty = $("#totalItemQty").val();
		let td_toalQty = $("#totalQty").val();
		var total = "<tr style='text-align:right' id='total_qty'>" +
			"<td width='50'></td>" +
			"<td width='120'></td><td width='110'></td><td width='180'></td>" +
			"<td width='100'>Total:</td>" +
			// "<td style='text-align:left' id='total_item'> total ItemSku:"+"<span id='span_item'>"+td_toItemQty+"</span>"+"</td>" +
			"<td width='100' style='text-align:right' tag='total_item2'  id='total_item'> total Item:"+td_toItemQty+"</td>" +
			"<td width='110' style='text-align:right' tag='total_qty1' id='total_qty1'> total qty:"+td_toalQty+"</td>" +
			"<td width='110'></td><td width='150'></td>" +
			"</tr>";

		$("#zgGridTtable_tbody").append(total);
	}
	// 格式化数字类型的日期
	function fmtIntDate(date){
		var res = "";
		if(date!=null&&date.trim()!=''&&date.length==8) {
			res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		}
		return res;
	}

	// 格式化数字类型的日期
	function fmtStrToInt(strDate){
		var res = "";
		res = strDate.replace(/-/g,"");
		return res;
	}
// 获取 adjustReason第一个
	function adjustReasonNum(reason) {
          return reason.toString().split(" ")[0]
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
		var res = "";
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
	// 	getReasonSelect("Adjust Reason","adjustReason", "00235");
	// }

	self.init = init;
	return self;
});
var _start = require('start');
var _index = require('stockAdjustmentEdit');
_start.load(function (_common) {
	_index.init(_common);
});
