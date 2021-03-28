require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");

var _myAjax=require("myAjax");
define('receiptEdit', function () {
    var self = {};
    var url_left = "",
		url_root = null,
	    paramGrid = null,
	    selectTrTemp = null,
		reThousands = null,
		toThousands = null,
		getThousands = null,
		aStore = null,
		orgOrderId = null,
	    tempTrObjValue = {},//临时行数据存储
    	common=null;
	const KEY = 'RECEIVING_AND_RETURN_DOCUMENT_CORRECTION_QUERY';
    var m = {
		toKen : null,
		use : null,
		error_pcode : null,
		identity : null,
		searchJson:null,
		viewSts:null,
		_storeCd:null,
		_orderId:null,
		// 基础信息
		storeCd:null,
		modificationType:null,
		voucherNo:null,
		orVoucherNo:null,
		modificationDate:null,
		vendorId:null,
		vendorName:null,
		description:null,
		// 商品信息
		barcode:null,
		itemId:null,
		itemName:null,
		specification:null,
		uom:null,
		orderPrice:null,
		orderQty:null,
		orderNochargeQty:null,
		rrOrderQty:null,
		receiveNochargeQty:null,
		modifyQty:null,
		modifyNochargeQty:null,
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
    	url_left = url_root + "/rrModifyQuery";
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

		initTable();//列表初始化
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
				if (result !== "true") {
					return false;
				}
				let detailType = null;
				if(m.modificationType.val() === "07"){
					detailType = "tmp_receive_corr";
				}else if(m.modificationType.val() === "08"){
					detailType = "tmp_return_corr";
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
						detailType: detailType,
						auditContent: auditContent,
						toKen: m.toKen.val()
					},
					dataType: "json",
					success: function (result) {
						if (result.success) {
							$("#approval_dialog").modal("hide");
							// 更新上次修正数量
							UpdatesLastCorr();
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

	var UpdatesLastCorr = function () {
		$.myAjaxs({
			url: url_left + "/updateLastCorr",
			async: true,
			cache: false,
			type: "post",
			data: {
				orderId:m.voucherNo.val()
			},
			dataType: "json",
			success : function (result) {
				if(result.o === 0){
					_common.prompt("Failed to update item last correction difference!", 5, "error");
				}
			},
			error: function (e) {
				_common.prompt("Failed to update item last correction difference!", 5, "error");
			},
			complete: _common.myAjaxComplete
		});
	}

    // 表格点击事件
    var table_event = function(){
    	// add
		/*$("#add").on("click", function () {
			var flg = m.viewSts.val();
			if(flg!="edit"){return false;}
			clearDialogValue();
			setDialogIsDisable(false);
			$('#update_dialog').attr("flg","add");
			$('#update_dialog').modal("show");
		});*/

		var switchOver = function (flag) {
			if(flag === '0'){
				$("#InventoryQtyByDct").show();
				$("#orderQtyById").hide();
			}else {
				$("#InventoryQtyByDct").hide();
				$("#orderQtyById").show();
			}
		};


		// view
		$("#view").on("click",function(){
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			clearDialogValue();
			setDialogIsDisable(true);
			// 获取值，设置显示
			let cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,spec,orderUnit,orderPrice,orderQty,orderNochargeQty,rrOrderQty,receiveNochargeQty,modifyQty,modifyNochargeQty,realtimeQty");
			$("#barcode").val(cols['barcode']);
			$("#itemId").val(cols['articleId']);
			$("#itemName").val(cols['articleName']);
			$("#specification").val(cols['spec']);
			$("#uom").val(cols['orderUnit']);
			$("#orderPrice").val(cols['orderPrice']);
			$("#orderQty").val(cols['orderQty']);
			$("#orderNochargeQty").val(cols['orderNochargeQty']);
			$("#rrOrderQty").val(cols['rrOrderQty']);
			$("#receiveNochargeQty").val(cols['receiveNochargeQty']);
			$("#modifyQty").val(cols['modifyQty']);
			$("#modifyNochargeQty").val(cols['modifyNochargeQty']);

			if($("#returnType").val()==='10'){
				switchOver('0');
				// getItemInfo(cols['articleId']);
			}
			if (m.modificationType.val()=="07"){
				$("#reOrderQty").hide();
				$("#rOrderQty").show();
			}
			if (m.modificationType.val()=="08"){
				$("#reOrderQty").show();
				$("#rOrderQty").hide();
			}

			$("#InventoryQty").val(cols['realtimeQty']);
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
			let flg = m.viewSts.val();
			if(flg=="view"){return false;}
			if(selectTrTemp==null){
				_common.prompt("Please select at least one row of data!",5,"error");
				return false;
			}
			clearDialogValue();
			setDialogIsDisable(false);
			// 获取值，设置显示
			let cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,spec,,orderUnit,orderPrice,orderQty,orderNochargeQty,rrOrderQty,receiveNochargeQty,modifyQty,modifyNochargeQty,realtimeQty");
			$("#barcode").val(cols['barcode']);
			$("#itemId").val(cols['articleId']);
			$("#itemName").val(cols['articleName']);
			$("#specification").val(cols['spec']);
			$("#uom").val(cols['orderUnit']);
			$("#orderPrice").val(cols['orderPrice']);
			$("#orderQty").val(cols['orderQty']);
			$("#orderNochargeQty").val(cols['orderNochargeQty']);
			$("#rrOrderQty").val(cols['rrOrderQty']);
			$("#receiveNochargeQty").val(cols['receiveNochargeQty']);
			$("#modifyQty").val(cols['modifyQty']);
			$("#modifyNochargeQty").val(cols['modifyNochargeQty']);
			if($("#returnType").val()==='10'){
				switchOver('0');
				// getItemInfo(cols['articleId']);
			}
			if (m.modificationType.val()=="07"){
				$("#reOrderQty").hide();
				$("#rOrderQty").show();
			}
			if (m.modificationType.val()=="08"){
				$("#reOrderQty").show();
				$("#rOrderQty").hide();
			}
			$("#InventoryQty").val(cols['realtimeQty']);
			$('#update_dialog').attr("flg","upt");
			$('#update_dialog').modal("show");
		});
    }

    // 画面按钮点击事件
    var but_event = function(){
    	// 修正类型下拉改变事件
    	m.modificationType.on("change",function(){
			let _type = m.modificationType.val();
			if (_type=="07"){
				tableGrid.showColumn("rrOrderQty");
				tableGrid.hideColumn("rrOrderQty1");
			}else {
				tableGrid.showColumn("rrOrderQty1");
				tableGrid.hideColumn("rrOrderQty");
			}
			let _val = $("#storeCd").attr("k");
			// 替换查询参数
			let str = "&type=" + _type + "&storeCd=" + _val;
			$.myAutomatic.replaceParam(orgOrderId, str);
			$.myAutomatic.cleanSelectObj(orgOrderId);
			if(!!_type && !!_val){
				$("#orVoucherNo").attr("disabled", false);
				$("#orgOrderRefresh").attr("disabled",false).css("pointer-events","auto");
				$("#orgOrderRefresh").show();
				$("#orgOrderRemove").show();
			}else{
				$("#orVoucherNo").attr("disabled", true);
				$("#orgOrderRefresh").attr("disabled",true).css("pointer-events","none");
				$("#orgOrderRefresh").hide();
				$("#orgOrderRemove").hide();
			}
		})

		$("#modifyQty").blur(function () {
			$("#modifyQty").val(toThousands(this.value));
		});

		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#modifyQty").focus(function(){
			$("#modifyQty").val(reThousands(this.value));
		});

		$("#modifyNochargeQty").blur(function () {
			$("#modifyNochargeQty").val(toThousands(this.value));
		});

		//光标进入，去除金额千分位，并去除小数后面多余的0
		$("#modifyNochargeQty").focus(function(){
			$("#modifyNochargeQty").val(reThousands(this.value));
		});

		// 返回一览
		// m.returnsViewBut.on("click",function(){
		// 		// 	top.location = url_left;
		// 		// });
		m.returnsViewBut.on("click",function(){
			var bank = $("#saveBtn").attr("disabled");
			if (bank!='disabled' ) {
				_common.myConfirm("Current change is not submitted yet，are you sure to exit?",function(result) {
					$("#modificationType").css("border-color","#CCC");
					$("#storeCd").css("border-color","#CCC");
					if (result==="true") {
						_common.updateBackFlg(KEY);
						top.location = url_left;
					}});
			}
			if (bank==='disabled' ) {
				_common.updateBackFlg(KEY);
				top.location = url_left;
			}
		});
		// 保存按钮
		m.saveBtn.on("click",function(){
			var checkFlg;
			// 如果修正数量皆与收货数量相同，给出提示
			$("#zgGridTable>.zgGrid-tbody tr").each(function () {
				// 金额计算
				let _qty = reThousands($(this).find('td[tag=modifyQty]').text());
				let receiveQty = reThousands($(this).find('td[tag=rrOrderQty]').text());
				if(_qty !== receiveQty){
					checkFlg = true;
				}
			});
			if(!checkFlg || checkFlg === 'undefined'){
				_common.prompt("The Modification Qty cannot all be the same as the Receipt/Return Qty!",5,"error");
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
			let _sts = m.viewSts.val();
			if(_sts=="view"){return;}
			// 数据检查
			if(verifySearch()){
				// 店铺编号
				let _storeCd = $("#orVoucherNo").attr("k");
				let _vendorId = $("#vendorId").val();
				// 遍历List，处理求和
				let itemDetail = [], num = 0, _amount = 0;
				$("#zgGridTable>.zgGrid-tbody tr").each(function () {
					num++;
					// 金额计算
					let _qty = reThousands($(this).find('td[tag=modifyQty]').text());
					let _price = reThousands($(this).find('td[tag=orderPrice]').text());
					let _amt = Number(accMul(_qty, _price)).toFixed(2);
					let orderItem = {
						storeCd:_storeCd,
						vendorId:_vendorId,
						serialNo:num,
						articleId:$(this).find('td[tag=articleId]').text(),
						barcode:$(this).find('td[tag=barcode]').text(),
						orderUnit:$(this).find('td[tag=orderUnit]').text(),
						orderQty:reThousands($(this).find('td[tag=orderQty]').text()),
						orderNochargeQty:reThousands($(this).find('td[tag=orderNochargeQty]').text()),
						orderAmt:reThousands($(this).find('td[tag=orderAmt]').text()),
						orderPrice:_price,
						receivePrice:reThousands($(this).find('td[tag=rrOrderPrice]').text()),
						receiveQty:reThousands($(this).find('td[tag=rrOrderQty]').text()),
						receiveTotalAmt:reThousands($(this).find('td[tag=rrOrderAmt]').text()),
						correctionDifference:_qty,
						correctionNochargeQty:reThousands($(this).find('td[tag=modifyNochargeQty]').text()),
						isFreeItem: $(this).find('td[tag=isFreeItem]').text()
					};
					itemDetail.push(orderItem);
					_amount = accAdd(_amount, _amt);
				});
				if(itemDetail.length < 1){
					_common.prompt("Commodity information cannot be empty!",2,"info");
					return false;
				}
				// 处理头档数据
				let bean = {
					storeCd:_storeCd,
					vendorId:_vendorId,
					orderId:m.voucherNo.val(),
					orgOrderId:$("#orVoucherNo").attr("v"),
					orderType:m.modificationType.val(),
					orderRemark:$("#description").val(),
					orderDate:subfmtDate(m.modificationDate.val()),
					orderAmt:_amount,
					returnType:$("#returnType").val()
				};
				let _data = {
					searchJson : JSON.stringify(bean),
					listJson : JSON.stringify(itemDetail),
					toKen: m.toKen.val(),
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
								m.toKen.val(result.toKen);
								m.viewSts.val("edit");
								setUodateDisable();
								m.voucherNo.val(result.o);
								_common.prompt("Data saved successfully",3,"success",function(){
									//发起审核
									var typeId =m.typeId.val();
									var	nReviewid =m.reviewId.val();
									var	recordCd = m.voucherNo.val();
									_common.initiateAudit(_storeCd,recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
										// 变为查看模式
										m.viewSts.val("view");
										setDisable();
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
							_common.prompt("Data saved failed！",5,"error");/*保存失败*/
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
		//12 18
		// $("#orVoucherNo").on("focus",function () {
		// 	if($("#orVoucherNo").val().trim().length>=5){
		// 		$("#orgOrderRefresh").show();
		// 		$("#orgOrderRemove").show();
		// 	}else {
		// 		$("#orgOrderRefresh").hide();
		// 		$("#orgOrderRemove").hide();
		// 	}
		// });
		// 详情弹窗Submit按钮
		m.affirm.on("click",function(){
			if(!verifyDialogSearch()){return false;}
			if(selectTrTemp==null){
				_common.prompt("Data update failed, please try again!",2,"info");
				return false;
			}
			_common.myConfirm("Are you sure you want to submit?",function(result){
				if(result == "true"){
					let cols = tableGrid.getSelectColValue(selectTrTemp,"articleId");
					$("#zgGridTable>.zgGrid-tbody tr").each(function () {
						let articleId = $(this).find('td[tag=articleId]').text();
						if(articleId==cols["articleId"]){
							$(this).find('td[tag=modifyQty]').text(m.modifyQty.val());
							$(this).find('td[tag=modifyNochargeQty]').text(m.modifyNochargeQty.val());
						}
					});
					$('#update_dialog').modal("hide");
				}
			});
		});

    }

	var getItemInfo = function (articleId) {
		$.myAjaxs({
			url: url_left + "/getItemInfo",
			async: true,
			cache: false,
			type: "get",
			data: {
				returnType: $("#returnType").val(),
				articleId: articleId,
				storeCd: m.storeCd.attr("k"),
			},
			dataType: "json",
			success: function (result) {
				if (result.success) {
					var data = result.data;
					$("#InventoryQty").val(data.onHandQty);
				} else {
					$("#InventoryQty").val('0');
				}
			},
			error: function (e) {
				_common.prompt("Failed to load item data!", 5, "error"); // 商品数据加载失败
				itemClear();
			},
			complete: _common.myAjaxComplete
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
		_temp = $("#storeCd").attr("k");
		if(_temp == null || $.trim(_temp)==""){
			_common.prompt("Please select a store first!",3,"error");/* 店铺不能为空 */
			$("#storeCd").focus();
			$("#storeCd").css("border-color","red");
			return false;
		}else {
			$("#storeCd").css("border-color","#CCC");
		}
		_temp = $("#modificationDate").val();
		if(_temp == null || $.trim(_temp)==""){
			_common.prompt("The modification date cannot be empty, please refresh!",3,"error");/* 修正日期不能为空 */
			$("#modificationDate").focus();
			return false;
		}
		_temp = $("#orVoucherNo").attr("v");
		if(_temp == null || $.trim(_temp)==""){
			_common.prompt("The original document No. cannot be empty!",3,"error");/* 原订单编号不能为空 */
			$("#orVoucherNo").focus();
			return false;
		}
    	return true;
    }

	// 验证弹窗页面录入项是否符合
	var verifyDialogSearch = function(){
		let reg = /^[0-9]*$/;
		let _temp = reThousands($("#modifyQty").val());
		if(_temp == null || $.trim(_temp)===""){
			_common.prompt("Modification Quantity cannot be empty",2,"Info");
			$("#modifyQty").css("border-color","red");
			$("#modifyQty").focus();
			return false;
		}else{
			$("#modifyQty").css("border-color","#CCCCCC");
		}
		if(!reg.test(_temp)){
			_common.prompt("Modification Quantity can only be a number!",2,"Info");/*商品数量只能是数字*/
			$("#modifyQty").css("border-color","red");
			$("#modifyQty").focus();
			return false;
		}else{
			$("#modifyQty").css("border-color","#CCCCCC");
		}
		// if($("#returnType").val() === '20'){
		// 	var tmp = _temp - reThousands($("#rrOrderQty").val());
		// 	if(tmp>parseInt(reThousands($("#InventoryQty").val()))){//修正差异数量不得大于库存数量
		// 		_common.prompt("Modification Quantity can not be greater than Inventory Quantity!",2,"Info");
		// 		$("#modifyQty").css("border-color","red");
		// 		$("#modifyQty").focus();
		// 		return false;
		// 	} else {
		// 		$("#modifyQty").css("border-color","#CCCCCC");
		// 	}
		// }else {
			if(_temp>9999999999){//不得大于最高数量
				_common.prompt("Modification Quantity can not be greater than 9,999,999,999!",5,"Info");
				$("#modifyQty").css("border-color","red");
				$("#modifyQty").focus();
				return false;
			} else {
				$("#modifyQty").css("border-color","#CCCCCC");
			}
		// }

		return true;
	}

	// 根据跳转方式，设置画面可否编辑、加载内容
	var setValueByType = function(){
		let _sts = m.viewSts.val();
		if(_sts == "add"){
			// 获取业务日期
			getBusinessDate();
			// 初始化订单选择
			initAutomatic();
			//禁用审核按钮
			m.approvalBut.prop("disabled",true);
			m.approvalBut.hide();
		}else if(_sts == "edit"){
			// 查看模式,加载数据
			let _data = {
				storeCd:m._storeCd.val(),
				orderId:m._orderId.val(),
				orderType:'0'
			}
			getRecord(JSON.stringify(_data), true);
			setUodateDisable();
			//禁用审核按钮
			m.approvalBut.prop("disabled",true);
		}else if(_sts == "view"){
			// 查看模式,加载数据
			let _data = {
				storeCd:m._storeCd.val(),
				orderId:m._orderId.val(),
				orderType:'0'
			}
			getRecord(JSON.stringify(_data), true);
			setDisable();
			//检查是否允许审核
			_common.checkRole(m._orderId.val(),m.typeId.val(),function (success) {
				if(success){
					m.approvalBut.prop("disabled",false);
				}else{
					m.approvalBut.prop("disabled",true);
				}
			});
		}
	}

	// 设置为查看模式
	var setDisable = function () {
		$("#storeCd").prop("disabled", true);
		$("#modificationType").prop("disabled", true);
		$("#voucherNo").prop("disabled", true);
		$("#orVoucherNo").prop("disabled", true);
		$("#modificationDate").prop("disabled", true);
		$("#vendorId").prop("disabled", true);
		$("#vendorName").prop("disabled", true);
		$("#description").prop("disabled", true);
		$("#saveBtn").prop("disabled", true);
		$("#update").prop("disabled", true);
		$("#storeCdRefresh").hide();
		$("#storeCdRemove").hide();
		$("#orgOrderRefresh").hide();
		$("#orgOrderRemove").hide();
	}

	// 修改  禁用
	var setUodateDisable = function () {
		$("#modificationType").prop("disabled", true);
		$("#storeCd").prop("disabled", true);
		$("#voucherNo").prop("disabled", true);
		$("#orVoucherNo").prop("disabled", true);
		$("#storeCdRefresh").hide();
		$("#storeCdRemove").hide();
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
		$("#orderPrice").val("");
		$("#orderQty").val("");
		$("#rrOrderQty").val("");
		$("#receiveNochargeQty").val("");
		$("#modifyQty").val("");
		$("#modifyNochargeQty").val("");
	}

	// 设置弹窗是否允许编辑
	var setDialogIsDisable = function (flag) {
		$("#modifyQty").prop("disabled", flag);
		$("#modifyNochargeQty").prop("disabled", flag);
		$("#orderQty").prop("disabled", flag);
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
					// 获取商品详情
					getDetails(param);
					// 加载数据
					if(flg){
						m.storeCd.val(result.o.storeName).attr("k",result.o.storeCd);
						m.modificationType.val(result.o.orderType);
						m.voucherNo.val(result.o.orderId);
						m.orVoucherNo.val(result.o.orgOrderId).attr("k",result.o.storeCd).attr("v",result.o.orgOrderId);
						m.modificationDate.val(fmtIntDate(result.o.orderDate));
						m.description.val(result.o.orderRemark);
					}
					$("#returnType").val(result.o.returnType);
					m.vendorId.val(result.o.vendorId);
					m.vendorName.val(result.o.vendorName);
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
			title:"Modification Details(Only the item quantity included in existing receiving & return document are modifiable)",
			param:paramGrid,
			localSort: true,
			colNames:["Item Barcode","Item Code","Item Name","Item English Name","Item Type","Item Type","Specification","UOM","Inventory Query","Amount",
				"Price","Order Qty","Order Free Qty","Amount","Price","Receipt Qty ","Return Qty","Received/Returned Free Qty","Correct Qty","Modified Free Qty"],
			colModel:[
				{name:"barcode",type:"text",text:"right",width:"100",ishide:false,css:""},
				{name:"articleId",type:"text",text:"right",width:"80",ishide:false,css:""},
				{name:"articleName",type:"text",text:"left",width:"200",ishide:false,css:""},
				{name:"articleNameEn",type:"text",text:"left",width:"180",ishide:false,css:""},
				{name:"isFreeItem",type:"text",text:"left",ishide:true},
				{name:"isFreeItemText",type:"text",text:"left",width:"100",ishide:false,css:"",getCustomValue:getItemType},
				{name:"spec",type:"text",text:"left",width:"100",ishide:false,css:""},
				{name:"orderUnit",type:"text",text:"left",width:"70",ishide:false,css:""},
				{name:"realtimeQty",type:"text",text:"left",width:"100",ishide:false,css:"",getCustomValue:getThousands},
				{name:"orderAmt",type:"text",ishide:true,getCustomValue:getString0},
				{name:"orderPrice",type:"text",ishide:true,getCustomValue:getString0},
				{name:"orderQty",type:"text",text:"right",width:"90",ishide:false,css:"",getCustomValue:getThousands},
				{name:"orderNochargeQty",type:"text",text:"right",width:"120",ishide:true,css:"",getCustomValue:getThousands},
				{name:"rrOrderAmt",type:"text",ishide:true,getCustomValue:getString0},
				{name:"rrOrderPrice",type:"text",ishide:true,getCustomValue:getString0},
				{name:"rrOrderQty",type:"text",text:"right",width:"120",ishide:false,css:"",getCustomValue:getThousands},
				{name:"rrOrderQty1",type:"text",text:"right",width:"120",ishide:false,css:"",getCustomValue:getThousands},
				{name:"receiveNochargeQty",type:"text",text:"right",width:"180",ishide:true,css:"",getCustomValue:getThousands},
				{name:"modifyQty",type:"text",text:"right",width:"110",ishide:false,getCustomValue:getThousands},
				{name:"modifyNochargeQty",type:"text",text:"right",width:"110",ishide:true,getCustomValue:getThousands}
			],//列内容
			width:"max",//宽度自动
			page:1,//当前页
			rowPerPage:10,//每页数据量
			isPage:true,//是否需要分页
			// sidx:"bm.bm_type,Plan.bm_code",//排序字段
			// sord:"asc",//升降序
			isCheckbox:false,
			loadEachBeforeEvent:function(trObj){
				tempTrObjValue={};
				return trObj;
			},
			ajaxSuccess:function(resData){
				return resData;
			},
			footerrow:true,
			loadCompleteEvent:function(self){
				if (!!m.modificationType.val()) {
					if (m.modificationType.val() == "07") {
						tableGrid.showColumn("isFreeItemText");
					} else {
						tableGrid.hideColumn("isFreeItemText");
					}
				}
				if (m.modificationType.val() == "07") {
					tableGrid.showColumn("rrOrderQty");
					tableGrid.hideColumn("rrOrderQty1");
				} else {
					tableGrid.showColumn("rrOrderQty1");
					tableGrid.hideColumn("rrOrderQty");
				}
				/* 统计功能 */
				var sum_BuilingdArea = $("#zgGridTable").getCol('orderPrice', false, 'sum');
				var sum_TotalRoom = $("#zgGridTable").getCol('orderQty', false, 'sum');
				$("#zgGridTable").footerData('set', {需要汇总的列名: sum_BuilingdArea, 需要汇总的列名: sum_TotalRoom });

			},
			userDataOnFooter: true,
			eachTrClick:function(trObj,tdObj){//正常左侧点击
				selectTrTemp = trObj;
			},
			buttonGroup:[
				/*{
					butType:"custom",
					butHtml:"<button id='add' type='button' class='btn btn-info btn-sm'><span class='glyphicon glyphicon-plus'></span> Add</button>"
				},*/
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
	function getItemType(tdObj, value) {

		let temp =""+ value;
		switch (temp) {
			case "0":
				value = "Normal Item";
				break;
			case "1":
				value = "Free Item";
				break;
			default:
				value = "";
		}
		return $(tdObj).text(toThousands(value));
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
		$("#orVoucherNo").attr("disabled", true);
		$("#orgOrderRefresh").attr("disabled",true).css("pointer-events","none");
		$("#orgOrderRefresh").hide();
		$("#orgOrderRemove").hide();

         // 原订单输入框事件绑定
		orgOrderId = $("#orVoucherNo").myAutomatic({
			url: url_left + "/getOrgOrder",
			ePageSize: 10,
			startCount: 0,
			selectEleClick: function (thisObj) {
				let _data = {
					storeCd:$("#orVoucherNo").attr("k"),
					orderId:$("#orVoucherNo").attr("v"),
					orderType:m.modificationType.val()
				}
				getRecord(JSON.stringify(_data),false);
			},
			cleanInput: function(){
				$("#zgGridTable>.zgGrid-tbody tr").empty();
			}
		});

		// 店铺输入框事件绑定
		aStore = $("#storeCd").myAutomatic({
			url: url_root + "/organizationalStructure/getAll",
			ePageSize: 8,
			startCount: 0,
			selectEleClick: function (thisObj) {
				let _type = m.modificationType.val();
				let _val = thisObj.attr("k");
				// 替换查询参数
				let str = "&type=" + _type + "&storeCd=" + _val;
				$.myAutomatic.replaceParam(orgOrderId, str);
				$.myAutomatic.cleanSelectObj(orgOrderId);
				if(!!_type && !!_val){
					$("#orVoucherNo").attr("disabled", false);
					$("#orgOrderRefresh").attr("disabled",false).css("pointer-events","auto");
					$("#orgOrderRefresh").show();
					$("#orgOrderRemove").show();
				}else{
					$("#orVoucherNo").attr("disabled", true);
					$("#orgOrderRefresh").attr("disabled",true).css("pointer-events","none");
					$("#orgOrderRefresh").hide();
					$("#orgOrderRemove").hide();
				}
			},
			cleanInput: function(){
				// 禁用原订单选择
				$.myAutomatic.cleanSelectObj(orgOrderId);
				$("#orVoucherNo").attr("disabled", true);
				$("#orgOrderRefresh").attr("disabled",true).css("pointer-events","none");
				$("#orgOrderRefresh").hide();
				$("#orgOrderRemove").hide();
			}
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
