require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");

var _myAjax=require("myAjax");
define('transfersModEdit', function () {
    var self = {};
    var url_left = "",
		url_root = null,
	    paramGrid = null,
	    selectTrTemp = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		from_store = null,
		to_store = null,
		orgOrderId = null,
	    tempTrObjValue = {},//临时行数据存储
    	common=null;
	const KEY = 'INVENTORY_DOCUMENTS_QUERY';
    var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson:null,
		viewSts:null,
		_storeCd:null,
		_voucherNo:null,
		_voucherType:null,
		_voucherDate:null,
		_storeCd1:null,
		str:null,
		// 基础信息
		fromStore:null,
		toStore:null,
		modificationType:null,
		voucherNo:null,
		orVoucherNo:null,
		modificationDate:null,
		description:null,
		// 商品信息
		barcode:null,
		itemId:null,
		itemName:null,
		specification:null,
		uom:null,
		inQty:null,
		outQty:null,
		rrOrderQty:null,
		modifyQty:null,
		// 按钮
		saveBtn:null,
		returnsViewBut:null,
		cancel:null,
		affirm:null,
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
    	url_left = url_root + "/transferCorrection";
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
    	// 根据登录人的不同身份权限来设定画面的现实样式
		initPageBytype(m.identity.val());
    	// 表格内按钮事件
    	table_event();
    	// 设置画面内容
		setValueByType();
		//审核事件
		approval_event();
    }

	//审核事件
	var approval_event = function () {
		//点击审核按钮
		m.approvalBut.on("click",function(){
			var recordId = m.voucherNo.val();
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
				var detailType = null;
				let _type = m.modificationType.val();
				if(_type === '504'){
					detailType = "tmp_transfer_in_corr";
				}else if(_type === '505'){
					detailType = "tmp_transfer_out_corr";
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
						detailType:detailType,
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
	}

    // 表格点击事件
    var table_event = function(){
		// view
		$("#view").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",3,"info");
				return false;
			}
			clearDialogValue();
			setDialogIsDisable(true);
			// 获取值，设置显示
			let _type = m.modificationType.val();
			let cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,spec,salesUnitId,qty1,qty2,actualQty");
			$("#barcode").val(cols['barcode']);
			$("#itemId").val(cols['articleId']);
			$("#itemName").val(cols['articleName']);
			$("#specification").val(cols['spec']);
			$("#uom").val(cols['salesUnitId']);
			$("#inQty").val(cols['qty1']);
			$("#outQty").val(cols['qty2']);
			$("#modifyQty").val(cols['actualQty']);
			$('#update_dialog').attr("flg","view");
			$('#update_dialog').modal("show");
		});
		// update
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
			if(m.viewSts.val()=="view"){return false;}
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",3,"info");
				return false;
			}
			clearDialogValue();
			setDialogIsDisable(false);
			// 获取值，设置显示
			let _type = m.modificationType.val();
			let cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,spec,salesUnitId,qty1,qty2,actualQty");
			$("#barcode").val(cols['barcode']);
			$("#itemId").val(cols['articleId']);
			$("#itemName").val(cols['articleName']);
			$("#specification").val(cols['spec']);
			$("#uom").val(cols['salesUnitId']);
			$("#inQty").val(cols['qty1']);
			$("#outQty").val(cols['qty2']);
			$("#modifyQty").val(cols['actualQty']);
			$('#update_dialog').attr("flg","upt");
			$('#update_dialog').modal("show");
		});
    }

    // 根据权限类型的不同初始化不同的画面样式
    var initPageBytype = function(flgType){
    	switch(flgType) {
			case "1":
				initTable();//列表初始化
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

    // 画面按钮点击事件
    var but_event = function(){
    	// 修正类型下拉改变事件
    	m.modificationType.on("change",function(){
			let _type = m.modificationType.val();
			if(_type == '504'){
				$("#fromStore").prop("disabled", true);
				$("#toStore").prop("disabled", false);
				$("#fromStoreRefresh").hide();
				$("#fromStoreRemove").hide();
				$("#toStoreRefresh").show();
				$("#toStoreRemove").show();
			}else if(_type == '505'){
				$("#fromStore").prop("disabled", false);
				$("#toStore").prop("disabled", true);
				$("#fromStoreRefresh").show();
				$("#fromStoreRemove").show();
				$("#toStoreRefresh").hide();
				$("#toStoreRemove").hide();
			}else{
				$("#fromStore").prop("disabled", true);
				$("#toStore").prop("disabled", true);
				$("#fromStoreRefresh").hide();
				$("#fromStoreRemove").hide();
				$("#toStoreRefresh").hide();
				$("#toStoreRemove").hide();
			}
			$.myAutomatic.cleanSelectObj(to_store);
			$.myAutomatic.cleanSelectObj(from_store);
		})

		$("#modifyQty").blur(function () {
			$("#modifyQty").val(toThousands(this.value));
		});

		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#modifyQty").focus(function(){
			$("#modifyQty").val(reThousands(this.value));
		});

		// 返回一览
		m.returnsViewBut.on("click",function(){
			if($("#saveBtn").prop("disabled") == true){
				_common.updateBackFlg(KEY);
				top.location = url_root + "/inventoryVoucher";
			}else {
				_common.myConfirm("Crrent change is not submitted yet,are you sure to exit?",function(result) {
					if (result == "true") {
						_common.updateBackFlg(KEY);
						top.location = url_root + "/inventoryVoucher";
					}
				})
			}
		});

		// 保存按钮
		m.saveBtn.on("click",function(){
			let _type = m.modificationType.val();

			var checkFlg;
			// 如果修正数量皆与收货数量相同，给出提示
			$("#zgGridTable>.zgGrid-tbody tr").each(function () {
				// 金额计算
				let _qty1 = reThousands($(this).find('td[tag=qty1]').text());
				let _qty2 = reThousands($(this).find('td[tag=qty2]').text());
				// 修正数量
				let _actualQty = reThousands($(this).find('td[tag=actualQty]').text());
				switch (_type) {
					case '504':
						if(_qty1 !== _actualQty){
							checkFlg = true;
						}
						break;
					case '505':
						if(_qty2 !== _actualQty){
							checkFlg = true;
						}
						break;
				}
			});
			if(!checkFlg || checkFlg === 'undefined'){
				_common.prompt("The tranfer Qty cannot all be the same as the Corrected Transfer Qty!",5,"error");
				return false;
			}
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
			// 判断是否允许保存
			if(m.viewSts.val()=="view"){return;}
			// 数据检查
			if(verifySearch()){
				let _storeCd = '', _storeCd1 = '';
				let _type = m.modificationType.val();
				let _date = subfmtDate(m.modificationDate.val());
				if(_type == '504'){
					_storeCd = m.toStore.attr('k');
					_storeCd1 = m.fromStore.attr('k');
				}else if(_type == '505'){
					_storeCd1 = m.toStore.attr('k');
					_storeCd = m.fromStore.attr('k');
				}
				// 遍历List，处理求和
				let itemDetail = [], num = 0, _amount = 0, _amountNoTax = 0, _tax = 0;
				$("#zgGridTable>.zgGrid-tbody tr").each(function () {
					num++;
					// 原单据数量
					let _orgQty = 0;
					if(_type == '504'){
						_orgQty = reThousands($(this).find('td[tag=qty1]').text());
					}else if(_type == '505'){
						_orgQty = reThousands($(this).find('td[tag=qty2]').text());
					}
					// 修正数量
					let _actualQty = reThousands($(this).find('td[tag=actualQty]').text());
					// 未税单价
					let _priceNoTax = $(this).find('td[tag=priceNoTax]').text();
					// 税率
					let _taxRate = $(this).find('td[tag=taxRate]').text();
					// 差异数量
					let _diffQty = accSub(_orgQty, _actualQty);
					// 修正未税
					let _actualAmt = Number(accMul(_actualQty, _priceNoTax)).toFixed(2);
					// 差异未税
					let _amtNoTax = Number(accMul(_diffQty, _priceNoTax)).toFixed(2);
					// 差异含税
					let _amt = Number(accMul(_amtNoTax, accAdd(1, _taxRate))).toFixed(2);
					// 差异税额
					let _amtTax = Number(accMul(_amtNoTax, _taxRate)).toFixed(2);
					let orderItem = {
						storeCd:_storeCd,
						voucherType:_type,
						voucherDate:_date,
						storeCd1:_storeCd1,
						adjustReason:$(this).find('td[tag=adjustReason]').text(),
						displaySeq:num,
						articleId:$(this).find('td[tag=articleId]').text(),
						barcode:$(this).find('td[tag=barcode]').text(),
						salesUnitId:$(this).find('td[tag=salesUnitId]').text(),
						qty1:_diffQty,
						priceNoTax:_priceNoTax,
						amtNoTax:_amtNoTax,
						amtTax:_amtTax,
						amt:_amt,
						taxRate:_taxRate,
						actualQty:_actualQty,
						actualAmt:_actualAmt
					}
					itemDetail.push(orderItem);
					_tax = accAdd(_tax, _amtTax);
					_amount = accAdd(_amount, _amt);
					_amountNoTax = accAdd(_amountNoTax, _amtNoTax);
				});
				if(itemDetail.length < 1){
					_common.prompt("Commodity information cannot be empty!",2,"info");
					return false;
				}
				// 处理头档数据
				let bean = {
					voucherNo:m.voucherNo.val(),
					storeCd:_storeCd,
					voucherType:_type,
					voucherDate:_date,
					storeCd1:_storeCd1,
					voucherNo1:m.orVoucherNo.attr('k'),
					remark:$("#description").val(),
					voucherTax:_tax,
					voucherAmt:_amount,
					voucherAmtNoTax:_amountNoTax
				}
				let _data = {
					searchJson : JSON.stringify(bean),
					listJson : JSON.stringify(itemDetail)
				}
				_common.myConfirm("Are you sure you want to save?",function(result){
					if(result!="true"){return false;}
					$.myAjaxs({
						url:url_left+'/save',
						async:true,
						cache:false,
						type :"post",
						data :_data,
						dataType:"json",
						success:function(result){
							if(result.success){
								m.viewSts.val("edit");
								setSomeDisable(false);
								m.voucherNo.val(result.o);
								_common.prompt("Data saved successfully",3,"success",function(){
									//发起审核
									var typeId = m.typeId.val();
									var	nReviewid = m.reviewId.val();
									var	recordCd = m.voucherNo.val();
									_common.initiateAudit(_storeCd,recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
										// 变为查看模式
										setSomeDisable(true);
										m.viewSts.val("view");
										//审核按钮禁用
										m.approvalBut.prop("disabled",true);
										m.toKen.val(token);
									})
								}, true);
							}else{
								_common.prompt(result.msg,3,"error");
							}
						},
						error : function(e){
							_common.prompt("Data saved failed!",3,"error");/*保存失败*/
						}
					});
				})
			}
		});

		// 详情弹窗Close按钮
		m.cancel.on("click",function(){
			let  flg = $('#update_dialog').attr("flg");
			if(flg=="view"){
				$('#update_dialog').modal("hide");
				return false;
			}
			_common.myConfirm("Are you sure you want to close?",function(result){
				if(result == "true"){
					$('#update_dialog').modal("hide");
				}
			});
		})

		// 详情弹窗Submit按钮
		m.affirm.on("click",function(){
			if(!verifyDialogSearch()){return false;}
			if(selectTrTemp==null){
				_common.prompt("Data update failed, please try again!",2,"info");
				return false;
			}
			let _orgQty = 0, _msg = null;
			let _temp = reThousands($("#modifyQty").val());
			let _type = m.modificationType.val();
			if(_type == '504'){
				_orgQty = reThousands($("#inQty").val());
			}else if(_type == '505'){
				_orgQty = reThousands($("#outQty").val());
			}
			if(_orgQty != _temp){
				_msg = 'Transfer In & Out Quantity does not equal to each other after correction, are you sure to submit?';
			}else{
				_msg = 'Are you sure you want to submit?';
			}
			_common.myConfirm(_msg, function(result){
				if(result == "true"){
					let cols = tableGrid.getSelectColValue(selectTrTemp,"articleId");
					$("#zgGridTable>.zgGrid-tbody tr").each(function () {
						let articleId = $(this).find('td[tag=articleId]').text();
						if(articleId==cols["articleId"]){
							$(this).find('td[tag=actualQty]').text(m.modifyQty.val());
						}
					});
					$('#update_dialog').modal("hide");
				}
			});
		});

    }

    // 验证数据是否合法
    var verifySearch = function(){
		let _temp = m.modificationType.val();
		if(_temp == null || $.trim(_temp)==""){
			_common.prompt("Correction differentiation cannot be empty!",3,"error");/* 修正区分不能为空 */
			m.modificationType.focus();
			$("#modificationType").css("border-color","red");
			return false;
		}else {
			$("#modificationType").css("border-color","#CCC");
		}
		_temp = $("#modificationDate").val();
		if(_temp == null || $.trim(_temp)==""){
			_common.prompt("The modification date cannot be empty, please refresh!",3,"error");/* 修正日期不能为空 */
			$("#modificationDate").focus();
			return false;
		}
		_temp = $("#fromStore").attr("k");
		if(_temp == null || $.trim(_temp)==""){
			_common.prompt("Please select a store first!",3,"error");/* 店铺不能为空 */
			$("#fromStore").focus();
			$("#fromStore").css("border-color","red");
			return false;
		}else {
			$("#fromStore").css("border-color","#CCC");
		}
		_temp = $("#toStore").attr("k");
		if(_temp == null || $.trim(_temp)==""){
			_common.prompt("Please select a store first!",3,"error");/* 店铺不能为空 */
			$("#toStore").focus();
			$("#toStore").css("border-color","red");
			return false;
		}else {
			$("#toStore").css("border-color","#CCC");
		}
		_temp = $("#orVoucherNo").attr("k");
		if(_temp == null || $.trim(_temp)==""){
			_common.prompt("The original document No. cannot be empty!",3,"error");/* 原订单编号不能为空 */
			$("#orVoucherNo").focus();
			return false;
		}
    	return true;
    }

	// 验证弹窗页面录入项是否符合
	var verifyDialogSearch = function(){
		let _temp = reThousands($("#modifyQty").val());
		if(_temp == null || $.trim(_temp)==""){
			_common.prompt("Correct quantity cannot be empty",2,"error");
			$("#modifyQty").focus();
			return false;
		}
		let reg = /^[0-9]*$/;
		if(!reg.test(_temp)){
			_common.prompt("Correct quantity can only be a number!",2,"error");/*商品数量只能是数字*/
			$("#modifyQty").focus();
			return false;
		}
		return true;
	}

	// 根据跳转方式，设置画面可否编辑、加载内容
	var setValueByType = function(){
		// 初始化订单选择
		initAutomatic();
		let _sts = m.viewSts.val();
		let _data = {
			storeCd:m._storeCd.val(),
			voucherNo:m._voucherNo.val(),
			voucherType:m._voucherType.val(),
			voucherDate:m._voucherDate.val(),
			storeCd1:m._storeCd1.val()
		}
		if(_sts == "add"){
			// 获取业务日期
			getBusinessDate();
			//禁用审核按钮
			m.approvalBut.prop("disabled",true);
			m.approvalBut.hide();
		}else if(_sts == "edit"){
			m.viewSts.val("edit");
			getRecord(JSON.stringify(_data), true);
			setSomeDisable(false);
			//禁用审核按钮
			m.approvalBut.prop("disabled",true);
		}else {
			// 编辑功能待确认，只有查看模式
			m.viewSts.val("view");
			getRecord(JSON.stringify(_data), true);
			setSomeDisable(true);
			//检查是否允许审核
			_common.checkRole(m._voucherNo.val(),m.typeId.val(),function (success) {
				if(success){
					m.approvalBut.prop("disabled",false);
				}else{
					m.approvalBut.prop("disabled",true);
				}
			});
			// if(_sts == "edit"){
			// 	setSomeDisable(false);
			// }else if(_sts == "view"){
			// 	setSomeDisable(true);
			// }
		}
	}

	// 设置为禁用
	var setSomeDisable = function (flg) {
		$("#fromStore").prop("disabled", true);
		$("#toStore").prop("disabled", true);
		$("#modificationType").prop("disabled", true);
		$("#voucherNo").prop("disabled", true);
		$("#orVoucherNo").prop("disabled", true);
		$("#modificationDate").prop("disabled", true);
		$("#description").prop("disabled", flg);
		$("#saveBtn").prop("disabled", flg);
		$("#update").prop("disabled", flg);
		$("#fromStoreRefresh").hide();
		$("#fromStoreRemove").hide();
		$("#toStoreRefresh").hide();
		$("#toStoreRemove").hide();
		$("#orgOrderRefresh").hide();
		$("#orgOrderRemove").hide();
	}

	// 清空弹窗内容
	var clearDialogValue = function () {
		$("#barcode").val("");
		$("#itemId").val("");
		$("#itemName").val("");
		$("#specification").val("");
		$("#uom").val("");
		$("#inQty").val("");
		$("#outQty").val("");
		$("#modifyQty").val("");
	}

	// 设置弹窗是否允许编辑
	var setDialogIsDisable = function (flag) {
		$("#modifyQty").prop("disabled", flag);
		$("#affirm").prop("disabled", flag);
	}

    // 获取详情信息
	var getRecord = function(param,flg){
		$.myAjaxs({
			url:url_left+"/get",
			async:true,
			cache:false,
			type :"post",
			data :"searchJson="+param,
			dataType:"json",
			success: function(result){
				if(result.success){
					// 加载数据
					if(flg){
						m.modificationType.val(result.o.voucherType);
						m.voucherNo.val(result.o.voucherNo);
						m.modificationDate.val(fmtIntDate(result.o.voucherDate));
						m.description.val(result.o.remark);
						$.myAutomatic.setValueTemp(orgOrderId,result.o.voucherNo1,result.o.voucherNo1);
					}else{
						let _data = {
							storeCd:result.o.storeCd,
							voucherNo:result.o.voucherNo,
							voucherType:result.o.voucherType,
							voucherDate:result.o.voucherDate,
							storeCd1:result.o.storeCd1
						}
						param = JSON.stringify(_data)
					}
					if(result.o.voucherType == '501' || result.o.voucherType == '504'){
						$.myAutomatic.setValueTemp(to_store,result.o.storeCd,result.o.storeName);
						$.myAutomatic.setValueTemp(from_store,result.o.storeCd1,result.o.storeName1);
					}else{
						$.myAutomatic.setValueTemp(to_store,result.o.storeCd1,result.o.storeName1);
						$.myAutomatic.setValueTemp(from_store,result.o.storeCd,result.o.storeName);
					}
					// 获取商品详情
					getDetails(param);
				}else{
					_common.prompt(result.msg,5,"error");
				}
			},
			error : function(e){
				_common.prompt("Failed to load data!",5,"error");
			}
		});
	}

	// 获取详情List
	var getDetails = function(param){
		paramGrid = "searchJson=" + param;
		tableGrid.setting("url", url_left + "/getDetails");
		tableGrid.setting("param", paramGrid);
		tableGrid.loadData();
	}

	//表格初始化
	var initTable = function(){
		tableGrid = $("#zgGridTable").zgGrid({
			title:"Correction Details",
			param:paramGrid,
			localSort: true,
			colNames:["Item Barcode","Item Code","Item Name","Item English Name","Specification","Purchase UOM",
				"Transfer In Qty","Transfer Out Qty","Corrected Transfer Qty","Price","Rate","Reason"],
			colModel:[
				{name:"barcode",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"articleId",type:"text",text:"right",width:"80",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"200",ishide:false,css:""},
				{name:"articleNameEn",type:"text",text:"left",width:"200",ishide:false,css:""},
				{name:"spec",type:"text",text:"left",width:"100",ishide:false,css:""},
				{name:"salesUnitId",type:"text",text:"left",width:"80",ishide:false,css:""},
				{name:"qty1",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
				{name:"qty2",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
				{name:"actualQty",type:"text",text:"right",width:"130",ishide:false,css:"",getCustomValue:getThousands},
				{name:"priceNoTax",type:"text",ishide:true,getCustomValue:getString0},
				{name:"taxRate",type:"text",ishide:true,getCustomValue:getString0},
				{name:"adjustReason",type:"text",ishide:true}
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:false,//是否需要分页
			isCheckbox:false,
			freezeHeader:true,
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
					butType:"custom",
					butHtml:"<button id='view' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-list-alt'></span> View</button>"
				},
				{
					butType:"custom",
					butHtml:"<button id='update' type='button' class='btn btn-info btn-sm''><span class='glyphicon glyphicon-pencil'></span> Modify</button>"
				}
			],
		});
	}

	// 为0返回'0'
	function getString0(tdObj, value){
		var _value = value == 0 ? "0" : value;
		return $(tdObj).text(_value);
	}

	// 运算
	function accAdd(arg1,arg2){
		var r1,r2,m;
		try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
		try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
		m=Math.pow(10,Math.max(r1,r2));
		return (accMul(arg1,m)+accMul(arg2,m))/m;
	}
	function accSub(arg1,arg2){
		var r1,r2,m,n;
		try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
		try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
		m=Math.pow(10,Math.max(r1,r2));
		return accMul(arg1,m)-accMul(arg2,m);
	}
	function accMul(arg1,arg2){
		var m=0,s1=arg1.toString(),s2=arg2.toString();
		try{m+=s1.split(".")[1].length}catch(e){}
		try{m+=s2.split(".")[1].length}catch(e){}
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
	}
	function accDiv(arg1,arg2){
		var t1=0,t2=0,r1,r2;
		try{t1=arg1.toString().split(".")[1].length}catch(e){}
		try{t2=arg2.toString().split(".")[1].length}catch(e){}
		with(Math){
			r1=Number(arg1.toString().replace(".",""))
			r2=Number(arg2.toString().replace(".",""))
			return (r1/r2)*pow(10,t2-t1);
		}
	}

	// 格式化数字类型的日期
	function fmtIntDate(date){
		let res = "";
		res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
		return res;
	}

	// 日期处理
	function subfmtDate(date){
		let res = "";
		res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
		return res;
	}

	// 格式化数字类型的日期
	function fmtStrToInt(strDate){
		let res = "";
		res = strDate.replace(/-/g,"");
		return res;
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
				_common.prompt(title+" Failed to load data!",5,"error");
			}
		});
	}

	// 获取业务日期
	function getBusinessDate(){
		getSelectOptions("Modification Date","/cm9060/getDate", null,function(res){
			let date = "";
			if(!!res && res.toString().length==8){
				res = res.toString();
				date = res.substring(6,8)+"/"+res.substring(4,6)+"/"+res.substring(0,4);
			}
			$('#modificationDate').val(date);
		});
	}

	// 初始化原订单查找
	function initAutomatic(){
		// 初始化，禁用
		$("#fromStore").prop("disabled", true);
		$("#toStore").prop("disabled", true);
		$("#orVoucherNo").prop("disabled", true);
		$("#fromStoreRefresh").hide();
		$("#fromStoreRemove").hide();
		$("#toStoreRefresh").hide();
		$("#toStoreRemove").hide();
		$("#orgOrderRefresh").hide();
		$("#orgOrderRemove").hide();


		// 原订单输入框事件绑定
		orgOrderId = $("#orVoucherNo").myAutomatic({
			url: url_left + "/getOrgOrder",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				let _storeCd = '';
				let _type = m.modificationType.val();
				if(_type == '504'){
					_type = '501';
					_storeCd = $("#toStore").attr("k");
				}else if(_type == '505'){
					_type = '502';
					_storeCd = $("#fromStore").attr("k");
				}else{
					_common.prompt("Correction differentiation cannot be empty!",3,"info");
					m.modificationType.focus();
					return false;
				}
				let _data = {
					storeCd:_storeCd,
					voucherNo:thisObj.attr("k"),
					voucherType:_type
				}
				getRecord(JSON.stringify(_data),false);
			},
			cleanInput: function(){
				$("#zgGridTable>.zgGrid-tbody tr").empty();
				let _type = m.modificationType.val();
				if(_type == '504'){
					$.myAutomatic.setValueTemp(from_store,null, null);
				}else if(_type == '505'){
					$.myAutomatic.setValueTemp(to_store,null, null);
				}
			}
		});

		// 店铺输入框事件绑定
		from_store = $("#fromStore").myAutomatic({
			url: url_root + "/organizationalStructure/getAll",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				let _type = m.modificationType.val();
				let _val = thisObj.attr("k");
				// 替换查询参数
				let str = "&type=" + _type + "&storeCd=" + _val;
				$.myAutomatic.replaceParam(orgOrderId, str);
				$.myAutomatic.cleanSelectObj(orgOrderId);
				if(_type && _val){
					$("#orVoucherNo").prop("disabled", false);
					$("#orgOrderRefresh").show();
					$("#orgOrderRemove").show();
				}else{
					$("#orVoucherNo").prop("disabled", true);
					$("#orgOrderRefresh").hide();
					$("#orgOrderRemove").hide();
				}
			},
			cleanInput: function(){
				// 禁用原订单选择
				$.myAutomatic.cleanSelectObj(orgOrderId);
				$("#orVoucherNo").prop("disabled", true);
				$("#orgOrderRefresh").hide();
				$("#orgOrderRemove").hide();
			}
		});

		// 店铺输入框事件绑定
		to_store = $("#toStore").myAutomatic({
			url: url_root + "/organizationalStructure/getAll",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				let _type = m.modificationType.val();
				let _val = thisObj.attr("k");
				// 替换查询参数
				let str = "&type=" + _type + "&storeCd=" + _val;
				$.myAutomatic.replaceParam(orgOrderId, str);
				$.myAutomatic.cleanSelectObj(orgOrderId);
				if(_type && _val){
					$("#orVoucherNo").prop("disabled", false);
					$("#orgOrderRefresh").show();
					$("#orgOrderRemove").show();
				}else{
					$("#orVoucherNo").prop("disabled", true);
					$("#orgOrderRefresh").hide();
					$("#orgOrderRemove").hide();
				}
			},
			cleanInput: function(){
				// 禁用原订单选择
				$.myAutomatic.cleanSelectObj(orgOrderId);
				$("#orVoucherNo").prop("disabled", true);
				$("#orgOrderRefresh").hide();
				$("#orgOrderRemove").hide();
			}
		});
    }

    self.init = init;
    return self;
});

var _start = require('start');
var _index = require('transfersModEdit');
_start.load(function (_common) {
	_index.init(_common);
});
