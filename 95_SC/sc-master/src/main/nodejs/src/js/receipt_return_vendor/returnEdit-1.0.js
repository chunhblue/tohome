require("zgGrid.css");
require("myAutoComplete.css");
require("bootstrap-datetimepicker.css");
require("zgGrid");
require("myAutomatic");

var _datetimepicker = require("datetimepicker");
var query_list = null;
var _myAjax=require("myAjax");
define('receiptReturnVendorEdit', function () {
    var self = {};
    var url_left = "",
        systemPath = "",
        paramGrid = null,
        selectTrTemp = null,
        a_store = null,
        a_item = null,
        reThousands = null,
        toThousands = null,
        getThousands = null,
        tempTrObjValue = {},//临时行数据存储
        tempTrBarcode = null,// 选中的商品条码
        tempTrItemCode = null,//选中的商品编号
        tempTrItemName = null,//选中的商品名称
        tempTrUnitId = null,//选中的商品单位id
        tempTrUom = null,//选中的商品单位
        tempTrSpec = null,//选中的商品规格
        tempTrPrice = null,//选中的商品单价
        tempTrItemAmt = null,//选中的商品退货总金额
        tempTrReasonId = null,//选中的退货原因
        tempTrReasonName = null,//选中的退货原因
        tempTrOrderQty = null,//选中的退货量
        tempTrReceiveQty = null,//实际退货数量
        tempTrOrderNochargeQty = null,//退货搭赠数量
        tempTrReceiveNochargeQty = null,//实际退货搭赠数量
        common=null;
    const KEY = 'ITEM_RETURN_ENTRY_TO_SUPPLIER';
    var m = {
        toKen : null,
        searchJson:null,
        // 基本信息
        order_id:null,//订单号
        org_order_id:null,//原订单号
        order_date:null,//退货时间
        order_type:null,
        order_sts:null,
        dialog_flg:null,//弹出框状态
        delivery_center_id:null,
        delivery_center_name:null,
        store_cd:null,
        store_name:null,
        update_ymd:null,
        total_qty:null,
        total_amt:null,
        order_remark:null,
        search:null,
        returnsViewBut:null,
        returnsSubmitBut:null,

        userId:null,
        flag:null,
        orderId:null,
        //后面追加
        update_user:null,
        create_user:null,
        create_date:null,

        //编辑
        update_dialog:null,
        barcode:null,
        itemCode:null,
        itemName:null,
        unitId:null,
        unitName:null,
        spec:null,
        vendor_id:null,
        vendor_name:null,
        vendorId:null,
        orderQty:null,
        orderNoChargeQty:null,
        receiveNoChargeQty:null,
        orgOrderQty:null,
        orgOrderNochargeQty:null,
        orderAmount:null,
        orderPrice:null,
        reasonId:null,
		    receiveQty:null,//实际退货数量
        //弹窗按钮
        close:null,
        submit:null,

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
        url_left=_common.config.surl+"/returnVendor";
        systemPath = _common.config.surl;
        reThousands=_common.reThousands;
        toThousands=_common.toThousands;
        getThousands=_common.getThousands;
        //画面按钮点击事件
        but_event();
        //原单据编号 下拉
        initAutoMatic();
        //列表初始化
        initTable1();
        //加载可选退货理由
        getSelectValue();

		//加载页面
		initPageByType();
		//表格内按钮事件
		table_event();
		//审核事件
		approval_event();
	}

	//审核事件
	var approval_event = function () {
		//点击审核按钮
		m.approvalBut.on("click",function(){
			var recordId = m.orderId.val();
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
                var detailType = "tmp_return";
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

    //初始化下拉
    var initAutoMatic = function () {
        a_orderId = $("#org_order_id").myAutomatic({
            url: url_left + "/getOrgOrderIdList",
            ePageSize: 10,
            startCount: 0,
            cleanInput: function (thisObj) {
                $(".zgGrid-tbody").empty();
            },
            selectEleClick: function (thisObj) {
                $(".zgGrid-tbody").empty();
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    $.myAjaxs({
                        url:url_left+"/getOrderInfo",
                        async:true,
                        cache:false,
                        data : {
                            orgOrderId:thisObj.attr("k"),
                            returnDiff:"0"
                        },
                        type :"get",
                        dataType:"json",
                        success:function(result){
                            if(result.success){
                                var data = result.data;
                                m.store_cd.val(data.storeCd);
                                m.store_name.val(data.storeCd+' '+data.storeName);
                                m.vendor_id.val(data.vendorId);
                                m.vendor_name.val(data.vendorId+' '+data.vendorName);
                            }else{
                                m.store_cd.val('');
                                m.store_name.val('');
                                m.vendor_id.val('');
                                m.vendor_name.val('');
                                _common.prompt(result.message,3,"error");
                            }
                        },
                        complete:_common.myAjaxComplete
                    });
                }
            }
        });
        var str = "&orderDifferentiate=1";
        $.myAutomatic.replaceParam(a_orderId, str);

        //商品
        a_item = $("#a_item").myAutomatic({
            url: url_left + "/getItems",
            ePageSize: 10,
            startCount: 0,
            cleanInput: function (thisObj) {
                itemClear();
            },
            selectEleClick: function (thisObj) {
                if (thisObj.attr("k") != null && thisObj.attr("k") != "") {
                    if($("#org_order_id").attr("k")!=null&&$("#org_order_id").attr("k")!=''){
                        getItemInfo(thisObj.attr("k"),$("#org_order_id").attr("k"));
                    }else{
                        _common.prompt("Source Document No. cannot be empty!",3,"error");
                        itemClear();
                    }
                }else{
                    itemClear();
                }
            }
        });
    }

    var getItemInfo = function(articleId,orgOrderId) {
        $.myAjaxs({
            url:url_left+"/getItemInfo",
            async:true,
            cache:false,
            type :"get",
            data :{
                orderId:orgOrderId,
                orderDate:subfmtDate(m.order_date.val()),
                articleId:articleId
            },
            dataType:"json",
            success:function(result){
                if(result.success){
                    var data = result.data;
                    m.barcode.val(data.barcode);
                    m.itemName.val(data.articleName);
                    m.unitName.val(data.unitName);
                    m.unitId.val(data.unitId);
                    m.spec.val(data.spec);
                    m.orderPrice.val(data.orderPrice);
                    m.orgOrderQty.val(data.orderQty);
                    m.orgOrderNochargeQty.val(data.orderNoChargeQty);
                    m.vendorId.val(data.vendorId);
                    m.receiveNoChargeQty.val(data.receiveNoChargeQty);
                    m.orderNoChargeQty.val(data.orderNoChargeQty);
                }else{
                    _common.prompt(result.message,3,"error");
                    itemClear();
                }
            },
            error : function(e){
                _common.prompt("Failed to load item data!",5,"error"); // 商品数据加载失败
                itemClear();
            },
            complete:_common.myAjaxComplete
        });
    }

    var initOrderData = function () {
        //设置订单号
        m.order_id.val(m.orderId.val());
        //Ajax请求头档事件
        $.myAjaxs({
            url:url_left+"/getOrderDetails",
            async:true,
            cache:false,
            type :"get",
            data : {
                orderId:m.orderId.val()
            },
            dataType:"json",
            success:function(result){
                if(result.success){
                    $.myAutomatic.setValueTemp(a_orderId,result.data.orgOrderId,result.data.orgOrderId);
                    m.order_date.val(fmtStringDate(result.data.orderDate));
                    m.store_cd.val(result.data.storeCd);
                    if(result.data.storeName == null){
                        result.data.storeName = "";
                    }
                    m.store_name.val(result.data.storeCd+' '+result.data.storeName);
                    m.vendor_id.val(result.data.vendorId);
                    m.vendor_name.val(result.data.vendorId+' '+result.data.vendorName);
                    m.order_remark.val(result.data.orderRemark);
					m.create_date.val(_common.formatCreateDate(result.data.createDate));
					m.update_ymd.val(_common.formatCreateDate(result.data.updateDate));
                    m.create_user.val(result.data.createBy);
                    m.update_user.val(result.data.modifiedBy);
                    //加载商品信息
                    paramGrid = "orderId=" + m.orderId.val() + "&flag=" + m.flag.val();
                    tableGrid.setting("url", url_left + "/getItemDetailByOrderId");
                    tableGrid.setting("param", paramGrid);
                    tableGrid.loadData(null);
                }else{
                    _common.prompt(result.message,5,"error"); // 退货单数据加载失败
                }
            },
            complete:_common.myAjaxComplete
        });
    }


	var initPageByType = function () {
		//判断View或Modify
		if(m.flag.val() == '1'||m.flag.val() == '2'){//Modify
			disableNotModify();
			initOrderData();
			//禁用审核按钮
			m.approvalBut.prop("disabled",true);
		}
		if(m.flag.val() == '0'){//View
			disableAll();
			initOrderData();
			//检查是否允许审核
			_common.checkRole(m.orderId.val(),m.typeId.val(),function (success) {
				if(success){
					m.approvalBut.prop("disabled",false);
				}else{
					m.approvalBut.prop("disabled",true);
				}
			});
		}
	}

    //禁用不可修改栏位
    var disableNotModify = function () {
        m.order_id.prop('disabled', true);
        m.org_order_id.prop('disabled', true);
        m.order_date.prop('disabled', true);
        m.total_qty.prop('disabled', true);
        m.total_amt.prop('disabled', true);
        m.store_cd.prop('disabled', true);
        m.store_name.prop('disabled', true);
        m.vendor_id.prop('disabled', true);
        m.vendor_name.prop('disabled', true);
        m.update_ymd.prop('disabled', true);
        //后面追加
        m.update_user.prop('disabled', true);
        m.create_user.prop('disabled', true);
        m.create_date.prop('disabled', true);
        m.returnsSubmitBut.prop('disabled', false);
        $("#modify").prop('disabled', false);
        $("#org_order_id_refresh").hide();
        $("#org_order_id_clear").hide();
    }

    //禁用弹窗不可修改栏位
    var disableDialog = function () {
        m.barcode.prop('disabled', true);
        m.itemCode.prop('disabled', true);
        m.unitName.prop('disabled', true);
        m.spec.prop('disabled', true);
        m.orderPrice.prop('disabled', true);
        m.orderAmount.prop('disabled', true);
        m.orderQty.prop('disabled', true);
        m.reasonId.prop('disabled',true);
        $("#a_item").prop('disabled',true);
        $("#item_refresh").hide();
        $("#item_clear").hide();
    }

    //View时禁用所有栏位
    var disableAll = function () {
        m.order_id.prop('disabled', true);
        m.org_order_id.prop('disabled', true);
        m.order_date.prop('disabled', true);
        m.total_qty.prop('disabled', true);
        m.total_amt.prop('disabled', true);
        m.store_cd.prop('disabled', true);
        m.store_name.prop('disabled', true);
        m.vendor_id.prop('disabled', true);
        m.vendor_name.prop('disabled', true);
        m.update_ymd.prop('disabled', true);
        //后面追加
        m.update_user.prop('disabled', true);
        m.create_user.prop('disabled', true);
        m.create_date.prop('disabled', true);
        m.order_remark.prop('disabled', true);
        m.returnsSubmitBut.prop('disabled', true);
        $("#modify").prop('disabled', true);
        $("#org_order_id_refresh").hide();
        $("#org_order_id_clear").hide();
    }

    //表格按钮事件
    var table_event = function(){
        //禁用商品信息框
        disableDialog();
        $("#modify").on("click", function () {
            var str = "&orderId=" + $("#org_order_id").attr("k")+"&orderDifferentiate=1&orderDate="+subfmtDate(m.order_date.val());
            $.myAutomatic.replaceParam(a_item, str);

            if(selectTrTemp==null){
                _common.prompt("Please select at least one row of data!",5,"error");
                return false;
            }

            var storeCd = m.store_cd.val();
            if(storeCd == null || storeCd == ""){
                _common.prompt("Please enter the correct Source Document No.!",3,"info");
                m.org_order_id.focus();
                return false;
            }
            itemClear();
            //弹出框赋值
            dialogSetData();
            m.dialog_flg.val("update");
            m.update_dialog.modal("show");
        });
        //返回一览
        m.returnsViewBut.on("click",function(){
    			var bank = $("#returnsSubmitBut").prop("disabled");
    			//var black = $("#approvalBut").attr("disabled");
    			if (!bank) {
    				_common.myConfirm("Current change is not submitted yet，are you sure to exit?",function(result) {
    					if (result==="true") {
    						_common.updateBackFlg(KEY);
    						top.location = url_left+"/receipt";
    					}});
    			}else{
    				_common.updateBackFlg(KEY);
    				top.location = url_left+"/receipt";
    			}
        });

        m.submit.on("click", function () {
            var articleId = $("#a_item").attr("k");
            if(articleId==null||articleId==""){
                _common.prompt("Please select an item!",5,"info"); // 请选择商品
                $("#a_item").focus();
                $("#a_item").css("border-color","red");
                return false;
            }else {
                $("#a_item").css("border-color","#CCC");
            }
            var reg = /((^[1-9]\d*)|^0)(\.\d+)?$/;
            var receiveQty = reThousands(m.receiveQty.val().trim());//退货数量
            if(!reg.test(receiveQty)){
            	_common.prompt("Please enter with correct data type!",3,"info");
            	m.receiveQty.focus();
                m.receiveQty.css("border-color","red");
            	return false;
            }else {
                m.receiveQty.css("border-color","#CCC");
            }
            var _receiveQty = parseFloat(receiveQty);
            // if(_receiveQty==0){//退货量不为空
            // 	_common.prompt("Returning Quantity can not be empty!",3,"info");
            // 	m.receiveQty.focus();
            // 	return false;
            // }

            var returnQty = parseFloat(m.orderQty.val());
            if(_receiveQty>returnQty) {//不得高于退货量
            	_common.prompt("Returning Quantity not be greater than Return Quantity!",3,"info");
            	m.receiveQty.focus();
                m.receiveQty.css("border-color","red");
            	return false;
            }else {
                m.receiveQty.css("border-color","#CCC");
            }
            tempTrOrderNochargeQtynew=m.orderNoChargeQty.val();
            tempTrReceiveNochargeQtynew=m.receiveNoChargeQty.val();
            var orderNochargeQty = parseFloat(m.orderNoChargeQty.val());
            var receiveNochargeQty = parseFloat(m.receiveNoChargeQty.val());
            if(receiveNochargeQty > orderNochargeQty) {//不得高于退货量
                _common.prompt("Actual Return Free Qty not be greater than Order Free  Qty!",3,"info");
                m.receiveNoChargeQty.focus();
                m.receiveNoChargeQty.css("border-color","red");
                return false;
            }else {
                m.receiveNoChargeQty.css("border-color","#CCC");
            }
            _common.myConfirm("Are you sure to Submit?",function(result){
                if(result=="true"){
					$(selectTrTemp).find('td[tag=receiveQty]').text(m.receiveQty.val());
					$(selectTrTemp).find('td[tag=receiveNoChargeQty]').text(m.receiveNoChargeQty.val());
					$('#update_dialog').modal("hide");
                    trClick_table1();
					_common.prompt("Data updated successfully！",2,"success"); // 商品修改成功
					// var param = {
					// 	orderId:m.order_id.val(),
					// 	articleId:$("#a_item").attr("k"),
					// 	receiveQty: m.receiveQty.val()
					// }
					// $.myAjaxs({
					// 	url:url_left+"/updateActualReturnQty",
					// 	async:true,
					// 	cache:false,
					// 	type :"post",
					// 	data:param,
					// 	dataType:"json",
					// 	success:function(res){
					// 		if(res.success){
					// 			_common.prompt("Data updated successfully!",3,"success");
					// 			// tableGrid.setting("url", url_left + "/getItemDetailByOrderId");
					// 			// tableGrid.setting("param", paramGrid);
					// 			tableGrid.loadData(null);
					// 			$('#update_dialog').modal("hide");
					// 		}else{
					// 			_common.prompt("Data updated failed!",3,"error");
					// 		}
					// 	},
					// 	complete:_common.myAjaxComplete
					// });
				}
			});
		});

		//更新退货商品实际退货量
		m.returnsSubmitBut.on("click",function(){
			var orderId = m.order_id.val();
			if(orderId==null||orderId==""){
				_common.prompt("Document No. can not be empty!",5,"error"); // 单据号不能为空
				return false;
			}
			var itemDetail = [];
			$("#zgGridTtable>.zgGrid-tbody tr").each(function () {
				var orderItem = {
					orderId:orderId,
					storeCd:m.store_cd.val(),
                    orderPrice:reThousands($(this).find('td[tag=orderPrice]').text()),//退货价格
					articleId:$(this).find('td[tag=articleId]').text(),
					receiveQty:reThousands($(this).find('td[tag=receiveQty]').text()),//实际退货数量
					receiveNochargeQty: 0//实际退搭赠量 为 0
				}
				itemDetail.push(orderItem);
			});

			var orderDetail = "";
			if(itemDetail.length>0){//退货商品
				orderDetail = JSON.stringify(itemDetail)
			}else{
				_common.prompt("Return Item can not be empty!",5,"info");
				return false;
			}

			_common.myConfirm("Are you sure you want to save？",function(result){
				if(result!="true"){return false;}
				$.myAjaxs({
					url:url_left+"/updateActualReturnQty",
                    async:true,
					cache:false,
					type :"post",
					data :{
						orderDetailJson:orderDetail,
                        orderRemark: m.order_remark.val(),
						toKen: m.toKen.val()
					},
					dataType:"json",
					success:function(result){
						if(result.success){
							m.toKen.val(result.toKen);
							_common.prompt("Data updated successfully!",3,"success");
							//发起审核
							var typeId =m.typeId.val();
							var	nReviewid =m.reviewId.val();
							var	recordCd = m.order_id.val();
							var _storeCd = m.store_cd.val();
							if($("#reType").val()=="10"){
								_storeCd = $("#aStore").attr('k');
							}
							_common.initiateAudit(_storeCd,recordCd,typeId,nReviewid,m.toKen.val(),function (token) {
								m.returnsSubmitBut.prop('disabled', true);
								$("#modify").prop('disabled', true);
                                disableAll();
								//审核按钮禁用
								m.approvalBut.prop("disabled",true);
								m.toKen.val(token);
							})
						}else{
							_common.prompt(result.message,5,"error");
						}
					},
					complete:_common.myAjaxComplete
				});
			})
        });
    }

    var itemClear = function () {
        $("#a_item").val('').attr("k",'').attr("v",'');
        m.barcode.val('');
        m.unitId.val('');
        m.unitName.val('');
        m.spec.val('');
        m.orderPrice.val('');
        m.orderAmount.val('');
        m.orderQty.val('');
        m.reasonId.val('');
    };

    //弹窗赋值
    var dialogSetData = function () {
        $.myAutomatic.setValueTemp(a_item,tempTrItemCode,tempTrItemName);
        m.barcode.val(tempTrBarcode);
        m.itemName.val(tempTrItemName);
        m.unitId.val(tempTrUnitId);
        m.unitName.val(tempTrUom);
        m.spec.val(tempTrSpec);
        m.orderPrice.val(tempTrPrice);
        m.orderAmount.val(tempTrItemAmt);
        m.orderQty.val(tempTrOrderQty);
        m.receiveQty.val(tempTrReceiveQty);
        m.reasonId.val(tempTrReasonName);
        m.receiveNoChargeQty.val(tempTrReceiveNochargeQty);
        m.orderNoChargeQty.val(tempTrOrderNochargeQty);
    }

    //临时存值置空
    var clearTempTr = function(){
        tempTrBarcode = null;// 选中的商品条码
        tempTrItemCode = null;//选中的商品编号
        tempTrItemName = null;//选中的商品名称
        tempTrUnitId = null;//选中的商品单位id
        tempTrUom = null;//选中的商品单位
        tempTrSpec = null;//选中的商品规格
        tempTrOrderQty = null;//选中的退货量
        tempTrReceiveQty = null;//选中的实际退货数量
        tempTrPrice = null;//选中的商品单价
        tempTrItemAmt = null;//选中的商品退货总金额
        tempTrReasonId = null;//选中的退货原因
        tempTrReasonName = null;//选中的退货原因
    }

    //画面按钮点击事件
    var but_event = function(){
        //盘点日期
        m.order_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });
        m.update_ymd.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });
        m.create_date.datetimepicker({
            language:'en',
            format: 'dd/mm/yyyy',
            maxView:4,
            startView:2,
            minView:2,
            autoclose:true,
            todayHighlight:true,
            todayBtn:true
        });

        //退货数量失去焦点计算总退货金额
        $("#orderQty").blur(function() {
            var orderQty = reThousands(m.orderQty.val());
            var price = reThousands(m.orderPrice.val());
            if (!!orderQty) {
                m.orderAmount.val(parseFloat(orderQty * price).toFixed(2));//保留两位小数
            }
            $("#orderQty").val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        $("#orderQty").focus(function(){
            $("#orderQty").val(reThousands(this.value));
        });

        //退货数量失去焦点计算总退货金额
        m.receiveQty.blur(function() {
            m.receiveQty.val(toThousands(this.value));
        });

        //退货搭赠数量失去焦点计算总退货金额
        m.receiveNoChargeQty.blur(function() {
            m.receiveNoChargeQty.val(toThousands(this.value));
        });

        //光标进入，去除金额千分位，并去除小数后面多余的0
        m.receiveQty.focus(function(){
            m.receiveQty.val(reThousands(this.value));
        });
    }

    var gridTotal = function(){
        var orderPriceTotal = 0;
        $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
            var orderTotalAmt = parseFloat(reThousands($(this).find('td[tag=orderAmount]').text()));
            if(!isNaN(orderTotalAmt))
                orderPriceTotal +=  parseFloat(orderTotalAmt);
        });
        return orderPriceTotal;
    }

    // 请求加载下拉列表
    function getSelectValue(){
        // 加载select
        // initSelectOptions("Return Reason","reasonId", "00400");
    }

    // 初始化下拉列表
    function initSelectOptions(title, selectId, code) {
        // 共通请求
        $.myAjaxs({
            url:systemPath+"/cm9010/getCode",
            async:true,
            cache:false,
            type :"post",
            data :"codeValue="+code,
            dataType:"json",
            success:function(result){
                var selectObj = $("#" + selectId);
                selectObj.find("option:not(:first)").remove();
                for (var i = 0; i < result.length; i++) {
                    var optionValue = result[i].codeValue;
                    var optionText = result[i].codeName;
                    selectObj.append(new Option(optionText, optionValue));
                }
            },
            error : function(e){
                _common.prompt(title+" Failed to load data!",5,"error");
            },
            complete:_common.myAjaxComplete
        });
    }

    // 选项选中事件
    var trClick_table1 = function(){
        //选中时先清空临时值
        clearTempTr();
        var cols = tableGrid.getSelectColValue(selectTrTemp,"barcode,articleId,articleName,unitId,unitName,spec,orderQty,orderNoChargeQty,receiveNoChargeQty,orderPrice,orderAmount,reasonId,reasonName,receiveQty");
        tempTrBarcode = cols["barcode"];
        tempTrItemCode = cols["articleId"];
        tempTrItemName = cols["articleName"];
        tempTrUnitId = cols["unitId"];
        tempTrUom = cols["unitName"];
        tempTrSpec = cols["spec"];
        tempTrPrice = cols["orderPrice"];
        tempTrItemAmt = cols["orderAmount"];
        tempTrOrderQty = cols["orderQty"];
        tempTrReasonId = cols["reasonId"];
        tempTrReasonName = cols["reasonName"];
        tempTrReceiveQty = cols['receiveQty'];
        tempTrOrderNochargeQty=cols['orderNoChargeQty'];
        tempTrReceiveNochargeQty=cols['receiveNoChargeQty'];
    }

    //表格初始化-退供应商申请单样式
    var initTable1 = function(){
        tableGrid = $("#zgGridTtable").zgGrid({
            title:"Return Detail",
            param:paramGrid,
            colNames:["Item Barcode","Item Code","Item Name","UOM ID","Vendor Id","Price","UOM","Spec","Return Qty",
                "Return Free Qty","Amount","Reason Id","Return Reason","Actual Return Qty","Actual Return Free Qty"],
            colModel:[
                {name:"barcode",type:"text",text:"right",width:"80",ishide:false,css:""},
                {name:"articleId",type:"text",text:"right",width: "80",ishide:false,css:""},
                {name:"articleName",type:"text",text:"left",width:"150",ishide:false,css:""},
                {name:"unitId",type:"text",text:"right",width:"80",ishide:true,css:""},
                {name:"vendorId",type:"text",text:"right",width:"80",ishide:true,css:""},
                {name:"orderPrice",type:"text",text:"right",width:"80",ishide:true,css:""},
                {name:"unitName",type:"text",text:"left",width:"50",ishide:false,css:""},
                {name:"spec",type:"text",text:"right",width:"80",ishide:false,css:""},
                {name:"orderQty",type:"text",text:"right",width:"80",ishide:false,css:"",getCustomValue:getThousands},
				{name:"orderNoChargeQty",type:"text",text:"right",width:"80",ishide:true,css:"",getCustomValue:getThousands},
                {name:"orderAmount",type:"text",text:"right",width:"130",ishide:true,css:"",getCustomValue:getThousands},
                {name:"reasonId",type:"text",text:"left",width:"80",ishide:true,css:""},
                {name:"reasonName",type:"text",text:"left",width:"200",ishide:false,css:""},
                {name:"receiveQty",type:"text",text:"right",width:"100",ishide:false,css:"",getCustomValue:getThousands},
				{name:"receiveNoChargeQty",type:"text",text:"right",width:"100",ishide:true,css:"",getCustomValue:getThousands},
            ],//列内容
            width:"max",//宽度自动
            height:300,
            page:1,//当前页
            rowPerPage:10,//每页数据量
            isPage:false,//是否需要分页
            isCheckbox:false,
            freezeHeader:true,
            loadEachBeforeEvent:function(trObj){
                tempTrObjValue={};
                return trObj;
            },
            loadCompleteEvent:function(self){
                selectTrTemp = null;//清空选择的行
                gridTotal();//合计
                return self;
            },
            ajaxSuccess:function(resData){
                return resData;
            },
            eachTrClick:function(trObj,tdObj){//正常左侧点击
                selectTrTemp = trObj;
                trClick_table1();
            },
            buttonGroup:[
                {
                    butType: "update",
                    butId: "modify",
                    butText: "Modify",
                    butSize: ""
                }
            ],

        });
    }

    var gridTotal = function(){
        var orderPriceTotal = 0,totalQty = 0;
        $("#zgGridTtable>.zgGrid-tbody tr").each(function () {
            var orderAmount = parseFloat(reThousands($(this).find('td[tag=orderAmount]').text()));
            var orderQty = parseFloat(reThousands($(this).find('td[tag=orderQty]').text()));
            if(!isNaN(orderAmount))
                orderPriceTotal +=  parseFloat(orderAmount);
            if(!isNaN(orderQty))
                totalQty +=  parseFloat(orderQty);
        });
        m.total_amt.val(orderPriceTotal);
        m.total_qty.val(toThousands(totalQty));
    }

    //小数格式化
    var floatFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''){value = parseFloat(value);}
        return $(tdObj).text(value);
    }

    //日期字段格式化格式
    var dateFmt = function(tdObj, value){
        if(value!=null&&value.trim()!=''&&value.length==8) {
            value = fmtStringDate(value);
        }
        return $(tdObj).text(value);
    }
    // 格式化数字类型的日期 yyyyMMdd → dd/MM/yyyy
    function fmtStringDate(date){
        var res = "";
        if(date!=null&&date.trim()!=''&&date.length==8) {
            res = date.replace(/^(\d{4})(\d{2})(\d{2})$/, '$3/$2/$1');
        }
        return res;
    }
    //字符串日期格式转换：dd/MM/yyyy → yyyyMMdd
    function subfmtDate(date){
        var res = "";
        if(date!=null&&date!='')
            res = date.replace(/\//g, '').replace(/^(\d{2})(\d{2})(\d{4})$/, '$3$2$1');
        return res;
    }

    self.init = init;
    return self;
});
var _start = require('start');
var _index = require('receiptReturnVendorEdit');
_start.load(function (_common) {
    _index.init(_common);
});
